package com.YeuTech.Application.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YeuTech.Domain.Entities.CmsContent;
import com.YeuTech.Domain.Entities.CmsContentPublication;
import com.YeuTech.Domain.Entities.CustomDomain;
import com.YeuTech.Domain.Repository.ICmsContentPublicationRepository;
import com.YeuTech.Domain.Repository.ICmsRepository;
import com.YeuTech.Domain.Repository.ICmsUserRepository;
import com.YeuTech.Domain.Repository.ICustomDomainRepository;
import com.YeuTech.Dtos.Request.PublishContentToDomainRequestDto;
import com.YeuTech.Dtos.Request.RegisterDomainRequestDto;
import com.YeuTech.Dtos.Response.ContentPublicationResponseDto;
import com.YeuTech.Dtos.Response.CustomDomainResponseDto;
import com.YeuTech.Dtos.Response.DomainDnsCheckResponseDto;
import com.YeuTech.Dtos.Response.DomainVerificationResponseDto;
import com.YeuTech.Dtos.Response.PublicUrlResponseDto;

@Service
public class CustomDomainService implements ICustomDomainService {

    private static final String STATUS_PUBLISHED = "PUBLISHED";
    private static final String VERIFICATION_PENDING = "PENDING";
    private static final String VERIFICATION_VERIFIED = "VERIFIED";
    private static final String VERIFICATION_FAILED = "FAILED";
    private static final String DNS_PENDING = "PENDING";
    private static final String DNS_POINTED = "POINTED";
    private static final String DNS_FAILED = "FAILED";
    private static final String SSL_PENDING = "PENDING";
    private static final String TOKEN_PREFIX = "yeutech-verify=";

    // Simple domain name pattern: allows subdomains, min 2 labels, TLD >= 2 chars
    private static final String DOMAIN_PATTERN = "^([a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$";

    private final ICustomDomainRepository domainRepository;
    private final ICmsContentPublicationRepository publicationRepository;
    private final ICmsRepository cmsRepository;
    private final ICmsUserRepository cmsUserRepository;
    private final IDnsVerificationService dnsVerificationService;

    public CustomDomainService(
            ICustomDomainRepository domainRepository,
            ICmsContentPublicationRepository publicationRepository,
            ICmsRepository cmsRepository,
            ICmsUserRepository cmsUserRepository,
            IDnsVerificationService dnsVerificationService) {
        this.domainRepository = domainRepository;
        this.publicationRepository = publicationRepository;
        this.cmsRepository = cmsRepository;
        this.cmsUserRepository = cmsUserRepository;
        this.dnsVerificationService = dnsVerificationService;
    }

    @Override
    @Transactional
    public CustomDomainResponseDto registerDomain(String currentUserEmail, RegisterDomainRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("domainName is required");
        }

        String userId = resolveCurrentUserId(currentUserEmail);
        String domainName = requireTrimmedLower(request.getDomainName(), "domainName is required");

        if (!domainName.matches(DOMAIN_PATTERN)) {
            throw new IllegalArgumentException("Invalid domain name format");
        }

        if (domainRepository.existsByDomainName(domainName)) {
            throw new IllegalArgumentException("Domain is already registered");
        }

        LocalDateTime now = LocalDateTime.now();
        String verificationToken = UUID.randomUUID().toString().replace("-", "");

        CustomDomain domain = new CustomDomain();
        domain.setDomainId(UUID.randomUUID().toString());
        domain.setUserId(userId);
        domain.setDomainName(domainName);
        domain.setVerificationToken(verificationToken);
        domain.setVerificationStatus(VERIFICATION_PENDING);
        domain.setDnsStatus(DNS_PENDING);
        domain.setSslStatus(SSL_PENDING);
        domain.setActive(false);
        domain.setCreatedDate(now);
        domain.setUpdatedDate(now);

        CustomDomain saved = domainRepository.save(domain);
        return toDomainResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomDomainResponseDto> listDomains(String currentUserEmail) {
        String userId = resolveCurrentUserId(currentUserEmail);
        return domainRepository.findByUserId(userId).stream()
                .map(this::toDomainResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public DomainVerificationResponseDto verifyDomain(String currentUserEmail, String domainId) {
        String userId = resolveCurrentUserId(currentUserEmail);
        CustomDomain domain = findDomainByIdAndUserId(domainId, userId);

        String expectedToken = TOKEN_PREFIX + domain.getVerificationToken();
        List<String> txtRecords = dnsVerificationService.lookupTxtRecords(domain.getDomainName());

        boolean verified = txtRecords.stream()
                .anyMatch(record -> record.equals(expectedToken));

        if (verified) {
            domain.setVerificationStatus(VERIFICATION_VERIFIED);
        } else {
            domain.setVerificationStatus(VERIFICATION_FAILED);
        }
        domain.setUpdatedDate(LocalDateTime.now());
        domainRepository.save(domain);

        String instructions = verified
                ? "Domain ownership verified successfully."
                : "Verification failed. Please add a TXT record with value: " + expectedToken
                  + " to your domain's DNS settings.";

        return new DomainVerificationResponseDto(
                domain.getDomainId(),
                expectedToken,
                domain.getVerificationStatus(),
                instructions);
    }

    @Override
    @Transactional
    public DomainDnsCheckResponseDto dnsCheck(String currentUserEmail, String domainId) {
        String userId = resolveCurrentUserId(currentUserEmail);
        CustomDomain domain = findDomainByIdAndUserId(domainId, userId);

        boolean hasRecords = dnsVerificationService.hasDnsRecords(domain.getDomainName());

        if (hasRecords) {
            domain.setDnsStatus(DNS_POINTED);
        } else {
            domain.setDnsStatus(DNS_FAILED);
        }
        domain.setUpdatedDate(LocalDateTime.now());
        domainRepository.save(domain);

        String message = hasRecords
                ? "DNS records found. Domain is pointing to the expected infrastructure."
                : "No DNS records found. Please add an A or CNAME record pointing your domain to YeuTech infrastructure.";

        return new DomainDnsCheckResponseDto(
                domain.getDomainId(),
                domain.getDnsStatus(),
                message);
    }

    @Override
    @Transactional
    public CustomDomainResponseDto activateDomain(String currentUserEmail, String domainId) {
        String userId = resolveCurrentUserId(currentUserEmail);
        CustomDomain domain = findDomainByIdAndUserId(domainId, userId);

        if (!VERIFICATION_VERIFIED.equals(domain.getVerificationStatus())) {
            throw new IllegalArgumentException(
                    "Domain must be verified before activation. Current status: " + domain.getVerificationStatus());
        }
        if (!DNS_POINTED.equals(domain.getDnsStatus())) {
            throw new IllegalArgumentException(
                    "Domain DNS must be pointed before activation. Current status: " + domain.getDnsStatus());
        }

        domain.setActive(true);
        domain.setUpdatedDate(LocalDateTime.now());
        CustomDomain saved = domainRepository.save(domain);
        return toDomainResponseDto(saved);
    }

    @Override
    @Transactional
    public ContentPublicationResponseDto publishContentToDomain(
            String currentUserEmail, String contentId, PublishContentToDomainRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("domainId and slug are required");
        }

        String userId = resolveCurrentUserId(currentUserEmail);
        String domainId = requireTrimmed(request.getDomainId(), "domainId is required");
        String slug = requireTrimmed(request.getSlug(), "slug is required");

        // Validate slug format: lowercase alphanumeric and hyphens only
        String normalizedSlug = slug.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9-]", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        if (normalizedSlug.isEmpty()) {
            throw new IllegalArgumentException("slug must contain at least one alphanumeric character");
        }

        // Validate domain exists, belongs to user, and is active
        CustomDomain domain = findDomainByIdAndUserId(domainId, userId);
        if (!domain.isActive()) {
            throw new IllegalArgumentException("Domain is not active. Please complete verification and activation first.");
        }

        // Validate content exists, belongs to user, and is PUBLISHED
        CmsContent content = cmsRepository.findByContentIdAndUserIdAndStatus(contentId, userId, STATUS_PUBLISHED)
                .orElseThrow(() -> new IllegalArgumentException(
                        "CMS content not found or not in PUBLISHED status"));

        // Strict tenant isolation: content owner must match domain owner
        if (!content.getUserId().equals(domain.getUserId())) {
            throw new IllegalArgumentException("Content owner does not match domain owner");
        }

        // Build canonical URL
        String canonicalUrl = "https://" + domain.getDomainName() + "/p/" + normalizedSlug + "/" + contentId;

        // Check for existing publication and update, or create new
        CmsContentPublication publication = publicationRepository
                .findByDomainIdAndContentId(domainId, contentId)
                .orElse(null);

        LocalDateTime now = LocalDateTime.now();
        if (publication == null) {
            publication = new CmsContentPublication();
            publication.setPublicationId(UUID.randomUUID().toString());
            publication.setContentId(contentId);
            publication.setUserId(userId);
            publication.setDomainId(domainId);
            publication.setPublishedDate(now);
        }

        publication.setSlug(normalizedSlug);
        publication.setCanonicalUrl(canonicalUrl);
        publication.setSummary(trimToNull(request.getSummary()));
        publication.setCoverImageUrl(trimToNull(request.getCoverImageUrl()));
        publication.setActive(true);

        CmsContentPublication saved = publicationRepository.save(publication);

        ContentPublicationResponseDto responseDto = new ContentPublicationResponseDto();
        responseDto.setPublicationId(saved.getPublicationId());
        responseDto.setContentId(saved.getContentId());
        responseDto.setDomainId(saved.getDomainId());
        responseDto.setDomainName(domain.getDomainName());
        responseDto.setSlug(saved.getSlug());
        responseDto.setCanonicalUrl(saved.getCanonicalUrl());
        responseDto.setSummary(saved.getSummary());
        responseDto.setCoverImageUrl(saved.getCoverImageUrl());
        responseDto.setPublishedDate(saved.getPublishedDate());
        responseDto.setActive(saved.isActive());
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public PublicUrlResponseDto getPublicUrl(String currentUserEmail, String contentId) {
        String userId = resolveCurrentUserId(currentUserEmail);

        // Verify content belongs to user
        if (!cmsRepository.existsByContentIdAndUserId(contentId, userId)) {
            throw new IllegalArgumentException("CMS content not found");
        }

        CmsContentPublication publication = publicationRepository.findByContentId(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content has not been published to any domain"));

        return new PublicUrlResponseDto(
                contentId,
                publication.getCanonicalUrl(),
                publication.isActive());
    }

    // ---- private helpers ----

    private CustomDomain findDomainByIdAndUserId(String domainId, String userId) {
        return domainRepository.findByDomainIdAndUserId(domainId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Custom domain not found"));
    }

    private String resolveCurrentUserId(String currentUserEmail) {
        String normalizedEmail = trimToNull(currentUserEmail);
        if (normalizedEmail == null) {
            throw new IllegalArgumentException("Current user not found");
        }
        return cmsUserRepository.findUserIdByEmailNormalized(normalizedEmail.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));
    }

    private CustomDomainResponseDto toDomainResponseDto(CustomDomain domain) {
        return new CustomDomainResponseDto(
                domain.getDomainId(),
                domain.getUserId(),
                domain.getDomainName(),
                domain.getVerificationToken(),
                domain.getVerificationStatus(),
                domain.getDnsStatus(),
                domain.getSslStatus(),
                domain.isActive(),
                domain.getCreatedDate(),
                domain.getUpdatedDate());
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String requireTrimmed(String value, String message) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }

    private String requireTrimmedLower(String value, String message) {
        String normalized = requireTrimmed(value, message);
        return normalized.toLowerCase(Locale.ROOT);
    }
}

package com.YeuTech.Application.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

import static org.junit.jupiter.api.Assertions.*;

class CustomDomainServiceTest {

    private static final String USER_EMAIL = "test@example.com";
    private static final String USER_ID = "user-001";

    private StubDomainRepo domainRepo;
    private StubPubRepo pubRepo;
    private StubCmsRepo cmsRepo;
    private StubDns dnsService;
    private CustomDomainService service;

    @BeforeEach
    void setUp() {
        domainRepo = new StubDomainRepo();
        pubRepo = new StubPubRepo();
        cmsRepo = new StubCmsRepo();
        ICmsUserRepository userRepo = email -> Optional.of(USER_ID);
        dnsService = new StubDns();
        service = new CustomDomainService(domainRepo, pubRepo, cmsRepo, userRepo, dnsService);
    }

    @Test @DisplayName("Register valid domain")
    void registerValid() {
        var req = new RegisterDomainRequestDto();
        req.setDomainName("example.com");
        CustomDomainResponseDto dto = service.registerDomain(USER_EMAIL, req);
        assertEquals("example.com", dto.getDomainName());
        assertEquals("PENDING", dto.getVerificationStatus());
        assertFalse(dto.isActive());
    }

    @Test @DisplayName("Reject null request")
    void rejectNull() {
        assertThrows(IllegalArgumentException.class, () -> service.registerDomain(USER_EMAIL, null));
    }

    @Test @DisplayName("Reject invalid domain format")
    void rejectInvalid() {
        var req = new RegisterDomainRequestDto();
        req.setDomainName("not_valid");
        assertThrows(IllegalArgumentException.class, () -> service.registerDomain(USER_EMAIL, req));
    }

    @Test @DisplayName("Reject duplicate domain")
    void rejectDuplicate() {
        domainRepo.existsResult = true;
        var req = new RegisterDomainRequestDto();
        req.setDomainName("taken.com");
        assertThrows(IllegalArgumentException.class, () -> service.registerDomain(USER_EMAIL, req));
    }

    @Test @DisplayName("Verify TXT success")
    void verifyOk() {
        CustomDomain d = makeDomain();
        domainRepo.findResult = Optional.of(d);
        dnsService.txtRecords = List.of("yeutech-verify=" + d.getVerificationToken());
        DomainVerificationResponseDto dto = service.verifyDomain(USER_EMAIL, d.getDomainId());
        assertEquals("VERIFIED", dto.getVerificationStatus());
    }

    @Test @DisplayName("Verify TXT failure")
    void verifyFail() {
        CustomDomain d = makeDomain();
        domainRepo.findResult = Optional.of(d);
        dnsService.txtRecords = List.of();
        DomainVerificationResponseDto dto = service.verifyDomain(USER_EMAIL, d.getDomainId());
        assertEquals("FAILED", dto.getVerificationStatus());
    }

    @Test @DisplayName("DNS check pointed")
    void dnsPointed() {
        CustomDomain d = makeDomain();
        domainRepo.findResult = Optional.of(d);
        dnsService.hasDns = true;
        DomainDnsCheckResponseDto dto = service.dnsCheck(USER_EMAIL, d.getDomainId());
        assertEquals("POINTED", dto.getDnsStatus());
    }

    @Test @DisplayName("DNS check failed")
    void dnsFailed() {
        CustomDomain d = makeDomain();
        domainRepo.findResult = Optional.of(d);
        dnsService.hasDns = false;
        DomainDnsCheckResponseDto dto = service.dnsCheck(USER_EMAIL, d.getDomainId());
        assertEquals("FAILED", dto.getDnsStatus());
    }

    @Test @DisplayName("Activate verified+pointed domain")
    void activateOk() {
        CustomDomain d = makeDomain();
        d.setVerificationStatus("VERIFIED");
        d.setDnsStatus("POINTED");
        domainRepo.findResult = Optional.of(d);
        CustomDomainResponseDto dto = service.activateDomain(USER_EMAIL, d.getDomainId());
        assertTrue(dto.isActive());
    }

    @Test @DisplayName("Reject activate when not verified")
    void activateNotVerified() {
        CustomDomain d = makeDomain();
        d.setDnsStatus("POINTED");
        domainRepo.findResult = Optional.of(d);
        assertThrows(IllegalArgumentException.class, () -> service.activateDomain(USER_EMAIL, d.getDomainId()));
    }

    @Test @DisplayName("Publish content to active domain")
    void publishOk() {
        CustomDomain d = makeDomain(); d.setActive(true); d.setVerificationStatus("VERIFIED"); d.setDnsStatus("POINTED");
        domainRepo.findResult = Optional.of(d);
        CmsContent c = makeContent();
        cmsRepo.findStatusResult = Optional.of(c);
        var req = new PublishContentToDomainRequestDto();
        req.setDomainId(d.getDomainId()); req.setSlug("summer-sale");
        ContentPublicationResponseDto dto = service.publishContentToDomain(USER_EMAIL, c.getContentId(), req);
        assertEquals("summer-sale", dto.getSlug());
        assertTrue(dto.getCanonicalUrl().contains("example.com"));
    }

    @Test @DisplayName("Reject publish to inactive domain")
    void publishInactive() {
        CustomDomain d = makeDomain();
        domainRepo.findResult = Optional.of(d);
        var req = new PublishContentToDomainRequestDto();
        req.setDomainId(d.getDomainId()); req.setSlug("test");
        assertThrows(IllegalArgumentException.class, () -> service.publishContentToDomain(USER_EMAIL, "c1", req));
    }

    // ---- Helpers ----

    private CustomDomain makeDomain() {
        CustomDomain d = new CustomDomain();
        d.setDomainId("dom-1"); d.setUserId(USER_ID); d.setDomainName("example.com");
        d.setVerificationToken("tok123"); d.setVerificationStatus("PENDING");
        d.setDnsStatus("PENDING"); d.setSslStatus("PENDING"); d.setActive(false);
        d.setCreatedDate(LocalDateTime.now()); d.setUpdatedDate(LocalDateTime.now());
        return d;
    }

    private CmsContent makeContent() {
        CmsContent c = new CmsContent();
        c.setContentId("c-1"); c.setUserId(USER_ID); c.setTitle("Test");
        c.setContentBody("<p>body</p>"); c.setContentType("BLOG"); c.setPlatform("WEB");
        c.setStatus("PUBLISHED"); c.setCreateDate(LocalDateTime.now()); c.setUpdateDate(LocalDateTime.now());
        return c;
    }

    // ---- Stubs ----

    static class StubDns implements IDnsVerificationService {
        List<String> txtRecords = List.of();
        boolean hasDns = false;
        public List<String> lookupTxtRecords(String d) { return txtRecords; }
        public boolean hasDnsRecords(String d) { return hasDns; }
    }

    static class StubDomainRepo implements ICustomDomainRepository {
        boolean existsResult = false;
        Optional<CustomDomain> findResult = Optional.empty();
        public CustomDomain save(CustomDomain d) { return d; }
        public Optional<CustomDomain> findByDomainId(String id) { return findResult; }
        public Optional<CustomDomain> findByDomainName(String n) { return findResult; }
        public Optional<CustomDomain> findByDomainIdAndUserId(String id, String uid) { return findResult; }
        public List<CustomDomain> findByUserId(String uid) { return findResult.map(List::of).orElse(List.of()); }
        public boolean existsByDomainName(String n) { return existsResult; }
    }

    static class StubPubRepo implements ICmsContentPublicationRepository {
        Optional<CmsContentPublication> findByContentIdResult = Optional.empty();
        public CmsContentPublication save(CmsContentPublication p) { return p; }
        public Optional<CmsContentPublication> findByDomainIdAndContentId(String d, String c) { return Optional.empty(); }
        public Optional<CmsContentPublication> findByDomainIdAndContentIdAndIsActive(String d, String c, boolean a) { return Optional.empty(); }
        public Optional<CmsContentPublication> findByContentId(String c) { return findByContentIdResult; }
        public List<CmsContentPublication> findByUserId(String u) { return List.of(); }
    }

    static class StubCmsRepo implements ICmsRepository {
        Optional<CmsContent> findStatusResult = Optional.empty();
        boolean existsByContentIdAndUserIdResult = false;
        public List<CmsContent> findAll() { return List.of(); }
        public CmsContent save(CmsContent c) { return c; }
        public Optional<CmsContent> findByContentIdAndUserId(String c, String u) { return Optional.empty(); }
        public List<CmsContent> findRecentByUserId(String u, int l) { return List.of(); }
        public org.springframework.data.domain.Page<CmsContent> findByUserIdWithFilters(
                String u, String s, String ct, String p, Boolean hg,
                java.time.LocalDateTime f, java.time.LocalDateTime t, String se,
                org.springframework.data.domain.Pageable pg) { return org.springframework.data.domain.Page.empty(); }
        public void delete(CmsContent c) {}
        public boolean existsByContentIdAndUserId(String c, String u) { return existsByContentIdAndUserIdResult; }
        public Optional<CmsContent> findByContentIdAndUserIdAndStatus(String c, String u, String s) { return findStatusResult; }
        public long countByUserIdAndCreateDateBetween(String u, java.time.LocalDateTime s, java.time.LocalDateTime e) { return 0; }
        public long countByUserIdAndStatusAndCreateDateBetween(String u, String st, java.time.LocalDateTime s, java.time.LocalDateTime e) { return 0; }
    }
}

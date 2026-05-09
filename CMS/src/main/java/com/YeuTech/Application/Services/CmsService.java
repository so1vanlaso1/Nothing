package com.YeuTech.Application.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YeuTech.Application.Mappers.CmsDtoMapper;
import com.YeuTech.Application.Utils.CmsHtmlRenderer;
import com.YeuTech.Domain.Entities.CmsContent;
import com.YeuTech.Domain.Entities.CmsMarketingGeneration;
import com.YeuTech.Domain.Repository.ICmsMarketingGenerationRepository;
import com.YeuTech.Domain.Repository.ICmsRepository;
import com.YeuTech.Domain.Repository.ICmsUserRepository;
import com.YeuTech.Dtos.Request.CmsContentQueryRequestDto;
import com.YeuTech.Dtos.Request.CreateCmsContentFromGenerationRequestDto;
import com.YeuTech.Dtos.Request.CreateCmsContentRequestDto;
import com.YeuTech.Dtos.Request.UpdateCmsContentRequestDto;
import com.YeuTech.Dtos.Response.CmsContentDetailResponseDto;
import com.YeuTech.Dtos.Response.CmsContentListResponseDto;
import com.YeuTech.Dtos.Response.CmsContentResponseDto;
import com.YeuTech.Dtos.Response.CmsDashboardRecentItemsResponseDto;
import com.YeuTech.Dtos.Response.CmsDashboardStatsResponseDto;
import com.YeuTech.Dtos.Response.CmsDashboardSummaryResponseDto;
import com.YeuTech.Dtos.Response.CmsDashboardTrendsResponseDto;
import com.YeuTech.Dtos.Response.CmsGenerationSourceResponseDto;
import com.YeuTech.Dtos.Response.CmsPaginationResponseDto;
import com.YeuTech.Dtos.Response.CmsRecentItemResponseDto;
import com.YeuTech.Dtos.Response.CmsStatusChangeResponseDto;

@Service
public class CmsService implements ICmsService {

    private static final String STATUS_DRAFT = "DRAFT";
    private static final String STATUS_PUBLISHED = "PUBLISHED";
    private static final String STATUS_ARCHIVED = "ARCHIVED";
    private static final String PERIOD_THIS_MONTH = "thisMonth";
    private static final String PERIOD_THIS_WEEK = "thisWeek";
    private static final String PERIOD_THIS_YEAR = "thisYear";
    private static final int DEFAULT_RECENT_ITEMS_LIMIT = 5;
    private static final int MAX_RECENT_ITEMS_LIMIT = 50;

    private final ICmsRepository cmsRepository;
    private final ICmsUserRepository cmsUserRepository;
    private final ICmsMarketingGenerationRepository cmsMarketingGenerationRepository;

    public CmsService(
            ICmsRepository cmsRepository,
            ICmsUserRepository cmsUserRepository,
            ICmsMarketingGenerationRepository cmsMarketingGenerationRepository) {
        this.cmsRepository = cmsRepository;
        this.cmsUserRepository = cmsUserRepository;
        this.cmsMarketingGenerationRepository = cmsMarketingGenerationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CmsContentResponseDto> getAllCms() {
        return cmsRepository.findAll().stream()
                .map(CmsDtoMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public CmsContentResponseDto createContent(String currentUserEmail, CreateCmsContentRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("title is required");
        }

        String userId = resolveCurrentUserId(currentUserEmail);
        String title = requireTrimmed(request.getTitle(), "title is required");
        String contentBody = requireTrimmed(request.getContentBody(), "contentBody is required");
        String contentType = requireTrimmed(request.getContentType(), "contentType is required");
        String platform = requireTrimmed(request.getPlatform(), "platform is required");
        String status = normalizeStatusOrDefault(request.getStatus(), STATUS_DRAFT);

        LocalDateTime now = LocalDateTime.now();
        CmsContent content = new CmsContent();
        content.setContentId(UUID.randomUUID().toString());
        content.setUserId(userId);
        content.setGenerationId(null);
        content.setTitle(title);
        content.setContentBody(contentBody);
        content.setContentType(contentType);
        content.setPlatform(platform);
        content.setStatus(status);
        content.setCreateDate(now);
        content.setUpdateDate(now);

        return CmsDtoMapper.toResponseDto(cmsRepository.save(content));
    }

    @Override
    @Transactional
    public CmsContentResponseDto createContentFromGeneration(
            String currentUserEmail,
            String generationId,
            CreateCmsContentFromGenerationRequestDto request) {
        String userId = resolveCurrentUserId(currentUserEmail);
        CreateCmsContentFromGenerationRequestDto safeRequest = request == null
                ? new CreateCmsContentFromGenerationRequestDto()
                : request;

        CmsMarketingGeneration generation = cmsMarketingGenerationRepository
                .findByGenerationIdAndUserId(generationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Marketing generation not found"));

        String contentType = firstNonBlank(safeRequest.getContentType(), generation.getContentType());
        if (contentType == null) {
            throw new IllegalArgumentException("contentType is required");
        }

        String platform = firstNonBlank(safeRequest.getPlatform(), generation.getPlatform());
        if (platform == null) {
            throw new IllegalArgumentException("platform is required");
        }

        String contentBody = firstNonBlank(safeRequest.getContentBody(), generation.getGeneratedContent());
        if (contentBody == null) {
            throw new IllegalArgumentException("contentBody is required");
        }

        String title = firstNonBlank(safeRequest.getTitle(), generation.getProductName());
        if (title == null) {
            title = "Generated " + contentType + " Content";
        }

        LocalDateTime now = LocalDateTime.now();
        CmsContent content = new CmsContent();
        content.setContentId(UUID.randomUUID().toString());
        content.setUserId(userId);
        content.setGenerationId(generationId);
        content.setTitle(title);
        content.setContentBody(contentBody);
        content.setContentType(contentType);
        content.setPlatform(platform);
        content.setStatus(STATUS_DRAFT);
        content.setCreateDate(now);
        content.setUpdateDate(now);

        return CmsDtoMapper.toResponseDto(cmsRepository.save(content));
    }

    @Override
    @Transactional(readOnly = true)
    public CmsContentListResponseDto listContents(String currentUserEmail, CmsContentQueryRequestDto request) {
        String userId = resolveCurrentUserId(currentUserEmail);
        CmsContentQueryRequestDto query = request == null ? new CmsContentQueryRequestDto() : request;

        int page = query.getPage() == null || query.getPage() < 0 ? 0 : query.getPage();
        int pageSize = query.getPageSize() == null ? 20 : query.getPageSize();
        if (pageSize < 1) {
            pageSize = 1;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }

        String sortBy = normalizeSortBy(query.getSortBy());
        Sort.Direction direction = normalizeSortOrder(query.getSortOrder());
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by(direction, sortBy));

        LocalDateTime fromDate = parseDateOrDateTime(query.getFromDate(), false);
        LocalDateTime toDate = parseDateOrDateTime(query.getToDate(), true);
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("fromDate must be before or equal to toDate");
        }

        String status = normalizeStatusOrNull(query.getStatus());
        String contentType = trimToNull(query.getContentType());
        String platform = trimToNull(query.getPlatform());
        String search = trimToNull(query.getSearch());

        Page<CmsContent> result = cmsRepository.findByUserIdWithFilters(
                userId,
                status,
                contentType,
                platform,
                query.getHasGeneration(),
                fromDate,
                toDate,
                search,
                pageable);

        List<CmsContentResponseDto> items = result.getContent().stream()
                .map(CmsDtoMapper::toResponseDto)
                .toList();
        CmsPaginationResponseDto pagination = new CmsPaginationResponseDto(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());

        return new CmsContentListResponseDto(items, pagination);
    }

        @Override
        @Transactional(readOnly = true)
        public CmsDashboardRecentItemsResponseDto getDashboardRecentItems(String currentUserEmail, Integer limit) {
            String userId = resolveCurrentUserId(currentUserEmail);
            int safeLimit = normalizeRecentItemsLimit(limit);

            List<CmsRecentItemResponseDto> generations = cmsMarketingGenerationRepository.findRecentByUserId(userId, safeLimit)
                    .stream()
                    .map(this::toRecentGenerationItem)
                    .toList();

            List<CmsRecentItemResponseDto> cmsItems = cmsRepository.findRecentByUserId(userId, safeLimit)
                    .stream()
                    .map(this::toRecentCmsItem)
                    .toList();

            return new CmsDashboardRecentItemsResponseDto(generations, cmsItems);
        }

        @Override
        @Transactional(readOnly = true)
        public CmsDashboardSummaryResponseDto getDashboardSummary(String currentUserEmail, String period) {
        String userId = resolveCurrentUserId(currentUserEmail);
        String normalizedPeriod = normalizeDashboardPeriod(period);
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime currentStart = getCurrentPeriodStart(normalizedPeriod, now);
        LocalDateTime currentEnd = now;
        LocalDateTime previousStart = getPreviousPeriodStart(normalizedPeriod, currentStart);
        LocalDateTime previousEnd = currentStart;

        long totalGenerations = cmsMarketingGenerationRepository.countByUserIdAndCreateDateBetween(
            userId,
            currentStart,
            currentEnd);
        long totalCms = cmsRepository.countByUserIdAndCreateDateBetween(userId, currentStart, currentEnd);
        long published = cmsRepository.countByUserIdAndStatusAndCreateDateBetween(
            userId,
            STATUS_PUBLISHED,
            currentStart,
            currentEnd);
        long drafts = cmsRepository.countByUserIdAndStatusAndCreateDateBetween(
            userId,
            STATUS_DRAFT,
            currentStart,
            currentEnd);

        long prevTotalGenerations = cmsMarketingGenerationRepository.countByUserIdAndCreateDateBetween(
            userId,
            previousStart,
            previousEnd);
        long prevTotalCms = cmsRepository.countByUserIdAndCreateDateBetween(userId, previousStart, previousEnd);
        long prevPublished = cmsRepository.countByUserIdAndStatusAndCreateDateBetween(
            userId,
            STATUS_PUBLISHED,
            previousStart,
            previousEnd);
        long prevDrafts = cmsRepository.countByUserIdAndStatusAndCreateDateBetween(
            userId,
            STATUS_DRAFT,
            previousStart,
            previousEnd);

        TrendData generationsTrend = calculateTrend(totalGenerations, prevTotalGenerations);
        TrendData cmsTrend = calculateTrend(totalCms, prevTotalCms);
        TrendData publishedTrend = calculateTrend(published, prevPublished);
        TrendData draftsTrend = calculateTrend(drafts, prevDrafts);

        CmsDashboardTrendsResponseDto trends = new CmsDashboardTrendsResponseDto(
            generationsTrend.trend,
            generationsTrend.direction,
            cmsTrend.trend,
            cmsTrend.direction,
            publishedTrend.trend,
            publishedTrend.direction,
            draftsTrend.trend,
            draftsTrend.direction);

        CmsDashboardStatsResponseDto stats = new CmsDashboardStatsResponseDto(
            totalGenerations,
            totalCms,
            published,
            drafts,
            trends,
            normalizedPeriod);

        return new CmsDashboardSummaryResponseDto(stats);
        }

    @Override
    @Transactional(readOnly = true)
    public CmsContentDetailResponseDto getContentById(String currentUserEmail, String contentId) {
        String userId = resolveCurrentUserId(currentUserEmail);
        CmsContent content = findContentByIdAndUserId(contentId, userId);

        CmsContentDetailResponseDto detail = new CmsContentDetailResponseDto();
        detail.setContentId(content.getContentId());
        detail.setUserId(content.getUserId());
        detail.setGenerationId(content.getGenerationId());
        detail.setTitle(content.getTitle());
        detail.setContentBody(content.getContentBody());
        detail.setContentType(content.getContentType());
        detail.setPlatform(content.getPlatform());
        detail.setStatus(content.getStatus());
        detail.setCreateDate(content.getCreateDate());
        detail.setUpdateDate(content.getUpdateDate());

        String generationId = trimToNull(content.getGenerationId());
        if (generationId == null) {
            detail.setSourceGeneration(null);
            return detail;
        }

        CmsGenerationSourceResponseDto source = cmsMarketingGenerationRepository
                .findByGenerationIdAndUserId(generationId, userId)
                .map(this::toGenerationSourceDto)
                .orElse(null);
        detail.setSourceGeneration(source);
        return detail;
    }

    @Override
    @Transactional(readOnly = true)
    public String getContentHtmlById(String currentUserEmail, String contentId) {
        String userId = resolveCurrentUserId(currentUserEmail);
        CmsContent content = findContentByIdAndUserId(contentId, userId);
        return CmsHtmlRenderer.renderHtml(content);
    }

    @Override
    @Transactional
    public CmsContentResponseDto updateContent(String currentUserEmail, String contentId, UpdateCmsContentRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("title is required");
        }

        String userId = resolveCurrentUserId(currentUserEmail);
        CmsContent content = findContentByIdAndUserId(contentId, userId);

        if (request.getTitle() != null) {
            content.setTitle(requireTrimmed(request.getTitle(), "title is required"));
        }
        if (request.getContentBody() != null) {
            content.setContentBody(requireTrimmed(request.getContentBody(), "contentBody is required"));
        }
        if (request.getContentType() != null) {
            content.setContentType(requireTrimmed(request.getContentType(), "contentType is required"));
        }
        if (request.getPlatform() != null) {
            content.setPlatform(requireTrimmed(request.getPlatform(), "platform is required"));
        }
        if (request.getStatus() != null) {
            content.setStatus(normalizeStatusOrRequired(request.getStatus()));
        }

        content.setUpdateDate(LocalDateTime.now());
        return CmsDtoMapper.toResponseDto(cmsRepository.save(content));
    }

    @Override
    @Transactional
    public CmsStatusChangeResponseDto publishContent(String currentUserEmail, String contentId) {
        return changeStatus(currentUserEmail, contentId, STATUS_PUBLISHED);
    }

    @Override
    @Transactional
    public CmsStatusChangeResponseDto moveContentToDraft(String currentUserEmail, String contentId) {
        return changeStatus(currentUserEmail, contentId, STATUS_DRAFT);
    }

    @Override
    @Transactional
    public CmsStatusChangeResponseDto archiveContent(String currentUserEmail, String contentId) {
        return changeStatus(currentUserEmail, contentId, STATUS_ARCHIVED);
    }

    @Override
    @Transactional
    public void deleteContent(String currentUserEmail, String contentId) {
        String userId = resolveCurrentUserId(currentUserEmail);
        CmsContent content = findContentByIdAndUserId(contentId, userId);
        cmsRepository.delete(content);
    }

    private CmsStatusChangeResponseDto changeStatus(String currentUserEmail, String contentId, String status) {
        String userId = resolveCurrentUserId(currentUserEmail);
        CmsContent content = findContentByIdAndUserId(contentId, userId);
        content.setStatus(status);
        content.setUpdateDate(LocalDateTime.now());
        CmsContent updated = cmsRepository.save(content);
        return new CmsStatusChangeResponseDto(updated.getContentId(), updated.getStatus(), updated.getUpdateDate());
    }

    private TrendData calculateTrend(long current, long previous) {
        if (previous == 0) {
            return new TrendData("+0%", "up");
        }

        double percentage = ((double) (current - previous) / previous) * 100;
        String trend = String.format(Locale.ROOT, "%s%d%%", percentage > 0 ? "+" : "", Math.round(percentage));
        String direction = percentage >= 0 ? "up" : "down";
        return new TrendData(trend, direction);
    }

    private int normalizeRecentItemsLimit(Integer limit) {
        if (limit == null) {
            return DEFAULT_RECENT_ITEMS_LIMIT;
        }
        if (limit < 1) {
            return 1;
        }
        if (limit > MAX_RECENT_ITEMS_LIMIT) {
            return MAX_RECENT_ITEMS_LIMIT;
        }
        return limit;
    }

    private CmsRecentItemResponseDto toRecentGenerationItem(CmsMarketingGeneration generation) {
        LocalDateTime timestamp = firstNonNull(generation.getCreateDate(), generation.getUpdateDate(), LocalDateTime.now());
        String title = firstNonBlank(generation.getProductName(), generation.getContentType());
        if (title == null) {
            title = "Untitled generation";
        }

        CmsRecentItemResponseDto item = new CmsRecentItemResponseDto();
        item.setId(generation.getGenerationId());
        item.setTitle(title);
        item.setPlatform(firstNonBlank(generation.getPlatform(), "Unknown"));
        item.setStatus(firstNonBlank(generation.getStatus(), "UNKNOWN"));
        item.setDate(formatRelativeDate(timestamp));
        item.setTimestamp(timestamp);
        return item;
    }

    private CmsRecentItemResponseDto toRecentCmsItem(CmsContent content) {
        LocalDateTime timestamp = firstNonNull(content.getCreateDate(), content.getUpdateDate(), LocalDateTime.now());
        String title = firstNonBlank(content.getTitle(), content.getContentType());
        if (title == null) {
            title = "Untitled content";
        }

        CmsRecentItemResponseDto item = new CmsRecentItemResponseDto();
        item.setId(content.getContentId());
        item.setTitle(title);
        item.setPlatform(firstNonBlank(content.getPlatform(), "Unknown"));
        item.setStatus(firstNonBlank(content.getStatus(), "UNKNOWN"));
        item.setDate(formatRelativeDate(timestamp));
        item.setTimestamp(timestamp);
        return item;
    }

    private String formatRelativeDate(LocalDateTime timestamp) {
        LocalDateTime now = LocalDateTime.now();
        if (timestamp.isAfter(now)) {
            return "just now";
        }

        long diffMinutes = ChronoUnit.MINUTES.between(timestamp, now);
        if (diffMinutes < 1) {
            return "just now";
        }
        if (diffMinutes < 60) {
            return diffMinutes == 1 ? "1 minute ago" : diffMinutes + " minutes ago";
        }

        long diffHours = ChronoUnit.HOURS.between(timestamp, now);
        if (diffHours < 24) {
            return diffHours == 1 ? "1 hour ago" : diffHours + " hours ago";
        }

        long diffDays = ChronoUnit.DAYS.between(timestamp, now);
        return diffDays == 1 ? "1 day ago" : diffDays + " days ago";
    }

    private LocalDateTime firstNonNull(LocalDateTime first, LocalDateTime second, LocalDateTime fallback) {
        if (first != null) {
            return first;
        }
        if (second != null) {
            return second;
        }
        return fallback;
    }

    private String normalizeDashboardPeriod(String period) {
        String normalized = trimToNull(period);
        if (normalized == null) {
            return PERIOD_THIS_MONTH;
        }

        String lowerCasePeriod = normalized.toLowerCase(Locale.ROOT);
        if (PERIOD_THIS_WEEK.toLowerCase(Locale.ROOT).equals(lowerCasePeriod)) {
            return PERIOD_THIS_WEEK;
        }
        if (PERIOD_THIS_YEAR.toLowerCase(Locale.ROOT).equals(lowerCasePeriod)) {
            return PERIOD_THIS_YEAR;
        }
        return PERIOD_THIS_MONTH;
    }

    private LocalDateTime getCurrentPeriodStart(String period, LocalDateTime now) {
        if (PERIOD_THIS_WEEK.equals(period)) {
            LocalDate monday = now.toLocalDate().with(java.time.DayOfWeek.MONDAY);
            return monday.atStartOfDay();
        }
        if (PERIOD_THIS_YEAR.equals(period)) {
            LocalDate firstDayOfYear = now.toLocalDate().with(TemporalAdjusters.firstDayOfYear());
            return firstDayOfYear.atStartOfDay();
        }

        LocalDate firstDayOfMonth = now.toLocalDate().withDayOfMonth(1);
        return firstDayOfMonth.atStartOfDay();
    }

    private LocalDateTime getPreviousPeriodStart(String period, LocalDateTime currentStart) {
        if (PERIOD_THIS_WEEK.equals(period)) {
            return currentStart.minusWeeks(1);
        }
        if (PERIOD_THIS_YEAR.equals(period)) {
            return currentStart.minusYears(1);
        }
        return currentStart.minusMonths(1);
    }

    private CmsContent findContentByIdAndUserId(String contentId, String userId) {
        return cmsRepository.findByContentIdAndUserId(contentId, userId)
                .orElseThrow(() -> new IllegalArgumentException("CMS content not found"));
    }

    private String resolveCurrentUserId(String currentUserEmail) {
        String normalizedEmail = trimToNull(currentUserEmail);
        if (normalizedEmail == null) {
            throw new IllegalArgumentException("Current user not found");
        }

        return cmsUserRepository.findUserIdByEmailNormalized(normalizedEmail.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));
    }

    private String requireTrimmed(String value, String message) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }

    private static final class TrendData {
        private final String trend;
        private final String direction;

        private TrendData(String trend, String direction) {
            this.trend = trend;
            this.direction = direction;
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String firstNonBlank(String first, String second) {
        String firstValue = trimToNull(first);
        if (firstValue != null) {
            return firstValue;
        }
        return trimToNull(second);
    }

    private String normalizeStatusOrRequired(String status) {
        String normalized = trimToNull(status);
        if (normalized == null) {
            throw new IllegalArgumentException("status is required");
        }
        return validateStatus(normalized.toUpperCase(Locale.ROOT));
    }

    private String normalizeStatusOrDefault(String status, String defaultValue) {
        String normalized = trimToNull(status);
        if (normalized == null) {
            return defaultValue;
        }
        return validateStatus(normalized.toUpperCase(Locale.ROOT));
    }

    private String normalizeStatusOrNull(String status) {
        String normalized = trimToNull(status);
        if (normalized == null) {
            return null;
        }
        return validateStatus(normalized.toUpperCase(Locale.ROOT));
    }

    private String validateStatus(String status) {
        if (!STATUS_DRAFT.equals(status) && !STATUS_PUBLISHED.equals(status) && !STATUS_ARCHIVED.equals(status)) {
            throw new IllegalArgumentException("status must be DRAFT, PUBLISHED, or ARCHIVED");
        }
        return status;
    }

    private String normalizeSortBy(String sortBy) {
        String normalized = trimToNull(sortBy);
        if (normalized == null) {
            return "createDate";
        }

        return switch (normalized) {
            case "createDate", "updateDate", "status" -> normalized;
            default -> throw new IllegalArgumentException("sortBy must be one of createDate, updateDate, status");
        };
    }

    private Sort.Direction normalizeSortOrder(String sortOrder) {
        String normalized = trimToNull(sortOrder);
        if (normalized == null) {
            return Sort.Direction.DESC;
        }

        if ("asc".equalsIgnoreCase(normalized)) {
            return Sort.Direction.ASC;
        }
        if ("desc".equalsIgnoreCase(normalized)) {
            return Sort.Direction.DESC;
        }
        throw new IllegalArgumentException("sortOrder must be asc or desc");
    }

    private LocalDateTime parseDateOrDateTime(String input, boolean endOfDayWhenDateOnly) {
        String value = trimToNull(input);
        if (value == null) {
            return null;
        }

        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ignored) {
        }

        try {
            LocalDate date = LocalDate.parse(value);
            return endOfDayWhenDateOnly ? date.atTime(LocalTime.MAX) : date.atStartOfDay();
        } catch (DateTimeParseException ignored) {
        }

        throw new IllegalArgumentException("Date values must be ISO-8601 date or date-time");
    }

    private CmsGenerationSourceResponseDto toGenerationSourceDto(CmsMarketingGeneration generation) {
        CmsGenerationSourceResponseDto source = new CmsGenerationSourceResponseDto();
        source.setGenerationId(generation.getGenerationId());
        source.setContentType(generation.getContentType());
        source.setPlatform(generation.getPlatform());
        source.setTargetAudience(generation.getTargetAudience());
        source.setTone(generation.getTone());
        source.setGoal(generation.getGoal());
        source.setProductName(generation.getProductName());
        source.setBrandName(generation.getBrandName());
        source.setPromptInput(generation.getPromptInput());
        source.setGeneratedContent(generation.getGeneratedContent());
        source.setStatus(generation.getStatus());
        source.setErrorMessage(generation.getErrorMessage());
        source.setCreateDate(generation.getCreateDate());
        source.setUpdateDate(generation.getUpdateDate());
        return source;
    }
}

package com.YeuTech.Application.Services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.YeuTech.Application.Mappers.MarketingDtoMapper;
import com.YeuTech.Domain.Repository.IMarketingRepository;
import com.YeuTech.Domain.Entities.MarketingGeneration;
import com.YeuTech.Dtos.Request.UpdateMarketingGenerationRequestDto;
import com.YeuTech.Dtos.Response.MarketingGenerationResponseDto;
import com.YeuTech.Dtos.Response.CmsDashboardRecentItemsResponseDto;
import com.YeuTech.Dtos.Response.CmsRecentItemResponseDto;

@Service
public class MarketingService implements IMarketingService {

    private final IMarketingRepository _marketingRepository;

    public MarketingService(IMarketingRepository marketingRepository) {
        this._marketingRepository = marketingRepository;
    }

    @Override
    public List<MarketingGenerationResponseDto> getAllMarketing() {
        return _marketingRepository.findAll().stream()
                .map(MarketingDtoMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<MarketingGenerationResponseDto> getMarketingByUserId(String userId) {
        return _marketingRepository.findByUserId(userId).stream()
                .map(MarketingDtoMapper::toResponseDto)
                .toList();
    }

    @Override
    public Optional<MarketingGenerationResponseDto> getMarketingByGenerationIdAndUserId(String generationId, String userId) {
        return _marketingRepository.findByGenerationIdAndUserId(generationId, userId)
                .map(MarketingDtoMapper::toResponseDto);
    }

    @Override
    public MarketingGenerationResponseDto patchMarketingByGenerationIdAndUserId(
            String generationId,
            String userId,
            UpdateMarketingGenerationRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("request is required");
        }

        MarketingGeneration generation = _marketingRepository
                .findByGenerationIdAndUserId(generationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Marketing generation not found"));

        if (request.getContentType() != null) {
            generation.setContentType(requireTrimmed(request.getContentType(), "contentType is required"));
        }
        if (request.getPlatform() != null) {
            generation.setPlatform(requireTrimmed(request.getPlatform(), "platform is required"));
        }
        if (request.getTarget_audience() != null) {
            generation.setTarget_audience(requireTrimmed(request.getTarget_audience(), "target_audience is required"));
        }
        if (request.getTone() != null) {
            generation.setTone(requireTrimmed(request.getTone(), "tone is required"));
        }
        if (request.getGoal() != null) {
            generation.setGoal(requireTrimmed(request.getGoal(), "goal is required"));
        }
        if (request.getProduct_name() != null) {
            generation.setProduct_name(requireTrimmed(request.getProduct_name(), "product_name is required"));
        }
        if (request.getBrand_name() != null) {
            generation.setBrand_name(requireTrimmed(request.getBrand_name(), "brand_name is required"));
        }
        if (request.getPrompt_input() != null) {
            generation.setPrompt_input(requireTrimmed(request.getPrompt_input(), "prompt_input is required"));
        }
        if (request.getGenerated_content() != null) {
            generation.setGenerated_content(requireTrimmed(request.getGenerated_content(), "generated_content is required"));
        }
        if (request.getStatus() != null) {
            generation.setStatus(normalizeStatusOrRequired(request.getStatus()));
        }
        if (request.getError_message() != null) {
            generation.setError_message(requireTrimmed(request.getError_message(), "error_message is required"));
        }

        generation.setUpdate_date(LocalDateTime.now());
        return MarketingDtoMapper.toResponseDto(_marketingRepository.save(generation));
    }

    @Override
    public CmsDashboardRecentItemsResponseDto getRecentItems(String userId, Integer limit) {
        int safeLimit = normalizeRecentItemsLimit(limit);

        List<CmsRecentItemResponseDto> generations = _marketingRepository.findRecentByUserId(userId, safeLimit)
                .stream()
                .map(this::toRecentGenerationItem)
                .toList();

        return new CmsDashboardRecentItemsResponseDto(generations, new ArrayList<>());
    }

    private int normalizeRecentItemsLimit(Integer limit) {
        if (limit == null) {
            return 5;
        }
        if (limit < 1) {
            return 1;
        }
        if (limit > 50) {
            return 50;
        }
        return limit;
    }

    @Override
    public MarketingGenerationResponseDto createMarketing(String userId, UpdateMarketingGenerationRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("request is required");
        }

        MarketingGeneration generation = new MarketingGeneration();
        LocalDateTime now = LocalDateTime.now();
        generation.setGenerationId(UUID.randomUUID().toString());
        generation.setUserId(userId);
        generation.setContentType(requireTrimmed(request.getContentType(), "contentType is required"));
        generation.setPlatform(requireTrimmed(request.getPlatform(), "platform is required"));
        generation.setTarget_audience(requireTrimmed(request.getTarget_audience(), "target_audience is required"));
        generation.setTone(requireTrimmed(request.getTone(), "tone is required"));
        generation.setGoal(requireTrimmed(request.getGoal(), "goal is required"));
        generation.setProduct_name(requireTrimmed(request.getProduct_name(), "product_name is required"));
        generation.setBrand_name(requireTrimmed(request.getBrand_name(), "brand_name is required"));
        generation.setPrompt_input(requireTrimmed(request.getPrompt_input(), "prompt_input is required"));
        String generatedContent = trimToNull(request.getGenerated_content());
        if (generatedContent != null) {
            generation.setGenerated_content(generatedContent);
        }
        generation.setStatus(normalizeStatusOrDefault(
                request.getStatus(),
                generatedContent == null ? "PENDING" : "COMPLETED"));
        if (request.getError_message() != null) {
            generation.setError_message(requireTrimmed(request.getError_message(), "error_message is required"));
        }
        generation.setCreateDate(now);
        generation.setUpdate_date(now);
        return MarketingDtoMapper.toResponseDto(_marketingRepository.save(generation));
    }

    private CmsRecentItemResponseDto toRecentGenerationItem(MarketingGeneration generation) {
        LocalDateTime timestamp = firstNonNull(generation.getCreateDate(), generation.getUpdate_date(), LocalDateTime.now());
        String title = firstNonBlank(generation.getProduct_name(), generation.getContentType());
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

    private String firstNonBlank(String first, String second) {
        if (first != null && !first.trim().isEmpty()) {
            return first;
        }
        if (second != null && !second.trim().isEmpty()) {
            return second;
        }
        return null;
    }

    private String requireTrimmed(String value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return trimmed;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeStatusOrRequired(String status) {
        String normalized = requireTrimmed(status, "status is required").toUpperCase(Locale.ROOT);
        if (!"PENDING".equals(normalized) && !"COMPLETED".equals(normalized) && !"FAILED".equals(normalized)) {
            throw new IllegalArgumentException("status must be one of: PENDING, COMPLETED, FAILED");
        }
        return normalized;
    }

    private String normalizeStatusOrDefault(String status, String defaultValue) {
        if (status == null || status.trim().isEmpty()) {
            return defaultValue;
        }
        return normalizeStatusOrRequired(status);
    }
}

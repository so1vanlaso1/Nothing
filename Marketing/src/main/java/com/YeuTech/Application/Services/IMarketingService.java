package com.YeuTech.Application.Services;

import java.util.List;
import java.util.Optional;

import com.YeuTech.Dtos.Request.UpdateMarketingGenerationRequestDto;
import com.YeuTech.Dtos.Response.CmsDashboardRecentItemsResponseDto;
import com.YeuTech.Dtos.Response.MarketingGenerationResponseDto;

public interface IMarketingService {
    List<MarketingGenerationResponseDto> getAllMarketing();
    List<MarketingGenerationResponseDto> getMarketingByUserId(String userId);
    Optional<MarketingGenerationResponseDto> getMarketingByGenerationIdAndUserId(String generationId, String userId);
    MarketingGenerationResponseDto patchMarketingByGenerationIdAndUserId(
            String generationId,
            String userId,
            UpdateMarketingGenerationRequestDto request);
    CmsDashboardRecentItemsResponseDto getRecentItems(String userId, Integer limit);
    MarketingGenerationResponseDto createMarketing(String userId, UpdateMarketingGenerationRequestDto request);
}
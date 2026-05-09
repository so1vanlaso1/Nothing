package com.YeuTech.Application.Mappers;

import com.YeuTech.Domain.Entities.MarketingGeneration;
import com.YeuTech.Dtos.Response.MarketingGenerationResponseDto;

public class MarketingDtoMapper {
    public static MarketingGenerationResponseDto toResponseDto(MarketingGeneration generation) {
        if (generation == null)
            return null;

        return new MarketingGenerationResponseDto(
                generation.getGenerationId(),
                generation.getUserId(),
                generation.getContentType(),
                generation.getPlatform(),
                generation.getTarget_audience(),
                generation.getTone(),
                generation.getGoal(),
                generation.getProduct_name(),
                generation.getBrand_name(),
                generation.getPrompt_input(),
                generation.getGenerated_content(),
                generation.getStatus(),
                generation.getError_message(),
                generation.getCreateDate(),
                generation.getUpdate_date()
        );
    }
}
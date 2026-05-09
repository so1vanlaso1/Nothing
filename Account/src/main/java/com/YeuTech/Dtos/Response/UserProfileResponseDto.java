package com.YeuTech.Dtos.Response;

import com.YeuTech.Dtos.MarketingConfigDto;

public record UserProfileResponseDto(
        String userId,
        String firstName,
        String lastName,
        String avatarUrl,
        MarketingConfigDto config
) {
}

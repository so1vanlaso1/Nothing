package com.YeuTech.Application.Services;

import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.MarketingConfigDto;
import com.YeuTech.Dtos.Response.UserProfileResponseDto;

public interface IUserProfileService {
    ApiResponseFormat<UserProfileResponseDto> getProfile(String email);

    ApiResponseFormat<UserProfileResponseDto> updateUserConfig(String email, MarketingConfigDto configDto);
}


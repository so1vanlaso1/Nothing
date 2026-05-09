package com.YeuTech.Api.Controllers;

import com.YeuTech.Application.Utils.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.YeuTech.Application.Services.IUserProfileService;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.MarketingConfigDto;
import com.YeuTech.Dtos.Response.UserProfileResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api/Account")
@Tag(name = "Profile", description = "User profile and marketing configuration APIs")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final IUserProfileService userProfileService;

    public ProfileController(IUserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/Settings")
    @Operation(summary = "Get user profile", description = "Retrieve current authenticated user's profile and marketing configuration")
    public ApiResponseFormat<UserProfileResponseDto> getProfile() {
        String email = SecurityUtils.getCurrentUserEmail();
        return userProfileService.getProfile(email);
    }

    @PatchMapping("/Settings")
    @Operation(summary = "Update user config", description = "Update user configuration (domain, industry, content) for the current user")
    public ApiResponseFormat<UserProfileResponseDto> updateConfig(@RequestBody MarketingConfigDto configDto) {
        String email = SecurityUtils.getCurrentUserEmail();
        return userProfileService.updateUserConfig(email, configDto);
    }
}


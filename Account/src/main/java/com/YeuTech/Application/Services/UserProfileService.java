package com.YeuTech.Application.Services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YeuTech.Application.Mappers.UserProfileMapper;
import com.YeuTech.Application.Utils.MergeUtils;
import com.YeuTech.Application.Utils.UserValidator;
import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Domain.Entities.UserProfile;
import com.YeuTech.Domain.Repository.IUserProfileRepository;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.MarketingConfigDto;
import com.YeuTech.Dtos.Response.UserProfileResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class UserProfileService implements IUserProfileService {

    private final IUserProfileRepository userProfileRepository;
    private final UserValidator userValidator;
    private final UserProfileMapper userProfileMapper;

    public UserProfileService(IUserProfileRepository userProfileRepository, UserValidator userValidator,
            UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userValidator = userValidator;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public ApiResponseFormat<UserProfileResponseDto> getProfile(String email) {
        User user = userValidator.findByEmailNormalized(email).orElse(null);
        if (user == null) {
            return new ApiResponseFormat<>(HttpStatus.NOT_FOUND.value(), "User not found", null);
        }

        if (!user.isActive() || !user.isEmailVerified()) {
            return new ApiResponseFormat<>(HttpStatus.FORBIDDEN.value(), "Account is not active or verified", null);
        }

        UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);
        if (profile == null) {
            return new ApiResponseFormat<>(HttpStatus.NOT_FOUND.value(), "Profile not found", null);
        }

        return new ApiResponseFormat<>(HttpStatus.OK.value(), "Profile retrieved successfully",
                userProfileMapper.toResponseDto(profile));
    }

    @Override
    @Transactional
    public ApiResponseFormat<UserProfileResponseDto> updateUserConfig(String email, MarketingConfigDto configDto) {
        User user = userValidator.findByEmailNormalized(email).orElse(null);
        if (user == null) {
            return new ApiResponseFormat<>(HttpStatus.NOT_FOUND.value(), "User not found", null);
        }

        if (!user.isActive() || !user.isEmailVerified()) {
            return new ApiResponseFormat<>(HttpStatus.FORBIDDEN.value(), "Account is not active or verified", null);
        }

        UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);
        if (profile == null) {
            return new ApiResponseFormat<>(HttpStatus.NOT_FOUND.value(), "Profile not found", null);
        }

        try {
            MarketingConfigDto existingConfig = userProfileMapper.toResponseDto(profile).config();

            MarketingConfigDto mergedConfig = new MarketingConfigDto(
                    MergeUtils.merge(configDto.domainName(),
                            existingConfig != null ? existingConfig.domainName() : null),
                    MergeUtils.merge(configDto.industryType(),
                            existingConfig != null ? existingConfig.industryType() : null),
                    MergeUtils.merge(configDto.relevantContent(),
                            existingConfig != null ? existingConfig.relevantContent() : null),
                    MergeUtils.merge(configDto.postingSchedule(),
                            existingConfig != null ? existingConfig.postingSchedule() : null));

            String configJson = userProfileMapper.toConfigJson(mergedConfig);
            profile.setConfig(configJson);
            userProfileRepository.save(profile);
            return new ApiResponseFormat<>(HttpStatus.OK.value(), "Marketing configuration updated successfully",
                    userProfileMapper.toResponseDto(profile));
        } catch (JsonProcessingException e) {
            return new ApiResponseFormat<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to process configuration", null);
        }
    }
}


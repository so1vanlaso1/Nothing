package com.YeuTech.Application.Mappers;

import org.springframework.stereotype.Component;

import com.YeuTech.Domain.Entities.UserProfile;
import com.YeuTech.Dtos.MarketingConfigDto;
import com.YeuTech.Dtos.Response.UserProfileResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserProfileMapper {

    private final ObjectMapper objectMapper;

    public UserProfileMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public UserProfileResponseDto toResponseDto(UserProfile profile) {
        if (profile == null) {
            return null;
        }

        MarketingConfigDto configDto = null;
        if (profile.getConfig() != null && !profile.getConfig().isEmpty()) {
            try {
                configDto = objectMapper.readValue(profile.getConfig(), MarketingConfigDto.class);
            } catch (JsonProcessingException e) {
                configDto = new MarketingConfigDto(null, null, null, null);
            }
        }

        return new UserProfileResponseDto(
                profile.getUserId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getAvatarUrl(),
                configDto);
    }

    public String toConfigJson(MarketingConfigDto configDto) throws JsonProcessingException {
        if (configDto == null) {
            return null;
        }
        return objectMapper.writeValueAsString(configDto);
    }
}

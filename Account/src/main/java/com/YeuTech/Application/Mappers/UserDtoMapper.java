package com.YeuTech.Application.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Dtos.Request.RegisterRequestDto;
import com.YeuTech.Dtos.Response.UserResponseDto;

public class UserDtoMapper {
    public static UserResponseDto toResponseDto(User user) {
        if (user == null)
            return null;
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.isActive(),
                user.isEmailVerified(),
                user.getCreatedDate(),
                user.getUpdatedDate());
    }

    public static User toDomain(RegisterRequestDto userDto) {
        if (userDto == null)
            return null;
        User user = new User();
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setActive(false);
        user.setEmailVerified(false);
        user.setCreatedDate(LocalDateTime.now());
        user.setUpdatedDate(LocalDateTime.now());
        return user;
    }

    public static User toDomain(RegisterRequestDto userDto, String encodedPassword) {
        if (userDto == null)
            return null;
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(userDto.email());
        user.setPassword(encodedPassword);
        user.setActive(false);
        user.setEmailVerified(false);
        user.setCreatedDate(LocalDateTime.now());
        user.setUpdatedDate(LocalDateTime.now());
        return user;
    }
}

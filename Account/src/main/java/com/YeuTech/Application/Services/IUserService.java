package com.YeuTech.Application.Services;

import java.util.List;
import java.util.Optional;

import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Dtos.Request.RegisterRequestDto;
import com.YeuTech.Dtos.Response.UserResponseDto;

public interface IUserService {
    List<UserResponseDto> getAllUsers();

    void addUser(RegisterRequestDto user);

    Optional<User> findByEmail(String email);
    UserResponseDto getUserByEmail(String email);

    UserResponseDto updateUserProfile(String currentEmail, RegisterRequestDto user);
}

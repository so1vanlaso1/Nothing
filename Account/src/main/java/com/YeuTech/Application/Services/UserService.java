package com.YeuTech.Application.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.YeuTech.Application.Mappers.UserDtoMapper;
import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Domain.Repository.IUserRepository;
import com.YeuTech.Dtos.Request.RegisterRequestDto;
import com.YeuTech.Dtos.Response.UserResponseDto;

@Service
public class UserService implements IUserService {

    private final IUserRepository _userRepository;

    public UserService(IUserRepository userRepository) {
        this._userRepository = userRepository;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return _userRepository.findAll().stream()
                .map(UserDtoMapper::toResponseDto)
                .toList();
    }

    @Override
    public void addUser(RegisterRequestDto userDto) {
        User user = UserDtoMapper.toDomain(userDto);
        _userRepository.save(user);
    }

    @Override
    public java.util.Optional<User> findByEmail(String email) {
        return _userRepository.findByEmail(email);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        User user = _userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return UserDtoMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto updateUserProfile(String currentEmail, RegisterRequestDto userDto) {
        User user = _userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + currentEmail));

        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setUpdatedDate(LocalDateTime.now());

        _userRepository.save(user);
        return UserDtoMapper.toResponseDto(user);
    }
}

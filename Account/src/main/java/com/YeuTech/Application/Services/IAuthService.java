package com.YeuTech.Application.Services;

import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.Request.ActiveRequestDto;
import com.YeuTech.Dtos.Request.ReactiveRequestDto;
import com.YeuTech.Dtos.Request.RefreshTokenRequestDto;
import com.YeuTech.Dtos.Request.RegisterRequestDto;
import com.YeuTech.Dtos.Response.AuthResponseDto;

public interface IAuthService {
    ApiResponseFormat<AuthResponseDto> register(RegisterRequestDto dto);

    ApiResponseFormat<AuthResponseDto> login(RegisterRequestDto dto);

    ApiResponseFormat<AuthResponseDto> activeAccount(ActiveRequestDto dto);

    ApiResponseFormat<AuthResponseDto> reactiveAccount(ReactiveRequestDto dto);

    ApiResponseFormat<AuthResponseDto> refreshToken(RefreshTokenRequestDto dto);
}


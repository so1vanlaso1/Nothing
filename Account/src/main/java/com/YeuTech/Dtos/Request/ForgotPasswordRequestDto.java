package com.YeuTech.Dtos.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ForgotPasswordRequestDto(
    @Schema(description = "User email", example = "user@example.com") String email,
    @Schema(description = "Preferred email language (en, vi, jp)", example = "jp") String language
)
{}

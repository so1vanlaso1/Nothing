package com.YeuTech.Dtos.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public record VerifyOtpRequestDto (
    @Schema(description = "User email", example = "user@example.com")
    String email,
    @Schema(description = "One-time password sent by email", example = "135790")
    String otp
) {} 

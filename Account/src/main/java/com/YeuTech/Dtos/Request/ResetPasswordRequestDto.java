package com.YeuTech.Dtos.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public record  ResetPasswordRequestDto(
    @Schema(description = "Verification token returned from OTP verification", example = "c2fcecb8-3f6c-4f44-91f5-8ec5f295ff88")
    String verificationToken,
    @Schema(description = "New password", example = "NewStrongPassword@123")
    String newPassword
) {}

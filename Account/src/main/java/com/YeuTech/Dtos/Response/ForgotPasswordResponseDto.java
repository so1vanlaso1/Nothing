package com.YeuTech.Dtos.Response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ForgotPasswordResponseDto(
    @Schema(description = "Short-lived verification token for reset password", example = "c2fcecb8-3f6c-4f44-91f5-8ec5f295ff88")
    String token
) {}

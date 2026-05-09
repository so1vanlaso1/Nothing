package com.YeuTech.Dtos.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

public record AuthResponseDto(
        @Schema(description = "User email", example = "user@example.com")
        String email,
        @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiJ9.access.token")
        String accessToken,
        @Schema(description = "JWT refresh token", example = "eyJhbGciOiJIUzI1NiJ9.refresh.token")
        String refreshToken,
        @Schema(description = "Access token expiration date", example = "2026-04-03T12:00:00.000+00:00")
        Date expiryTime) {
}

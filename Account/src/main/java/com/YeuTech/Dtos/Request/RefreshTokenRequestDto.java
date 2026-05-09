package com.YeuTech.Dtos.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public record RefreshTokenRequestDto (
        @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiJ9.refresh.token")
        String refreshToken
) {
}

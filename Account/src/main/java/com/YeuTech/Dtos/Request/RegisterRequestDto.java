package com.YeuTech.Dtos.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterRequestDto(
                @Schema(description = "User email", example = "user@example.com") String email,
                @Schema(description = "User password", example = "StrongPassword@123") String password,
                @Schema(description = "Preferred email language (en, vi, jp)", example = "vi") String language) {
}

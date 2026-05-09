package com.YeuTech.Dtos.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record UserResponseDto(
                @Schema(description = "User identifier", example = "d6cb2f72-eccc-4f02-9f89-c9f6ca04835f")
                String id,
                @Schema(description = "User email", example = "user@example.com")
                String email,
                @Schema(description = "Account activation status", example = "true")
                boolean isActive,
                @Schema(description = "Email verification status", example = "true")
                boolean isEmailVerified,
                @Schema(description = "Creation date", example = "2026-04-01T08:30:00")
                LocalDateTime createdDate,
                @Schema(description = "Last update date", example = "2026-04-02T10:00:00")
                LocalDateTime updatedDate) {
}

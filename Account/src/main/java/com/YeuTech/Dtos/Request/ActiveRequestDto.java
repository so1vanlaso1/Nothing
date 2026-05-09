package com.YeuTech.Dtos.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ActiveRequestDto(
        @Schema(description = "User email", example = "user@example.com")
        String email,
        @Schema(description = "Activation code sent to email", example = "873921")
        String code) {
}

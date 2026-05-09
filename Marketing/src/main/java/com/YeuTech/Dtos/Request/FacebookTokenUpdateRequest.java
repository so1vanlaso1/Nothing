package com.YeuTech.Dtos.Request;

import jakarta.validation.constraints.NotBlank;

public record FacebookTokenUpdateRequest(
        @NotBlank(message = "Access token is required") String accessToken,
        @NotBlank(message = "Page ID is required") String pageId,
        boolean exchangeForLongLived) {
}
package com.YeuTech.Dtos.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record FacebookPostRequest(
    @NotBlank(message = "Message cannot be empty")
    @Schema(description = "The message to post on the Facebook Page", example = "Hello, world!")
    String message
) {}

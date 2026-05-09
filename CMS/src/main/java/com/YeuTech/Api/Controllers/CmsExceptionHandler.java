package com.YeuTech.Api.Controllers;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.YeuTech.Dtos.ApiResponseFormat;

@RestControllerAdvice(basePackages = "com.YeuTech.Api.Controllers")
public class CmsExceptionHandler {

    private static final Set<String> NOT_FOUND_MESSAGES = Set.of(
            "CMS content not found",
            "Marketing generation not found",
            "Current user not found",
            "Custom domain not found",
            "Content has not been published to any domain",
            "CMS content not found or not in PUBLISHED status");

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseFormat<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        HttpStatus status = NOT_FOUND_MESSAGES.contains(ex.getMessage())
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;

        ApiResponseFormat<Object> response = new ApiResponseFormat<>(status.value(), ex.getMessage(), null);
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseFormat<Object>> handleUnexpected(Exception ex) {
        ApiResponseFormat<Object> response = new ApiResponseFormat<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected CMS error",
                null);
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

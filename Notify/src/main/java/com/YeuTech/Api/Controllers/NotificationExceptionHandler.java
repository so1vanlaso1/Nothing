package com.YeuTech.Api.Controllers;

import java.time.LocalDateTime;

import com.YeuTech.Dtos.ApiResponseFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = NotificationController.class)
public class NotificationExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseFormat<Object>> handleBadRequest(IllegalArgumentException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if ("Notification not found".equals(ex.getMessage())) {
            status = HttpStatus.NOT_FOUND;
        }

        ApiResponseFormat<Object> response = new ApiResponseFormat<>(status.value(), ex.getMessage(), null);
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseFormat<Object>> handleUnexpected(Exception ex) {
        ApiResponseFormat<Object> response = new ApiResponseFormat<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected notification error",
                null);
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


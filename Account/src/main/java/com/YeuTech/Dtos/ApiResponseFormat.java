package com.YeuTech.Dtos;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper")
public class ApiResponseFormat<T> {
    @Schema(description = "HTTP status code", example = "200")
    private int status;

    @Schema(description = "Human-readable message", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Response payload")
    private T data;

    @Schema(description = "Response timestamp", example = "2026-04-02T12:00:00")
    private LocalDateTime timestamp;

    public ApiResponseFormat(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

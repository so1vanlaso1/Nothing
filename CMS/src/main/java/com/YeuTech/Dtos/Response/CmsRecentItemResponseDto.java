package com.YeuTech.Dtos.Response;

import java.time.LocalDateTime;

public class CmsRecentItemResponseDto {
    private String id;
    private String title;
    private String platform;
    private String status;
    private String date;
    private LocalDateTime timestamp;

    public CmsRecentItemResponseDto() {
    }

    public CmsRecentItemResponseDto(
            String id,
            String title,
            String platform,
            String status,
            String date,
            LocalDateTime timestamp) {
        this.id = id;
        this.title = title;
        this.platform = platform;
        this.status = status;
        this.date = date;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
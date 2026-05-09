package com.YeuTech.Dtos;

import java.time.LocalDateTime;

public class TokenStatusDto {

    private boolean connected;
    private boolean tokenValid;
    private boolean canAutoRefresh;
    private LocalDateTime tokenExpiryDate;
    private String pageId;
    private String message;

    public static TokenStatusDto notConnected() {
        TokenStatusDto dto = new TokenStatusDto();
        dto.connected = false;
        dto.tokenValid = false;
        dto.canAutoRefresh = false;
        dto.message = "No Facebook connection found. Please complete the OAuth flow.";
        return dto;
    }

    public static TokenStatusDto valid(String pageId, LocalDateTime expiryDate, boolean canAutoRefresh) {
        TokenStatusDto dto = new TokenStatusDto();
        dto.connected = true;
        dto.tokenValid = true;
        dto.canAutoRefresh = canAutoRefresh;
        dto.pageId = pageId;
        dto.tokenExpiryDate = expiryDate;
        dto.message = "Token is valid.";
        return dto;
    }

    public static TokenStatusDto expired(String pageId, boolean canAutoRefresh) {
        TokenStatusDto dto = new TokenStatusDto();
        dto.connected = true;
        dto.tokenValid = false;
        dto.canAutoRefresh = canAutoRefresh;
        dto.pageId = pageId;
        dto.message = canAutoRefresh
                ? "Token expired but can be auto-refreshed. Call POST /marketing/config/facebook/token/refresh."
                : "Token expired and cannot be auto-refreshed. Please reconnect your Facebook account.";
        return dto;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isTokenValid() {
        return tokenValid;
    }

    public void setTokenValid(boolean tokenValid) {
        this.tokenValid = tokenValid;
    }

    public boolean isCanAutoRefresh() {
        return canAutoRefresh;
    }

    public void setCanAutoRefresh(boolean canAutoRefresh) {
        this.canAutoRefresh = canAutoRefresh;
    }

    public LocalDateTime getTokenExpiryDate() {
        return tokenExpiryDate;
    }

    public void setTokenExpiryDate(LocalDateTime tokenExpiryDate) {
        this.tokenExpiryDate = tokenExpiryDate;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
package com.YeuTech.Domain.Entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class EmailOtp {

    private String id;
    private String userId;
    private String otpCode;
    private boolean isUsed;
    private LocalDateTime expiresAt;
    private LocalDateTime usedAt;
    private LocalDateTime createdDate;

    public EmailOtp() {
    }

    public EmailOtp(String userId, String otpCode, int expireMinutes) {
        this.id          = UUID.randomUUID().toString();
        this.userId      = userId;
        this.otpCode     = otpCode;
        this.isUsed      = false;
        this.expiresAt   = LocalDateTime.now().plusMinutes(expireMinutes);
        this.createdDate = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    public void markAsUsed() {
        this.isUsed = true;
        this.usedAt = LocalDateTime.now();
    }

    public boolean verify(String inputCode) {
        return this.otpCode.equals(inputCode);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
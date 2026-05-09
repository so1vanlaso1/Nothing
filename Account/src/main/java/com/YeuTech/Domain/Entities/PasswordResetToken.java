package com.YeuTech.Domain.Entities;

import java.time.LocalDateTime;

public class PasswordResetToken {
    private String tokenId;
    private String userId;
    private String tokenHash;
    private LocalDateTime expiresAt;
    private boolean used = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime usedAt;

    public PasswordResetToken() {}

    public PasswordResetToken(String userId, String tokenHash, LocalDateTime expiresAt) {
        this.tokenId = java.util.UUID.randomUUID().toString();
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
    }   

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

 
}

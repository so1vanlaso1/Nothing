package com.YeuTech.Domain.Entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class RefreshToken {
    private String id;
    private String userId;
    private String tokenHash;
    private String jti;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private boolean isRevoked;
    private LocalDateTime revokedAt;
    private LocalDateTime createDate;

    public RefreshToken() {}

    public RefreshToken(String userId, String tokenHash, String jti,
                        LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.id          = UUID.randomUUID().toString();
        this.userId      = userId;
        this.tokenHash   = tokenHash;
        this.jti         = jti;
        this.issuedAt    = issuedAt;
        this.expiresAt   = expiresAt;
        this.isRevoked   = false;
        this.createDate  = LocalDateTime.now();
    }

    public void revoke() {
        this.isRevoked = true;
        this.revokedAt = LocalDateTime.now();
    }
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }

    public String getJti() { return jti; }
    public void setJti(String jti) { this.jti = jti; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public boolean isRevoked() { return isRevoked; }
    public void setRevoked(boolean revoked) { isRevoked = revoked; }

    public LocalDateTime getRevokedAt() { return revokedAt; }
    public void setRevokedAt(LocalDateTime revokedAt) { this.revokedAt = revokedAt; }

    public LocalDateTime getCreateDate() { return createDate; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }
}
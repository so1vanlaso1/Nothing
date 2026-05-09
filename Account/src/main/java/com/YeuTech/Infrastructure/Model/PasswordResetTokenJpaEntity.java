package com.YeuTech.Infrastructure.Model;

import java.sql.Types;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetTokenJpaEntity {

    @Id
    @JdbcTypeCode(Types.CHAR)
    @Column(name = "token_id", length = 36, nullable = false)
    private String tokenId;

    @JdbcTypeCode(Types.CHAR)
    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "token_hash", nullable = false, length = 255)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "used", nullable = false)
    private boolean used = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    
    public String getTokenId() { return tokenId; }
    public String getUserId() { return userId; }
    public String getTokenHash() { return tokenHash; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public boolean isUsed() { return used; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUsedAt() { return usedAt; }

    // Setters
    public void setTokenId(String tokenId) { this.tokenId = tokenId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public void setUsed(boolean used) { this.used = used; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }

    // public void CreateTokenForUser(String userId, String tokenHash, LocalDateTime expiresAt) {
    //     this.userId = userId;
    //     this.tokenHash = tokenHash;
    //     this.expiresAt = expiresAt;
    // }
}
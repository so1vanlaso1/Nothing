package com.YeuTech.Domain.Entities;

import java.time.LocalDateTime;

import com.YeuTech.Domain.Enums.SocialPlatform;

public class SocialMediaConnection {
    private String id;
    private String userId;
    private SocialPlatform platform;
    private String accessToken;
    private String longLivedUserToken;
    private String pageId;
    private LocalDateTime tokenExpiryDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public SocialMediaConnection() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SocialPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(SocialPlatform platform) {
        this.platform = platform;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLongLivedUserToken() {
        return longLivedUserToken;
    }

    public void setLongLivedUserToken(String longLivedUserToken) {
        this.longLivedUserToken = longLivedUserToken;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public LocalDateTime getTokenExpiryDate() {
        return tokenExpiryDate;
    }

    public void setTokenExpiryDate(LocalDateTime tokenExpiryDate) {
        this.tokenExpiryDate = tokenExpiryDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}

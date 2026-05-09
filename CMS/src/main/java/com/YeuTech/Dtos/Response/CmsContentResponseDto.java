package com.YeuTech.Dtos.Response;

import java.time.LocalDateTime;

public class CmsContentResponseDto {
    private String contentId;
    private String userId;
    private String generationId;
    private String title;
    private String contentBody;
    private String contentType;
    private String platform;
    private String status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public CmsContentResponseDto() {
    }

    public CmsContentResponseDto(
            String contentId,
            String userId,
            String generationId,
            String title,
            String contentBody,
            String contentType,
            String platform,
            String status,
            LocalDateTime createDate,
            LocalDateTime updateDate) {
        this.contentId = contentId;
        this.userId = userId;
        this.generationId = generationId;
        this.title = title;
        this.contentBody = contentBody;
        this.contentType = contentType;
        this.platform = platform;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGenerationId() {
        return generationId;
    }

    public void setGenerationId(String generationId) {
        this.generationId = generationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}

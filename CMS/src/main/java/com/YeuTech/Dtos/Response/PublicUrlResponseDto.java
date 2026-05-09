package com.YeuTech.Dtos.Response;

public class PublicUrlResponseDto {
    private String contentId;
    private String canonicalUrl;
    private boolean isActive;

    public PublicUrlResponseDto() {
    }

    public PublicUrlResponseDto(String contentId, String canonicalUrl, boolean isActive) {
        this.contentId = contentId;
        this.canonicalUrl = canonicalUrl;
        this.isActive = isActive;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

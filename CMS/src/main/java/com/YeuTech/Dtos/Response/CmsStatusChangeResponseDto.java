package com.YeuTech.Dtos.Response;

import java.time.LocalDateTime;

public class CmsStatusChangeResponseDto {
    private String contentId;
    private String status;
    private LocalDateTime updateDate;

    public CmsStatusChangeResponseDto() {
    }

    public CmsStatusChangeResponseDto(String contentId, String status, LocalDateTime updateDate) {
        this.contentId = contentId;
        this.status = status;
        this.updateDate = updateDate;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}

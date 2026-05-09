package com.YeuTech.Dtos.Response;

import java.time.LocalDateTime;

public class CustomDomainResponseDto {
    private String domainId;
    private String userId;
    private String domainName;
    private String verificationToken;
    private String verificationStatus;
    private String dnsStatus;
    private String sslStatus;
    private boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public CustomDomainResponseDto() {
    }

    public CustomDomainResponseDto(
            String domainId,
            String userId,
            String domainName,
            String verificationToken,
            String verificationStatus,
            String dnsStatus,
            String sslStatus,
            boolean isActive,
            LocalDateTime createdDate,
            LocalDateTime updatedDate) {
        this.domainId = domainId;
        this.userId = userId;
        this.domainName = domainName;
        this.verificationToken = verificationToken;
        this.verificationStatus = verificationStatus;
        this.dnsStatus = dnsStatus;
        this.sslStatus = sslStatus;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getDnsStatus() {
        return dnsStatus;
    }

    public void setDnsStatus(String dnsStatus) {
        this.dnsStatus = dnsStatus;
    }

    public String getSslStatus() {
        return sslStatus;
    }

    public void setSslStatus(String sslStatus) {
        this.sslStatus = sslStatus;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

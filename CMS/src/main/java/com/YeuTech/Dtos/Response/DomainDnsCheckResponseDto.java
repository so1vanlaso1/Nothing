package com.YeuTech.Dtos.Response;

public class DomainDnsCheckResponseDto {
    private String domainId;
    private String dnsStatus;
    private String message;

    public DomainDnsCheckResponseDto() {
    }

    public DomainDnsCheckResponseDto(String domainId, String dnsStatus, String message) {
        this.domainId = domainId;
        this.dnsStatus = dnsStatus;
        this.message = message;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getDnsStatus() {
        return dnsStatus;
    }

    public void setDnsStatus(String dnsStatus) {
        this.dnsStatus = dnsStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

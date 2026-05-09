package com.YeuTech.Dtos.Response;

public class DomainVerificationResponseDto {
    private String domainId;
    private String verificationToken;
    private String verificationStatus;
    private String instructions;

    public DomainVerificationResponseDto() {
    }

    public DomainVerificationResponseDto(
            String domainId,
            String verificationToken,
            String verificationStatus,
            String instructions) {
        this.domainId = domainId;
        this.verificationToken = verificationToken;
        this.verificationStatus = verificationStatus;
        this.instructions = instructions;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}

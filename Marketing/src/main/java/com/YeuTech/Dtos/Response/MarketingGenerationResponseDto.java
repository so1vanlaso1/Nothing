package com.YeuTech.Dtos.Response;

import java.time.LocalDateTime;

public class MarketingGenerationResponseDto {
    private String generationId;
    private String userId;
    private String contentType;
    private String platform;
    private String target_audience;
    private String status;
    private String error_message;
    private LocalDateTime createDate;
    private LocalDateTime update_date;
    private String product_name;
    private String brand_name;
    private String prompt_input;
    private String generated_content;
    private String tone;
    private String goal;

    public MarketingGenerationResponseDto() {
    }

    public MarketingGenerationResponseDto(String generationId, String userId, String contentType, String platform, String target_audience, String tone, String goal, String product_name, String brand_name, String prompt_input, String generated_content,
            String status, String error_message, LocalDateTime createDate, LocalDateTime update_date) {
        this.generationId = generationId;
        this.userId = userId;
        this.contentType = contentType;
        this.platform = platform;
        this.target_audience = target_audience;
        this.status = status;
        this.error_message = error_message;
        this.createDate = createDate;
        this.update_date = update_date;
        this.contentType = contentType;
        this.platform = platform;
        this.product_name = product_name;
        this.brand_name = brand_name;
        this.prompt_input = prompt_input;
        this.generated_content = generated_content;
        this.tone = tone;
        this.goal = goal;
    }

    public String getGenerationId() {
        return generationId;
    }

    public void setGenerationId(String generationId) {
        this.generationId = generationId;
    }

    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getTarget_audience() {
        return target_audience;
    }

    public void setTarget_audience(String target_audience) {
        this.target_audience = target_audience;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(LocalDateTime update_date) {
        this.update_date = update_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getPrompt_input() {
        return prompt_input;
    }

    public void setPrompt_input(String prompt_input) {
        this.prompt_input = prompt_input;
    }

    public String getGenerated_content() {
        return generated_content;
    }

    public void setGenerated_content(String generated_content) {
        this.generated_content = generated_content;
    }

    public String getTone() {
        return tone;
    }
    
    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
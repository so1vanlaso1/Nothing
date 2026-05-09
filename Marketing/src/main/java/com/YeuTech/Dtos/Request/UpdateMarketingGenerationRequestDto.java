package com.YeuTech.Dtos.Request;

import com.fasterxml.jackson.annotation.JsonAlias;

public class UpdateMarketingGenerationRequestDto {
    private String contentType;
    private String platform;
    @JsonAlias("targetAudience")
    private String target_audience;
    private String tone;
    private String goal;
    @JsonAlias("productName")
    private String product_name;
    @JsonAlias("brandName")
    private String brand_name;
    @JsonAlias("promptInput")
    private String prompt_input;
    @JsonAlias("generatedContent")
    private String generated_content;
    private String status;
    @JsonAlias("errorMessage")
    private String error_message;

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
}

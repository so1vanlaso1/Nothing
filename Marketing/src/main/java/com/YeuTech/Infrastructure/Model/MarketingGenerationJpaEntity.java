package com.YeuTech.Infrastructure.Model;

import java.sql.Types;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "marketing_generations")
public class MarketingGenerationJpaEntity {

    @Id
    @JdbcTypeCode(Types.CHAR)
    @Column(name = "generation_id", nullable = false, length = 36)
    private String generationId;

    @JdbcTypeCode(Types.CHAR)
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "content_type", nullable = false, length = 50)
    private String contentType;

    @Column(name = "platform", length = 50)
    private String platform;

    @Column(name = "target_audience", length = 255)
    private String target_audience;

    @Column(name = "tone", length = 50)
    private String tone;

    @Column(name = "goal", length = 255)
    private String goal;

    @Column(name = "product_name", length = 255)
    private String product_name;

    @Column(name = "brand_name", length = 255)
    private String brand_name;

    @Column(name = "prompt_input", columnDefinition = "TEXT")
    private String prompt_input;

    @Column(name = "generated_content", columnDefinition = "LONGTEXT")
    private String generated_content;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "error_message", length = 1000)
    private String error_message;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime update_date;

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
}

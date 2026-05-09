package com.YeuTech.Dtos.Response;

import java.time.LocalDateTime;

public class NotificationResponseDto {
    private String notificationId;
    private String generationId;
    private String contentId;
    private String channel;
    private String eventType;
    private String title;
    private String message;
    private String recipientEmail;
    private String actionUrl;
    private String metadataJson;
    private String deliveryStatus;
    private LocalDateTime sentAt;
    private String errorMessage;
    private LocalDateTime readAt;
    private LocalDateTime createDate;

    public NotificationResponseDto() {
    }

    public NotificationResponseDto(String notificationId, String generationId, String contentId, String channel,
            String eventType, String title, String message, String recipientEmail, String actionUrl,
            String metadataJson, String deliveryStatus, LocalDateTime sentAt, String errorMessage,
            LocalDateTime readAt, LocalDateTime createDate) {
        this.notificationId = notificationId;
        this.generationId = generationId;
        this.contentId = contentId;
        this.channel = channel;
        this.eventType = eventType;
        this.title = title;
        this.message = message;
        this.recipientEmail = recipientEmail;
        this.actionUrl = actionUrl;
        this.metadataJson = metadataJson;
        this.deliveryStatus = deliveryStatus;
        this.sentAt = sentAt;
        this.errorMessage = errorMessage;
        this.readAt = readAt;
        this.createDate = createDate;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getGenerationId() {
        return generationId;
    }

    public void setGenerationId(String generationId) {
        this.generationId = generationId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getMetadataJson() {
        return metadataJson;
    }

    public void setMetadataJson(String metadataJson) {
        this.metadataJson = metadataJson;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}

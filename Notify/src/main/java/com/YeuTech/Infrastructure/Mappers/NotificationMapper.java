package com.YeuTech.Infrastructure.Mappers;

import com.YeuTech.Domain.Entities.Notification;
import com.YeuTech.Infrastructure.Model.NotificationJpaEntity;

public class NotificationMapper {
    public static Notification toDomain(NotificationJpaEntity entity) {
        Notification notification = new Notification();
        notification.setNotificationId(entity.getNotificationId());
        notification.setUserId(entity.getUserId());
        notification.setGenerationId(entity.getGenerationId());
        notification.setContentId(entity.getContentId());
        notification.setChannel(entity.getChannel());
        notification.setEventType(entity.getEventType());
        notification.setTitle(entity.getTitle());
        notification.setMessage(entity.getMessage());
        notification.setRecipientEmail(entity.getRecipientEmail());
        notification.setActionUrl(entity.getActionUrl());
        notification.setMetadataJson(entity.getMetadataJson());
        notification.setDeliveryStatus(entity.getDeliveryStatus());
        notification.setSentAt(entity.getSentAt());
        notification.setErrorMessage(entity.getErrorMessage());
        notification.setReadAt(entity.getReadAt());
        notification.setCreateDate(entity.getCreateDate());
        notification.setUpdateDate(entity.getUpdateDate());
        return notification;
    }

    public static NotificationJpaEntity toEntity(Notification notification) {
        NotificationJpaEntity entity = new NotificationJpaEntity();
        entity.setNotificationId(notification.getNotificationId());
        entity.setUserId(notification.getUserId());
        entity.setGenerationId(notification.getGenerationId());
        entity.setContentId(notification.getContentId());
        entity.setChannel(notification.getChannel());
        entity.setEventType(notification.getEventType());
        entity.setTitle(notification.getTitle());
        entity.setMessage(notification.getMessage());
        entity.setRecipientEmail(notification.getRecipientEmail());
        entity.setActionUrl(notification.getActionUrl());
        entity.setMetadataJson(notification.getMetadataJson());
        entity.setDeliveryStatus(notification.getDeliveryStatus());
        entity.setSentAt(notification.getSentAt());
        entity.setErrorMessage(notification.getErrorMessage());
        entity.setReadAt(notification.getReadAt());
        entity.setCreateDate(notification.getCreateDate());
        entity.setUpdateDate(notification.getUpdateDate());
        return entity;
    }
}

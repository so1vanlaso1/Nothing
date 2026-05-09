package com.YeuTech.Application.Mappers;

import com.YeuTech.Domain.Entities.Notification;
import com.YeuTech.Dtos.Response.NotificationResponseDto;

public class NotificationDtoMapper {
    public static NotificationResponseDto toResponseDto(Notification notification) {
        if (notification == null)
            return null;

        return new NotificationResponseDto(
                notification.getNotificationId(),
                notification.getGenerationId(),
                notification.getContentId(),
                notification.getChannel(),
                notification.getEventType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getRecipientEmail(),
                notification.getActionUrl(),
                notification.getMetadataJson(),
                notification.getDeliveryStatus(),
                notification.getSentAt(),
                notification.getErrorMessage(),
                notification.getReadAt(),
                notification.getCreateDate());
    }
}

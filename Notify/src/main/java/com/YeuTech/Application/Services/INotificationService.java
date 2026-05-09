package com.YeuTech.Application.Services;

import com.YeuTech.Dtos.Request.NotificationQueryRequestDto;
import com.YeuTech.Dtos.Request.SendNotificationRequestDto;
import com.YeuTech.Dtos.Response.MarkReadResponseDto;
import com.YeuTech.Dtos.Response.NotificationListResponseDto;
import com.YeuTech.Dtos.Response.NotificationResponseDto;

public interface INotificationService {
    NotificationResponseDto sendNotification(String currentUserEmail, SendNotificationRequestDto request);

    NotificationListResponseDto listMyNotifications(String currentUserEmail, NotificationQueryRequestDto request);

    NotificationResponseDto markAsRead(String currentUserEmail, String notificationId);

    MarkReadResponseDto markAllAsRead(String currentUserEmail);
}

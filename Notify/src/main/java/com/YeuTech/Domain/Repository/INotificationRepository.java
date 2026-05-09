package com.YeuTech.Domain.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.YeuTech.Domain.Entities.Notification;

public interface INotificationRepository {
    Notification save(Notification notification);

    Page<Notification> findByUserIdWithFilters(
            String userId,
            String channel,
            String eventType,
            String deliveryStatus,
            Boolean isRead,
            Pageable pageable);

    Optional<Notification> findByNotificationIdAndUserId(String notificationId, String userId);

    long countUnreadByUserId(String userId);

    int markAllAsRead(String userId, LocalDateTime readAt);
}

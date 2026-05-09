package com.YeuTech.Infrastructure.Dao;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.YeuTech.Infrastructure.Model.NotificationJpaEntity;

@Repository
public interface NotificationJpaDao extends JpaRepository<NotificationJpaEntity, String> {

    @Query("""
            SELECT n
            FROM NotificationJpaEntity n
            WHERE n.userId = :userId
              AND (:channel IS NULL OR n.channel = :channel)
              AND (:eventType IS NULL OR n.eventType = :eventType)
              AND (:deliveryStatus IS NULL OR n.deliveryStatus = :deliveryStatus)
              AND (
                   :isRead IS NULL
                   OR (:isRead = TRUE AND n.readAt IS NOT NULL)
                   OR (:isRead = FALSE AND n.readAt IS NULL)
              )
            ORDER BY n.createDate DESC
            """)
    Page<NotificationJpaEntity> findByUserIdWithFilters(
            @Param("userId") String userId,
            @Param("channel") String channel,
            @Param("eventType") String eventType,
            @Param("deliveryStatus") String deliveryStatus,
            @Param("isRead") Boolean isRead,
            Pageable pageable);

    java.util.Optional<NotificationJpaEntity> findByNotificationIdAndUserId(String notificationId, String userId);

    long countByUserIdAndReadAtIsNull(String userId);

    @Modifying
    @Query("""
            UPDATE NotificationJpaEntity n
            SET n.readAt = :readAt
            WHERE n.userId = :userId
              AND n.readAt IS NULL
            """)
    int markAllAsRead(@Param("userId") String userId, @Param("readAt") LocalDateTime readAt);
}

package com.YeuTech.Infrastructure.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.YeuTech.Domain.Entities.Notification;
import com.YeuTech.Domain.Repository.INotificationRepository;
import com.YeuTech.Infrastructure.Dao.NotificationJpaDao;
import com.YeuTech.Infrastructure.Mappers.NotificationMapper;

@Repository
public class NotificationRepository implements INotificationRepository {

    private final NotificationJpaDao notificationJpaDao;

    public NotificationRepository(NotificationJpaDao notificationJpaDao) {
        this.notificationJpaDao = notificationJpaDao;
    }

    @Override
    public Notification save(Notification notification) {
        return NotificationMapper.toDomain(notificationJpaDao.save(NotificationMapper.toEntity(notification)));
    }

    @Override
    public Page<Notification> findByUserIdWithFilters(String userId, String channel, String eventType,
            String deliveryStatus, Boolean isRead, Pageable pageable) {
        return notificationJpaDao.findByUserIdWithFilters(userId, channel, eventType, deliveryStatus, isRead, pageable)
                .map(NotificationMapper::toDomain);
    }

    @Override
    public Optional<Notification> findByNotificationIdAndUserId(String notificationId, String userId) {
        return notificationJpaDao.findByNotificationIdAndUserId(notificationId, userId)
                .map(NotificationMapper::toDomain);
    }

    @Override
    public long countUnreadByUserId(String userId) {
        return notificationJpaDao.countByUserIdAndReadAtIsNull(userId);
    }

    @Override
    public int markAllAsRead(String userId, LocalDateTime readAt) {
        return notificationJpaDao.markAllAsRead(userId, readAt);
    }
}

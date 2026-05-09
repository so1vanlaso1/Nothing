package com.YeuTech.Application.Services;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YeuTech.Application.Mappers.NotificationDtoMapper;
import com.YeuTech.Domain.Entities.Notification;
import com.YeuTech.Domain.Entities.User;
import com.YeuTech.Domain.Repository.ICmsRepository;
import com.YeuTech.Domain.Repository.IMarketingRepository;
import com.YeuTech.Domain.Repository.INotificationRepository;
import com.YeuTech.Domain.Repository.IUserRepository;
import com.YeuTech.Dtos.Request.NotificationQueryRequestDto;
import com.YeuTech.Dtos.Request.SendNotificationRequestDto;
import com.YeuTech.Dtos.Response.MarkReadResponseDto;
import com.YeuTech.Dtos.Response.NotificationListResponseDto;
import com.YeuTech.Dtos.Response.NotificationResponseDto;


@Service
public class NotificationService implements INotificationService {

    private static final String CHANNEL_EMAIL = "EMAIL";
    private static final String CHANNEL_IN_APP = "IN_APP";
    private static final String DELIVERY_PENDING = "PENDING";
    private static final String DELIVERY_SENT = "SENT";
    private static final String DELIVERY_FAILED = "FAILED";

    private final INotificationRepository _notificationRepository;
    private final IUserRepository _userRepository;
    private final IMarketingRepository _marketingRepository;
    private final ICmsRepository _cmsRepository;
    private final IMailService _mailService;

    public NotificationService(
            INotificationRepository notificationRepository,
            IUserRepository userRepository,
            IMarketingRepository marketingRepository,
            ICmsRepository cmsRepository,
            IMailService mailService) {
        this._notificationRepository = notificationRepository;
        this._userRepository = userRepository;
        this._marketingRepository = marketingRepository;
        this._cmsRepository = cmsRepository;
        this._mailService = mailService;
    }

    @Override
    @Transactional
    public NotificationResponseDto sendNotification(String currentUserEmail, SendNotificationRequestDto request) {
        String userId = resolveCurrentUserId(currentUserEmail);

        String channel = normalizeUpper(request.getChannel());
        validateChannel(channel);
        validateRequiredFields(request);
        validateBusinessReferences(userId, request);

        Notification notification = new Notification();
        notification.setNotificationId(UUID.randomUUID().toString());
        notification.setUserId(userId);
        notification.setGenerationId(request.getGenerationId());
        notification.setContentId(request.getContentId());
        notification.setChannel(channel);
        notification.setEventType(normalizeUpper(request.getEventType()));
        notification.setTitle(request.getTitle().trim());
        notification.setMessage(request.getMessage().trim());
        notification.setActionUrl(request.getActionUrl());
        notification.setMetadataJson(request.getMetadataJson());
        notification.setDeliveryStatus(DELIVERY_PENDING);
        notification.setCreateDate(LocalDateTime.now());
        notification.setUpdateDate(LocalDateTime.now());

        if (CHANNEL_EMAIL.equals(channel)) {
            String recipientEmail = request.getRecipientEmail();
            if (recipientEmail == null || recipientEmail.isBlank()) {
                recipientEmail = currentUserEmail;
            }
            notification.setRecipientEmail(recipientEmail);
        }

        Notification saved = _notificationRepository.save(notification);

        if (CHANNEL_IN_APP.equals(channel)) {
            saved.setDeliveryStatus(DELIVERY_SENT);
            saved.setSentAt(LocalDateTime.now());
            saved.setUpdateDate(LocalDateTime.now());
            return NotificationDtoMapper.toResponseDto(_notificationRepository.save(saved));
        }

        try {
            _mailService.sendNotificationEmail(
                saved.getRecipientEmail(),
                saved.getTitle(),
                saved.getMessage(),
                saved.getActionUrl(),
                request.getLanguage());

            saved.setDeliveryStatus(DELIVERY_SENT);
            saved.setSentAt(LocalDateTime.now());
            saved.setErrorMessage(null);
        } catch (Exception ex) {
            saved.setDeliveryStatus(DELIVERY_FAILED);
            saved.setErrorMessage(limitError(ex.getMessage()));
        }

        saved.setUpdateDate(LocalDateTime.now());
        return NotificationDtoMapper.toResponseDto(_notificationRepository.save(saved));
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationListResponseDto listMyNotifications(String currentUserEmail, NotificationQueryRequestDto request) {
        String userId = resolveCurrentUserId(currentUserEmail);

        int page = request.getPage() == null || request.getPage() < 0 ? 0 : request.getPage();
        int size = request.getSize() == null ? 20 : request.getSize();
        if (size < 1) {
            size = 20;
        }
        if (size > 100) {
            size = 100;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> pageResult = _notificationRepository.findByUserIdWithFilters(
                userId,
                normalizeUpperOrNull(request.getChannel()),
                normalizeUpperOrNull(request.getEventType()),
                normalizeUpperOrNull(request.getDeliveryStatus()),
                request.getIsRead(),
                pageable);

        long unreadCount = _notificationRepository.countUnreadByUserId(userId);

        return new NotificationListResponseDto(
                pageResult.getContent().stream().map(NotificationDtoMapper::toResponseDto).toList(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                unreadCount);
    }

    @Override
    @Transactional
    public NotificationResponseDto markAsRead(String currentUserEmail, String notificationId) {
        String userId = resolveCurrentUserId(currentUserEmail);
        Notification notification = _notificationRepository.findByNotificationIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (notification.getReadAt() == null) {
            notification.setReadAt(LocalDateTime.now());
            notification.setUpdateDate(LocalDateTime.now());
            notification = _notificationRepository.save(notification);
        }

        return NotificationDtoMapper.toResponseDto(notification);
    }

    @Override
    @Transactional
    public MarkReadResponseDto markAllAsRead(String currentUserEmail) {
        String userId = resolveCurrentUserId(currentUserEmail);
        int updated = _notificationRepository.markAllAsRead(userId, LocalDateTime.now());
        return new MarkReadResponseDto(updated);
    }

    private void validateChannel(String channel) {
        if (!CHANNEL_EMAIL.equals(channel) && !CHANNEL_IN_APP.equals(channel)) {
            throw new IllegalArgumentException("channel must be EMAIL or IN_APP");
        }
    }

    private void validateRequiredFields(SendNotificationRequestDto request) {
        if (request.getEventType() == null || request.getEventType().isBlank()) {
            throw new IllegalArgumentException("eventType is required");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            throw new IllegalArgumentException("message is required");
        }
    }

    private void validateBusinessReferences(String userId, SendNotificationRequestDto request) {
        if (request.getGenerationId() != null && !request.getGenerationId().isBlank()) {
            boolean generationExists = _marketingRepository
                    .existsByGenerationIdAndUserId(request.getGenerationId(), userId);
            if (!generationExists) {
                throw new IllegalArgumentException("generationId does not exist for current user");
            }
        }

        if (request.getContentId() != null && !request.getContentId().isBlank()) {
            boolean contentExists = _cmsRepository.existsByContentIdAndUserId(request.getContentId(), userId);
            if (!contentExists) {
                throw new IllegalArgumentException("contentId does not exist for current user");
            }
        }
    }

    private String resolveCurrentUserId(String currentUserEmail) {
        User user = _userRepository.findByEmailNormalized(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));
                System.out.println("Notify for: " + user.getId());
        return user.getId();
    }

    private String normalizeUpper(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeUpperOrNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim().toUpperCase(Locale.ROOT);
    }

    private String limitError(String error) {
        if (error == null || error.isBlank()) {
            return "Unknown email delivery error";
        }
        if (error.length() <= 1000) {
            return error;
        }
        return error.substring(0, 1000);
    }
}

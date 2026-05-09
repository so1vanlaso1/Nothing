package com.YeuTech.Api.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.YeuTech.Application.Services.INotificationService;
import com.YeuTech.Dtos.ApiResponseFormat;
import com.YeuTech.Dtos.Request.NotificationQueryRequestDto;
import com.YeuTech.Dtos.Request.SendNotificationRequestDto;
import com.YeuTech.Dtos.Response.MarkReadResponseDto;
import com.YeuTech.Dtos.Response.NotificationListResponseDto;
import com.YeuTech.Dtos.Response.NotificationResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api/notify")
@Tag(name = "Notify", description = "Notification APIs")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final INotificationService _notificationService;

    public NotificationController(INotificationService notificationService) {
        this._notificationService = notificationService;
    }

    @GetMapping("/status")
    @Operation(summary = "Notify status", description = "Simple endpoint to verify notify API availability")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notify API is available")
    })
    public String status() {
        return "OK";
    }

    @PostMapping("/send")
    @Operation(summary = "Send notification", description = "Create and deliver a notification by EMAIL or IN_APP")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification processed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponseFormat<NotificationResponseDto>> send(
            Authentication authentication,
            @RequestBody SendNotificationRequestDto request) {
        NotificationResponseDto dto = _notificationService.sendNotification(authentication.getName(), request);
        ApiResponseFormat<NotificationResponseDto> response = new ApiResponseFormat<>(
                200,
                "Notification processed successfully",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/my")
    @Operation(summary = "List my notifications", description = "List notifications for current user with filters and pagination")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponseFormat<NotificationListResponseDto>> listMyNotifications(
            Authentication authentication,
            @RequestParam(name = "channel", required = false) String channel,
            @RequestParam(name = "eventType", required = false) String eventType,
            @RequestParam(name = "deliveryStatus", required = false) String deliveryStatus,
            @RequestParam(name = "isRead", required = false) Boolean isRead,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {
        NotificationQueryRequestDto request = new NotificationQueryRequestDto();
        request.setChannel(channel);
        request.setEventType(eventType);
        request.setDeliveryStatus(deliveryStatus);
        request.setIsRead(isRead);
        request.setPage(page);
        request.setSize(size);
        NotificationListResponseDto dto = _notificationService.listMyNotifications(authentication.getName(), request);
        ApiResponseFormat<NotificationListResponseDto> response = new ApiResponseFormat<>(
            200,
            "Notifications retrieved successfully",
            dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Mark one as read", description = "Mark a notification as read for current user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification marked as read"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<ApiResponseFormat<NotificationResponseDto>> markAsRead(
            Authentication authentication,
            @PathVariable("notificationId") String notificationId) {
        NotificationResponseDto dto = _notificationService.markAsRead(authentication.getName(), notificationId);
        ApiResponseFormat<NotificationResponseDto> response = new ApiResponseFormat<>(
                200,
                "Notification marked as read",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/read-all")
    @Operation(summary = "Mark all as read", description = "Mark all unread notifications as read for current user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notifications updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponseFormat<MarkReadResponseDto>> markAllAsRead(Authentication authentication) {
        MarkReadResponseDto dto = _notificationService.markAllAsRead(authentication.getName());
        ApiResponseFormat<MarkReadResponseDto> response = new ApiResponseFormat<>(
                200,
                "Notifications updated",
                dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}


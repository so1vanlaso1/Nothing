package com.YeuTech.Dtos.Response;

import java.util.List;

public class NotificationListResponseDto {
    private List<NotificationResponseDto> items;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
    private long unreadCount;

    public NotificationListResponseDto() {
    }

    public NotificationListResponseDto(List<NotificationResponseDto> items, int page, int size, long totalItems,
            int totalPages, long unreadCount) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.unreadCount = unreadCount;
    }

    public List<NotificationResponseDto> getItems() {
        return items;
    }

    public void setItems(List<NotificationResponseDto> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }
}

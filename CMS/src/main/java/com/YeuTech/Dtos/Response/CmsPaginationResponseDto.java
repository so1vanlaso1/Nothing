package com.YeuTech.Dtos.Response;

public class CmsPaginationResponseDto {
    private int page;
    private int pageSize;
    private long totalItems;
    private int totalPages;

    public CmsPaginationResponseDto() {
    }

    public CmsPaginationResponseDto(int page, int pageSize, long totalItems, int totalPages) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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
}

package com.YeuTech.Dtos.Response;

import java.util.List;

public class CmsContentListResponseDto {
    private List<CmsContentResponseDto> items;
    private CmsPaginationResponseDto pagination;

    public CmsContentListResponseDto() {
    }

    public CmsContentListResponseDto(List<CmsContentResponseDto> items, CmsPaginationResponseDto pagination) {
        this.items = items;
        this.pagination = pagination;
    }

    public List<CmsContentResponseDto> getItems() {
        return items;
    }

    public void setItems(List<CmsContentResponseDto> items) {
        this.items = items;
    }

    public CmsPaginationResponseDto getPagination() {
        return pagination;
    }

    public void setPagination(CmsPaginationResponseDto pagination) {
        this.pagination = pagination;
    }
}

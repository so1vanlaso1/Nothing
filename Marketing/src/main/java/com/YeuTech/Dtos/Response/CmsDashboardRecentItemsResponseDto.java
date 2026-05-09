package com.YeuTech.Dtos.Response;

import java.util.ArrayList;
import java.util.List;

public class CmsDashboardRecentItemsResponseDto {
    private List<CmsRecentItemResponseDto> generations;
    private List<CmsRecentItemResponseDto> cmsItems;

    public CmsDashboardRecentItemsResponseDto() {
        this.generations = new ArrayList<>();
        this.cmsItems = new ArrayList<>();
    }

    public CmsDashboardRecentItemsResponseDto(
            List<CmsRecentItemResponseDto> generations,
            List<CmsRecentItemResponseDto> cmsItems) {
        this.generations = generations;
        this.cmsItems = cmsItems;
    }

    public List<CmsRecentItemResponseDto> getGenerations() {
        return generations;
    }

    public void setGenerations(List<CmsRecentItemResponseDto> generations) {
        this.generations = generations;
    }

    public List<CmsRecentItemResponseDto> getCmsItems() {
        return cmsItems;
    }

    public void setCmsItems(List<CmsRecentItemResponseDto> cmsItems) {
        this.cmsItems = cmsItems;
    }
}

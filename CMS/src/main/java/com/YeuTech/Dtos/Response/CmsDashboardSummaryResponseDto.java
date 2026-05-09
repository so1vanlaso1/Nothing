package com.YeuTech.Dtos.Response;

public class CmsDashboardSummaryResponseDto {
    private CmsDashboardStatsResponseDto stats;

    public CmsDashboardSummaryResponseDto() {
    }

    public CmsDashboardSummaryResponseDto(CmsDashboardStatsResponseDto stats) {
        this.stats = stats;
    }

    public CmsDashboardStatsResponseDto getStats() {
        return stats;
    }

    public void setStats(CmsDashboardStatsResponseDto stats) {
        this.stats = stats;
    }
}

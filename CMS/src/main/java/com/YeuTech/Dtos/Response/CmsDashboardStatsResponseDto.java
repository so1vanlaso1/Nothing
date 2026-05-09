package com.YeuTech.Dtos.Response;

public class CmsDashboardStatsResponseDto {
    private long totalGenerations;
    private long totalCms;
    private long published;
    private long drafts;
    private CmsDashboardTrendsResponseDto trends;
    private String period;

    public CmsDashboardStatsResponseDto() {
    }

    public CmsDashboardStatsResponseDto(
            long totalGenerations,
            long totalCms,
            long published,
            long drafts,
            CmsDashboardTrendsResponseDto trends,
            String period) {
        this.totalGenerations = totalGenerations;
        this.totalCms = totalCms;
        this.published = published;
        this.drafts = drafts;
        this.trends = trends;
        this.period = period;
    }

    public long getTotalGenerations() {
        return totalGenerations;
    }

    public void setTotalGenerations(long totalGenerations) {
        this.totalGenerations = totalGenerations;
    }

    public long getTotalCms() {
        return totalCms;
    }

    public void setTotalCms(long totalCms) {
        this.totalCms = totalCms;
    }

    public long getPublished() {
        return published;
    }

    public void setPublished(long published) {
        this.published = published;
    }

    public long getDrafts() {
        return drafts;
    }

    public void setDrafts(long drafts) {
        this.drafts = drafts;
    }

    public CmsDashboardTrendsResponseDto getTrends() {
        return trends;
    }

    public void setTrends(CmsDashboardTrendsResponseDto trends) {
        this.trends = trends;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}

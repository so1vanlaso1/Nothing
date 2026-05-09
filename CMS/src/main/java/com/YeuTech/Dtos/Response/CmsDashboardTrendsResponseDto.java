package com.YeuTech.Dtos.Response;

public class CmsDashboardTrendsResponseDto {
    private String generationsTrend;
    private String generationsTrendDir;
    private String cmsTrend;
    private String cmsTrendDir;
    private String publishedTrend;
    private String publishedTrendDir;
    private String draftsTrend;
    private String draftsTrendDir;

    public CmsDashboardTrendsResponseDto() {
    }

    public CmsDashboardTrendsResponseDto(
            String generationsTrend,
            String generationsTrendDir,
            String cmsTrend,
            String cmsTrendDir,
            String publishedTrend,
            String publishedTrendDir,
            String draftsTrend,
            String draftsTrendDir) {
        this.generationsTrend = generationsTrend;
        this.generationsTrendDir = generationsTrendDir;
        this.cmsTrend = cmsTrend;
        this.cmsTrendDir = cmsTrendDir;
        this.publishedTrend = publishedTrend;
        this.publishedTrendDir = publishedTrendDir;
        this.draftsTrend = draftsTrend;
        this.draftsTrendDir = draftsTrendDir;
    }

    public String getGenerationsTrend() {
        return generationsTrend;
    }

    public void setGenerationsTrend(String generationsTrend) {
        this.generationsTrend = generationsTrend;
    }

    public String getGenerationsTrendDir() {
        return generationsTrendDir;
    }

    public void setGenerationsTrendDir(String generationsTrendDir) {
        this.generationsTrendDir = generationsTrendDir;
    }

    public String getCmsTrend() {
        return cmsTrend;
    }

    public void setCmsTrend(String cmsTrend) {
        this.cmsTrend = cmsTrend;
    }

    public String getCmsTrendDir() {
        return cmsTrendDir;
    }

    public void setCmsTrendDir(String cmsTrendDir) {
        this.cmsTrendDir = cmsTrendDir;
    }

    public String getPublishedTrend() {
        return publishedTrend;
    }

    public void setPublishedTrend(String publishedTrend) {
        this.publishedTrend = publishedTrend;
    }

    public String getPublishedTrendDir() {
        return publishedTrendDir;
    }

    public void setPublishedTrendDir(String publishedTrendDir) {
        this.publishedTrendDir = publishedTrendDir;
    }

    public String getDraftsTrend() {
        return draftsTrend;
    }

    public void setDraftsTrend(String draftsTrend) {
        this.draftsTrend = draftsTrend;
    }

    public String getDraftsTrendDir() {
        return draftsTrendDir;
    }

    public void setDraftsTrendDir(String draftsTrendDir) {
        this.draftsTrendDir = draftsTrendDir;
    }
}

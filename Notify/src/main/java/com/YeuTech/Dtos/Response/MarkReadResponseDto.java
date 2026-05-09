package com.YeuTech.Dtos.Response;

public class MarkReadResponseDto {
    private int updatedCount;

    public MarkReadResponseDto() {
    }

    public MarkReadResponseDto(int updatedCount) {
        this.updatedCount = updatedCount;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount(int updatedCount) {
        this.updatedCount = updatedCount;
    }
}

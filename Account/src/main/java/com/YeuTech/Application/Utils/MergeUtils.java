package com.YeuTech.Application.Utils;

public final class MergeUtils {

    private MergeUtils() {
    }

    public static <T> T merge(T incoming, T existing) {
        return incoming != null ? incoming : existing;
    }
}
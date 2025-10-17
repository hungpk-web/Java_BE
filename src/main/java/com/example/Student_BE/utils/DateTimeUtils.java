package com.example.Student_BE.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class cho xử lý DateTime
 */
public class DateTimeUtils {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String generateTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }

    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
    /**
     * Format epoch millis theo mẫu mặc định yyyy-MM-dd HH:mm:ss (theo timezone hệ thống)
     */
    public static String formatEpochMillis(long epochMillis) {
        return java.time.Instant.ofEpochMilli(epochMillis)
                .atZone(java.time.ZoneId.systemDefault())
                .format(DISPLAY_FORMATTER);
    }

    /**
     * Format epoch millis theo mẫu truyền vào (theo timezone hệ thống)
     */
    public static String formatEpochMillis(long epochMillis, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return java.time.Instant.ofEpochMilli(epochMillis)
                .atZone(java.time.ZoneId.systemDefault())
                .format(formatter);
    }
}

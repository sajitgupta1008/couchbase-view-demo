package com.adpush.task.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    
    private DateUtil() {
    
    }
    
    public static String formatDate(long epochTime) {
        return Instant.ofEpochMilli(epochTime)
                .atZone(ZoneId.systemDefault())
                .format(FORMATTER);
    }
}

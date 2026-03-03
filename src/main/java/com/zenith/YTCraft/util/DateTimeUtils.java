package com.zenith.YTCraft.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.LiveChatMessage;

/**
 * Utility class for date and time operations
 */
public class DateTimeUtils {

    /**
     * Get current time in GMT timezone
     */
    public static LocalDateTime getGMTTimeNow() {
        return LocalDateTime.parse(ZonedDateTime.now(ZoneId.of("GMT")).toString().substring(0, 19));
    }

    /**
     * Extract timestamp from YouTube chat message
     */
    public static LocalDateTime getMessageTime(LiveChatMessage message) {
        DateTime msgTime = message.getSnippet().getPublishedAt();
        return LocalDateTime.parse(msgTime.toStringRfc3339().substring(0, 19));
    }
}

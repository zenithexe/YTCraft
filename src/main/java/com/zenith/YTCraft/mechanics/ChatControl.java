package com.zenith.YTCraft.mechanics;

import java.time.LocalDateTime;
import java.util.List;

import org.bukkit.Bukkit;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.api.YoutubeAPI;
import com.zenith.YTCraft.chatactions.ChatActionHandler;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.util.DateTimeUtils;

/**
 * Main chat control loop that fetches YouTube chat messages and processes
 * viewer actions with timestamp filtering
 */
public class ChatControl implements Runnable {

    public static LocalDateTime ReadTimeStamp;

    public static void setTimeStamp(LocalDateTime TS) {
        ReadTimeStamp = TS;
    }

    @Override
    public void run() {
        if (!PluginState.isChatControlEnabled()) {
            return;
        }

        // Initialize timestamp on first run
        if (ReadTimeStamp == null) {
            ReadTimeStamp = DateTimeUtils.getGMTTimeNow();
        }

        // Fetch current stats
        int viewers = YoutubeAPI.getConcurrentViewers().intValue();

        PluginState.setViewers(viewers);
        PluginState.setSubscriberCount(YoutubeAPI.getSubscribers().intValue());

        // Handle subscriber mechanics
        SubscriberMechanics.spawnMob(PluginState.getSubscriberCount());

        // Fetch chat messages
        List<LiveChatMessage> chats = YoutubeAPI.getChats();

        if (chats == null || chats.isEmpty()) {
            return;
        }

        // Process each message with timestamp filtering
        for (LiveChatMessage message : chats) {
            LocalDateTime messageTimeStamp = DateTimeUtils.getMessageTime(message);

            // Only process messages newer than our last read timestamp
            if (messageTimeStamp.compareTo(ReadTimeStamp) > 0) {

                handleMessage(message);
                ReadTimeStamp = messageTimeStamp;
            }
        }
    }

    /**
     * Process a single chat message
     */
    private void handleMessage(LiveChatMessage message) {

        String author = message.getAuthorDetails().getDisplayName();
        String text = message.getSnippet().getDisplayMessage();

        if (text == null || text.trim().isEmpty()) {
            return;
        }

        // Log the message
        Bukkit.getLogger().info(String.format("%s >> %s", author, text));

        // Try to process as an action
        ChatActionHandler.handler(message);

    }

}

package com.zenith.YTCraft.mechanics;

import java.time.LocalDateTime;
import java.util.List;

import org.bukkit.Bukkit;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.api.YoutubeAPI;
import com.zenith.YTCraft.chatactions.ChatActionHandler;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.util.DateTimeUtils;

/**
 * Main chat control loop that fetches YouTube chat messages asynchronously and
 * processes viewer actions with timestamp filtering
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

        // Run API calls asynchronously to prevent server lag
        Bukkit.getScheduler().runTaskAsynchronously(YTCraft.getPlugin(), () -> {
            try {
                // These run in background thread - doesn't block server
                int viewers = YoutubeAPI.getConcurrentViewers().intValue();
                int subscribers = YoutubeAPI.getSubscribers().intValue();
                List<LiveChatMessage> chats = YoutubeAPI.getChats();

                // Switch back to main thread for Bukkit operations
                Bukkit.getScheduler().runTask(YTCraft.getPlugin(), () -> {
                    // Update state on main thread
                    PluginState.setViewers(viewers);
                    PluginState.setSubscriberCount(subscribers);

                    // Handle subscriber mechanics
                    SubscriberMechanics.spawnMob(subscribers);

                    // Process chat messages
                    if (chats != null && !chats.isEmpty()) {
                        handleMessages(chats);
                    }
                });

            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning(String.format("Error in async YouTube API fetch: %s", e.getMessage()));
            }
        });
    }

    /**
     * Process all chat messages (runs on main thread)
     */
    private void handleMessages(List<LiveChatMessage> messages) {
        for (LiveChatMessage message : messages) {

            LocalDateTime messageTimeStamp = DateTimeUtils.getMessageTime(message);

            // Only process messages newer than our last read timestamp
            if (messageTimeStamp.compareTo(ReadTimeStamp) > 0) {
                String author = message.getAuthorDetails().getDisplayName();
                String text = message.getSnippet().getDisplayMessage();

                if (text == null || text.trim().isEmpty()) {
                    return;
                }

                // Log the message
                Bukkit.getLogger().info(String.format("%s >> %s", author, text));

                // Try to process as an action
                ChatActionHandler.handler(message);

                ReadTimeStamp = messageTimeStamp;
            }
        }
    }

}

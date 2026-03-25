package com.zenith.YTCraft.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.api.ApiKeyManager;
import com.zenith.YTCraft.api.YoutubeAPI;

/**
 * Manages loading and processing of plugin configurations
 */
public class ConfigManager {

    public static void loadConfig() {
        // Load API keys - support both list (API_KEYS) and single (API_KEY)
        List<String> apiKeys = new ArrayList<>();

        List<String> keysList = YTCraft.getPlugin().getConfig().getStringList("API_KEYS");
        if (!keysList.isEmpty()) {
            for (String key : keysList) {
                if (key != null && !key.isEmpty()) {
                    apiKeys.add(key);
                }
            }
        }

        // Backward compat: also check singular API_KEY
        String singleKey = YTCraft.getPlugin().getConfig().getString("API_KEY");
        if (singleKey != null && !singleKey.isEmpty() && !apiKeys.contains(singleKey)) {
            apiKeys.add(singleKey);
        }

        ApiKeyManager.setKeys(apiKeys);

        String channelId = YTCraft.getPlugin().getConfig().getString("CHANNEL_ID");
        String videoId = YTCraft.getPlugin().getConfig().getString("VIDEO_ID");

        YoutubeAPI.setAPI(channelId, videoId);

        Bukkit.getLogger().info(String.format("[YTCraft] YouTube API credentials loaded (%d API key(s))", ApiKeyManager.getKeyCount()));
    }

}

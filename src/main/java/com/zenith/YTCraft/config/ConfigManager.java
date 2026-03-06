package com.zenith.YTCraft.config;

import org.bukkit.Bukkit;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.api.YoutubeAPI;

/**
 * Manages loading and processing of plugin configurations
 */
public class ConfigManager {

    public static void loadConfig() {
        String apiKey = YTCraft.getPlugin().getConfig().getString("API_KEY");
        String videoId = YTCraft.getPlugin().getConfig().getString("VIDEO_ID");

        YoutubeAPI.setAPI(apiKey, videoId);

        Bukkit.getLogger().info("YouTube API credentials loaded");
    }

}

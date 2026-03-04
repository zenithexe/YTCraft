package com.zenith.YTCraft.config;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.api.YoutubeAPI;
import com.zenith.YTCraft.data.MobManager;
import com.zenith.YTCraft.data.PluginState;

public class ConfigUtils {

    public static void apiConfig() {
        String API_KEY = YTCraft.getPlugin().getConfig().getString("API_KEY");
        String VIDEO_ID = YTCraft.getPlugin().getConfig().getString("VIDEO_ID");
        YoutubeAPI.setAPI(API_KEY, VIDEO_ID);
    }

    public static void timerConfig() {

        int activeTime = YTCraft.getPlugin().getConfig().getInt("ACTIVE_TIME");
        int restTime = YTCraft.getPlugin().getConfig().getInt("REST_TIME");

        Bukkit.getLogger().info("Timer Set.");

        PluginState.setActiveTime(activeTime);
        Bukkit.getLogger().info("Active-Time Configuration is done.");

        PluginState.setRestTime(restTime);
        Bukkit.getLogger().info("Rest-Time Configuration is done.");
    }

    public static void authorMobConfig() {

        ConfigurationSection mobs = YTCraft.getPlugin().getConfig().getConfigurationSection("MOBS");
        Set<String> viewLevels = mobs.getKeys(false);

        for (String viewLevel : viewLevels) {
            List<String> values = mobs.getStringList(viewLevel);
            for (String value : values) {
                MobManager.getEntityTypeToMinViewers().put(value, Integer.parseInt(viewLevel));
            }
        }
        Bukkit.getLogger().info("EntityType to Viewers Mapping Configuration done.");
    }
}

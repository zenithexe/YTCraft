package com.zenith.YTCraft.config;

import org.bukkit.configuration.file.FileConfiguration;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.data.PluginState;

public class SaveConfiguration {

    public static void saveYTCraftConfig() {
        FileConfiguration config = YTCraft.getPlugin().getConfig();

        config.set("ACTIVE_TIME", PluginState.getActiveTime());
        config.set("REST_TIME", PluginState.getRestTime());

        YTCraft.getPlugin().saveConfig();
    }
}

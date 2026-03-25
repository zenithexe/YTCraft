package com.zenith.YTCraft.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.data.SpawnQueue;
import com.zenith.YTCraft.util.MobUtils;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // Only track streamer deaths
        if (player.equals(PluginState.getStreamer())) {
            PluginState.incrementStreamerDeathCount();

            if (SettingsLoader.getSettings().getMobSpawnSettings().isKillAllOnDeath()) {
                SpawnQueue.clear();
                MobUtils.killAllAuthorMobs();
            }
        }
    }
}

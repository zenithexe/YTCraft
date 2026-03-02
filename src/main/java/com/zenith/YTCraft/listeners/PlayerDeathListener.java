package com.zenith.YTCraft.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.zenith.YTCraft.data.PluginState;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        // Only track streamer deaths
        if (player.equals(PluginState.getStreamer())) {
            PluginState.incrementStreamerDeathCount();
        }
    }
}

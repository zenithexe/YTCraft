package com.zenith.YTCraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.zenith.YTCraft.commands.subcommands.StartSubcommand;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.ui.BossBarUI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SessionPlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // If session is running, add the joining player to boss bar only
        if (StartSubcommand.isRunning()) {
            Player player = event.getPlayer();
            BossBarUI.addPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Player streamer = PluginState.getStreamer();

        // If the streamer disconnects, stop the session
        if (streamer != null && player.getUniqueId().equals(streamer.getUniqueId())) {
            if (StartSubcommand.isRunning()) {
                Bukkit.broadcast(Component.text("Streamer disconnected. Stopping YTCraft session...")
                        .color(NamedTextColor.YELLOW));
                
                StartSubcommand.stop();
                BossBarUI.removeBossBar();
                
                // Reset streamer state
                PluginState.setStreamer(null);
            }
        }
    }
}

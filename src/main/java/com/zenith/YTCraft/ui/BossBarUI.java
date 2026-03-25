package com.zenith.YTCraft.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.zenith.YTCraft.data.PluginState;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BossBarUI {

    private static BossBar bossBar;

    public static void createBossBar() {
        // Create Adventure boss bar
        bossBar = BossBar.bossBar(
            Component.text("Initializing..."),
            1.0f,
            BossBar.Color.GREEN,
            BossBar.Overlay.PROGRESS
        );
        
        // Show to all online players
        Bukkit.getOnlinePlayers().forEach(player -> player.showBossBar(bossBar));
        
        // Initial update
        updateBossBar(0, 0);
    }

    public static void addPlayer(Player player) {
        if (bossBar != null && player != null) {
            player.showBossBar(bossBar);
        }
    }

    public static void updateBossBar(int currentSeconds, int totalSeconds) {
        if (bossBar == null) return;

        Player streamer = PluginState.getStreamer();

        if (streamer == null) return;

        boolean isChatMode = PluginState.isTimerActiveMode();
        
        // Calculate progress (0.0 to 1.0)
        float progress = calculateProgress(currentSeconds, totalSeconds);
        
        // Format time display with hours if needed
        String timeDisplay = formatTime(currentSeconds);
        
        // Update boss bar based on mode
        if (isChatMode) {
            bossBar = bossBar.color(BossBar.Color.RED)
                             .name(Component.text("Chat Mode - " + timeDisplay, NamedTextColor.RED))
                             .progress(progress);
        } else {
            bossBar = bossBar.color(BossBar.Color.GREEN)
                             .name(Component.text("Peace Mode - " + timeDisplay, NamedTextColor.GREEN))
                             .progress(progress);
        }
        
        // Boss bar updates automatically for all players who have it shown
    }

    private static String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds / 60) % 60;
        int seconds = totalSeconds % 60;
        
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    private static float calculateProgress(int currentSeconds, int totalSeconds) {
        if (totalSeconds == 0) return 1.0f;
        
        float progress = (float) currentSeconds / totalSeconds;
        
        // Clamp between 0.0 and 1.0
        return Math.max(0.0f, Math.min(1.0f, progress));
    }

    public static void removeBossBar() {
        if (bossBar != null) {
            // Hide from all online players
            Bukkit.getOnlinePlayers().forEach(player -> player.hideBossBar(bossBar));
            bossBar = null;
        }
    }
}

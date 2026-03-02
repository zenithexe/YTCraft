package com.zenith.YTCraft.ui;

import org.bukkit.entity.Player;

import com.zenith.YTCraft.data.PluginState;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BossBarUI {

    private static BossBar bossBar;

    public static void createBossBar(Player player) {
        if (player == null) return;

        // Create Adventure boss bar
        bossBar = BossBar.bossBar(
            Component.text("Initializing..."),
            1.0f,
            BossBar.Color.GREEN,
            BossBar.Overlay.PROGRESS
        );
        
        player.showBossBar(bossBar);
        
        // Initial update
        updateBossBar(0, 0, 0, 0);
    }

    public static void updateBossBar(int currentMin, int currentSec, int totalMin, int totalSec) {
        if (bossBar == null) return;

        Player streamer = PluginState.getStreamer();

        if (streamer == null) return;

        boolean isChatMode = PluginState.isChatControlEnabled();
        
        // Calculate progress (0.0 to 1.0)
        float progress = calculateProgress(currentMin, currentSec, totalMin, totalSec);
        
        // Format time display
        String timeDisplay = String.format("%02d:%02d", currentMin, currentSec);
        
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
        
        // Show updated boss bar
        streamer.showBossBar(bossBar);
    }

    private static float calculateProgress(int currentMin, int currentSec, int totalMin, int totalSec) {
        int totalSeconds = (totalMin * 60) + totalSec;
        if (totalSeconds == 0) return 1.0f;
        
        int remainingSeconds = (currentMin * 60) + currentSec;
        float progress = (float) remainingSeconds / totalSeconds;
        
        // Clamp between 0.0 and 1.0
        return Math.max(0.0f, Math.min(1.0f, progress));
    }

    public static void removeBossBar() {
        if (bossBar != null) {
            Player streamer = PluginState.getStreamer();
            if (streamer != null) {
                streamer.hideBossBar(bossBar);
            }
            bossBar = null;
        }
    }
}

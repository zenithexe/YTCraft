package com.zenith.YTCraft.ui;

import java.time.Duration;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.data.PluginState;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;

public class TitlesUI {

    public static void showTimerActiveTitle() {
        Player streamer = PluginState.getStreamer();

        if (streamer == null) {
            return;
        }

        YTCraft.getPlugin().getLogger().info("Showing Active Title to: " + streamer.getName());

        final Component mainTitle = Component.text("Chat Control Activated!", NamedTextColor.RED);
        final Component subtitle = Component.text("Now Viewers can take Actions", NamedTextColor.WHITE);
        final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(2000), Duration.ofMillis(500));

        final Title title = Title.title(mainTitle, subtitle, times);

        // Use Paper's native showTitle method directly on the player
        streamer.showTitle(title);
        YTCraft.getPlugin().getLogger().info("Title sent successfully!");

        streamer.playSound(streamer.getLocation(), Sound.ENTITY_TNT_PRIMED, 1.0f, 1.0f);
    }

    public static void showTimerRestTitle() {
        Player streamer = PluginState.getStreamer();
        if (streamer == null) {
            YTCraft.getPlugin().getLogger().warning("showTimerRestTitle: streamer is null!");
            return;
        }

        YTCraft.getPlugin().getLogger().info("Showing Rest Title to: " + streamer.getName());

        final Component mainTitle = Component.text("Rest Time", NamedTextColor.RED);
        final Component subtitle = Component.text("Viewers actions disabled.", NamedTextColor.WHITE);
        final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(2000), Duration.ofMillis(500));

        final Title title = Title.title(mainTitle, subtitle, times);

        // Use Paper's native showTitle method directly on the player
        streamer.showTitle(title);
        YTCraft.getPlugin().getLogger().info("Title sent successfully!");

        streamer.playSound(streamer.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 1.0f);
    }
}

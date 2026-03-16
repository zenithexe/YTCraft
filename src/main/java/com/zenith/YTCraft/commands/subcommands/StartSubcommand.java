package com.zenith.YTCraft.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.api.YoutubeAPI;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.mechanics.ChatControl;
import com.zenith.YTCraft.mechanics.MobSpawning;
import com.zenith.YTCraft.timer.PluginTimer;
import com.zenith.YTCraft.ui.BossBarUI;
import com.zenith.YTCraft.ui.ScoreboardUI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class StartSubcommand implements Subcommand {

    private static BukkitTask youtubeTask;
    private static BukkitTask timerTask;
    private static BukkitTask mobSpawnTask;
    private static boolean isRunning = false;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (isRunning) {
            sender.sendMessage(Component.text("YTCraft is already running!").color(NamedTextColor.YELLOW));
            return true;
        }

        // Validate YouTube API configuration
        if (!YoutubeAPI.hasChannelId()) {
            sender.sendMessage(Component.text("[YTCraft] Cannot start — no channel ID configured.").color(NamedTextColor.RED));
            sender.sendMessage(Component.text("Set CHANNEL_ID in config.yml, then use /yt connect.").color(NamedTextColor.YELLOW));
            return true;
        }

        if (!YoutubeAPI.isConfigured()) {
            sender.sendMessage(Component.text("[YTCraft] Cannot start — no active livestream connected.").color(NamedTextColor.RED));
            sender.sendMessage(Component.text("Use /yt connect to link a live broadcast.").color(NamedTextColor.YELLOW));
            return true;
        }

        Player player = (Player) sender;
        PluginState.setStreamer(player);

        YTCraft plugin = YTCraft.getPlugin();
        if (plugin == null) {
            sender.sendMessage(Component.text("Plugin not initialized!").color(NamedTextColor.RED));
            return true;
        }

        // Start tasks
        youtubeTask = Bukkit.getScheduler().runTaskTimer(plugin, new ChatControl(), 0, 20L * 5);
        timerTask = Bukkit.getScheduler().runTaskTimer(plugin, new PluginTimer(), 0, 20);
        mobSpawnTask = Bukkit.getScheduler().runTaskTimer(plugin, new MobSpawning(), 0, 20L);

        isRunning = true;

        // Create UI - scoreboard only for streamer, boss bar for all players
        ScoreboardUI.createNewScoreBoard(player);
        BossBarUI.createBossBar();

        Bukkit.broadcast(Component.text("[YTCraft] Session started! ").color(NamedTextColor.GREEN)
                .append(Component.text(player.getName()).color(NamedTextColor.YELLOW))
                .append(Component.text(" is the Streamer.").color(NamedTextColor.WHITE)));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Start the YTCraft session";
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static void stop() {
        if (youtubeTask != null) {
            youtubeTask.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (mobSpawnTask != null) {
            mobSpawnTask.cancel();
        }
        isRunning = false;
    }
}

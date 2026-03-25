package com.zenith.YTCraft.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;
import com.zenith.YTCraft.config.types.TimerSettings;
import com.zenith.YTCraft.data.MobSpawnState;
import com.zenith.YTCraft.data.PluginState;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class StatusSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MobSpawnSettings mobSettings = SettingsLoader.getSettings().getMobSpawnSettings();
        TimerSettings timerSettings = SettingsLoader.getSettings().getTimerSettings();

        sender.sendMessage(Component.text("=== YTCraft Status ===").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text(""));
        
        // Timer Status
        sender.sendMessage(Component.text("Timer:").color(NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("  Always Active: " + timerSettings.isAlwaysActive()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  Active Time: " + timerSettings.getActiveSeconds() + "s").color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  Rest Time: " + timerSettings.getRestSeconds() + "s").color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text(""));
        
        // Mob Spawn Status
        sender.sendMessage(Component.text("Mob Spawning:").color(NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("  Enabled: " + mobSettings.isEnabled()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  Mode: " + mobSettings.getMode()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  Max Spawns: " + mobSettings.getMaxSpawns()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  Kill on Death: " + mobSettings.isKillAllOnDeath()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  Allow Viewer Kill: " + mobSettings.isAllowViewerMobKill()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  Active Mobs: " + MobSpawnState.getChannelIdToAuthorMob().size()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text(""));
        
        // Stream Status
        sender.sendMessage(Component.text("Stream:").color(NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("  Viewers: " + PluginState.getViewers()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  Subscribers: " + PluginState.getSubscriberCount()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  Timer Mode: " + PluginState.getTimerMode()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  Chat Control: " + PluginState.isChatControlEnabled()).color(NamedTextColor.GRAY));
        
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public String getDescription() {
        return "Show current plugin status";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

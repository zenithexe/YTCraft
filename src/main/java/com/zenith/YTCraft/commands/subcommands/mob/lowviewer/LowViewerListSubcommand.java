package com.zenith.YTCraft.commands.subcommands.mob.lowviewer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class LowViewerListSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MobSpawnSettings.LowViewerMode lowViewerMode = SettingsLoader.getSettings().getMobSpawnSettings().getLowViewerMode();
        
        if (lowViewerMode == null) {
            sender.sendMessage(Component.text("Low viewer mode not configured").color(NamedTextColor.YELLOW));
            return true;
        }

        sender.sendMessage(Component.text("=== Low Viewer Mode Settings ===").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("Enabled: " + lowViewerMode.isEnabled()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("Threshold: " + lowViewerMode.getViewersThreshold()).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("Mode: " + lowViewerMode.getMode()).color(NamedTextColor.GRAY));
        
        if (lowViewerMode.getAllow() != null && !lowViewerMode.getAllow().isEmpty()) {
            sender.sendMessage(Component.text("Allow list: " + String.join(", ", lowViewerMode.getAllow())).color(NamedTextColor.GRAY));
        }
        
        if (lowViewerMode.getExclude() != null && !lowViewerMode.getExclude().isEmpty()) {
            sender.sendMessage(Component.text("Exclude list: " + String.join(", ", lowViewerMode.getExclude())).color(NamedTextColor.GRAY));
        }
        
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Show low viewer mode settings";
    }
}

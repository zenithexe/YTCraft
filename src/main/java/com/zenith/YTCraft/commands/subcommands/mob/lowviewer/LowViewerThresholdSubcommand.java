package com.zenith.YTCraft.commands.subcommands.mob.lowviewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class LowViewerThresholdSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft mob lowviewer threshold <number>").color(NamedTextColor.RED));
            return true;
        }

        try {
            int threshold = Integer.parseInt(args[0]);
            MobSpawnSettings.LowViewerMode lowViewerMode = SettingsLoader.getSettings().getMobSpawnSettings().getLowViewerMode();
            if (lowViewerMode != null) {
                lowViewerMode.setViewersThreshold(threshold);
                SettingsLoader.saveSettings();
                sender.sendMessage(Component.text("Viewer threshold set to: " + threshold).color(NamedTextColor.GREEN));
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid number!").color(NamedTextColor.RED));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("5", "10", "20", "50");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "threshold";
    }

    @Override
    public String getDescription() {
        return "Set viewer threshold for low viewer mode";
    }
}

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

public class LowViewerModeSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft mob lowviewer mode <all|tier|allow_only>").color(NamedTextColor.RED));
            return true;
        }

        try {
            MobSpawnSettings.LowViewerMode.LowViewModeSettings mode = 
                    MobSpawnSettings.LowViewerMode.LowViewModeSettings.valueOf(args[0].toUpperCase());
            MobSpawnSettings.LowViewerMode lowViewerMode = SettingsLoader.getSettings().getMobSpawnSettings().getLowViewerMode();
            if (lowViewerMode != null) {
                lowViewerMode.setMode(mode);
                SettingsLoader.saveSettings();
                sender.sendMessage(Component.text("Low viewer mode set to: " + mode).color(NamedTextColor.GREEN));
            }
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text("Invalid mode! Use: all, tier, or allow_only").color(NamedTextColor.RED));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("ALL", "TIER", "ALLOW_ONLY");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "mode";
    }

    @Override
    public String getDescription() {
        return "Set low viewer mode type";
    }
}

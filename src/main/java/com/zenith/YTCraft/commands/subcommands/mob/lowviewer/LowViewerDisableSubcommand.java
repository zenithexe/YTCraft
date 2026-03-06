package com.zenith.YTCraft.commands.subcommands.mob.lowviewer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class LowViewerDisableSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MobSpawnSettings.LowViewerMode lowViewerMode = SettingsLoader.getSettings().getMobSpawnSettings().getLowViewerMode();
        if (lowViewerMode != null) {
            lowViewerMode.setEnabled(false);
            SettingsLoader.saveSettings();
            sender.sendMessage(Component.text("Low viewer mode disabled!").color(NamedTextColor.RED));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "disable";
    }

    @Override
    public String getDescription() {
        return "Disable low viewer mode";
    }
}

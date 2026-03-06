package com.zenith.YTCraft.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ReloadSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        try {
            SettingsLoader.loadSettings();
            sender.sendMessage(Component.text("Settings reloaded successfully!").color(NamedTextColor.GREEN));
        } catch (Exception e) {
            sender.sendMessage(Component.text("Failed to reload settings: " + e.getMessage()).color(NamedTextColor.RED));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload settings from file";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

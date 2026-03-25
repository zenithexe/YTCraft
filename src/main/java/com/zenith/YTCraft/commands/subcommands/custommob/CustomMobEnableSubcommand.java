package com.zenith.YTCraft.commands.subcommands.custommob;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CustomMobEnableSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        SettingsLoader.getSettings().getCustomMobSettings().setEnabled(true);
        SettingsLoader.saveSettings();

        sender.sendMessage(Component.text("Custom mobs ENABLED").color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "enable";
    }

    @Override
    public String getDescription() {
        return "Enable custom mob spawning";
    }
}

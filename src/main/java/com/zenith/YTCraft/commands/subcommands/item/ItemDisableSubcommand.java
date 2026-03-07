package com.zenith.YTCraft.commands.subcommands.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ItemDisableSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        SettingsLoader.getSettings().getItemGiveSettings().setEnabled(false);
        SettingsLoader.saveSettings();

        sender.sendMessage(Component.text("Item giving disabled!").color(NamedTextColor.RED));
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
        return "Disable item giving";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

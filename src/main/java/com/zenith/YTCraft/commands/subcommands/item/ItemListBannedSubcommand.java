package com.zenith.YTCraft.commands.subcommands.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ItemListBannedSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        List<String> bannedItems = SettingsLoader.getSettings().getItemGiveSettings().getBannedItems();

        if (bannedItems == null || bannedItems.isEmpty()) {
            sender.sendMessage(Component.text("No banned items!").color(NamedTextColor.YELLOW));
            return true;
        }

        sender.sendMessage(Component.text("=== Banned Items ===").color(NamedTextColor.GOLD));
        for (String item : bannedItems) {
            sender.sendMessage(Component.text("  - ").color(NamedTextColor.GRAY)
                    .append(Component.text(item).color(NamedTextColor.RED)));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "listbanned";
    }

    @Override
    public String getDescription() {
        return "List all banned items";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

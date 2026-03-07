package com.zenith.YTCraft.commands.subcommands.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ItemListAllowedSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        List<String> allowedItems = SettingsLoader.getSettings().getItemGiveSettings().getAllowedItems();

        if (allowedItems == null || allowedItems.isEmpty()) {
            sender.sendMessage(Component.text("No allowed items!").color(NamedTextColor.YELLOW));
            return true;
        }

        sender.sendMessage(Component.text("=== Allowed Items ===").color(NamedTextColor.GOLD));
        for (String item : allowedItems) {
            sender.sendMessage(Component.text("  - ").color(NamedTextColor.GRAY)
                    .append(Component.text(item).color(NamedTextColor.GREEN)));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "listallowed";
    }

    @Override
    public String getDescription() {
        return "List all allowed items";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

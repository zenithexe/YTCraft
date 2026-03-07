package com.zenith.YTCraft.commands.subcommands.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.processors.ItemGiveSettingsProcessor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ItemDisallowSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft item disallow <item>").color(NamedTextColor.RED));
            return true;
        }

        String itemName = args[0].toUpperCase();
        List<String> allowedItems = SettingsLoader.getSettings().getItemGiveSettings().getAllowedItems();

        if (allowedItems == null || !allowedItems.contains(itemName)) {
            sender.sendMessage(Component.text(itemName + " is not in allowed list!").color(NamedTextColor.YELLOW));
            return true;
        }

        allowedItems.remove(itemName);
        SettingsLoader.saveSettings();
        ItemGiveSettingsProcessor.process(SettingsLoader.getSettings().getItemGiveSettings());

        sender.sendMessage(Component.text("Removed from allowed list: ").color(NamedTextColor.GREEN)
                .append(Component.text(itemName).color(NamedTextColor.YELLOW)));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> allowedItems = SettingsLoader.getSettings().getItemGiveSettings().getAllowedItems();
            if (allowedItems != null) {
                List<String> suggestions = new ArrayList<>();
                for (String item : allowedItems) {
                    if (item.toLowerCase().startsWith(args[0].toLowerCase())) {
                        suggestions.add(item);
                    }
                }
                return suggestions;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "disallow";
    }

    @Override
    public String getDescription() {
        return "Remove item from allowed list";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

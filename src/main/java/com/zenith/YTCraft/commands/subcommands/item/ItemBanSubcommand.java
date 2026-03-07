package com.zenith.YTCraft.commands.subcommands.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.processors.ItemGiveSettingsProcessor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ItemBanSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft item ban <item>").color(NamedTextColor.RED));
            return true;
        }

        String itemName = args[0].toUpperCase();

        // Validate material
        Material material = Material.getMaterial(itemName);
        if (material == null || !material.isItem()) {
            sender.sendMessage(Component.text("Invalid item: " + itemName).color(NamedTextColor.RED));
            return true;
        }

        List<String> bannedItems = SettingsLoader.getSettings().getItemGiveSettings().getBannedItems();
        if (bannedItems == null) {
            bannedItems = new ArrayList<>();
            SettingsLoader.getSettings().getItemGiveSettings().setBannedItems(bannedItems);
        }

        if (bannedItems.contains(itemName)) {
            sender.sendMessage(Component.text(itemName + " is already banned!").color(NamedTextColor.YELLOW));
            return true;
        }

        bannedItems.add(itemName);
        SettingsLoader.saveSettings();
        ItemGiveSettingsProcessor.process(SettingsLoader.getSettings().getItemGiveSettings());

        sender.sendMessage(Component.text("Banned item: ").color(NamedTextColor.GREEN)
                .append(Component.text(itemName).color(NamedTextColor.RED)));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (Material material : Material.values()) {
                if (material.isItem() && material.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                    suggestions.add(material.name());
                }
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getDescription() {
        return "Ban an item from being given";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

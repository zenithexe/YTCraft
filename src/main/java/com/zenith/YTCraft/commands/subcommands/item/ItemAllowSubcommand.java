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

public class ItemAllowSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft item allow <item>").color(NamedTextColor.RED));
            return true;
        }

        String itemName = args[0].toUpperCase();

        // Validate material
        Material material = Material.getMaterial(itemName);
        if (material == null || !material.isItem()) {
            sender.sendMessage(Component.text("Invalid item: " + itemName).color(NamedTextColor.RED));
            return true;
        }

        List<String> allowedItems = SettingsLoader.getSettings().getItemGiveSettings().getAllowedItems();
        if (allowedItems == null) {
            allowedItems = new ArrayList<>();
            SettingsLoader.getSettings().getItemGiveSettings().setAllowedItems(allowedItems);
        }

        if (allowedItems.contains(itemName)) {
            sender.sendMessage(Component.text(itemName + " is already allowed!").color(NamedTextColor.YELLOW));
            return true;
        }

        allowedItems.add(itemName);
        SettingsLoader.saveSettings();
        ItemGiveSettingsProcessor.process(SettingsLoader.getSettings().getItemGiveSettings());

        sender.sendMessage(Component.text("Added to allowed list: ").color(NamedTextColor.GREEN)
                .append(Component.text(itemName).color(NamedTextColor.YELLOW)));
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
        return "allow";
    }

    @Override
    public String getDescription() {
        return "Add item to allowed list (for allowed_only mode)";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

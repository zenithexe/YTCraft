package com.zenith.YTCraft.commands.subcommands.item.alias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.processors.ItemGiveSettingsProcessor;
import com.zenith.YTCraft.config.types.ItemGiveSettings.ItemAlias;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AliasCreateSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Component.text("Usage: /ytcraft item alias create <alias> <item> <qty>").color(NamedTextColor.RED));
            return true;
        }

        String aliasName = args[0].toLowerCase();
        String itemName = args[1].toUpperCase();
        int quantity;

        try {
            quantity = Integer.parseInt(args[2]);
            if (quantity <= 0) {
                sender.sendMessage(Component.text("Quantity must be positive!").color(NamedTextColor.RED));
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid quantity: " + args[2]).color(NamedTextColor.RED));
            return true;
        }

        // Validate material
        Material material = Material.getMaterial(itemName);
        if (material == null || !material.isItem()) {
            sender.sendMessage(Component.text("Invalid item: " + itemName).color(NamedTextColor.RED));
            return true;
        }

        Map<String, ItemAlias> aliases = SettingsLoader.getSettings().getItemGiveSettings().getItemAliases();
        if (aliases == null) {
            aliases = new HashMap<>();
            SettingsLoader.getSettings().getItemGiveSettings().setItemAliases(aliases);
        }

        if (aliases.containsKey(aliasName)) {
            sender.sendMessage(Component.text("Alias already exists: " + aliasName).color(NamedTextColor.YELLOW));
            sender.sendMessage(Component.text("Use /ytcraft item alias delete first").color(NamedTextColor.GRAY));
            return true;
        }

        // Create new alias
        ItemAlias newAlias = new ItemAlias();
        newAlias.setItem(itemName);
        newAlias.setQty(quantity);
        newAlias.setEnchantments(new ArrayList<>());

        aliases.put(aliasName, newAlias);
        SettingsLoader.saveSettings();
        ItemGiveSettingsProcessor.process(SettingsLoader.getSettings().getItemGiveSettings());

        sender.sendMessage(Component.text("Created alias: ").color(NamedTextColor.GREEN)
                .append(Component.text(aliasName).color(NamedTextColor.YELLOW))
                .append(Component.text(" -> ").color(NamedTextColor.GRAY))
                .append(Component.text(itemName + " x" + quantity).color(NamedTextColor.GREEN)));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> suggestions = new ArrayList<>();
            for (Material material : Material.values()) {
                if (material.isItem() && material.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                    suggestions.add(material.name());
                }
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Create new alias";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

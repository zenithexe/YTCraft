package com.zenith.YTCraft.commands.subcommands.item.alias;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.processors.ItemGiveSettingsProcessor;
import com.zenith.YTCraft.config.types.ItemGiveSettings;
import com.zenith.YTCraft.config.types.ItemGiveSettings.ItemAlias;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AliasAddEnchSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Component.text("Usage: /ytcraft item alias addench <alias> <enchantment> <level>").color(NamedTextColor.RED));
            return true;
        }

        String aliasName = args[0].toLowerCase();
        String enchantmentName = args[1].toUpperCase();
        int level;

        try {
            level = Integer.parseInt(args[2]);
            if (level <= 0) {
                sender.sendMessage(Component.text("Level must be positive!").color(NamedTextColor.RED));
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid level: " + args[2]).color(NamedTextColor.RED));
            return true;
        }

        Map<String, ItemAlias> aliases = SettingsLoader.getSettings().getItemGiveSettings().getItemAliases();

        if (aliases == null || !aliases.containsKey(aliasName)) {
            sender.sendMessage(Component.text("Alias not found: " + aliasName).color(NamedTextColor.RED));
            return true;
        }

        // Validate enchantment
        @SuppressWarnings("deprecation")
        Enchantment enchantment = Enchantment.getByName(enchantmentName);
        if (enchantment == null) {
            sender.sendMessage(Component.text("Invalid enchantment: " + enchantmentName).color(NamedTextColor.RED));
            return true;
        }

        ItemAlias alias = aliases.get(aliasName);

        // Check if enchantment already exists
        if (alias.getEnchantments() != null) {
            for (ItemGiveSettings.Enchantment ench : alias.getEnchantments()) {
                if (ench.getEnchantment().equalsIgnoreCase(enchantmentName)) {
                    sender.sendMessage(Component.text("Enchantment already exists! Remove it first.").color(NamedTextColor.YELLOW));
                    return true;
                }
            }
        } else {
            alias.setEnchantments(new ArrayList<>());
        }

        // Add enchantment
        ItemGiveSettings.Enchantment newEnch = new ItemGiveSettings.Enchantment();
        newEnch.setEnchantment(enchantmentName);
        newEnch.setLevel(level);
        alias.getEnchantments().add(newEnch);

        SettingsLoader.saveSettings();
        ItemGiveSettingsProcessor.process(SettingsLoader.getSettings().getItemGiveSettings());

        sender.sendMessage(Component.text("Added enchantment to ").color(NamedTextColor.GREEN)
                .append(Component.text(aliasName).color(NamedTextColor.YELLOW))
                .append(Component.text(": ").color(NamedTextColor.GRAY))
                .append(Component.text(enchantmentName + " " + level).color(NamedTextColor.LIGHT_PURPLE)));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Map<String, ItemAlias> aliases = SettingsLoader.getSettings().getItemGiveSettings().getItemAliases();
            if (aliases != null) {
                List<String> suggestions = new ArrayList<>();
                for (String alias : aliases.keySet()) {
                    if (alias.startsWith(args[0].toLowerCase())) {
                        suggestions.add(alias);
                    }
                }
                return suggestions;
            }
        } else if (args.length == 2) {
            List<String> suggestions = new ArrayList<>();
            for (Enchantment ench : Enchantment.values()) {
                if (ench.getKey().getKey().toLowerCase().startsWith(args[1].toLowerCase())) {
                    suggestions.add(ench.getKey().getKey().toUpperCase());
                }
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "addench";
    }

    @Override
    public String getDescription() {
        return "Add enchantment to alias";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

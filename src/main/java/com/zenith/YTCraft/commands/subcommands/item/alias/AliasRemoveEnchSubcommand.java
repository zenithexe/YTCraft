package com.zenith.YTCraft.commands.subcommands.item.alias;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.processors.ItemGiveSettingsProcessor;
import com.zenith.YTCraft.config.types.ItemGiveSettings;
import com.zenith.YTCraft.config.types.ItemGiveSettings.ItemAlias;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AliasRemoveEnchSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /ytcraft item alias removeench <alias> <enchantment>").color(NamedTextColor.RED));
            return true;
        }

        String aliasName = args[0].toLowerCase();
        String enchantmentName = args[1].toUpperCase();

        Map<String, ItemAlias> aliases = SettingsLoader.getSettings().getItemGiveSettings().getItemAliases();

        if (aliases == null || !aliases.containsKey(aliasName)) {
            sender.sendMessage(Component.text("Alias not found: " + aliasName).color(NamedTextColor.RED));
            return true;
        }

        ItemAlias alias = aliases.get(aliasName);

        if (alias.getEnchantments() == null || alias.getEnchantments().isEmpty()) {
            sender.sendMessage(Component.text("Alias has no enchantments!").color(NamedTextColor.YELLOW));
            return true;
        }

        // Find and remove enchantment
        boolean found = false;
        for (int i = 0; i < alias.getEnchantments().size(); i++) {
            if (alias.getEnchantments().get(i).getEnchantment().equalsIgnoreCase(enchantmentName)) {
                alias.getEnchantments().remove(i);
                found = true;
                break;
            }
        }

        if (!found) {
            sender.sendMessage(Component.text("Enchantment not found: " + enchantmentName).color(NamedTextColor.RED));
            return true;
        }

        SettingsLoader.saveSettings();
        ItemGiveSettingsProcessor.process(SettingsLoader.getSettings().getItemGiveSettings());

        sender.sendMessage(Component.text("Removed enchantment from ").color(NamedTextColor.GREEN)
                .append(Component.text(aliasName).color(NamedTextColor.YELLOW))
                .append(Component.text(": ").color(NamedTextColor.GRAY))
                .append(Component.text(enchantmentName).color(NamedTextColor.LIGHT_PURPLE)));
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
            Map<String, ItemAlias> aliases = SettingsLoader.getSettings().getItemGiveSettings().getItemAliases();
            if (aliases != null && aliases.containsKey(args[0].toLowerCase())) {
                ItemAlias alias = aliases.get(args[0].toLowerCase());
                if (alias.getEnchantments() != null) {
                    List<String> suggestions = new ArrayList<>();
                    for (ItemGiveSettings.Enchantment ench : alias.getEnchantments()) {
                        if (ench.getEnchantment().toLowerCase().startsWith(args[1].toLowerCase())) {
                            suggestions.add(ench.getEnchantment());
                        }
                    }
                    return suggestions;
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "removeench";
    }

    @Override
    public String getDescription() {
        return "Remove enchantment from alias";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

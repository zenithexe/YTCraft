package com.zenith.YTCraft.commands.subcommands.item.alias;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.ItemGiveSettings;
import com.zenith.YTCraft.config.types.ItemGiveSettings.ItemAlias;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AliasInfoSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft item alias info <alias>").color(NamedTextColor.RED));
            return true;
        }

        String aliasName = args[0].toLowerCase();
        Map<String, ItemAlias> aliases = SettingsLoader.getSettings().getItemGiveSettings().getItemAliases();

        if (aliases == null || !aliases.containsKey(aliasName)) {
            sender.sendMessage(Component.text("Alias not found: " + aliasName).color(NamedTextColor.RED));
            return true;
        }

        ItemAlias alias = aliases.get(aliasName);

        sender.sendMessage(Component.text("=== Alias: " + aliasName + " ===").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("  Item: ").color(NamedTextColor.GRAY)
                .append(Component.text(alias.getItem()).color(NamedTextColor.GREEN)));
        sender.sendMessage(Component.text("  Quantity: ").color(NamedTextColor.GRAY)
                .append(Component.text(String.valueOf(alias.getQty())).color(NamedTextColor.YELLOW)));

        if (alias.getEnchantments() != null && !alias.getEnchantments().isEmpty()) {
            sender.sendMessage(Component.text("  Enchantments:").color(NamedTextColor.GRAY));
            for (ItemGiveSettings.Enchantment ench : alias.getEnchantments()) {
                sender.sendMessage(Component.text("    - ").color(NamedTextColor.GRAY)
                        .append(Component.text(ench.getEnchantment() + " " + ench.getLevel()).color(NamedTextColor.LIGHT_PURPLE)));
            }
        } else {
            sender.sendMessage(Component.text("  Enchantments: None").color(NamedTextColor.GRAY));
        }

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
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Show alias details";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

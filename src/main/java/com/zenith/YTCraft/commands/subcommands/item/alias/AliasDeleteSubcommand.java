package com.zenith.YTCraft.commands.subcommands.item.alias;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.processors.ItemGiveSettingsProcessor;
import com.zenith.YTCraft.config.types.ItemGiveSettings.ItemAlias;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AliasDeleteSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft item alias delete <alias>").color(NamedTextColor.RED));
            return true;
        }

        String aliasName = args[0].toLowerCase();
        Map<String, ItemAlias> aliases = SettingsLoader.getSettings().getItemGiveSettings().getItemAliases();

        if (aliases == null || !aliases.containsKey(aliasName)) {
            sender.sendMessage(Component.text("Alias not found: " + aliasName).color(NamedTextColor.RED));
            return true;
        }

        aliases.remove(aliasName);
        SettingsLoader.saveSettings();
        ItemGiveSettingsProcessor.process(SettingsLoader.getSettings().getItemGiveSettings());

        sender.sendMessage(Component.text("Deleted alias: ").color(NamedTextColor.GREEN)
                .append(Component.text(aliasName).color(NamedTextColor.YELLOW)));
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
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Delete an alias";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

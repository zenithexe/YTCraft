package com.zenith.YTCraft.commands.subcommands.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.processors.ItemGiveSettingsProcessor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ItemUnbanSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft item unban <item>").color(NamedTextColor.RED));
            return true;
        }

        String itemName = args[0].toUpperCase();
        List<String> bannedItems = SettingsLoader.getSettings().getItemGiveSettings().getBannedItems();

        if (bannedItems == null || !bannedItems.contains(itemName)) {
            sender.sendMessage(Component.text(itemName + " is not banned!").color(NamedTextColor.YELLOW));
            return true;
        }

        bannedItems.remove(itemName);
        SettingsLoader.saveSettings();
        ItemGiveSettingsProcessor.process(SettingsLoader.getSettings().getItemGiveSettings());

        sender.sendMessage(Component.text("Unbanned item: ").color(NamedTextColor.GREEN)
                .append(Component.text(itemName).color(NamedTextColor.YELLOW)));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> bannedItems = SettingsLoader.getSettings().getItemGiveSettings().getBannedItems();
            if (bannedItems != null) {
                List<String> suggestions = new ArrayList<>();
                for (String item : bannedItems) {
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
        return "unban";
    }

    @Override
    public String getDescription() {
        return "Unban an item";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

package com.zenith.YTCraft.commands.subcommands.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.ItemGiveSettings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ItemModeSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            ItemGiveSettings.ModeSettings currentMode = SettingsLoader.getSettings().getItemGiveSettings().getMode();
            sender.sendMessage(Component.text("Current mode: ").color(NamedTextColor.GRAY)
                    .append(Component.text(currentMode.toString()).color(NamedTextColor.YELLOW)));
            sender.sendMessage(Component.text("Available modes: all, allowed_only").color(NamedTextColor.GRAY));
            return true;
        }

        String mode = args[0].toLowerCase();
        ItemGiveSettings.ModeSettings newMode;

        switch (mode) {
            case "all":
                newMode = ItemGiveSettings.ModeSettings.ALL;
                break;
            case "allowed_only":
                newMode = ItemGiveSettings.ModeSettings.ALLOWED_ONLY;
                break;
            default:
                sender.sendMessage(Component.text("Invalid mode! Use: all, allowed_only").color(NamedTextColor.RED));
                return true;
        }

        SettingsLoader.getSettings().getItemGiveSettings().setMode(newMode);
        SettingsLoader.saveSettings();

        sender.sendMessage(Component.text("Item mode set to: ").color(NamedTextColor.GREEN)
                .append(Component.text(newMode.toString()).color(NamedTextColor.YELLOW)));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("all");
            suggestions.add("allowed_only");
            return suggestions;
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "mode";
    }

    @Override
    public String getDescription() {
        return "Set item mode (all/allowed_only)";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

package com.zenith.YTCraft.commands.subcommands.custommob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CustomMobToggleSpawnmeSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            // Show current status
            boolean enabled = SettingsLoader.getSettings().getCustomMobSettings().isUsernameSpawnsEnabled();
            sender.sendMessage(Component.text("Username spawns (spawnme) is currently: ").color(NamedTextColor.GRAY)
                    .append(Component.text(enabled ? "ENABLED" : "DISABLED")
                            .color(enabled ? NamedTextColor.GREEN : NamedTextColor.RED)));
            sender.sendMessage(Component.text("Use: /ytcraft custommob spawnme <on|off>").color(NamedTextColor.GRAY));
            return true;
        }

        String action = args[0].toLowerCase();
        boolean newValue;

        switch (action) {
            case "on":
            case "enable":
            case "true":
                newValue = true;
                break;
            case "off":
            case "disable":
            case "false":
                newValue = false;
                break;
            default:
                sender.sendMessage(Component.text("Invalid option. Use: on/off, enable/disable, or true/false").color(NamedTextColor.RED));
                return true;
        }

        // Update the setting
        SettingsLoader.getSettings().getCustomMobSettings().setUsernameSpawns(newValue);
        SettingsLoader.saveSettings();

        sender.sendMessage(Component.text("✓ Username spawns (spawnme) ").color(NamedTextColor.GREEN)
                .append(Component.text(newValue ? "ENABLED" : "DISABLED")
                        .color(newValue ? NamedTextColor.GREEN : NamedTextColor.RED)));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("enable", "disable");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "spawnme";
    }

    @Override
    public String getDescription() {
        return "Toggle username spawns (spawnme command)";
    }
}

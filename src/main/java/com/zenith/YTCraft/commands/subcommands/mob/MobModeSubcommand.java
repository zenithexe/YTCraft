package com.zenith.YTCraft.commands.subcommands.mob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MobModeSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft mob mode <all|tier_only|tier_plus_unlisted>").color(NamedTextColor.RED));
            return true;
        }

        try {
            MobSpawnSettings.ModeSettings mode = MobSpawnSettings.ModeSettings.valueOf(args[0].toUpperCase());
            SettingsLoader.getSettings().getMobSpawnSettings().setMode(mode);
            SettingsLoader.saveSettings();
            
            sender.sendMessage(Component.text("Mob spawn mode set to: " + mode).color(NamedTextColor.GREEN));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text("Invalid mode! Use: all, tier_only, or tier_plus_unlisted").color(NamedTextColor.RED));
        }
        
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("all", "tier_only", "tier_plus_unlisted");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "mode";
    }

    @Override
    public String getDescription() {
        return "Set mob spawn mode";
    }
}

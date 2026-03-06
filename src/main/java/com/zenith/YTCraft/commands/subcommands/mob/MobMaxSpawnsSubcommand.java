package com.zenith.YTCraft.commands.subcommands.mob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MobMaxSpawnsSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft mob maxspawns <number>").color(NamedTextColor.RED));
            return true;
        }

        try {
            int maxSpawns = Integer.parseInt(args[0]);
            if (maxSpawns < 0) {
                sender.sendMessage(Component.text("Max spawns must be positive!").color(NamedTextColor.RED));
                return true;
            }

            SettingsLoader.getSettings().getMobSpawnSettings().setMaxSpawns(maxSpawns);
            SettingsLoader.saveSettings();

            sender.sendMessage(Component.text("Max spawns set to: " + maxSpawns).color(NamedTextColor.GREEN));
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid number!").color(NamedTextColor.RED));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("50", "100", "200");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "maxspawns";
    }

    @Override
    public String getDescription() {
        return "Set maximum number of spawned mobs";
    }
}

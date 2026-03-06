package com.zenith.YTCraft.commands.subcommands.mob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MobKillOnDeathSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft mob killondeath <true|false>").color(NamedTextColor.RED));
            return true;
        }

        boolean value = Boolean.parseBoolean(args[0]);
        SettingsLoader.getSettings().getMobSpawnSettings().setKillAllOnDeath(value);
        SettingsLoader.saveSettings();
        
        sender.sendMessage(Component.text("Kill all mobs on death: " + value).color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("true", "false");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "killondeath";
    }

    @Override
    public String getDescription() {
        return "Set whether to kill all mobs on player death";
    }
}

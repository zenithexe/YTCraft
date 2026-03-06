package com.zenith.YTCraft.commands.subcommands.mob;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MobUnbanSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft mob unban <mob_type>").color(NamedTextColor.RED));
            return true;
        }

        String mobType = args[0].toUpperCase();
        List<String> bannedMobs = SettingsLoader.getSettings().getMobSpawnSettings().getBannedMobs();
        
        if (bannedMobs == null || !bannedMobs.contains(mobType)) {
            sender.sendMessage(Component.text(mobType + " is not banned!").color(NamedTextColor.YELLOW));
            return true;
        }
        
        bannedMobs.remove(mobType);
        SettingsLoader.saveSettings();
        
        sender.sendMessage(Component.text("Unbanned mob: " + mobType).color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> bannedMobs = SettingsLoader.getSettings().getMobSpawnSettings().getBannedMobs();
            if (bannedMobs != null) {
                List<String> suggestions = new ArrayList<>();
                for (String mob : bannedMobs) {
                    String name = mob.toLowerCase();
                    if (name.startsWith(args[0].toLowerCase())) {
                        suggestions.add(name);
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
        return "Unban a mob type";
    }
}

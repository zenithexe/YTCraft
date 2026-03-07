package com.zenith.YTCraft.commands.subcommands.mob;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MobBanSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft mob ban <mob_type>").color(NamedTextColor.RED));
            return true;
        }

        try {
            EntityType.valueOf(args[0].toUpperCase());
            String mobType = args[0].toUpperCase();
            
            List<String> bannedMobs = SettingsLoader.getSettings().getMobSpawnSettings().getBannedMobs();
            if (bannedMobs == null) {
                bannedMobs = new ArrayList<>();
                SettingsLoader.getSettings().getMobSpawnSettings().setBannedMobs(bannedMobs);
            }
            
            if (bannedMobs.contains(mobType)) {
                sender.sendMessage(Component.text(mobType + " is already banned!").color(NamedTextColor.YELLOW));
                return true;
            }
            
            bannedMobs.add(mobType);
            SettingsLoader.saveSettings();
            
            sender.sendMessage(Component.text("Banned mob: " + mobType).color(NamedTextColor.GREEN));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text("Invalid mob type!").color(NamedTextColor.RED));
        }
        
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (EntityType type : EntityType.values()) {
                if (type.isAlive()) {
                    String name = type.name();
                    if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
                        suggestions.add(name);
                    }
                }
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getDescription() {
        return "Ban a mob type from spawning";
    }
}

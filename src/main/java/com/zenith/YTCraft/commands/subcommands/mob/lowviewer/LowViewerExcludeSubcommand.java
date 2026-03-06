package com.zenith.YTCraft.commands.subcommands.mob.lowviewer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class LowViewerExcludeSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft mob lowviewer exclude <mob_type>").color(NamedTextColor.RED));
            return true;
        }

        try {
            EntityType.valueOf(args[0].toUpperCase());
            String mobType = args[0].toUpperCase();
            MobSpawnSettings.LowViewerMode lowViewerMode = SettingsLoader.getSettings().getMobSpawnSettings().getLowViewerMode();
            
            if (lowViewerMode != null) {
                List<String> excludeList = lowViewerMode.getExclude();
                if (excludeList == null) {
                    excludeList = new ArrayList<>();
                    lowViewerMode.setExclude(excludeList);
                }
                
                if (!excludeList.contains(mobType)) {
                    excludeList.add(mobType);
                    SettingsLoader.saveSettings();
                    sender.sendMessage(Component.text("Added " + mobType + " to exclude list").color(NamedTextColor.GREEN));
                } else {
                    sender.sendMessage(Component.text(mobType + " is already in exclude list").color(NamedTextColor.YELLOW));
                }
            }
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
                    String name = type.name().toLowerCase();
                    if (name.startsWith(args[0].toLowerCase())) {
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
        return "exclude";
    }

    @Override
    public String getDescription() {
        return "Add mob to exclude list";
    }
}

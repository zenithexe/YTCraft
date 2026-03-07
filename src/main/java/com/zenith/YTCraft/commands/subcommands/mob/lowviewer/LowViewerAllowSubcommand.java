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

public class LowViewerAllowSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft mob lowviewer allow <mob_type>").color(NamedTextColor.RED));
            return true;
        }

        try {
            EntityType.valueOf(args[0].toUpperCase());
            String mobType = args[0].toUpperCase();
            MobSpawnSettings.LowViewerMode lowViewerMode = SettingsLoader.getSettings().getMobSpawnSettings().getLowViewerMode();
            
            if (lowViewerMode != null) {
                List<String> allowList = lowViewerMode.getAllow();
                if (allowList == null) {
                    allowList = new ArrayList<>();
                    lowViewerMode.setAllow(allowList);
                }
                
                if (!allowList.contains(mobType)) {
                    allowList.add(mobType);
                    SettingsLoader.saveSettings();
                    sender.sendMessage(Component.text("Added " + mobType + " to allow list").color(NamedTextColor.GREEN));
                } else {
                    sender.sendMessage(Component.text(mobType + " is already in allow list").color(NamedTextColor.YELLOW));
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
        return "allow";
    }

    @Override
    public String getDescription() {
        return "Add mob to allow list";
    }
}

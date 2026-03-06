package com.zenith.YTCraft.commands.subcommands.mob;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MobListBannedSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        List<String> bannedMobs = SettingsLoader.getSettings().getMobSpawnSettings().getBannedMobs();
        
        if (bannedMobs == null || bannedMobs.isEmpty()) {
            sender.sendMessage(Component.text("No mobs are banned").color(NamedTextColor.YELLOW));
            return true;
        }
        
        sender.sendMessage(Component.text("Banned mobs:").color(NamedTextColor.GOLD));
        for (String mob : bannedMobs) {
            sender.sendMessage(Component.text("  - " + mob).color(NamedTextColor.GRAY));
        }
        
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "listbanned";
    }

    @Override
    public String getDescription() {
        return "List all banned mobs";
    }
}

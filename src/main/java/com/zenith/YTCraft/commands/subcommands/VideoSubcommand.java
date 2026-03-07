package com.zenith.YTCraft.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.config.ConfigManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class VideoSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft video <videoid>").color(NamedTextColor.RED));
            return true;
        }

        String videoId = args[0];
        
        // Basic validation for YouTube video ID format (11 characters)
        if (videoId.length() != 11) {
            sender.sendMessage(Component.text("Invalid video ID format. YouTube video IDs are 11 characters long.").color(NamedTextColor.RED));
            return true;
        }

        try {
            YTCraft.getPlugin().getConfig().set("VIDEO_ID", videoId);
            YTCraft.getPlugin().saveConfig();
            ConfigManager.loadConfig();
            sender.sendMessage(Component.text("Video ID set to: " + videoId).color(NamedTextColor.GREEN));
        } catch (Exception e) {
            sender.sendMessage(Component.text("Failed to set video ID: " + e.getMessage()).color(NamedTextColor.RED));
        }
        
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "video";
    }

    @Override
    public String getDescription() {
        return "Set the YouTube video ID";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

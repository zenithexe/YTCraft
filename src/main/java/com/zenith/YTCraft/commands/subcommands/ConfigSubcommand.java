package com.zenith.YTCraft.commands.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.api.YoutubeAPI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * /yt config channel <channel-id | @handle | username>
 *   - if a channel ID (starts with "UC", 24 chars): sets runtime + saves to config
 *   - if a username/handle: resolves to channel ID via API, then sets runtime + saves to config
 */
public class ConfigSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sendUsage(sender);
            return true;
        }

        String sub = args[0].toLowerCase();
        String value = args[1];

        switch (sub) {
            case "channel":
                handleChannelInput(sender, value);
                break;

            default:
                sendUsage(sender);
        }

        return true;
    }

    private void handleChannelInput(CommandSender sender, String input) {
        // Channel IDs start with "UC" and are 24 characters
        if (input.startsWith("UC") && input.length() == 24) {
            applyChannelId(sender, input);
        } else {
            // Treat as username/handle — resolve async to avoid blocking the server
            sender.sendMessage(Component.text("[YTCraft] Resolving channel username...").color(NamedTextColor.GRAY));
            Bukkit.getScheduler().runTaskAsynchronously(YTCraft.getPlugin(), () -> {
                String resolvedId = YoutubeAPI.resolveChannelIdByUsername(input);
                Bukkit.getScheduler().runTask(YTCraft.getPlugin(), () -> {
                    if (resolvedId == null) {
                        sender.sendMessage(Component.text("[YTCraft] Could not find a channel for: " + input)
                                .color(NamedTextColor.RED));
                        sender.sendMessage(Component.text("Check the username/handle and try again.")
                                .color(NamedTextColor.YELLOW));
                    } else {
                        applyChannelId(sender, resolvedId);
                    }
                });
            });
        }
    }

    private void applyChannelId(CommandSender sender, String channelId) {
        YoutubeAPI.updateChannelId(channelId);
        YTCraft.getPlugin().getConfig().set("CHANNEL_ID", channelId);
        YTCraft.getPlugin().saveConfig();
        sender.sendMessage(Component.text("[YTCraft] Channel ID set to: " + channelId)
                .color(NamedTextColor.GREEN));
        sender.sendMessage(Component.text("Saved to config.yml. Use /yt connect to find live broadcasts.")
                .color(NamedTextColor.GRAY));
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(Component.text("Usage: /yt config channel <channel-id | @handle | username>").color(NamedTextColor.YELLOW));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("channel");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "config";
    }

    @Override
    public String getDescription() {
        return "Set the channel ID or username";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

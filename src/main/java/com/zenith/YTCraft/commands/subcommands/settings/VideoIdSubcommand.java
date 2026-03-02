package com.zenith.YTCraft.commands.subcommands.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.api.YoutubeAPI;
import com.zenith.YTCraft.commands.subcommands.Subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class VideoIdSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /ytcraft settings videoid <video_id>").color(NamedTextColor.RED));
            return true;
        }

        String videoId = args[0];
        YTCraft.getPlugin().getConfig().set("VIDEO_ID", videoId);
        YTCraft.getPlugin().saveConfig();

        YoutubeAPI.updateVideoId(videoId);

        sender.sendMessage(Component.text("Video ID set to: ").color(NamedTextColor.AQUA)
                .append(Component.text(videoId).color(NamedTextColor.GREEN)));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("<video_id>");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "videoid";
    }

    @Override
    public String getDescription() {
        return "Set the YouTube video ID";
    }
}

package com.zenith.YTCraft.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.api.YoutubeAPI;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.timer.PluginTimer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class YTSettingsCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args[0].equalsIgnoreCase("setVideoId") && args.length == 2) {
            YTCraft.getPlugin().getConfig().set("VIDEO_ID", args[1]);
            YTCraft.getPlugin().saveConfig();

            YoutubeAPI.updateVideoId(args[1]);

            sender.sendMessage(Component.text("Video Id set to " + args[1]).color(NamedTextColor.AQUA));
            return true;
        }

        if (args[0].equalsIgnoreCase("timer") && args.length >= 2) {

            if (args[1].equalsIgnoreCase("setRestTime") && args.length == 4) {
                try {
                    PluginState.setRestTime(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                    sender.sendMessage(Component.text("Rest-Time set to ").color(NamedTextColor.AQUA)
                            .append(Component.text(args[2] + ":" + args[3]).color(NamedTextColor.GREEN)));
                    return true;
                } catch (Exception e) {
                    sender.sendMessage(Component.text("Error : Wrong Command Arguments !!").color(NamedTextColor.RED));
                    return false;
                }
            }

            if (args[1].equalsIgnoreCase("setActiveTime") && args.length == 4) {
                try {
                    PluginState.setActiveTime(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                    sender.sendMessage(Component.text("Active-Time set to ")
                            .color(NamedTextColor.AQUA)
                            .append(Component.text(args[2] + ":" + args[3]).color(NamedTextColor.GREEN)));
                    return true;
                } catch (Exception e) {
                    sender.sendMessage(Component.text("Error : Wrong Command Arguments !!").color(NamedTextColor.RED));
                    return false;
                }
            }

            if (args[1].equalsIgnoreCase("skip")) {
                PluginTimer.isForceToggle = true;

                sender.sendMessage(Component.text("Current Timer skipped!").color(NamedTextColor.GREEN));

                return true;
            }

            if (args.length < 4) {
                sender.sendMessage(Component.text("Error : Wrong Command Arguments !!").color(NamedTextColor.RED));
                return false;
            }
        }

        return false;
    }

    @Override
    public @Nullable
    List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {
            return Arrays.asList("setVideoId", "timer");
        }

        if (args.length == 2) {
            if (args[0].equals("setVideoId")) {
                return Arrays.asList("{video_id}");
            }
        }

        if (args.length == 2) {
            if (args[0].equals("timer")) {
                return Arrays.asList("setRestTime", "setActiveTime", "skip");
            }
        }

        if (args.length == 3) {
            if (args[1].equals("setRestTime") || args[1].equals("setActiveTime")) {
                return Arrays.asList("{Minute_Value}");
            }
        }

        if (args.length == 4) {
            if (args[1].equals("setRestTime") || args[1].equals("setActiveTime")) {
                return Arrays.asList("{Second_Value}");
            }
        }

        return new ArrayList<>();
    }
}

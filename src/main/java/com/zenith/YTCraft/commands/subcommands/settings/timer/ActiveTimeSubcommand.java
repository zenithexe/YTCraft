package com.zenith.YTCraft.commands.subcommands.settings.timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.data.PluginState;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ActiveTimeSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /ytcraft settings timer active <minutes> <seconds>").color(NamedTextColor.RED));
            return true;
        }

        try {
            int minutes = Integer.parseInt(args[0]);
            int seconds = Integer.parseInt(args[1]);

            if (minutes < 0 || seconds < 0 || seconds >= 60) {
                sender.sendMessage(Component.text("Invalid time values! Minutes >= 0, Seconds 0-59").color(NamedTextColor.RED));
                return true;
            }

            PluginState.setActiveTime(minutes, seconds);
            sender.sendMessage(Component.text("Active time set to: ").color(NamedTextColor.AQUA)
                    .append(Component.text(String.format("%02d:%02d", minutes, seconds)).color(NamedTextColor.GREEN)));

            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid number format!").color(NamedTextColor.RED));
            return true;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("<minutes>");
        } else if (args.length == 2) {
            return Arrays.asList("<seconds>");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "active";
    }

    @Override
    public String getDescription() {
        return "Set the active time duration";
    }
}

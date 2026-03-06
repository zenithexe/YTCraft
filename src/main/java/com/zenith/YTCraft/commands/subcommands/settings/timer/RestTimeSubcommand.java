package com.zenith.YTCraft.commands.subcommands.settings.timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.data.TimerState;
import com.zenith.YTCraft.timer.PluginTimer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class RestTimeSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /ytcraft settings timer rest <seconds>").color(NamedTextColor.RED));
            sender.sendMessage(Component.text("Example: /ytcraft settings timer rest 180 (for 3 minutes)").color(NamedTextColor.GRAY));
            return true;
        }

        try {
            int seconds = Integer.parseInt(args[0]);

            if (seconds < 0) {
                sender.sendMessage(Component.text("Invalid time value! Seconds must be >= 0").color(NamedTextColor.RED));
                return true;
            }

            TimerState.setRestTime(seconds);
            
            // Update the running timer instance if it exists
            PluginTimer timer = PluginTimer.getInstance();
            if (timer != null) {
                timer.setRestTimer(seconds);
            }
            
            // Format display
            int hours = seconds / 3600;
            int minutes = (seconds / 60) % 60;
            int secs = seconds % 60;
            
            String timeDisplay;
            if (hours > 0) {
                timeDisplay = String.format("%02d:%02d:%02d", hours, minutes, secs);
            } else {
                timeDisplay = String.format("%02d:%02d", minutes, secs);
            }
            
            sender.sendMessage(Component.text("Rest time set to: ").color(NamedTextColor.AQUA)
                    .append(Component.text(timeDisplay + " (" + seconds + " seconds)").color(NamedTextColor.GREEN)));

            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid number format!").color(NamedTextColor.RED));
            return true;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("<seconds>", "60", "180", "300", "600");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "rest";
    }

    @Override
    public String getDescription() {
        return "Set the rest time duration";
    }
}

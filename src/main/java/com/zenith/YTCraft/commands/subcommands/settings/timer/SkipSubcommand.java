package com.zenith.YTCraft.commands.subcommands.settings.timer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.timer.PluginTimer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SkipSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        PluginTimer.skipTimerMode();
        sender.sendMessage(Component.text("Current timer skipped!").color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skip the current timer phase";
    }
}

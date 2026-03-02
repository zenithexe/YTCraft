package com.zenith.YTCraft.commands.subcommands.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.commands.subcommands.settings.timer.TimerSubcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SettingsSubcommand implements Subcommand {

    private final Map<String, Subcommand> subcommands = new HashMap<>();

    public SettingsSubcommand() {
        registerSubcommand(new VideoIdSubcommand());
        registerSubcommand(new TimerSubcommand());
    }

    private void registerSubcommand(Subcommand subcommand) {
        subcommands.put(subcommand.getName().toLowerCase(), subcommand);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Settings subcommands:").color(NamedTextColor.YELLOW));
            for (Subcommand sub : subcommands.values()) {
                sender.sendMessage(Component.text("  - " + sub.getName() + ": " + sub.getDescription()).color(NamedTextColor.GRAY));
            }
            return true;
        }

        String subcommandName = args[0].toLowerCase();
        Subcommand subcommand = subcommands.get(subcommandName);

        if (subcommand == null) {
            sender.sendMessage(Component.text("Unknown settings subcommand: " + subcommandName).color(NamedTextColor.RED));
            return true;
        }

        // Pass remaining args to subcommand
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, args.length - 1);

        return subcommand.execute(sender, subArgs);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(subcommands.keySet());
        }

        if (args.length > 1) {
            Subcommand subcommand = subcommands.get(args[0].toLowerCase());
            if (subcommand != null) {
                String[] subArgs = new String[args.length - 1];
                System.arraycopy(args, 1, subArgs, 0, args.length - 1);
                return subcommand.tabComplete(sender, subArgs);
            }
        }

        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public String getDescription() {
        return "Configure YTCraft settings";
    }
}

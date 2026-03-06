package com.zenith.YTCraft.commands.subcommands.mob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MobSubcommand implements Subcommand {

    private final Map<String, Subcommand> subcommands = new HashMap<>();

    public MobSubcommand() {
        registerSubcommand(new MobEnableSubcommand());
        registerSubcommand(new MobDisableSubcommand());
        registerSubcommand(new MobKillOnDeathSubcommand());
        registerSubcommand(new MobAllowKillSubcommand());
        registerSubcommand(new MobMaxSpawnsSubcommand());
        registerSubcommand(new MobModeSubcommand());
        registerSubcommand(new MobBanSubcommand());
        registerSubcommand(new MobUnbanSubcommand());
        registerSubcommand(new MobListBannedSubcommand());
        registerSubcommand(new MobLowViewerSubcommand());
        registerSubcommand(new MobTierSubcommand());
    }

    private void registerSubcommand(Subcommand subcommand) {
        subcommands.put(subcommand.getName().toLowerCase(), subcommand);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subcommandName = args[0].toLowerCase();
        Subcommand subcommand = subcommands.get(subcommandName);

        if (subcommand == null) {
            sender.sendMessage(Component.text("Unknown mob command: " + subcommandName).color(NamedTextColor.RED));
            sendHelp(sender);
            return true;
        }

        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, args.length - 1);

        return subcommand.execute(sender, subArgs);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (String name : subcommands.keySet()) {
                if (name.startsWith(args[0].toLowerCase())) {
                    suggestions.add(name);
                }
            }
            return suggestions;
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
        return "mob";
    }

    @Override
    public String getDescription() {
        return "Manage mob spawn settings";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("=== Mob Commands ===").color(NamedTextColor.GOLD));
        for (Subcommand sub : subcommands.values()) {
            sender.sendMessage(Component.text("  /ytcraft mob " + sub.getName()).color(NamedTextColor.YELLOW)
                    .append(Component.text(" - " + sub.getDescription()).color(NamedTextColor.GRAY)));
        }
    }
}

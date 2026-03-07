package com.zenith.YTCraft.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.zenith.YTCraft.commands.subcommands.EndSubcommand;
import com.zenith.YTCraft.commands.subcommands.ReloadSubcommand;
import com.zenith.YTCraft.commands.subcommands.SaveSubcommand;
import com.zenith.YTCraft.commands.subcommands.StartSubcommand;
import com.zenith.YTCraft.commands.subcommands.StatusSubcommand;
import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.commands.subcommands.VideoSubcommand;
import com.zenith.YTCraft.commands.subcommands.custommob.CustomMobSubcommand;
import com.zenith.YTCraft.commands.subcommands.item.ItemSubcommand;
import com.zenith.YTCraft.commands.subcommands.mob.MobSubcommand;
import com.zenith.YTCraft.commands.subcommands.timer.TimerSubcommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class YTCraftCommand implements TabExecutor {

    private final Map<String, Subcommand> subcommands = new HashMap<>();

    public YTCraftCommand() {
        registerSubcommand(new StartSubcommand());
        registerSubcommand(new EndSubcommand());
        registerSubcommand(new MobSubcommand());
        registerSubcommand(new ItemSubcommand());
        registerSubcommand(new TimerSubcommand());
        registerSubcommand(new CustomMobSubcommand());
        registerSubcommand(new ReloadSubcommand());
        registerSubcommand(new SaveSubcommand());
        registerSubcommand(new StatusSubcommand());
        registerSubcommand(new VideoSubcommand());
    }

    private void registerSubcommand(Subcommand subcommand) {
        subcommands.put(subcommand.getName().toLowerCase(), subcommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subcommandName = args[0].toLowerCase();
        Subcommand subcommand = subcommands.get(subcommandName);

        if (subcommand == null) {
            sender.sendMessage(Component.text("Unknown command: " + subcommandName).color(NamedTextColor.RED));
            sender.sendMessage(Component.text("Use /ytcraft for help").color(NamedTextColor.GRAY));
            return true;
        }

        // Check permission if required
        String permission = subcommand.getPermission();
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage(Component.text("You don't have permission to use this command!").color(NamedTextColor.RED));
            return true;
        }

        // Pass remaining args to subcommand
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, args.length - 1);

        return subcommand.execute(sender, subArgs);
    }

    @Override
    public @Nullable
    List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

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

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("=== YTCraft Commands ===").color(NamedTextColor.GOLD));
        for (Subcommand sub : subcommands.values()) {
            sender.sendMessage(Component.text("  /ytcraft " + sub.getName()).color(NamedTextColor.YELLOW)
                    .append(Component.text(" - " + sub.getDescription()).color(NamedTextColor.GRAY)));
        }
    }
}

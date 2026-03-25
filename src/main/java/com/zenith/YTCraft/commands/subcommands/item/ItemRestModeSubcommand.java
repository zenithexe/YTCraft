package com.zenith.YTCraft.commands.subcommands.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ItemRestModeSubcommand implements Subcommand {

    private final Map<String, Subcommand> subcommands = new HashMap<>();

    public ItemRestModeSubcommand() {
        subcommands.put("enable", new ItemRestModeEnableSubcommand());
        subcommands.put("disable", new ItemRestModeDisableSubcommand());
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            boolean enabled = SettingsLoader.getSettings().getItemGiveSettings().isActiveInRestMode();
            sender.sendMessage(Component.text("Item giving in rest mode: ").color(NamedTextColor.GRAY)
                    .append(Component.text(enabled ? "ENABLED" : "DISABLED")
                            .color(enabled ? NamedTextColor.GREEN : NamedTextColor.RED)));
            sender.sendMessage(Component.text("Use: /ytcraft item restmode <enable|disable>").color(NamedTextColor.GRAY));
            return true;
        }

        Subcommand subcommand = subcommands.get(args[0].toLowerCase());
        if (subcommand == null) {
            sender.sendMessage(Component.text("Use: /ytcraft item restmode <enable|disable>").color(NamedTextColor.RED));
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
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "restmode";
    }

    @Override
    public String getDescription() {
        return "Toggle item giving during rest mode";
    }
}

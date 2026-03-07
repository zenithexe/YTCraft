package com.zenith.YTCraft.commands.subcommands.custommob;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.custommobs.CustomMobRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CustomMobInfoSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /ytcraft custommob info <mob_key_or_alias>").color(NamedTextColor.RED));
            return true;
        }

        String mobKey = args[0].toLowerCase();
        CustomMob mob = CustomMobRegistry.getCustomMob(mobKey);

        if (mob == null) {
            sender.sendMessage(Component.text("Custom mob not found: " + mobKey).color(NamedTextColor.RED));
            return true;
        }

        sender.sendMessage(Component.text("=== Custom Mob: " + mob.getMobKey() + " ===").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("Display Name: ").color(NamedTextColor.YELLOW)
                .append(Component.text(mob.getMobName()).color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("Entity Type: ").color(NamedTextColor.YELLOW)
                .append(Component.text(mob.getEntityType().toString()).color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("Player Username: ").color(NamedTextColor.YELLOW)
                .append(Component.text(mob.getPlayerUsername()).color(NamedTextColor.WHITE)));
        
        if (mob.hasAliases()) {
            sender.sendMessage(Component.text("Aliases: ").color(NamedTextColor.YELLOW)
                    .append(Component.text(String.join(", ", mob.getAliases())).color(NamedTextColor.WHITE)));
        } else {
            sender.sendMessage(Component.text("Aliases: ").color(NamedTextColor.YELLOW)
                    .append(Component.text("None").color(NamedTextColor.GRAY)));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (String key : CustomMobRegistry.getAllCustomMobs().keySet()) {
                if (key.startsWith(args[0].toLowerCase())) {
                    suggestions.add(key);
                }
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "View detailed info about a custom mob";
    }
}

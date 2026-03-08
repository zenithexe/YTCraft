package com.zenith.YTCraft.commands.subcommands.custommob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.CustomMobSettings;
import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.custommobs.CustomMobRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CustomMobAliasSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage:").color(NamedTextColor.RED));
            sender.sendMessage(Component.text("  /ytcraft custommob alias add <mob_key> <alias>").color(NamedTextColor.GRAY));
            sender.sendMessage(Component.text("  /ytcraft custommob alias remove <mob_key> <alias>").color(NamedTextColor.GRAY));
            sender.sendMessage(Component.text("  /ytcraft custommob alias list <mob_key>").color(NamedTextColor.GRAY));
            return true;
        }

        String action = args[0].toLowerCase();
        String mobKey = args[1].toLowerCase();

        CustomMob mob = CustomMobRegistry.getCustomMob(mobKey);
        if (mob == null) {
            sender.sendMessage(Component.text("Custom mob not found: " + mobKey).color(NamedTextColor.RED));
            return true;
        }

        String actualKey = mob.getMobKey();

        switch (action) {
            case "add":
                return handleAdd(sender, args, actualKey);
            case "remove":
                return handleRemove(sender, args, actualKey, mob);
            case "list":
                return handleList(sender, mob);
            default:
                sender.sendMessage(Component.text("Unknown action: " + action).color(NamedTextColor.RED));
                sender.sendMessage(Component.text("Valid actions: add, remove, list").color(NamedTextColor.GRAY));
                return true;
        }
    }

    private boolean handleAdd(CommandSender sender, String[] args, String mobKey) {
        if (args.length < 3) {
            sender.sendMessage(Component.text("Usage: /ytcraft custommob alias add <mob_key> <alias>").color(NamedTextColor.RED));
            return true;
        }

        String newAlias = args[2].toLowerCase();

        // Check if alias already exists
        if (CustomMobRegistry.isCustomMob(newAlias)) {
            sender.sendMessage(Component.text("Alias already exists: " + newAlias).color(NamedTextColor.RED));
            return true;
        }

        // Get config
        CustomMobSettings customMobSettings = SettingsLoader.getSettings().getCustomMobSettings();
        CustomMobSettings.CustomMobConfig config = customMobSettings.getMobs().get(mobKey);

        // Add alias
        String[] currentAliases = config.getAliases();
        List<String> aliasList = new ArrayList<>(currentAliases != null ? Arrays.asList(currentAliases) : new ArrayList<>());
        aliasList.add(newAlias);
        config.setAliases(aliasList.toArray(new String[0]));

        // Save and reload
        SettingsLoader.saveSettings();
        SettingsLoader.loadCustomMobs();

        sender.sendMessage(Component.text("✓ Added alias '").color(NamedTextColor.GREEN)
                .append(Component.text(newAlias).color(NamedTextColor.YELLOW))
                .append(Component.text("' to custom mob '").color(NamedTextColor.GREEN))
                .append(Component.text(mobKey).color(NamedTextColor.YELLOW))
                .append(Component.text("'").color(NamedTextColor.GREEN)));

        return true;
    }

    private boolean handleRemove(CommandSender sender, String[] args, String mobKey, CustomMob mob) {
        if (args.length < 3) {
            sender.sendMessage(Component.text("Usage: /ytcraft custommob alias remove <mob_key> <alias>").color(NamedTextColor.RED));
            return true;
        }

        String aliasToRemove = args[2].toLowerCase();

        // Check if alias exists
        boolean found = false;
        for (String alias : mob.getAliases()) {
            if (alias.equalsIgnoreCase(aliasToRemove)) {
                found = true;
                break;
            }
        }

        if (!found) {
            sender.sendMessage(Component.text("Alias not found: " + aliasToRemove).color(NamedTextColor.RED));
            return true;
        }

        // Get config
        CustomMobSettings customMobSettings = SettingsLoader.getSettings().getCustomMobSettings();
        CustomMobSettings.CustomMobConfig config = customMobSettings.getMobs().get(mobKey);

        // Remove alias
        List<String> aliasList = new ArrayList<>(Arrays.asList(config.getAliases()));
        aliasList.removeIf(alias -> alias.equalsIgnoreCase(aliasToRemove));
        config.setAliases(aliasList.toArray(new String[0]));

        // Save and reload
        SettingsLoader.saveSettings();
        SettingsLoader.loadCustomMobs();

        sender.sendMessage(Component.text("✓ Removed alias '").color(NamedTextColor.GREEN)
                .append(Component.text(aliasToRemove).color(NamedTextColor.YELLOW))
                .append(Component.text("' from custom mob '").color(NamedTextColor.GREEN))
                .append(Component.text(mobKey).color(NamedTextColor.YELLOW))
                .append(Component.text("'").color(NamedTextColor.GREEN)));

        return true;
    }

    private boolean handleList(CommandSender sender, CustomMob mob) {
        if (mob.hasAliases()) {
            sender.sendMessage(Component.text("Aliases for '").color(NamedTextColor.YELLOW)
                    .append(Component.text(mob.getMobKey()).color(NamedTextColor.GOLD))
                    .append(Component.text("': ").color(NamedTextColor.YELLOW))
                    .append(Component.text(String.join(", ", mob.getAliases())).color(NamedTextColor.WHITE)));
        } else {
            sender.sendMessage(Component.text("No aliases for '").color(NamedTextColor.YELLOW)
                    .append(Component.text(mob.getMobKey()).color(NamedTextColor.GOLD))
                    .append(Component.text("'").color(NamedTextColor.YELLOW)));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("add", "remove", "list").stream()
                    .filter(action -> action.startsWith(args[0].toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
        } else if (args.length == 2) {
            // Suggest mob keys
            List<String> suggestions = new ArrayList<>();
            for (CustomMob mob : CustomMobRegistry.getAllCustomMobsRegistry().values()) {
                String key = mob.getMobKey();
                if (key.startsWith(args[1].toLowerCase()) && !suggestions.contains(key)) {
                    suggestions.add(key);
                }
            }
            return suggestions;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("remove")) {
            // Suggest existing aliases for remove
            CustomMob mob = CustomMobRegistry.getCustomMob(args[1].toLowerCase());
            if (mob != null && mob.hasAliases()) {
                return Arrays.stream(mob.getAliases())
                        .filter(alias -> alias.startsWith(args[2].toLowerCase()))
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "alias";
    }

    @Override
    public String getDescription() {
        return "Manage custom mob aliases";
    }
}

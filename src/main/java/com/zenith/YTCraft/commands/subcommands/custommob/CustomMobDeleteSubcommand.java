package com.zenith.YTCraft.commands.subcommands.custommob;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.CustomMobSettings;
import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.custommobs.CustomMobRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CustomMobDeleteSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /ytcraft custommob delete <mob_key>").color(NamedTextColor.RED));
            return true;
        }

        String mobKey = args[0].toLowerCase();
        CustomMob mob = CustomMobRegistry.getCustomMob(mobKey);

        if (mob == null) {
            sender.sendMessage(Component.text("Custom mob not found: " + mobKey).color(NamedTextColor.RED));
            return true;
        }

        // Get the actual mob key (in case an alias was provided)
        String actualKey = mob.getMobKey();
        int aliasCount = mob.getAliases().length;

        // Remove from settings
        CustomMobSettings customMobSettings = SettingsLoader.getSettings().getCustomMobSettings();
        if (customMobSettings.getMobs() != null) {
            customMobSettings.getMobs().remove(actualKey);
        }

        // Save settings
        SettingsLoader.saveSettings();

        // Reload custom mobs
        SettingsLoader.loadCustomMobs();

        sender.sendMessage(Component.text("✓ Deleted custom mob '").color(NamedTextColor.GREEN)
                .append(Component.text(actualKey).color(NamedTextColor.YELLOW))
                .append(Component.text("'").color(NamedTextColor.GREEN)));
        
        if (aliasCount > 0) {
            sender.sendMessage(Component.text("  Removed " + aliasCount + " alias(es)").color(NamedTextColor.GRAY));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            // Only suggest main keys, not aliases
            for (CustomMob mob : CustomMobRegistry.getAllCustomMobsRegistry().values()) {
                String key = mob.getMobKey();
                if (key.startsWith(args[0].toLowerCase()) && !suggestions.contains(key)) {
                    suggestions.add(key);
                }
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Delete a custom mob";
    }
}

package com.zenith.YTCraft.commands.subcommands.mob.tier;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;
import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.custommobs.CustomMobRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TierAddSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /ytcraft mob tier add <min_viewers> <mob_type_or_custom_key>").color(NamedTextColor.RED));
            return true;
        }

        try {
            int minViewers = Integer.parseInt(args[0]);
            String mobInput = args[1].toLowerCase();
            String mobType;

            // Check if it's a custom mob first
            if (CustomMobRegistry.isCustomMob(mobInput)) {
                // Use the custom mob key (normalized to uppercase for consistency)
                CustomMob customMob = CustomMobRegistry.getCustomMob(mobInput);
                mobType = customMob.getMobKey().toUpperCase();
            } else {
                // Try to parse as vanilla EntityType
                try {
                    mobType = EntityType.valueOf(mobInput.toUpperCase()).toString();
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Component.text("Invalid mob type or custom mob key: " + mobInput).color(NamedTextColor.RED));
                    return true;
                }
            }

            List<MobSpawnSettings.MobTier> tiers = SettingsLoader.getSettings().getMobSpawnSettings().getMobTiers();

            // First, remove mob from any existing tier
            MobSpawnSettings.MobTier oldTier = null;
            for (MobSpawnSettings.MobTier tier : tiers) {

                if (tier.getMinViewers() == minViewers && tier.getMobs().contains(mobType)) {
                    sender.sendMessage(Component.text(mobType + " is already in this tier").color(NamedTextColor.YELLOW));
                    return true;
                }

                if (tier.getMobs().contains(mobType)) {
                    oldTier = tier;
                    tier.getMobs().remove(mobType);
                    break;
                }
            }

            // Find or create tier with this viewer count
            MobSpawnSettings.MobTier targetTier = null;
            for (MobSpawnSettings.MobTier tier : tiers) {
                if (tier.getMinViewers() == minViewers) {
                    targetTier = tier;
                    break;
                }
            }

            if (targetTier == null) {
                // Create new tier
                List<String> mobList = new ArrayList<>();
                targetTier = new MobSpawnSettings.MobTier(minViewers, mobList);
                tiers.add(targetTier);
                sender.sendMessage(Component.text("Created new tier with min viewers: " + minViewers).color(NamedTextColor.AQUA));
            }

            // Add mob to target tier
            targetTier.getMobs().add(mobType);
            SettingsLoader.saveSettings();

            // Reload mob spawn state
            SettingsLoader.loadMobTiers();

            if (oldTier != null) {
                sender.sendMessage(Component.text("Moved " + mobType + " from tier (" + oldTier.getMinViewers() + " viewers) to tier (" + minViewers + " viewers)").color(NamedTextColor.GREEN));
            } else {
                sender.sendMessage(Component.text("Added " + mobType + " to tier (min viewers: " + minViewers + ")").color(NamedTextColor.GREEN));
            }

        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid number!").color(NamedTextColor.RED));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> suggestions = new ArrayList<>();
            String input = args[1].toUpperCase();
            
            // Add vanilla EntityTypes
            for (EntityType type : EntityType.values()) {
                if (type.isAlive()) {
                    String name = type.name();
                    if (name.toUpperCase().startsWith(input)) {
                        suggestions.add(name);
                    }
                }
            }
            
            // Add custom mobs
            for (String customMobKey : CustomMobRegistry.getAllCustomMobs().keySet()) {
                if (customMobKey.toUpperCase().startsWith(input)) {
                    suggestions.add(customMobKey);
                }
            }
            
            return suggestions;
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Add mob to a tier";
    }
}

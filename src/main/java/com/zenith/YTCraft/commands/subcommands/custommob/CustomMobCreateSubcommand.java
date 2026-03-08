package com.zenith.YTCraft.commands.subcommands.custommob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.CustomMobSettings;
import com.zenith.YTCraft.custommobs.CustomMobRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CustomMobCreateSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(Component.text("Usage: /ytcraft custommob create <key> <mob_name> <entity_type> <player_username>").color(NamedTextColor.RED));
            return true;
        }

        String mobKey = args[0].toUpperCase();
        String mobName = args[1];
        String entityTypeStr = args[2].toUpperCase();
        String playerUsername = args[3];

        // Check if mob already exists
        if (CustomMobRegistry.isCustomMob(mobKey)) {
            sender.sendMessage(Component.text("Custom mob already exists: " + mobKey).color(NamedTextColor.RED));
            return true;
        }

        // Validate entity type
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(entityTypeStr);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text("Invalid entity type: " + entityTypeStr).color(NamedTextColor.RED));
            sender.sendMessage(Component.text("Use tab completion to see valid types").color(NamedTextColor.GRAY));
            return true;
        }

        // Create new config entry
        CustomMobSettings.CustomMobConfig config = new CustomMobSettings.CustomMobConfig();
        config.setMobName(mobName);
        config.setEntityType(entityTypeStr);
        config.setPlayerUsername(playerUsername);
        config.setAliases(new String[0]); // No aliases initially

        // Add to settings
        CustomMobSettings customMobSettings = SettingsLoader.getSettings().getCustomMobSettings();
        if (customMobSettings.getMobs() == null) {
            customMobSettings.setMobs(new java.util.HashMap<>());
        }
        customMobSettings.getMobs().put(mobKey, config);

        // Save settings
        SettingsLoader.saveSettings();

        // Reload custom mobs
        SettingsLoader.loadCustomMobs();

        sender.sendMessage(Component.text("✓ Created custom mob '").color(NamedTextColor.GREEN)
                .append(Component.text(mobKey).color(NamedTextColor.YELLOW))
                .append(Component.text("'").color(NamedTextColor.GREEN)));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 3) {
            // Suggest entity types
            String input = args[2].toUpperCase();
            return Arrays.stream(EntityType.values())
                    .filter(EntityType::isAlive)
                    .map(EntityType::name)
                    .filter(name -> name.startsWith(input))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Create a new custom mob";
    }
}

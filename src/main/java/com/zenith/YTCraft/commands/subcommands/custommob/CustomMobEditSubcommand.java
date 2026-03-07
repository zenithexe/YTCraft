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
import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.custommobs.CustomMobRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CustomMobEditSubcommand implements Subcommand {

    private static final List<String> PROPERTIES = Arrays.asList("name", "entity", "username");

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Component.text("Usage: /ytcraft custommob edit <mob_key> <property> <value>").color(NamedTextColor.RED));
            sender.sendMessage(Component.text("Properties: name, entity, username").color(NamedTextColor.GRAY));
            return true;
        }

        String mobKey = args[0].toLowerCase();
        String property = args[1].toLowerCase();
        String value = args[2];

        CustomMob mob = CustomMobRegistry.getCustomMob(mobKey);
        if (mob == null) {
            sender.sendMessage(Component.text("Custom mob not found: " + mobKey).color(NamedTextColor.RED));
            return true;
        }

        // Get the actual mob key
        String actualKey = mob.getMobKey();

        // Get the config
        CustomMobSettings customMobSettings = SettingsLoader.getSettings().getCustomMobSettings();
        CustomMobSettings.CustomMobConfig config = customMobSettings.getMobs().get(actualKey);

        if (config == null) {
            sender.sendMessage(Component.text("Error: Config not found for mob").color(NamedTextColor.RED));
            return true;
        }

        // Update the property
        switch (property) {
            case "name":
                config.setMobName(value);
                sender.sendMessage(Component.text("✓ Updated mob name to: ").color(NamedTextColor.GREEN)
                        .append(Component.text(value).color(NamedTextColor.YELLOW)));
                break;

            case "entity":
                try {
                    EntityType.valueOf(value.toUpperCase());
                    config.setEntityType(value.toUpperCase());
                    sender.sendMessage(Component.text("✓ Updated entity type to: ").color(NamedTextColor.GREEN)
                            .append(Component.text(value.toUpperCase()).color(NamedTextColor.YELLOW)));
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Component.text("Invalid entity type: " + value).color(NamedTextColor.RED));
                    return true;
                }
                break;

            case "username":
                config.setPlayerUsername(value);
                sender.sendMessage(Component.text("✓ Updated player username to: ").color(NamedTextColor.GREEN)
                        .append(Component.text(value).color(NamedTextColor.YELLOW)));
                break;

            default:
                sender.sendMessage(Component.text("Unknown property: " + property).color(NamedTextColor.RED));
                sender.sendMessage(Component.text("Valid properties: name, entity, username").color(NamedTextColor.GRAY));
                return true;
        }

        // Save settings
        SettingsLoader.saveSettings();

        // Reload custom mobs
        SettingsLoader.loadCustomMobs();

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            // Suggest mob keys
            List<String> suggestions = new ArrayList<>();
            for (CustomMob mob : CustomMobRegistry.getAllCustomMobs().values()) {
                String key = mob.getMobKey();
                if (key.startsWith(args[0].toLowerCase()) && !suggestions.contains(key)) {
                    suggestions.add(key);
                }
            }
            return suggestions;
        } else if (args.length == 2) {
            // Suggest properties
            return PROPERTIES.stream()
                    .filter(prop -> prop.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 3 && args[1].equalsIgnoreCase("entity")) {
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
        return "edit";
    }

    @Override
    public String getDescription() {
        return "Edit a custom mob's properties";
    }
}

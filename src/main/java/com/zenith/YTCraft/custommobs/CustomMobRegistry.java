package com.zenith.YTCraft.custommobs;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.CustomMobSettings;
import com.zenith.YTCraft.custommobs.CustomMob.SkinSource;

/**
 * Registry for custom mobs loaded from configuration
 * Provides lookup by name and validation
 */
public class CustomMobRegistry {
    
    private static final Map<String, CustomMob> customMobs = new HashMap<>();

    /**
     * Load custom mobs from settings
     */
    public static void loadCustomMobs() {
        customMobs.clear();
        
        CustomMobSettings customMobSettings = SettingsLoader.getSettings().getCustomMobSettings();
        
        if (customMobSettings == null) {
            Bukkit.getLogger().info("No custom mobs configured");
            return;
        }

        if (!customMobSettings.isEnabled()) {
            Bukkit.getLogger().info("Custom mobs are disabled");
            return;
        }

        if (customMobSettings.getMobs() == null || customMobSettings.getMobs().isEmpty()) {
            Bukkit.getLogger().info("No custom mobs configured");
            return;
        }

        for (Map.Entry<String, CustomMobSettings.CustomMobConfig> entry : customMobSettings.getMobs().entrySet()) {
            String mobName = entry.getKey().toLowerCase();
            CustomMobSettings.CustomMobConfig config = entry.getValue();

            try {
                EntityType entityType = EntityType.valueOf(config.getEntityType().toUpperCase());
                SkinSource skinSource = SkinSource.valueOf(config.getSkinSource().toUpperCase());
                String skinValue = config.getSkinValue();

                CustomMob customMob = new CustomMob(mobName, entityType, skinSource, skinValue);
                customMobs.put(mobName, customMob);
                
                Bukkit.getLogger().info(String.format(
                    "Loaded custom mob: %s -> %s (skin: %s from %s)", 
                    mobName, entityType, skinValue, skinSource
                ));
                
            } catch (Exception e) {
                Bukkit.getLogger().warning(String.format(
                    "Failed to load custom mob '%s': %s", 
                    mobName, e.getMessage()
                ));
            }
        }
    }

    /**
     * Check if a name is a custom mob
     */
    public static boolean isCustomMob(String name) {
        return customMobs.containsKey(name.toLowerCase());
    }

    /**
     * Get custom mob by name
     */
    public static CustomMob getCustomMob(String name) {
        return customMobs.get(name.toLowerCase());
    }

    /**
     * Get all registered custom mobs
     */
    public static Map<String, CustomMob> getAllCustomMobs() {
        return new HashMap<>(customMobs);
    }
}

package com.zenith.YTCraft.config.processors;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import com.zenith.YTCraft.config.types.CustomMobSettings;
import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.custommobs.CustomMob.SkinSource;
import com.zenith.YTCraft.custommobs.CustomMobRegistry;


/**
 * Processes CustomMobSettings into CustomMobRegistry
 */
public class CustomMobSettingsProcessor {

    /**
     * Process custom mob settings into CustomMobRegistry
     */
    public static void process(CustomMobSettings settings) {
        Map<String, CustomMob> customMobs = buildCustomMobsMap(settings);
        CustomMobRegistry.loadCustomMobs(customMobs);
    }

    /**
     * Build custom mobs map from settings
     */
    private static Map<String, CustomMob> buildCustomMobsMap(CustomMobSettings settings) {
        Map<String, CustomMob> customMobs = new HashMap<>();

        if (settings == null) {
            Bukkit.getLogger().info("No custom mobs configured");
            return customMobs;
        }

        if (!settings.isEnabled()) {
            Bukkit.getLogger().info("Custom mobs are disabled");
            return customMobs;
        }

        if (settings.getMobs() == null || settings.getMobs().isEmpty()) {
            Bukkit.getLogger().info("No custom mobs configured");
            return customMobs;
        }

        for (Map.Entry<String, CustomMobSettings.CustomMobConfig> entry : settings.getMobs().entrySet()) {

            String mobKey = entry.getKey().toLowerCase();

            CustomMobSettings.CustomMobConfig customMobConfig = entry.getValue();

            try {
                String mobName = customMobConfig.getMobName();

                EntityType entityType = EntityType.valueOf(customMobConfig.getEntity().toUpperCase());
                SkinSource skinSource = SkinSource.valueOf(customMobConfig.getSkinSrc().toUpperCase());

                String skinValue = customMobConfig.getSkinValue();
                

                CustomMob customMob = new CustomMob(mobKey, mobName, entityType, skinSource, skinValue);
                customMobs.put(mobKey, customMob);

                Bukkit.getLogger().info(String.format("Loaded custom mob: %s (%s) -> %s (skin: %s from %s)", mobKey, mobName, entityType, skinValue, skinSource));

            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning(String.format("Failed to load custom mob '%s': Invalid entity type or skin source - %s", mobKey, e.getMessage()));
            } catch (Exception e) {
                Bukkit.getLogger().warning(String.format("Failed to load custom mob '%s': %s", mobKey, e.getMessage()));
            }
        }

        return customMobs;
    }
}

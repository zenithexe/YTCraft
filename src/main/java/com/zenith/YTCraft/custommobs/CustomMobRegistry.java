package com.zenith.YTCraft.custommobs;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for custom mobs loaded from configuration
 * Provides lookup by name and validation
 */
public class CustomMobRegistry {
    
    private static final Map<String, CustomMob> customMobs = new HashMap<>();

    /**
     * Load custom mobs from processed map
     * Called by CustomMobSettingsProcessor
     */
    public static void loadCustomMobs(Map<String, CustomMob> mobs) {
        customMobs.clear();
        customMobs.putAll(mobs);
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

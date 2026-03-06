package com.zenith.YTCraft.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.ItemGiveSettings;
import com.zenith.YTCraft.config.types.ItemGiveSettings.ItemAlias;

public class ItemGiveState {

    private static final Set<String> bannedItems = new HashSet<>();
    private static final Set<String> allowedItems = new HashSet<>();
    private static final Map<String, ItemAlias> validatedAliases = new HashMap<>();

    public static Set<String> getBannedItems() {
        return bannedItems;
    }

    public static Set<String> getAllowedItems() {
        return allowedItems;
    }

    public static Map<String, ItemAlias> getValidatedAliases() {
        return validatedAliases;
    }

    public static void setBannedItems(Set<String> items) {
        bannedItems.clear();
        bannedItems.addAll(items);
    }

    public static void setAllowedItems(Set<String> items) {
        allowedItems.clear();
        allowedItems.addAll(items);
    }

    public static void setValidatedAliases(Map<String, ItemAlias> aliases) {
        validatedAliases.clear();
        validatedAliases.putAll(aliases);
    }

    /**
     * Check if an item can be given based on cached settings
     */
    public static boolean canGiveItem(String itemName) {
        ItemGiveSettings settings = SettingsLoader.getSettings().getItemGiveSettings();

        if (!settings.isEnabled()) {
            return false;
        }

        itemName = itemName.toUpperCase();

        // Check banned list (O(1) lookup)
        if (bannedItems.contains(itemName)) {
            return false;
        }

        // If mode is ALLOWED_ONLY, check allowed list
        if (settings.getMode() == ItemGiveSettings.ModeSettings.ALLOWED_ONLY) {
            return allowedItems.contains(itemName);
        }

        // Mode is ALL - allow everything except banned
        return true;
    }

    /**
     * Check if an alias exists in validated cache
     */
    public static boolean isAlias(String aliasName) {
        return validatedAliases.containsKey(aliasName.toLowerCase());
    }

    /**
     * Get a validated alias by name
     */
    public static ItemAlias getAlias(String aliasName) {
        return validatedAliases.get(aliasName.toLowerCase());
    }

    public static void clearAll() {
        bannedItems.clear();
        allowedItems.clear();
        validatedAliases.clear();
    }

}

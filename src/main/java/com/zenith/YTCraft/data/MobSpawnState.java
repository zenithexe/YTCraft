package com.zenith.YTCraft.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;
import com.zenith.YTCraft.types.AuthorMob;

public class MobSpawnState {

    private static final Map<String, Integer> EntityTypeToMinViewers = new HashMap<>();
    private static final Map<String, AuthorMob> ChannelIdToAuthorMob = new HashMap<>();

    public static Map<String, Integer> getEntityTypeToMinViewers() {
        return EntityTypeToMinViewers;
    }

    public static Map<String, AuthorMob> getChannelIdToAuthorMob() {
        return ChannelIdToAuthorMob;
    }

    public static void setEntityTypeToMinViewers(Map<String, Integer> map) {
        EntityTypeToMinViewers.clear();
        EntityTypeToMinViewers.putAll(map);
    }

    public static boolean isMobSpawnable(EntityType entityType) {
        int viewers = PluginState.getViewers();
        MobSpawnSettings mobSpawnSettings = SettingsLoader.getSettings().getMobSpawnSettings();

        // Check if mob spawn is enabled
        if (!mobSpawnSettings.isEnabled()) {
            return false;
        }

        // Check if mob is banned
        if (mobSpawnSettings.getBannedMobs() != null && mobSpawnSettings.getBannedMobs().contains(entityType.toString())) {
            return false;
        }

        //If Set to 'All'
        if (mobSpawnSettings.getMode() == MobSpawnSettings.ModeSettings.ALL) {
            return true;
        }

        // Check low viewer mode
        MobSpawnSettings.LowViewerMode lowViewerMode = mobSpawnSettings.getLowViewerMode();
        if (lowViewerMode != null && lowViewerMode.isEnabled() && viewers < lowViewerMode.getViewersThreshold()) {
            return isSpawnableInLowViewerMode(entityType, lowViewerMode);
        }

        // Normal mode: check tier requirements
        Integer minViewers = EntityTypeToMinViewers.get(entityType.toString());

        if (minViewers == null) {
            //Mob not in tier
            //True is mode set to 'tier_plus_unlisted'
            return mobSpawnSettings.getMode() == MobSpawnSettings.ModeSettings.TIER_PLUS_UNLISTED;
        }

        return viewers >= minViewers;
    }

    /**
     * Check if mob is spawnable in low viewer mode
     */
    private static boolean isSpawnableInLowViewerMode(EntityType entityType, MobSpawnSettings.LowViewerMode mode) {
        String entityTypeStr = entityType.toString();

        switch (mode.getMode()) {
            case ALLOW_ONLY:
                // Only mobs in allow list
                return mode.getAllow() != null && mode.getAllow().contains(entityTypeStr);

            case ALL:
                // All mobs except those in exclude list
                return mode.getExclude() == null || !mode.getExclude().contains(entityTypeStr);

            case TIER:
                // Check if in Tier-List and Not in 'Exclude'
                return EntityTypeToMinViewers.containsKey(entityTypeStr) && (mode.getExclude() == null || !mode.getExclude().contains(entityTypeStr));

            default:
                return false;
        }
    }

    public static void clearAll() {
        EntityTypeToMinViewers.clear();
        ChannelIdToAuthorMob.clear();
    }

}

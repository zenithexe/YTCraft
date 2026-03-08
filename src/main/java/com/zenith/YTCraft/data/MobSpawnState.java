package com.zenith.YTCraft.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;
import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.types.AuthorMob;

public class MobSpawnState {

    private static final Map<String, Integer> MobToMinViewers = new HashMap<>();
    private static final Map<String, AuthorMob> ChannelIdToAuthorMob = new HashMap<>();

    public static Map<String, Integer> getMobToMinViewers() {
        return MobToMinViewers;
    }

    public static Map<String, AuthorMob> getChannelIdToAuthorMob() {
        return ChannelIdToAuthorMob;
    }

    public static void setMobToMinViewers(Map<String, Integer> map) {
        MobToMinViewers.clear();
        MobToMinViewers.putAll(map);
    }

    public static boolean isMobSpawnable(String mobKey) {
        int viewers = PluginState.getViewers();
        MobSpawnSettings mobSpawnSettings = SettingsLoader.getSettings().getMobSpawnSettings();

        // Normalize mob key to uppercase for consistency
        String normalizedKey = mobKey.toUpperCase();

        // Check if mob spawn is enabled
        if (!mobSpawnSettings.isEnabled()) {
            Bukkit.getLogger().info(String.format("Cannot Spawn Mob :: Mob Spawning is Disabled."));
            return false;
        }

        // Check if mob is banned
        if (mobSpawnSettings.getBannedMobs() != null && mobSpawnSettings.getBannedMobs().contains(normalizedKey)) {
            Bukkit.getLogger().info(String.format(String.format("Cannot Spawn Mob :: %s is banned.", mobKey)));
            return false;
        }

        // If Set to 'All'
        if (mobSpawnSettings.getMode() == MobSpawnSettings.ModeSettings.ALL) {
            return true;
        }

        // Check low viewer mode
        MobSpawnSettings.LowViewerMode lowViewerMode = mobSpawnSettings.getLowViewerMode();
        if (lowViewerMode != null && lowViewerMode.isEnabled() && viewers < lowViewerMode.getViewersThreshold()) {
            return isSpawnableInLowViewerMode(normalizedKey, lowViewerMode);
        }

        // Normal mode: check tier requirements
        Integer minViewers = MobToMinViewers.get(normalizedKey);

        if (minViewers == null) {
            if (mobSpawnSettings.getMode() == MobSpawnSettings.ModeSettings.TIER_PLUS_UNLISTED) {
                return true;
            } else {
                Bukkit.getLogger().info(String.format(String.format("Cannot Spawn Mob :: %s is not mentioned in tier-list.", mobKey)));
                return false;
            }
        }

        if (viewers < minViewers) {
            Bukkit.getLogger().info(String.format(String.format("Cannot Spawn Mob :: Low Viewer for %s.", mobKey)));
            return false;
        }

        return true;
    }

    /**
     * Check if custom mob (by key) is spawnable in low viewer mode
     */
    private static boolean isSpawnableInLowViewerMode(String mobKey, MobSpawnSettings.LowViewerMode mode) {
        switch (mode.getMode()) {
            case ALLOW_ONLY:
                // Only mobs in allow list
                return mode.getAllow() != null && mode.getAllow().contains(mobKey);

            case ALL:
                // All mobs except those in exclude list
                return mode.getExclude() == null || !mode.getExclude().contains(mobKey);

            case TIER:
                // Check if in Tier-List and Not in 'Exclude'
                return MobToMinViewers.containsKey(mobKey) && (mode.getExclude() == null || !mode.getExclude().contains(mobKey));

            default:
                return false;
        }
    }

    /**
     * Add mob to tracking map
     */
    public static void addAuthorMob(LivingEntity creature, String author, String channelId) {
        AuthorMob authorMob = new AuthorMob(channelId, author, creature);
        MobSpawnState.getChannelIdToAuthorMob().put(channelId, authorMob);
    }

    /**
     * Add custom mob to tracking map
     */
    public static void addAuthorMob(LivingEntity creature, String author, String channelId, boolean isCustomMob, CustomMob customMob) {
        AuthorMob authorMob = new AuthorMob(channelId, author, creature, isCustomMob, customMob);
        MobSpawnState.getChannelIdToAuthorMob().put(channelId, authorMob);
    }

    public static void clearAll() {
        MobToMinViewers.clear();
        ChannelIdToAuthorMob.clear();
    }

}

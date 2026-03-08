package com.zenith.YTCraft.config.processors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zenith.YTCraft.config.types.MobSpawnSettings;
import com.zenith.YTCraft.data.MobSpawnState;

/**
 * Processes MobSpawnSettings into runtime state
 */
public class MobSpawnSettingsProcessor {

    /**
     * Process mob spawn settings into MobSpawnState
     */
    public static void process(MobSpawnSettings settings) {
        MobSpawnState.setMobToMinViewers(buildMobToMinViewersMap(settings));
    }

    /**
     * Build mob to min viewers mapping from tiers
     */
    private static Map<String, Integer> buildMobToMinViewersMap(MobSpawnSettings settings) {
        Map<String, Integer> mobToMinViewers = new HashMap<>();

        if (settings.getMobTiers() == null) {
            return mobToMinViewers;
        }

        for (MobSpawnSettings.MobTier tier : settings.getMobTiers()) {
            int minViewers = tier.getMinViewers();
            List<String> mobs = tier.getMobs();

            if (mobs != null) {
                for (String mob : mobs) {
                    mobToMinViewers.put(mob.toUpperCase(), minViewers);
                }
            }
        }

        return mobToMinViewers;
    }
}

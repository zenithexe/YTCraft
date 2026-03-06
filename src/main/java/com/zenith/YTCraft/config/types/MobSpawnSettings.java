package com.zenith.YTCraft.config.types;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MobSpawnSettings {

    private boolean enabled;
    private boolean kill_all_on_death;
    private boolean allow_viewer_mob_kill;
    private int max_spawns;
    private ModeSettings mode;
    private List<String> banned_mobs;
    private LowViewerMode low_viewer_mode;
    private List<MobTier> mob_tiers;

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isKillAllOnDeath() {
        return kill_all_on_death;
    }

    public boolean isAllowViewerMobKill() {
        return allow_viewer_mob_kill;
    }

    public int getMaxSpawns() {
        return max_spawns;
    }

    public ModeSettings getMode() {
        return mode;
    }

    public List<String> getBannedMobs() {
        return banned_mobs;
    }

    public LowViewerMode getLowViewerMode() {
        return low_viewer_mode;
    }

    public List<MobTier> getMobTiers() {
        return mob_tiers;
    }

    // Setters
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setKillAllOnDeath(boolean killAllOnDeath) {
        this.kill_all_on_death = killAllOnDeath;
    }

    public void setAllowViewerMobKill(boolean allowViewerMobKill) {
        this.allow_viewer_mob_kill = allowViewerMobKill;
    }

    public void setMaxSpawns(int maxSpawns) {
        this.max_spawns = maxSpawns;
    }

    public void setMode(ModeSettings mode) {
        this.mode = mode;
    }

    public void setBannedMobs(List<String> bannedMobs) {
        this.banned_mobs = bannedMobs;
    }

    public static class LowViewerMode {

        private boolean enabled;
        private int viewers_threshold;
        private LowViewModeSettings mode;
        private List<String> allow;
        private List<String> exclude;

        public boolean isEnabled() {
            return enabled;
        }

        public int getViewersThreshold() {
            return viewers_threshold;
        }

        public LowViewModeSettings getMode() {
            return mode;
        }

        public List<String> getAllow() {
            return allow;
        }

        public List<String> getExclude() {
            return exclude;
        }

        // Setters
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setViewersThreshold(int viewersThreshold) {
            this.viewers_threshold = viewersThreshold;
        }

        public void setMode(LowViewModeSettings mode) {
            this.mode = mode;
        }

        public void setAllow(List<String> allow) {
            this.allow = allow;
        }

        public void setExclude(List<String> exclude) {
            this.exclude = exclude;
        }

        public static enum LowViewModeSettings {
            @SerializedName("all")
            ALL,
            @SerializedName("tier")
            TIER,
            @SerializedName("allow_only")
            ALLOW_ONLY
        }

    }

    public static class MobTier {

        private int min_viewers;
        private List<String> mobs;

        public MobTier(int minViewers, List<String> mobs) {
            this.min_viewers = minViewers;
            this.mobs = mobs;
        }

        public int getMinViewers() {
            return min_viewers;
        }

        public List<String> getMobs() {
            return mobs;
        }
    }

    public static enum ModeSettings {
        @SerializedName("all")
        ALL,
        @SerializedName("tier_only")
        TIER_ONLY,
        @SerializedName("tier_plus_unlisted")
        TIER_PLUS_UNLISTED
    }

}

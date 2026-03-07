package com.zenith.YTCraft.config.types;

import java.util.Map;

public class CustomMobSettings {

    private boolean enabled;
    private Map<String, CustomMobConfig> mobs;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, CustomMobConfig> getMobs() {
        return mobs;
    }

    public void setMobs(Map<String, CustomMobConfig> mobs) {
        this.mobs = mobs;
    }

    public static class CustomMobConfig {
        
        private String entityType;
        private String skinSource;
        private String skinValue;

        public String getEntityType() {
            return entityType;
        }

        public void setEntityType(String entityType) {
            this.entityType = entityType;
        }

        public String getSkinSource() {
            return skinSource;
        }

        public void setSkinSource(String skinSource) {
            this.skinSource = skinSource;
        }

        public String getSkinValue() {
            return skinValue;
        }

        public void setSkinValue(String skinValue) {
            this.skinValue = skinValue;
        }
    }
}

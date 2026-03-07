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
        
        private String mob_name;
        private String entity;
        private String skin_src;
        private String skin_value;

        public String getMobName() {
            return mob_name;
        }

        public void setMobName(String mobName) {
            this.mob_name = mobName;
        }

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public String getSkinSrc() {
            return skin_src;
        }

        public void setSkinSrc(String skinSrc) {
            this.skin_src = skinSrc;
        }

        public String getSkinValue() {
            return skin_value;
        }

        public void setSkinValue(String skinValue) {
            this.skin_value = skinValue;
        }
    }
}

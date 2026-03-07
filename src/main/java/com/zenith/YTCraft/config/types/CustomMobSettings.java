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
        private String entity_type;
        private String player_username;
        private String[] aliases;

        public String getMobName() {
            return mob_name;
        }

        public void setMobName(String mobName) {
            this.mob_name = mobName;
        }

        public String getEntityType() {
            return entity_type;
        }

        public void setEntityType(String entityType) {
            this.entity_type = entityType;
        }

        public String getPlayerUsername() {
            return player_username;
        }

        public void setPlayerUsername(String playerUsername) {
            this.player_username = playerUsername;
        }

        public String[] getAliases() {
            return aliases;
        }

        public void setAliases(String[] aliases) {
            this.aliases = aliases;
        }
    }
}

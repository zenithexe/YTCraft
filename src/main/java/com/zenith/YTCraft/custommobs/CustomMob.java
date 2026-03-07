package com.zenith.YTCraft.custommobs;

import org.bukkit.entity.EntityType;

/**
 * Represents a custom mob configuration
 * Maps a custom name to an entity type with player username for skin
 */
public class CustomMob {

    private final String mobKey;
    private final String mobName;
    private final EntityType entityType;
    private final String playerUsername;

    public CustomMob(String mobKey, String mobName, EntityType entityType, String playerUsername) {
        this.mobKey = mobKey;
        this.mobName = mobName;
        this.entityType = entityType;
        this.playerUsername = playerUsername;
    }

    public String getMobKey() {
        return mobKey;
    }

    public String getMobName() {
        return mobName;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }
}

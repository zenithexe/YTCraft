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
    private final String[] aliases;

    public CustomMob(String mobKey, String mobName, EntityType entityType, String playerUsername, String[] aliases) {
        this.mobKey = mobKey;
        this.mobName = mobName;
        this.entityType = entityType;
        this.playerUsername = playerUsername;
        this.aliases = aliases != null ? aliases : new String[0];
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

    public String[] getAliases() {
        return aliases;
    }

    public boolean hasAliases() {
        return aliases != null && aliases.length > 0;
    }
}

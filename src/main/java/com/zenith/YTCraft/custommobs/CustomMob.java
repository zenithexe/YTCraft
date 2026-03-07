package com.zenith.YTCraft.custommobs;

import org.bukkit.entity.EntityType;

/**
 * Represents a custom mob configuration Maps a custom name to an entity type
 * with skin data
 */
public class CustomMob {

    private final String mobKey;
    private final String mobName;
    private final EntityType entityType;
    private final SkinSource skinSource;
    private final String skinValue;

    public CustomMob(String id, String mobName, EntityType entityType, SkinSource skinSource, String skinValue) {
        this.mobKey = id;
        this.mobName = mobName;
        this.entityType = entityType;
        this.skinSource = skinSource;
        this.skinValue = skinValue;
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

    public SkinSource getSkinSource() {
        return skinSource;
    }

    public String getSkinValue() {
        return skinValue;
    }

    public enum SkinSource {
        PLAYER, // Fetch from Mojang using player name
        FILE, // Load from PNG file
        URL      // Load from direct URL
    }
}

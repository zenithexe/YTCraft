package com.zenith.YTCraft.custommobs;

import org.bukkit.entity.EntityType;

/**
 * Represents a custom mob configuration
 * Maps a custom name to an entity type with skin data
 */
public class CustomMob {
    private final String name;
    private final EntityType entityType;
    private final SkinSource skinSource;
    private final String skinValue;

    public CustomMob(String name, EntityType entityType, SkinSource skinSource, String skinValue) {
        this.name = name;
        this.entityType = entityType;
        this.skinSource = skinSource;
        this.skinValue = skinValue;
    }

    public String getName() {
        return name;
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
        PLAYER,  // Fetch from Mojang using player name
        FILE,    // Load from PNG file
        URL      // Load from direct URL
    }
}

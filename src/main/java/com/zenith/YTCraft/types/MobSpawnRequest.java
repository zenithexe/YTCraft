package com.zenith.YTCraft.types;

import org.bukkit.entity.EntityType;

import com.zenith.YTCraft.custommobs.CustomMob;

/**
 * Represents a mob spawn request from a viewer
 * Encapsulates all data needed to spawn and track a mob
 */
public class MobSpawnRequest {
    private final EntityType entityType;
    private final String author;
    private final String channelId;
    private final CustomMob customMob;

    public MobSpawnRequest(EntityType entityType, String author, String channelId) {
        this(entityType, author, channelId, null);
    }

    public MobSpawnRequest(EntityType entityType, String author, String channelId, CustomMob customMob) {
        this.entityType = entityType;
        this.author = author;
        this.channelId = channelId;
        this.customMob = customMob;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getAuthor() {
        return author;
    }

    public String getChannelId() {
        return channelId;
    }

    public CustomMob getCustomMob() {
        return customMob;
    }

    public boolean isCustomMob() {
        return customMob != null;
    }
}

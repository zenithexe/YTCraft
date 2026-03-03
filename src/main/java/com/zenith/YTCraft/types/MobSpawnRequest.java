package com.zenith.YTCraft.types;

import org.bukkit.entity.EntityType;

/**
 * Represents a mob spawn request from a viewer
 * Encapsulates all data needed to spawn and track a mob
 */
public class MobSpawnRequest {
    private final EntityType entityType;
    private final String author;
    private final String channelId;

    public MobSpawnRequest(EntityType entityType, String author, String channelId) {
        this.entityType = entityType;
        this.author = author;
        this.channelId = channelId;
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
}

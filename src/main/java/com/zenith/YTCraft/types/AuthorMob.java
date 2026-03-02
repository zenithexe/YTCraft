package com.zenith.YTCraft.types;

import org.bukkit.entity.LivingEntity;

public class AuthorMob {

    private final String channelId;
    private final String authorName;
    private final LivingEntity mob;

    public AuthorMob(String channelId, String authorName, LivingEntity entity) {
        this.channelId = channelId;
        this.authorName = authorName;
        this.mob = entity;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getAuthor() {
        return authorName;
    }

    public LivingEntity getMob() {
        return mob;
    }

    @Override
    public String toString() {
        return "AuthorMob{channelId='" + channelId + "', author='" + authorName + "', mobType='" + mob.getType() + "'}";
    }
}

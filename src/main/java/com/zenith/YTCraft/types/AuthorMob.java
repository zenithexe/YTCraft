package com.zenith.YTCraft.types;

import org.bukkit.entity.LivingEntity;

import com.zenith.YTCraft.custommobs.CustomMob;

public class AuthorMob {

    private final String channelId;
    private final String authorName;
    private final LivingEntity mob;
    private final boolean isCustomMob;
    private final CustomMob customMob;

    public AuthorMob(String channelId, String authorName, LivingEntity entity) {
        this(channelId, authorName, entity, false, null);
    }

    public AuthorMob(String channelId, String authorName, LivingEntity entity, boolean isCustomMob, CustomMob customMob) {
        this.channelId = channelId;
        this.authorName = authorName;
        this.mob = entity;
        this.isCustomMob = isCustomMob;
        this.customMob = customMob;
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

    public boolean isCustomMob() {
        return isCustomMob;
    }

    public CustomMob getCustomMob() {
        return customMob;
    }

    public String getDisplayName() {
        return isCustomMob && customMob != null ? customMob.getMobName() : mob.getType().toString();
    }

    @Override
    public String toString() {
        return "AuthorMob{channelId='" + channelId + "', author='" + authorName + "', mobType='" + mob.getType()
                + "', isCustomMob=" + isCustomMob + ", mobName='" + customMob.getMobName() + "'}";
    }
}

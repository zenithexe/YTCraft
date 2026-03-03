package com.zenith.YTCraft.data;

import java.util.HashMap;
import java.util.Map;

import com.zenith.YTCraft.types.AuthorMob;

public class MobManager {

    private static final Map<String, Integer> entityTypeToMinViewers = new HashMap<>();
    private static final Map<String, AuthorMob> channelIdToAuthorMob = new HashMap<>();

    public static Map<String, Integer> getEntityTypeToMinViewers() {
        return entityTypeToMinViewers;
    }

    public static Map<String, AuthorMob> getChannelIdToAuthorMob() {
        return channelIdToAuthorMob;
    }

    public static boolean isMobSpawnable(String entityType) {

        int viewers = PluginState.getViewers();

        return (MobManager.getEntityTypeToMinViewers().containsKey(entityType.toUpperCase())
                && MobManager.getEntityTypeToMinViewers().get(entityType.toUpperCase()) <= viewers);
    }

    public static void clearAll() {
        entityTypeToMinViewers.clear();
        channelIdToAuthorMob.clear();
    }
}

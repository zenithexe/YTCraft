package com.zenith.YTCraft.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.LivingEntity;

public class MobManager {
    
    private static final Map<String, Integer> entityTypeToViewersMap = new HashMap<>();
    private static final Set<String> aliveAuthorMobChannelIds = new HashSet<>();
    private static final List<LivingEntity> aliveAuthorMobsList = new LinkedList<>();
    private static final Map<String, Map<String, String>> channelIdToAuthorMobMap = new HashMap<>();

    public static Map<String, Integer> getEntityTypeToViewersMap() {
        return entityTypeToViewersMap;
    }

    public static Set<String> getAliveAuthorMobChannelIds() {
        return aliveAuthorMobChannelIds;
    }

    public static List<LivingEntity> getAliveAuthorMobsList() {
        return aliveAuthorMobsList;
    }

    public static Map<String, Map<String, String>> getChannelIdToAuthorMobMap() {
        return channelIdToAuthorMobMap;
    }

    public static void clearAll() {
        entityTypeToViewersMap.clear();
        aliveAuthorMobChannelIds.clear();
        aliveAuthorMobsList.clear();
        channelIdToAuthorMobMap.clear();
    }
}

package com.zenith.YTCraft.data;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class SpawnQueue {
    
    private static final Queue<EntityType> mobQueue = new LinkedList<>();
    private static final Queue<String> channelIdQueue = new LinkedList<>();
    private static final Queue<String> authorQueue = new LinkedList<>();
    private static final Set<Material> blacklistedMaterials = new HashSet<>();

    public static Queue<EntityType> getMobQueue() {
        return mobQueue;
    }

    public static Queue<String> getChannelIdQueue() {
        return channelIdQueue;
    }

    public static Queue<String> getAuthorQueue() {
        return authorQueue;
    }

    public static Set<Material> getBlacklistedMaterials() {
        return blacklistedMaterials;
    }

    public static void clearQueues() {
        mobQueue.clear();
        channelIdQueue.clear();
        authorQueue.clear();
    }
}

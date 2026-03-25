package com.zenith.YTCraft.data;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.zenith.YTCraft.types.MobSpawnRequest;

/**
 * Spawn queue that enforces one pending spawn per viewer.
 * Uses LinkedHashMap keyed by channelId to prevent duplicate requests
 * and maintain FIFO order.
 */
public class SpawnQueue {

    private static final LinkedHashMap<String, MobSpawnRequest> spawnQueue = new LinkedHashMap<>();

    /**
     * Add a mob spawn request to the queue.
     * If the viewer already has a pending request, it is replaced.
     */
    public static void add(MobSpawnRequest request) {
        // Remove first so re-insertion goes to the tail (preserves correct order)
        spawnQueue.remove(request.getChannelId());
        spawnQueue.put(request.getChannelId(), request);
    }

    /**
     * Poll the next spawn request from the queue (FIFO order).
     * Returns null if queue is empty.
     */
    public static MobSpawnRequest poll() {
        Iterator<Map.Entry<String, MobSpawnRequest>> it = spawnQueue.entrySet().iterator();
        if (!it.hasNext()) return null;
        Map.Entry<String, MobSpawnRequest> entry = it.next();
        it.remove();
        return entry.getValue();
    }

    /**
     * Check if a viewer has a pending spawn request in the queue.
     */
    public static boolean containsChannel(String channelId) {
        return spawnQueue.containsKey(channelId);
    }

    /**
     * Remove a pending spawn request for a specific viewer.
     */
    public static void remove(String channelId) {
        spawnQueue.remove(channelId);
    }

    /**
     * Clear all pending spawn requests.
     */
    public static void clear() {
        spawnQueue.clear();
    }
}

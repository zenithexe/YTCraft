package com.zenith.YTCraft.data;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.zenith.YTCraft.types.MobSpawnRequest;

/**
 * Thread-safe spawn queue using a single queue of spawn requests Prevents
 * desynchronization and race conditions
 */
public class SpawnQueue {

    // ConcurrentLinkedQueue is thread-safe for add/poll operations
    private static final ConcurrentLinkedQueue<MobSpawnRequest> spawnQueue = new ConcurrentLinkedQueue<>();

    /**
     * Add a mob spawn request to the queue (thread-safe)
     */
    public static void add(MobSpawnRequest request) {
        spawnQueue.add(request);
    }

    /**
     * Poll the next spawn request from the queue (thread-safe) Returns null if
     * queue is empty
     */
    public static MobSpawnRequest poll() {
        return spawnQueue.poll();
    }

    /**
     * Clear all pending spawn requests
     */
    public static void clear() {
        spawnQueue.clear();
    }
}

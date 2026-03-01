package com.zenith.YTCraft.data;

import org.bukkit.entity.Player;

public class PluginState {
    
    private static Player streamer;
    private static int activeTimeMin = 5;
    private static int activeTimeSec = 0;
    private static int restTimeMin = 5;
    private static int restTimeSec = 0;
    private static boolean isActiveTimerMode = false;
    private static int mobSpawnRadius = 2;
    private static boolean enableItemSpawn = true;
    private static int subscriberCount = 0;

    public static Player getStreamer() {
        return streamer;
    }

    public static void setStreamer(Player player) {
        streamer = player;
    }

    public static void setActiveTime(int min, int sec) {
        activeTimeMin = min;
        activeTimeSec = sec;
    }

    public static void setRestTime(int min, int sec) {
        restTimeMin = min;
        restTimeSec = sec;
    }

    public static int[] getActiveTime() {
        return new int[]{activeTimeMin, activeTimeSec};
    }

    public static int[] getRestTime() {
        return new int[]{restTimeMin, restTimeSec};
    }

    public static boolean isActiveTimerMode() {
        return isActiveTimerMode;
    }

    public static void setActiveTimerMode(boolean active) {
        isActiveTimerMode = active;
    }

    public static int getMobSpawnRadius() {
        return mobSpawnRadius;
    }

    public static void setMobSpawnRadius(int radius) {
        mobSpawnRadius = radius;
    }

    public static boolean isItemSpawnEnabled() {
        return enableItemSpawn;
    }

    public static void setItemSpawnEnabled(boolean enabled) {
        enableItemSpawn = enabled;
    }

    public static int getSubscriberCount() {
        return subscriberCount;
    }

    public static void setSubscriberCount(int count) {
        subscriberCount = count;
    }
}

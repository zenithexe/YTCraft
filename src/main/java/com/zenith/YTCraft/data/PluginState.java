package com.zenith.YTCraft.data;

import org.bukkit.entity.Player;

public class PluginState {

    private static Player streamer;

    private static int activeTimeSec = 300;
    private static int restTimeSec = 300;

    private static boolean isChatControlEnabled = false;

    private static int mobSpawnRadius = 2;

    private static boolean enableItemSpawn = true;

    private static int subscriberCount = 0;
    private static int viewers = 0;
    private static int streamerDeathCount = 0;

    public static Player getStreamer() {
        return streamer;
    }

    public static void setStreamer(Player player) {
        streamer = player;
    }

    public static void setActiveTime(int seconds) {
        activeTimeSec = seconds;
    }

    public static void setRestTime(int seconds) {
        restTimeSec = seconds;
    }

    public static int getActiveTime() {
        return activeTimeSec;
    }

    public static int getRestTime() {
        return restTimeSec;
    }

    public static boolean isChatControlEnabled() {
        return isChatControlEnabled;
    }

    public static void setChatControl(boolean active) {
        isChatControlEnabled = active;
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

    public static int getViewers() {
        return viewers;
    }

    public static void setViewers(int v) {
        viewers = v;
    }

    public static int getSteamerDeathCount() {
        return streamerDeathCount;
    }

    public static void setStreamerDeathCount(int count) {
        streamerDeathCount = count;
    }

    public static void incrementStreamerDeathCount() {
        streamerDeathCount++;
    }

    public static void resetStreamerDeathCount() {
        streamerDeathCount = 0;
    }
}

package com.zenith.YTCraft.data;

import org.bukkit.entity.Player;

import com.zenith.YTCraft.types.TimerMode;

public class PluginState {

    private static Player streamer;

    private static boolean isChatControlEnabled = false;

    private static TimerMode timerMode = TimerMode.REST;

    private static int mobSpawnRadius = 2;

    private static int subscriberCount = 0;
    private static int viewers = 0;
    private static int streamerDeathCount = 0;

    public static Player getStreamer() {
        return streamer;
    }

    public static void setStreamer(Player player) {
        streamer = player;
    }

    public static boolean isChatControlEnabled() {
        return isChatControlEnabled;
    }

    public static void setChatControlEnabled(boolean active) {
        isChatControlEnabled = active;
    }

    public static TimerMode getTimerMode() {
        return timerMode;
    }

    public static void setTimerMode(TimerMode mode) {
        timerMode = mode;
    }

    public static boolean isTimerActiveMode() {
        return timerMode == TimerMode.ACTIVE;
    }

    public static boolean isTimerRestMode() {
        return timerMode == TimerMode.REST;
    }

    public static int getMobSpawnRadius() {
        return mobSpawnRadius;
    }

    public static void setMobSpawnRadius(int radius) {
        mobSpawnRadius = radius;
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

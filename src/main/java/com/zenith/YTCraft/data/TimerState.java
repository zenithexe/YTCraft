package com.zenith.YTCraft.data;

public class TimerState {

    private static int activeSeconds = 300;
    private static int restSeconds = 300;

    private static boolean isAlwaysActive = false;

    public static void setActiveTime(int seconds) {
        activeSeconds = seconds;
    }

    public static int getActiveTime() {
        return activeSeconds;
    }

    public static void setRestTime(int seconds) {
        restSeconds = seconds;
    }

    public static int getRestTime() {
        return restSeconds;
    }

    public static void setIsAlwaysActive(boolean value) {
        isAlwaysActive = value;
    }

    public static boolean getIsAlwaysActive() {
        return isAlwaysActive;
    }
}

package com.zenith.YTCraft.config.types;

public class TimerSettings {

    private boolean always_active;
    private int active_seconds;
    private int rest_seconds;

    public boolean isAlwaysActive() {
        return always_active;
    }

    public int getActiveSeconds() {
        return active_seconds;
    }

    public int getRestSeconds() {
        return rest_seconds;
    }

    // Setters
    public void setAlwaysActive(boolean alwaysActive) {
        this.always_active = alwaysActive;
    }

    public void setActiveSeconds(int activeSeconds) {
        this.active_seconds = activeSeconds;
    }

    public void setRestSeconds(int restSeconds) {
        this.rest_seconds = restSeconds;
    }
}

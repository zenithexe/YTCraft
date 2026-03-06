package com.zenith.YTCraft.config.types;

public class PluginSettings {

    private String video_id;
    private TimerSettings timer;
    private MobSpawnSettings mob_spawn;
    private ItemGiveSettings item_give;

    public String getVideoId() {
        return video_id;
    }

    public void setVideoId(String videoId) {
        this.video_id = videoId;
    }

    public TimerSettings getTimerSettings() {
        return timer;
    }

    public MobSpawnSettings getMobSpawnSettings() {
        return mob_spawn;
    }

    public ItemGiveSettings getItemGiveSettings() {
        return item_give;
    }

}


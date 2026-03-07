package com.zenith.YTCraft.config.types;

public class PluginSettings {

    private TimerSettings timer;
    private MobSpawnSettings mob_spawn;
    private ItemGiveSettings item_give;
    private CustomMobSettings custom_mobs;



    public TimerSettings getTimerSettings() {
        return timer;
    }

    public MobSpawnSettings getMobSpawnSettings() {
        return mob_spawn;
    }

    public ItemGiveSettings getItemGiveSettings() {
        return item_give;
    }

    public CustomMobSettings getCustomMobSettings() {
        return custom_mobs;
    }

    public void setCustomMobSettings(CustomMobSettings customMobSettings) {
        this.custom_mobs = customMobSettings;
    }

}


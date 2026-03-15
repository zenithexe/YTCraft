package com.zenith.YTCraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.zenith.YTCraft.commands.YTCraftCommand;
import com.zenith.YTCraft.config.ConfigManager;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.data.ItemGiveState;
import com.zenith.YTCraft.data.MobSpawnState;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.data.SpawnQueue;
import com.zenith.YTCraft.listeners.EntityCombustListener;
import com.zenith.YTCraft.listeners.EntityDeathListener;
import com.zenith.YTCraft.listeners.EntityExplodeListener;
import com.zenith.YTCraft.listeners.PlayerDeathListener;
import com.zenith.YTCraft.listeners.SessionPlayerListener;
import com.zenith.YTCraft.ui.BossBarUI;

public final class YTCraft extends JavaPlugin {

    private static YTCraft plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("YTCraft has been enabled.");

        //Config
        saveDefaultConfig(); //function from JavaPlugin
        ConfigManager.loadConfig();

        SettingsLoader.init(this);
        SettingsLoader.processSettings();

        //Events
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new EntityCombustListener(), this);
        getServer().getPluginManager().registerEvents(new SessionPlayerListener(), this);

        //Commands
        getCommand("YTCraft").setExecutor(new YTCraftCommand());
    }

    @Override
    public void onDisable() {

        // Cancel all running tasks
        Bukkit.getScheduler().cancelTasks(this);

        // Clear all static data
        MobSpawnState.clearAll();
        ItemGiveState.clearAll();
        SpawnQueue.clear();

        // Reset plugin state
        PluginState.setStreamer(null);
        PluginState.setChatControl(false);
        PluginState.setSubscriberCount(0);
        PluginState.resetStreamerDeathCount();

        //Remove Boss Bar
        BossBarUI.removeBossBar();

        getLogger().info("YTCraft has been disabled.");
    }

    public static YTCraft getPlugin() {
        return plugin;
    }

    /**
     * Reload plugin settings
     */
    public static void reloadSettings() {
        SettingsLoader.reload();
    }

}

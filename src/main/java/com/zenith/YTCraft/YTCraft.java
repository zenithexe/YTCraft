package com.zenith.YTCraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.zenith.YTCraft.commands.YTCraftCommand;
import com.zenith.YTCraft.config.Configuration;
import com.zenith.YTCraft.config.SaveConfiguration;
import com.zenith.YTCraft.data.MobManager;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.data.SpawnQueue;
import com.zenith.YTCraft.listeners.EntityDeathListener;
import com.zenith.YTCraft.listeners.EntityExplodeListener;
import com.zenith.YTCraft.listeners.PlayerDeathListener;
import com.zenith.YTCraft.ui.BossBarUI;

public final class YTCraft extends JavaPlugin {

    private static YTCraft plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("YTCraft has been enabled.");

        //Config
        saveDefaultConfig();
        Configuration.setupConfiguration();

        //Event
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

        //Commands
        getCommand("YTCraft").setExecutor(new YTCraftCommand());
    }

    @Override
    public void onDisable() {

        // Cancel all running tasks
        Bukkit.getScheduler().cancelTasks(this);

        // Clear all static data
        MobManager.clearAll();
        SpawnQueue.clear();

        // Reset plugin state
        PluginState.setStreamer(null);
        PluginState.setChatControl(false);
        PluginState.setSubscriberCount(0);
        PluginState.resetStreamerDeathCount();

        //Remove Boss Bar
        BossBarUI.removeBossBar();

        //Save Config
        SaveConfiguration.saveYTCraftConfig();

        getLogger().info("YTCraft has been disabled.");
    }

    public static YTCraft getPlugin() {
        return plugin;
    }

}

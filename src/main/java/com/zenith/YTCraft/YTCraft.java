package com.zenith.YTCraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.zenith.YTCraft.commands.YTCraftCommand;
import com.zenith.YTCraft.commands.YTSettingsCommand;
import com.zenith.YTCraft.config.Configuration;
import com.zenith.YTCraft.config.SaveConfiguration;
import com.zenith.YTCraft.data.MobManager;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.data.SpawnQueue;
import com.zenith.YTCraft.listeners.EntityDeathListener;
import com.zenith.YTCraft.listeners.EntityExplodeListener;

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

        //Commands
        getCommand("YTSettings").setExecutor(new YTSettingsCommand());
        getCommand("YTCraft").setExecutor(new YTCraftCommand());
    }

    @Override
    public void onDisable() {

        // Cancel all running tasks
        Bukkit.getScheduler().cancelTasks(this);

        // Clear all static data
        MobManager.clearAll();
        SpawnQueue.clearQueues();

        // Reset plugin state
        PluginState.setStreamer(null);
        PluginState.setActiveMode(false);
        PluginState.setSubscriberCount(0);

        //Save Config
        SaveConfiguration.saveYTCraftConfig();

        getLogger().info("YTCraft has been disabled.");
    }

    public static YTCraft getPlugin() {
        return plugin;
    }

}

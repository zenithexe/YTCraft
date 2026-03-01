package com.zenith.YTCraft;



import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import com.zenith.YTCraft.commands.YTCraftCommand;
import com.zenith.YTCraft.commands.YTSettingsCommand;
import com.zenith.YTCraft.config.Configuration;
import com.zenith.YTCraft.config.SaveConfiguration;
import com.zenith.YTCraft.listeners.EntityDeathListener;
import com.zenith.YTCraft.listeners.EntityExplodeListener;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;


public final class YTCraft extends JavaPlugin {

    private static YTCraft plugin;

    private BukkitAudiences adventure;

    public @NotNull BukkitAudiences adventure() {
        if(this.adventure==null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
    @Override
    public void onEnable() {
        plugin=this;

        getLogger().info("YTCraft has been enabled.");

        //Config
        saveDefaultConfig();
        Configuration.setupConfiguration();

        //AdventureLib
        this.adventure = BukkitAudiences.create(this);

        //Event
        getServer().getPluginManager().registerEvents(new EntityDeathListener(),this);
        getServer().getPluginManager().registerEvents(new EntityExplodeListener(), this);

        //Commands
        getCommand("YTSettings").setExecutor(new YTSettingsCommand());
        getCommand("YTCraft").setExecutor(new YTCraftCommand());
    }

    @Override
    public void onDisable() {

        SaveConfiguration.saveYTCraftConfig();

        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }

        getLogger().info("YTCraft has been disabled.");
    }


    public static YTCraft getPlugin(){
        return plugin;
    }

}

package mod.zenith.ytcraft;



import mod.zenith.ytcraft.Configuration.Configuration;
import mod.zenith.ytcraft.Configuration.SaveConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import mod.zenith.ytcraft.Commands.YTSettingsCommand;
import mod.zenith.ytcraft.Commands.YTCraftCommand;
import mod.zenith.ytcraft.EventListeners.EntityDeathListener;
import mod.zenith.ytcraft.EventListeners.EntityExplodeListener;
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

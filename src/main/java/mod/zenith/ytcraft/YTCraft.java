package mod.zenith.ytcraft;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import mod.zenith.ytcraft.Commands.VideoIdCommand;
import mod.zenith.ytcraft.Commands.YoutubeCommand;
import mod.zenith.ytcraft.EventListeners.EntityDeathListener;
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
        Configuration.configure(this);

        //AdventureLib
        this.adventure = BukkitAudiences.create(this);

        //Event
        getServer().getPluginManager().registerEvents(new EntityDeathListener(),this);

        //Commands
        getCommand("VideoId").setExecutor(new VideoIdCommand());
        getCommand("Youtube").setExecutor(new YoutubeCommand());
    }

    @Override
    public void onDisable() {


        saveConfig();

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

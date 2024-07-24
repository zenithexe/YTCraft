package mod.zenith.ytcraft;

import mod.zenith.ytcraft.Commands.VideoIdCommand;
import mod.zenith.ytcraft.Commands.YoutubeCommand;
import mod.zenith.ytcraft.EventListeners.EntityDeathListener;
import org.bukkit.plugin.java.JavaPlugin;


public final class YTCraft extends JavaPlugin {

    private static YTCraft plugin;
    @Override
    public void onEnable() {
        
        getLogger().info("YTCraft has been enabled.");
        plugin=this;
        saveDefaultConfig();
        Configuration.configure(this);

        getServer().getPluginManager().registerEvents(new EntityDeathListener(),this);
        getCommand("VideoId").setExecutor(new VideoIdCommand());
        getCommand("Youtube").setExecutor(new YoutubeCommand());
    }

    @Override
    public void onDisable() {

        getConfig().set("GLOBAL_CHAT_TIMESTAMP",ChatSpawn.getTimeStamp());
        saveConfig();

        getLogger().info("YTCraft has been disabled.");
    }


    public static YTCraft getPlugin(){
        return plugin;
    }

}

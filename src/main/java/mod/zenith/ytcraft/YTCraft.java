package mod.zenith.ytcraft;

import mod.zenith.ytcraft.YoutubeAPI;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class YTCraft extends JavaPlugin {

    @Override
    public void onEnable() {
        
        getLogger().info("YTCraft has been enabled.");
        saveDefaultConfig();
        
        Configuration.configure(this);

        Bukkit.getScheduler().runTaskTimer(this,this::run, 20L,20L*10);
    }

    @Override
    public void onDisable() {
        
        getLogger().info("YTCraft has been enabled.");
    }

    

    public void run(){
        YoutubeAPI.getChats();
    }
}

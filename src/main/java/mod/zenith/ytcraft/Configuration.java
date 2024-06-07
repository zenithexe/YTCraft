package mod.zenith.ytcraft;

import mod.zenith.ytcraft.YoutubeAPI;
import org.bukkit.Bukkit;

public class Configuration {

    public static void configure(YTCraft plugin){
        String API_KEY = plugin.getConfig().getString("API_KEY");
        String VIDEO_ID = plugin.getConfig().getString("VIDEO_ID");
        YoutubeAPI.setAPI(API_KEY,VIDEO_ID);
        Bukkit.getLogger().info("Keys Configuration is done.");
    }


    
}

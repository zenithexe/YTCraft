package mod.zenith.ytcraft;

import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import mod.zenith.ytcraft.YoutubeAPI;

public class Configuration {

    public static void configure(YTCraft plugin){
        String API_KEY = plugin.getConfig().getString("API_KEY");
        String VIDEO_ID = plugin.getConfig().getString("VIDEO_ID");

        List<Integer> activeTime = plugin.getConfig().getIntegerList("ACTIVE_TIME");
        List<Integer> restTime = plugin.getConfig().getIntegerList("REST_TIME");




        Set<String> configKey = plugin.getConfig().getConfigurationSection("HEAD").getKeys(true);

        for (String sub: configKey){
            String data = plugin.getConfig().getConfigurationSection("HEAD").getString(sub);
            Bukkit.getLogger().info(data);
        }


        Bukkit.getLogger().info(configKey.toString());
        YoutubeAPI.setAPI(API_KEY,VIDEO_ID);
        Bukkit.getLogger().info("Keys Configuration is done.");

        if(activeTime.get(0)!=null && activeTime.get(1)!=null){
            Data.setActiveTime(activeTime.get(0),activeTime.get(1));
        }
        Bukkit.getLogger().info("Active-Time Configuration is done.");

        if(restTime.get(0)!=null && restTime.get(1)!=null){
            Data.setRestTime(restTime.get(0),restTime.get(1));
        }
        Bukkit.getLogger().info("Rest-Time Configuration is done.");
    }

}

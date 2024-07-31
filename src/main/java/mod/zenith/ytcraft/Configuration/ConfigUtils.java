package mod.zenith.ytcraft.Configuration;

import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YTCraft;
import mod.zenith.ytcraft.YoutubeAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Set;

public class ConfigUtils {
    public static void apiConfig(){
        String API_KEY = YTCraft.getPlugin().getConfig().getString("API_KEY");
        String VIDEO_ID = YTCraft.getPlugin().getConfig().getString("VIDEO_ID");
        YoutubeAPI.setAPI(API_KEY,VIDEO_ID);
    }

    public static void timerConfig(){

        List<Integer> activeTime = YTCraft.getPlugin().getConfig().getIntegerList("ACTIVE_TIME");
        List<Integer> restTime = YTCraft.getPlugin().getConfig().getIntegerList("REST_TIME");


        Bukkit.getLogger().info("Timer Set.");

        if(activeTime.get(0)!=null && activeTime.get(1)!=null){
            Data.setActiveTime(activeTime.get(0),activeTime.get(1));
        }
        Bukkit.getLogger().info("Active-Time Configuration is done.");

        if(restTime.get(0)!=null && restTime.get(1)!=null){
            Data.setRestTime(restTime.get(0),restTime.get(1));
        }
        Bukkit.getLogger().info("Rest-Time Configuration is done.");
    }

    public static void authorMobConfig(){

        ConfigurationSection mobs = YTCraft.getPlugin().getConfig().getConfigurationSection("MOBS");
        Set<String> viewLevels = mobs.getKeys(false);

        for(String viewLevel: viewLevels){
            List<String> values = mobs.getStringList(viewLevel);
            for(String value: values){
                Data.Config_EntityType_To_NViewers_List.put(value, Integer.parseInt(viewLevel));
            }
        }
        Bukkit.getLogger().info("EntityType to Viewers Mapping Configuration done.");
    }
}

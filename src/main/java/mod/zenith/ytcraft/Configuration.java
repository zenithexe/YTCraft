package mod.zenith.ytcraft;

import org.bukkit.Bukkit;

import java.util.Set;

public class Configuration {

    public static void configure(YTCraft plugin){
        String API_KEY = plugin.getConfig().getString("API_KEY");
        String VIDEO_ID = plugin.getConfig().getString("VIDEO_ID");
        Set<String> configKey = plugin.getConfig().getConfigurationSection("HEAD").getKeys(true);

        for (String sub: configKey){
            String data = plugin.getConfig().getConfigurationSection("HEAD").getString(sub);
            Bukkit.getLogger().info(data);
        }


        Bukkit.getLogger().info(configKey.toString());
        YoutubeAPI.setAPI(API_KEY,VIDEO_ID);
        Bukkit.getLogger().info("Keys Configuration is done.");

    }

}

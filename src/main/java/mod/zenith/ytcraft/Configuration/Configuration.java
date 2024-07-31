package mod.zenith.ytcraft.Configuration;

import java.util.List;
import java.util.Set;

import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YTCraft;
import mod.zenith.ytcraft.YoutubeAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class Configuration {

    public static void setupConfiguration(){

        ConfigUtils.apiConfig();
        ConfigUtils.timerConfig();
        ConfigUtils.authorMobConfig();

    }
}

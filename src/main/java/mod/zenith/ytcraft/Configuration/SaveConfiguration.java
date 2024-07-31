package mod.zenith.ytcraft.Configuration;

import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YTCraft;
import org.bukkit.configuration.file.FileConfiguration;

public class SaveConfiguration {
    public static void saveYTCraftConfig(){
        FileConfiguration config = YTCraft.getPlugin().getConfig();

        config.set("ACTIVE_TIME",Data.getActiveTime());
        config.set("REST_TIME", Data.getResTime());
        YTCraft.getPlugin().saveConfig();
    }
}

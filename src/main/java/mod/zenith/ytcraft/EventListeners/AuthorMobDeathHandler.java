package mod.zenith.ytcraft.EventListeners;

import mod.zenith.ytcraft.AdventureLib.TabList;
import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YTCraft;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public class AuthorMobDeathHandler {

    public static void removeAuthorMob(Entity entity){
        if(entity.getPersistentDataContainer().has(new NamespacedKey(YTCraft.getPlugin(),"IsChatSpawned"), PersistentDataType.BOOLEAN))
        {
            String spawnedAuthor = entity.getPersistentDataContainer().get(new NamespacedKey(YTCraft.getPlugin(), "SpawnedAuthor"), PersistentDataType.STRING);
            String spawnedChannelId = entity.getPersistentDataContainer().get(new NamespacedKey(YTCraft.getPlugin(), "SpawnedChannelId"), PersistentDataType.STRING);

            Data.Alive_AuthorMobs.remove(spawnedChannelId);

            Data.ChannelId_To_AuthorMob_List.remove(spawnedChannelId);

            TabList.updateFooterTabList();
            TabList.updateHeaderTabList();
        }
    }
}

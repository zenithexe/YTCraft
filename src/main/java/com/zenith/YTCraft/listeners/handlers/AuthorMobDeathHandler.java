package com.zenith.YTCraft.listeners.handlers;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.data.MobManager;
import com.zenith.YTCraft.ui.TabList;

public class AuthorMobDeathHandler {

    public static void removeAuthorMob(Entity entity){
        if(entity.getPersistentDataContainer().has(new NamespacedKey(YTCraft.getPlugin(),"IsChatSpawned"), PersistentDataType.BOOLEAN))
        {
            String spawnedChannelId = entity.getPersistentDataContainer().get(new NamespacedKey(YTCraft.getPlugin(), "SpawnedChannelId"), PersistentDataType.STRING);

            MobManager.getChannelIdToAuthorMob().remove(spawnedChannelId);

            TabList.updateFooterTabList();
            TabList.updateHeaderTabList();
        }
    }
}

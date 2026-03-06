package com.zenith.YTCraft.listeners.handlers;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.data.MobSpawnState;
import com.zenith.YTCraft.ui.TabListUI;

public class AuthorMobDeathHandler {

    public static void removeAuthorMob(Entity entity){
        if(entity.getPersistentDataContainer().has(new NamespacedKey(YTCraft.getPlugin(),"IsChatSpawned"), PersistentDataType.BOOLEAN))
        {
            String spawnedChannelId = entity.getPersistentDataContainer().get(new NamespacedKey(YTCraft.getPlugin(), "SpawnedChannelId"), PersistentDataType.STRING);

            MobSpawnState.getChannelIdToAuthorMob().remove(spawnedChannelId);

            TabListUI.updateFooterTabList();
            TabListUI.updateHeaderTabList();
        }
    }
}

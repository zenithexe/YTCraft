package com.zenith.YTCraft.util;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.data.PluginState;

/**
 * Utility class for item operations
 */
public class ItemUtils {

    /**
     * Clear all items spawned by viewers from YouTube chat
     */
    public static void clearAllAuthorItems() {
        for (Entity en : PluginState.getStreamer().getWorld().getEntities()) {
            if (en instanceof Item) {
                ItemMeta meta = ((Item) en).getItemStack().getItemMeta();
                boolean value = meta.getPersistentDataContainer().has(
                    new NamespacedKey(YTCraft.getPlugin(), "IsChatSpawned"), 
                    PersistentDataType.BOOLEAN
                );
                if (value) {
                    en.remove();
                }
            }
        }
    }
}

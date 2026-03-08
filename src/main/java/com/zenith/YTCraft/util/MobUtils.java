package com.zenith.YTCraft.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.data.MobSpawnState;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.types.AuthorMob;
import com.zenith.YTCraft.ui.TabListUI;

/**
 * Utility class for mob operations
 */
public class MobUtils {

    /**
     * Tame entity if it's tameable
     */
    public static void tameEntity(LivingEntity creature, Player p) {
        if (creature instanceof Tameable) {
            Tameable tameable = (Tameable) creature;
            tameable.setOwner(p);
        }
    }

    /**
     * Set NBT data on spawned mob to mark it as chat-spawned
     */
    public static void setAuthorMobNBT(LivingEntity entity, String channelId) {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(new NamespacedKey(YTCraft.getPlugin(), "IsChatSpawned"), PersistentDataType.BOOLEAN, true);
        data.set(new NamespacedKey(YTCraft.getPlugin(), "SpawnedChannelId"), PersistentDataType.STRING, channelId);
    }



    public static void killAuthorMob(AuthorMob authorMob) {

        LivingEntity creature = authorMob.getMob();
        creature.setHealth(0);
        MobSpawnState.getChannelIdToAuthorMob().remove(authorMob.getChannelId());

        TabListUI.updateFooterTabList();
        TabListUI.updateHeaderTabList();

    }

    /**
     * Kill all mobs spawned by viewers
     */
    public static void killAllAuthorMobs() {
        // Create a copy of channel IDs to avoid ConcurrentModificationException
        // when death events trigger and remove entries from the map

        for (AuthorMob authorMob : new java.util.ArrayList<>(MobSpawnState.getChannelIdToAuthorMob().values())) {
            LivingEntity creature = authorMob.getMob();
            creature.setHealth(0);
        }

        // Clear any remaining entries (in case some weren't removed by death events)
        MobSpawnState.getChannelIdToAuthorMob().clear();

        TabListUI.updateFooterTabList();
        TabListUI.updateHeaderTabList();
    }

    /**
     * Find a valid spawn location near the player
     */
    @SuppressWarnings("null")
    public static Location getMobSpawnLocation(Player player) {
        Location playerLocation = player.getLocation();
        Location confirmSpawn = playerLocation;

        for (int x = PluginState.getMobSpawnRadius(); x >= -PluginState.getMobSpawnRadius(); x--) {
            for (int z = PluginState.getMobSpawnRadius(); z >= -PluginState.getMobSpawnRadius(); z--) {

                if (x == 0 && z == 0) {
                    continue;
                }

                Location spawnLocation = playerLocation.clone().add(x, 0, z);

                // Get block below spawn location
                Block block = spawnLocation.getBlock().getRelative(0, -1, 0);

                if (block.getType().isSolid() && block.getRelative(0, 1, 0).getType() == Material.AIR
                        && block.getRelative(0, 2, 0).getType() == Material.AIR) {

                    confirmSpawn = spawnLocation;
                    return confirmSpawn;
                }
            }
        }

        return confirmSpawn;
    }
}

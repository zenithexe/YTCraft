package com.zenith.YTCraft.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.custommobs.SkinApplier;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.data.SpawnQueue;
import com.zenith.YTCraft.types.MobSpawnRequest;
import com.zenith.YTCraft.ui.TabListUI;
import com.zenith.YTCraft.util.MessageUtils;
import com.zenith.YTCraft.util.MobUtils;

import net.kyori.adventure.text.Component;

/**
 * Handles mob spawning from the queue
 * Runs every tick to process one spawn request
 */
public class MobSpawning implements Runnable {

    /**
     * Add a mob spawn request to the queue (thread-safe)
     */
    public static void addMob(EntityType entityType, String author, String channelId) {
        MobSpawnRequest request = new MobSpawnRequest(entityType, author, channelId);
        SpawnQueue.add(request);
    }

    /**
     * Add a custom mob spawn request to the queue (thread-safe)
     */
    public static void addCustomMob(CustomMob customMob, String author, String channelId) {
        MobSpawnRequest request = new MobSpawnRequest(
            customMob.getEntityType(), 
            author, 
            channelId, 
            customMob
        );
        SpawnQueue.add(request);
    }

    @Override
    public void run() {
        // Poll next spawn request (atomic operation, thread-safe)
        MobSpawnRequest request = SpawnQueue.poll();

        if (request == null) {
            return; // Queue is empty
        }

        // Spawn the mob
        Player player = PluginState.getStreamer();
        Location playerLocation = player.getLocation();
        Location confirmSpawn = MobUtils.getMobSpawnLocation(player);

        LivingEntity livingMob = (LivingEntity) playerLocation.getWorld().spawnEntity(
            confirmSpawn, 
            request.getEntityType()
        );

        livingMob.customName(Component.text(request.getAuthor()));
        livingMob.setCustomNameVisible(true);
        livingMob.setRemoveWhenFarAway(false);

        MobUtils.tameEntity(livingMob, player);
        MobUtils.setAuthorMobNBT(livingMob, request.getChannelId());
        MobUtils.addAuthorMobData(livingMob, request.getAuthor(), request.getChannelId());

        // Apply custom skin if this is a custom mob
        if (request.isCustomMob()) {
            SkinApplier.applySkin(livingMob, request.getCustomMob());
        }

        TabListUI.updateHeaderTabList();
        TabListUI.updateFooterTabList();

        MessageUtils.sendAuthorMobSpawnMessage(livingMob, request.getAuthor());
    }
}

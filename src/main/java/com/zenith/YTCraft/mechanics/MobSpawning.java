package com.zenith.YTCraft.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.data.SpawnQueue;
import com.zenith.YTCraft.ui.TabList;
import com.zenith.YTCraft.util.MobUtils;

import net.kyori.adventure.text.Component;

public class MobSpawning implements Runnable {

    public static void addMob(EntityType en, String author, String channelId){
        SpawnQueue.getMobQueue().add(en);
        SpawnQueue.getChannelIdQueue().add(channelId);
        SpawnQueue.getAuthorQueue().add(author);
    }

    @Override
    public void run() {
        EntityType entityType = SpawnQueue.getMobQueue().poll();
        String channelId = SpawnQueue.getChannelIdQueue().poll();
        String author = SpawnQueue.getAuthorQueue().poll();

        if(entityType!=null && channelId!=null && author!=null){

            Player player = PluginState.getStreamer();
            Location playerLocation = player.getLocation();
            Location confirmSpawn = MobUtils.getMobSpawnLocation(player);

            LivingEntity livingMob = (LivingEntity) playerLocation.getWorld().spawnEntity(confirmSpawn, entityType);

            livingMob.customName(Component.text(author));
            livingMob.setCustomNameVisible(true);
            livingMob.setRemoveWhenFarAway(false);

            MobUtils.entityTaming(livingMob,player);
            MobUtils.setAuthorMobNBT(livingMob,channelId);
            MobUtils.addAuthorMobData(livingMob,author,channelId);

            TabList.updateHeaderTabList();
            TabList.updateFooterTabList();

            MobUtils.sendAuthorMobSpawnMessage(livingMob,author);

        }
    }
}

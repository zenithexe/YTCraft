package com.zenith.YTCraft.mechanics;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.zenith.YTCraft.data.MobManager;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.util.MobUtils;

public class SubscriberMechanics {

    public static int SubscriberCountLimit=0;
    public static void spawnMob(int currentSubscriberCount){
        if(SubscriberCountLimit==0){
            SubscriberCountLimit=currentSubscriberCount;
            return;
        }

        if(currentSubscriberCount>SubscriberCountLimit){
            int subscriberGained = currentSubscriberCount-SubscriberCountLimit;
            for(int i=1;i<=subscriberGained;i++){
                Player player = PluginState.getStreamer();
                Location playerLocation = player.getLocation();
                Location confirmSpawn = MobUtils.getMobSpawnLocation(player);

                LivingEntity livingMob = (LivingEntity) playerLocation.getWorld().spawnEntity(confirmSpawn, EntityType.WITHER);
                livingMob.setCustomName("Subscriber");
                livingMob.setCustomNameVisible(true);
                livingMob.setRemoveWhenFarAway(false);

                MobUtils.setAuthorMobNBT(livingMob,"RandomChannelId");
                MobManager.getAliveAuthorMobsList().add(livingMob);

                MobUtils.sendAuthorMobSpawnMessage(livingMob,"New Subscriber");

            }
            SubscriberCountLimit=currentSubscriberCount;
        }
    }

}

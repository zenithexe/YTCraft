package com.zenith.YTCraft.mechanics;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.zenith.YTCraft.data.MobSpawnState;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.util.MessageUtils;
import com.zenith.YTCraft.util.MobUtils;

import net.kyori.adventure.text.Component;

public class SubscriberMechanics {

    private static final AtomicInteger subCounter = new AtomicInteger(0);
    public static int SubscriberCountLimit = 0;

    public static void spawnMob(int currentSubscriberCount) {
        if (SubscriberCountLimit == 0) {
            SubscriberCountLimit = currentSubscriberCount;
            return;
        }

        if (currentSubscriberCount > SubscriberCountLimit) {
            int subscriberGained = currentSubscriberCount - SubscriberCountLimit;
            for (int i = 1; i <= subscriberGained; i++) {
                Player player = PluginState.getStreamer();
                Location playerLocation = player.getLocation();
                Location confirmSpawn = MobUtils.getMobSpawnLocation(player);

                LivingEntity livingMob = (LivingEntity) playerLocation.getWorld().spawnEntity(confirmSpawn, EntityType.WITHER);
                livingMob.customName(Component.text("Subscriber"));
                livingMob.setCustomNameVisible(true);
                livingMob.setRemoveWhenFarAway(false);

                String uniqueId = "_YTCRAFT_Subscriber_" + subCounter.incrementAndGet();
                MobUtils.setAuthorMobNBT(livingMob, uniqueId);
                MobSpawnState.addAuthorMob(livingMob, "New Subscriber", uniqueId);

                MessageUtils.sendAuthorMobSpawnMessage(livingMob, "New Subscriber");

            }
            SubscriberCountLimit = currentSubscriberCount;
        }
    }

}

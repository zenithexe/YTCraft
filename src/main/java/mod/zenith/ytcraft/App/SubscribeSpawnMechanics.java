package mod.zenith.ytcraft.App;

import mod.zenith.ytcraft.Data;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class SubscribeSpawnMechanics {

    public static int SubscriberCountLimit=0;
    public static void spawnMob(int currentSubscriberCount){
        if(SubscriberCountLimit==0){
            SubscriberCountLimit=currentSubscriberCount;
            return;
        }

        if(currentSubscriberCount>SubscriberCountLimit){
            int subscriberGained = currentSubscriberCount-SubscriberCountLimit;
            for(int i=1;i<=subscriberGained;i++){
                Player player = Data.streamer;
                Location playerLocation = player.getLocation();
                Location confirmSpawn = Utils.getMobSpawnLocation(player);

                LivingEntity livingMob = (LivingEntity) playerLocation.getWorld().spawnEntity(confirmSpawn, EntityType.WITHER);
                livingMob.setCustomName("Subscriber");
                livingMob.setCustomNameVisible(true);

                Utils.setAuthorMobNBT(livingMob,"RandomChannelId");
                Data.CreatureList_Of_Alive_AuthorMobs.add(livingMob);

                Utils.sendAuthorMobSpawnMessage(livingMob,"New Subscriber");

            }
            SubscriberCountLimit=currentSubscriberCount;
        }
    }

}

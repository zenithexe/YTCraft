package mod.zenith.ytcraft.App;

import mod.zenith.ytcraft.AdventureLib.TabList;
import mod.zenith.ytcraft.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MobQueueSpawning implements Runnable {

    public static void addMob(EntityType en, String author, String channelId){
        Data.Mob_SpawnQueue.add(en);
        Data.ChannelId_SpawnQueue.add(channelId);
        Data.Author_SpawnQueue.add(author);
    }

    @Override
    public void run() {
        EntityType entityType = Data.Mob_SpawnQueue.poll();
        String channelId = Data.ChannelId_SpawnQueue.poll();
        String author = Data.Author_SpawnQueue.poll();

        if(entityType!=null && channelId!=null && author!=null){

            Player player = Data.streamer;
            Location playerLocation = player.getLocation();
            Location confirmSpawn = Utils.getMobSpawnLocation(player);

            LivingEntity livingMob = (LivingEntity) playerLocation.getWorld().spawnEntity(confirmSpawn, entityType);
            livingMob.setCustomName(author);
            livingMob.setCustomNameVisible(true);
            livingMob.setRemoveWhenFarAway(false);

            Utils.entityTaming(livingMob,player);
            Utils.setAuthorMobNBT(livingMob,channelId);
            Utils.addAuthorMobData(livingMob,author,channelId);
            Data.CreatureList_Of_Alive_AuthorMobs.add(livingMob);

            TabList.updateHeaderTabList();
            TabList.updateFooterTabList();

            Utils.sendAuthorMobSpawnMessage(livingMob,author);

        }
    }
}

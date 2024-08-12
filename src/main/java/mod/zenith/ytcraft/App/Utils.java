package mod.zenith.ytcraft.App;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.LiveChatMessage;
import mod.zenith.ytcraft.AdventureLib.TabList;
import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YTCraft;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static LocalDateTime getGMTTimeNow(){
        return LocalDateTime.parse(ZonedDateTime.now(ZoneId.of("GMT")).toString().substring(0, 19));
    }

    public static LocalDateTime getMessageTime(LiveChatMessage message){
        DateTime msgTime = message.getSnippet().getPublishedAt();
        return LocalDateTime.parse(msgTime.toStringRfc3339().substring(0, 19));
    }

    public static boolean isEntityType_To_NViewers(String[] chatArgs, int viewers){
        return (Data.Config_EntityType_To_NViewers_List.containsKey(chatArgs[1].toUpperCase())
                && Data.Config_EntityType_To_NViewers_List.get(chatArgs[1].toUpperCase()) <= viewers);
    }

    public static void entityTaming(LivingEntity creature, Player p){
        if(creature instanceof Tameable){
            Tameable tameable = (Tameable) creature;
            tameable.setOwner(p);
        }
    }

    public static void setAuthorMobNBT(LivingEntity creature, String channelId){
        PersistentDataContainer data = creature.getPersistentDataContainer();
        data.set(new NamespacedKey(YTCraft.getPlugin(), "IsChatSpawned"), PersistentDataType.BOOLEAN, true);
        data.set(new NamespacedKey(YTCraft.getPlugin(), "SpawnedChannelId"), PersistentDataType.STRING, channelId);
    }

    public static void addAuthorMobData(LivingEntity creature, String author, String channelId){
        Data.ChannelId_Of_Alive_AuthorMobs.add(channelId);

        Map<String,String> AuthorMob = new HashMap<String, String>(){{ put(author,creature.getType().toString());}};
        Data.ChannelId_To_AuthorMob_List.put(channelId,AuthorMob);
    }

    public static void sendAuthorMobSpawnMessage(LivingEntity creature, String author){
        Component broadcastMessage = Component.text(author)
                .color(NamedTextColor.YELLOW)
                .appendSpace()
                .append(Component.text("has spawned a").color(NamedTextColor.WHITE))
                .appendSpace()
                .append(Component.text(creature.getType().toString()).color(NamedTextColor.RED));

        Bukkit.getServer().broadcast(broadcastMessage);
    }

    public static void sendAuthorItemSpawnMessage(ItemStack item, String author){
        Component broadcastMessage = Component.text(author)
                .color(NamedTextColor.YELLOW)
                .appendSpace()
                .append(Component.text("gave you").color(NamedTextColor.WHITE))
                .appendSpace()
                .append(Component.text(item.getType().toString()+" X "+item.getAmount()).color(NamedTextColor.GREEN));

        Bukkit.getServer().broadcast(broadcastMessage);
    }

    public static void killAllAuthorMobs() {
        for(LivingEntity creature: Data.CreatureList_Of_Alive_AuthorMobs){
            String spawnedChannelId = creature.getPersistentDataContainer().get(new NamespacedKey(YTCraft.getPlugin(), "SpawnedChannelId"), PersistentDataType.STRING);
            creature.setHealth(0);

            if(Data.ChannelId_To_AuthorMob_List.containsKey(spawnedChannelId)){
                Data.ChannelId_To_AuthorMob_List.remove(spawnedChannelId);
            }

            if(Data.ChannelId_Of_Alive_AuthorMobs.contains(spawnedChannelId)){
                Data.ChannelId_Of_Alive_AuthorMobs.remove(spawnedChannelId);
            }
        }

        TabList.updateFooterTabList();
        TabList.updateHeaderTabList();
    }
}

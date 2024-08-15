package mod.zenith.ytcraft.App;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.LiveChatMessage;
import mod.zenith.ytcraft.AdventureLib.TabList;
import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YTCraft;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static LocalDateTime getGMTTimeNow() {
        return LocalDateTime.parse(ZonedDateTime.now(ZoneId.of("GMT")).toString().substring(0, 19));
    }

    public static LocalDateTime getMessageTime(LiveChatMessage message) {
        DateTime msgTime = message.getSnippet().getPublishedAt();
        return LocalDateTime.parse(msgTime.toStringRfc3339().substring(0, 19));
    }

    public static boolean isEntityType_To_NViewers(String[] chatArgs, int viewers) {
        return (Data.Config_EntityType_To_NViewers_List.containsKey(chatArgs[1].toUpperCase())
                && Data.Config_EntityType_To_NViewers_List.get(chatArgs[1].toUpperCase()) <= viewers);
    }

    public static void entityTaming(LivingEntity creature, Player p) {
        if (creature instanceof Tameable) {
            Tameable tameable = (Tameable) creature;
            tameable.setOwner(p);
        }
    }

    public static void setAuthorMobNBT(LivingEntity creature, String channelId) {
        PersistentDataContainer data = creature.getPersistentDataContainer();
        data.set(new NamespacedKey(YTCraft.getPlugin(), "IsChatSpawned"), PersistentDataType.BOOLEAN, true);
        data.set(new NamespacedKey(YTCraft.getPlugin(), "SpawnedChannelId"), PersistentDataType.STRING, channelId);
    }

    public static void addAuthorMobData(LivingEntity creature, String author, String channelId) {
        Data.ChannelId_Of_Alive_AuthorMobs.add(channelId);

        Map<String, String> AuthorMob = new HashMap<String, String>() {{
            put(author, creature.getType().toString());
        }};
        Data.ChannelId_To_AuthorMob_List.put(channelId, AuthorMob);
    }

    public static void sendAuthorMobSpawnMessage(LivingEntity creature, String author) {
        Component broadcastMessage = Component.text(author)
                .color(NamedTextColor.YELLOW)
                .appendSpace()
                .append(Component.text("has spawned a").color(NamedTextColor.WHITE))
                .appendSpace()
                .append(Component.text(creature.getType().toString()).color(NamedTextColor.RED));

        Bukkit.getServer().broadcast(broadcastMessage);
    }

    public static void sendAuthorItemSpawnMessage(ItemStack item, String author) {
        Component broadcastMessage = Component.text(author)
                .color(NamedTextColor.YELLOW)
                .appendSpace()
                .append(Component.text("gave you").color(NamedTextColor.WHITE))
                .appendSpace()
                .append(Component.text(item.getType().toString() + " X " + item.getAmount()).color(NamedTextColor.GREEN));

        Bukkit.getServer().broadcast(broadcastMessage);
    }

    public static void killAllAuthorMobs() {
        for (LivingEntity creature : Data.CreatureList_Of_Alive_AuthorMobs) {
            String spawnedChannelId = creature.getPersistentDataContainer().get(new NamespacedKey(YTCraft.getPlugin(), "SpawnedChannelId"), PersistentDataType.STRING);
            creature.setHealth(0);

            if (Data.ChannelId_To_AuthorMob_List.containsKey(spawnedChannelId)) {
                Data.ChannelId_To_AuthorMob_List.remove(spawnedChannelId);
            }

            if (Data.ChannelId_Of_Alive_AuthorMobs.contains(spawnedChannelId)) {
                Data.ChannelId_Of_Alive_AuthorMobs.remove(spawnedChannelId);
            }
        }

        TabList.updateFooterTabList();
        TabList.updateHeaderTabList();
    }

    public static void clearAllAuthorItems() {
        for (Entity en : Data.streamer.getWorld().getEntities()) {
            if (en instanceof Item) {
                ItemMeta meta = ((Item) en).getItemStack().getItemMeta();
                boolean value = meta.getPersistentDataContainer().has(new NamespacedKey(YTCraft.getPlugin(), "IsChatSpawned"), PersistentDataType.BOOLEAN);
                if (value) {
                    en.remove();
                }
            }
        }
    }


    public static Location getMobSpawnLocation(Player player) {
        Location playerLocation = player.getLocation();
        Location confirmSpawn = playerLocation;

        Outer:
        for (int x = Data.Mob_SpawnRadius; x >= -Data.Mob_SpawnRadius; x--) {
            for (int z = Data.Mob_SpawnRadius; z >= -Data.Mob_SpawnRadius; z--) {

                if (x == 0 && z == 0) {
                    continue;
                }

                Location spawnLocation = playerLocation.clone().add(x, 0, z);

                Block block = spawnLocation.clone().subtract(0, 1, 0).getBlock();

                if (block.getType().isSolid() && block.getRelative(0, 1, 0).getType() == Material.AIR
                        && block.getRelative(0, 2, 0).getType() == Material.AIR) {

                    confirmSpawn = spawnLocation;
                    break Outer;
                }
            }
        }

        return confirmSpawn;
    }
}

package mod.zenith.ytcraft.App;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YTCraft;
import mod.zenith.ytcraft.YoutubeAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.google.api.services.youtube.model.LiveChatMessage;

import mod.zenith.ytcraft.AdventureLib.TabList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@SuppressWarnings("deprecation")
public class ChatControl implements Runnable {

    private static int viewers;
    private static int currentSubscriberCount;
    public static LocalDateTime ReadTimeStamp;
    
    public static void setTimeStamp(LocalDateTime TS) {
        ReadTimeStamp = TS;
    }

    @Override
    public void run() {

        if (Data.isActiveTimerMode) {
            if (ReadTimeStamp == null) {
                ReadTimeStamp = Utils.getGMTTimeNow();
            }
            
            viewers = YoutubeAPI.getConcurrentViewers().intValue();
            Data.SubscriberCount = YoutubeAPI.getSubscribers().intValue();

            SubscribeSpawnMechanics.spawnMob(Data.SubscriberCount);

            List<LiveChatMessage> Chats = YoutubeAPI.getChats();

            if (Chats == null) {
                return;
            }

            for (LiveChatMessage message : Chats) {

                LocalDateTime MessageTimeStamp = Utils.getMessageTime(message);
                
                if (MessageTimeStamp.compareTo(ReadTimeStamp) > 0 ) {

                    String author = message.getAuthorDetails().getDisplayName();
                    String text = message.getSnippet().getDisplayMessage();
                    String channelId = message.getAuthorDetails().getChannelId();

                    Bukkit.getLogger().info(author + ">>" + text);
                    ReadTimeStamp = MessageTimeStamp;

                    if (text != null && text.startsWith("spawn")) {

                        String[] chatArgs = text.split(" +");

                        EntityType userArgEntityType = null;

                        if(chatArgs.length==2){
                            userArgEntityType = EntityType.valueOf(chatArgs[1].toUpperCase());
                        }

                        //!Data.ChannelId_Of_Alive_AuthorMobs.contains(channelId)
                        if (!Data.ChannelId_Of_Alive_AuthorMobs.contains(channelId) || viewers<=10) {
                            if(userArgEntityType!=null && Utils.isEntityType_To_NViewers(chatArgs,viewers)){
                                MobQueueSpawning.addMob(userArgEntityType,author,channelId);
                            }
                        }

                    } else if (text!=null && text.startsWith("give") && Data.Enable_ItemSpawn) {
                        String[] charArgs = text.split(" +");

                        Bukkit.getLogger().info("Args ::"+Arrays.toString(charArgs));

                        if(charArgs.length<=3 && charArgs.length>1) {

                            Material material =  Material.getMaterial(charArgs[1].toUpperCase());

                            Bukkit.getLogger().info("Material ::"+ material.toString());
                            int count = 1;
                            if(charArgs.length==3){
                                count = Math.min(Integer.parseInt(charArgs[2]),16);
                            }
                            if(material.isItem()) {
                                Bukkit.getLogger().info("Passed Item Check");
                                ItemStack itemStack = new ItemStack(material,count);

                                ItemMeta meta = itemStack.getItemMeta();
                                PersistentDataContainer data = meta.getPersistentDataContainer();
                                data.set(new NamespacedKey(YTCraft.getPlugin(), "IsChatSpawned"), PersistentDataType.BOOLEAN, true);
                                itemStack.setItemMeta(meta);

                                if (Data.streamer.getInventory().addItem(itemStack).isEmpty()) {

                                } else {
                                    Location playerLocation = Data.streamer.getLocation();
                                    Data.streamer.getWorld().dropItemNaturally(playerLocation, itemStack);
                                }

                                Utils.sendAuthorItemSpawnMessage(itemStack,author);
                            }
                        }
                    }
                }
            }
        }
    }
}

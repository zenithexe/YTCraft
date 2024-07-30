package mod.zenith.ytcraft.App;

import java.time.LocalDateTime;
import java.util.List;

import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YoutubeAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.google.api.services.youtube.model.LiveChatMessage;

import mod.zenith.ytcraft.AdventureLib.TabList;

@SuppressWarnings("deprecation")
public class ChatControl implements Runnable {

    public static int viewers;
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

                    if ((text != null && text.startsWith("spawn"))) {

                        String[] chatArgs = text.split(" +");

                        EntityType userArgEntityType = null;
                        if(chatArgs.length>1){
                            userArgEntityType = EntityType.valueOf(chatArgs[1].toUpperCase());
                        }

                        if (!Data.ChannelId_Of_Alive_AuthorMobs.contains(channelId)) {

                            Player player = Data.streamer;
                            Location location = player.getLocation();

                            if(userArgEntityType!=null && Utils.isEntityType_To_NViewers(chatArgs,viewers)){

                                Creature creature = (Creature) location.getWorld().spawnEntity(location, userArgEntityType);
                                creature.setCustomName(author);
                                creature.setCustomNameVisible(true);

                                Utils.entityTaming(creature,player);
                                Utils.setAuthorMobNBT(creature,channelId);
                                Utils.addAuthorMobData(creature,author,channelId);
                                Data.CreatureList_Of_Alive_AuthorMobs.add(creature);

                                TabList.updateHeaderTabList();
                                TabList.updateFooterTabList();

                                Utils.sendAuthorMobSpawnMessage(creature,author);
                            }
                        }
                    }
                }
            }
        }
    }
}

package mod.zenith.ytcraft;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.LiveChatMessage;

@SuppressWarnings("deprecation")
public class ChatControl implements Runnable {

    public static int viewers;
    public static LocalDateTime ReadTimeStamp;

    public static void setTimeStamp(LocalDateTime TS) {
        ReadTimeStamp = TS;
    }

    @Override
    public void run() {
        Bukkit.getLogger().info(Data.EntityTypeToViewersList.toString());

        if (Data.isActiveTimerMode) {

            if (ReadTimeStamp == null) {
                ReadTimeStamp = LocalDateTime.parse(ZonedDateTime.now(ZoneId.of("GMT")).toString().substring(0, 19));
                Bukkit.getLogger().info("LocalTimeSet :: "+ReadTimeStamp.toString());
            }
            
            viewers = YoutubeAPI.getConcurrentViewers().intValue();
            List<LiveChatMessage> chats = YoutubeAPI.getChats();

            if (chats == null) {
                return;
            }

            for (LiveChatMessage message : chats) {

                DateTime msgTime = message.getSnippet().getPublishedAt();
                LocalDateTime msgLocalTime = LocalDateTime.parse(msgTime.toStringRfc3339().substring(0, 19));
                
                if (msgLocalTime.compareTo(ReadTimeStamp) > 0 ) {

                    String author = message.getAuthorDetails().getDisplayName();
                    String text = message.getSnippet().getDisplayMessage();
                    String channelId = message.getAuthorDetails().getChannelId();
                    
                // if(author.equalsIgnoreCase("Zenith GG")){
                // Bukkit.getLogger().info(msgLocalTime.toString()+" >>> "+ReadTimeStamp.toString()+" || "+msgLocalTime.compareTo(ReadTimeStamp)+" :: "+text);
                // }
                     Bukkit.getLogger().info(author + ">>" + text);
                      ReadTimeStamp = msgLocalTime;

                    if ((text != null && text.startsWith("spawn")) || true) {

                        String word[] = text.split(" +");
                        Bukkit.getLogger().info("Word :::: "+Arrays.toString(word));

                        EntityType en = EntityType.valueOf(word[1].toUpperCase());

                        if (!Data.isUserMobAlive.contains(channelId) || true) {

                            Player player = Data.streamer;
                            Location location = player.getLocation();



                            if(en!=null && Data.EntityTypeToViewersList.containsKey(word[1].toUpperCase()) && Data.EntityTypeToViewersList.get(word[1].toUpperCase()) <= viewers){

                                Creature c = (Creature) location.getWorld().spawnEntity(location, en);
                                c.setCustomName(author);
                                c.setCustomNameVisible(true);

                                Bukkit.getServer().broadcastMessage(author + " has spawned a "+en.getName());
                            }
                            else {
                                Bukkit.getLogger().info("En is null");
                            }



//                            Data.entityByUser.put(c, channelId);
//                            Data.isUserMobAlive.add(channelId);

                        }

                    }
                }
            }

        }

    }

}

package mod.zenith.ytcraft;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Allay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.LiveChatMessage;

@SuppressWarnings("deprecation")
public class ChatControl implements Runnable {

    public static LocalDateTime ReadTimeStamp;

    public static void setTimeStamp(LocalDateTime TS) {
        ReadTimeStamp = TS;
    }

    @Override
    public void run() {

        if (Data.isActiveTimerMode) {

            if (ReadTimeStamp == null) {
                ReadTimeStamp = LocalDateTime.parse(ZonedDateTime.now(ZoneId.of("GMT")).toString().substring(0, 19));
                Bukkit.getLogger().info("LocalTimeSet :: "+ReadTimeStamp.toString());
            }

            List<LiveChatMessage> chats = YoutubeAPI.getChats();

            if (chats == null) {
                return;
            }

            for (LiveChatMessage message : chats) {

                DateTime msgTime = message.getSnippet().getPublishedAt();
                LocalDateTime msgLocalTime = LocalDateTime.parse(msgTime.toStringRfc3339().substring(0, 19));
                
                if (msgLocalTime.compareTo(ReadTimeStamp) < 10 ) {

                    String author = message.getAuthorDetails().getDisplayName();
                    String text = message.getSnippet().getDisplayMessage();
                    String channelId = message.getAuthorDetails().getChannelId();
                    
                // if(author.equalsIgnoreCase("Zenith GG")){
                // Bukkit.getLogger().info(msgLocalTime.toString()+" >>> "+ReadTimeStamp.toString()+" || "+msgLocalTime.compareTo(ReadTimeStamp)+" :: "+text);
                // }
                     Bukkit.getLogger().info(author + ">>" + text);
                      ReadTimeStamp = msgLocalTime;

                    if (text != null && text.equalsIgnoreCase("spawn")) {

                        if (!Data.isUserMobAlive.contains(channelId)) {

                            Player player = Data.streamer;
                            Location location = player.getLocation();
                            Allay c = (Allay) location.getWorld().spawnEntity(location, EntityType.ALLAY);
                            c.setCustomName(author);
                            c.setCustomNameVisible(true);
                            c.setTarget(Data.streamer);

                            Bukkit.getServer().broadcastMessage(author + " has spawned a creeper.");

                            Data.entityByUser.put(c, channelId);
                            Data.isUserMobAlive.add(channelId);

                        }

                    }
                }
            }

        }

    }

}

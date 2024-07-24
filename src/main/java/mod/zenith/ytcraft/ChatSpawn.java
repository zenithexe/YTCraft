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

public class ChatSpawn implements Runnable {

    private static LocalDateTime timestamp = null;

    public static Player streamer;

    public static String getTimeStamp() {
        if (timestamp != null) {
            return timestamp.toString();
        } else {
            return "";
        }
    }

    public static void setTimeStamp(LocalDateTime ts) {
        timestamp = ts;
    }

    @Override
    public void run() {

        
        if(timestamp==null){
            timestamp=LocalDateTime.parse(ZonedDateTime.now(ZoneId.of("GMT")).toString().substring(0, 19));
           
            Bukkit.getLogger().info(timestamp.toString());
        }

        List<LiveChatMessage> chats = YoutubeAPI.getChats();

        if (chats == null) {
            return;
        }


        for (LiveChatMessage message : chats) {

            DateTime msgTime = message.getSnippet().getPublishedAt();
            LocalDateTime msgLocalTime = LocalDateTime.parse(msgTime.toStringRfc3339().substring(0, 19));

            // Bukkit.getLogger().info(timestamp.toString()+"  :::::  "+msgLocalTime.toString()+" = "+msgLocalTime.compareTo(timestamp));

            if ( msgLocalTime.compareTo(timestamp) > 0 ) {

                String author = message.getAuthorDetails().getDisplayName();
                String text = message.getSnippet().getDisplayMessage();
                String channelId = message.getAuthorDetails().getChannelId();


                
                Bukkit.getLogger().info(author + ">>" + text);
                timestamp = msgLocalTime;

                if (text != null && text.equalsIgnoreCase("spawn")) {

                    if (!Data.isUserMobAlive.contains(channelId)) {

                        Player player = streamer;
                        Location location = player.getLocation();
                        Allay c = (Allay) location.getWorld().spawnEntity(location, EntityType.ALLAY);
                        c.setCustomName(author);
                        c.setCustomNameVisible(true);
                        c.setTarget(streamer);


                        Bukkit.getServer().broadcastMessage(author + " has spawned a creeper.");

                        Data.entityByUser.put(c, channelId);
                        Data.isUserMobAlive.add(channelId);

                    }

                }
            }

        }

    }

}

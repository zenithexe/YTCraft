package mod.zenith.ytcraft;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.LiveChatMessage;

import java.time.LocalDateTime;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Allay;

public class ChatSpawn implements Runnable {

    private static LocalDateTime timestamp = null;

    public static Player streamer;

    public static String getTimeStamp(){
        if (timestamp!=null) {
            return timestamp.toString();
        }
        else {
            return "";
        }
    }

    @Override
    public void run() {

        String GLOBAL_CHAT_TIMESTAMP = YTCraft.getPlugin().getConfig().getString("GLOBAL_CHAT_TIMESTAMP");
        if (!GLOBAL_CHAT_TIMESTAMP.isEmpty()) {
            timestamp = LocalDateTime.parse(GLOBAL_CHAT_TIMESTAMP);
        }
        List<LiveChatMessage> chats = YoutubeAPI.getChats();

        if(chats==null){
            return;
        }


        for (LiveChatMessage message : chats) {

            DateTime msgTime = message.getSnippet().getPublishedAt();
            LocalDateTime msgLocalTime = LocalDateTime.parse(msgTime.toStringRfc3339().substring(0, 19));

            if (timestamp == null || msgLocalTime.compareTo(timestamp) > 0) {

                String author = message.getAuthorDetails().getDisplayName();
                String text = message.getSnippet().getDisplayMessage();
                String channelId = message.getAuthorDetails().getChannelId();

                Bukkit.getLogger().info(author + ">>" + text);
                timestamp = msgLocalTime;

                if (text.equalsIgnoreCase("spawn")) {

                    if (!Data.isUserMobAlive.contains(channelId)) {

                        Player player = streamer;
                        Location location = player.getLocation();
                        Allay c = (Allay) location.getWorld().spawnEntity(location, EntityType.ALLAY);
                        c.setCustomName(author);
                        c.setCustomNameVisible(true);
                        c.setTarget(streamer);

                        
                        
                        Bukkit.getServer().broadcastMessage(author + " has spawned a creeper.");

                        Data.entityByUser.put(c,channelId);
                        Data.isUserMobAlive.add(channelId);

                    }

                }
            }

        }

        if(!GLOBAL_CHAT_TIMESTAMP.equals(timestamp.toString())){
            YTCraft.getPlugin().getConfig().set("GLOBAL_CHAT_TIMESTAMP",ChatSpawn.getTimeStamp());
            YTCraft.getPlugin().saveConfig();
        }
    }

}

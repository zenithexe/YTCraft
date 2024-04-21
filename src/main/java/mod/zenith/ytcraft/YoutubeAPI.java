package mod.zenith.ytcraft;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.LiveChatMessage;
import com.google.api.services.youtube.model.LiveChatMessageListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import java.util.List;

import org.bukkit.Bukkit;

public class YoutubeAPI {
    private static final String APPLICATION_NAME = "YTCraft-MinecraftPlugin";
    private static String API_KEY = "";
    private static String VIDEO_ID = "";
    private static String LIVE_CHAT_ID = ""; 

    public static  void setAPI(String apiKey, String videoId){
        API_KEY=apiKey;
        VIDEO_ID=videoId;
        setLiveChatId();
    }

    private static YouTube getYoutube(){
        try{
            final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
            
            return new YouTube(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null);
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
        
    }

    private static Video getVideo(String parts){
        try{
            YouTube.Videos.List req = getYoutube().videos().list(parts);
            req.setKey(API_KEY);
            req.setId(VIDEO_ID);

            VideoListResponse res = req.execute();

            return res.getItems().get(0);
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

    private static void setLiveChatId(){

        LIVE_CHAT_ID = getVideo("liveStreamingDetails").getLiveStreamingDetails().getActiveLiveChatId();
    
    }

    public static void call(){
    
        try {

            
            


        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void getChats(){
        try{
            YouTube.LiveChatMessages.List req = getYoutube().liveChatMessages().list(LIVE_CHAT_ID,"snippet,authorDetails");
            req.setKey(API_KEY);

            LiveChatMessageListResponse res =  req.execute();
            

            List<LiveChatMessage> messages = res.getItems();
            

            if (messages != null){
                for(int i=0;i<messages.size();i++){
                    LiveChatMessage msg = messages.get(i);
                    Bukkit.getLogger().info("----------------");
                    Bukkit.getLogger().info(msg.getAuthorDetails().getDisplayName()+">>"+msg.getSnippet().getDisplayMessage());
                }
            } else{
                System.out.println("No messages found.");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        
    }
}

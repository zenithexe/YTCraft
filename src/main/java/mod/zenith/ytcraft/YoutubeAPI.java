package mod.zenith.ytcraft;

import java.math.BigInteger;
import java.util.List;

import org.bukkit.Bukkit;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.LiveChatMessage;
import com.google.api.services.youtube.model.LiveChatMessageListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoLiveStreamingDetails;

public class YoutubeAPI {
    private static final String APP_NAME = "YTCraft-MinecraftPlugin";
    private static String API_KEY;
    private static String VIDEO_ID;
    private static String LIVE_CHAT_ID;
    private static String CHANNEL_ID;

    public static void setAPI(String apiKey, String videoId) {
        API_KEY = apiKey;
        VIDEO_ID = videoId;
//        setChannelId();
        setLiveChatId();
    }

    private static void setLiveChatId(){
        VideoLiveStreamingDetails stream = getVideo("liveStreamingDetails").getLiveStreamingDetails();
        if(stream!=null){
            LIVE_CHAT_ID = stream.getActiveLiveChatId();
            Bukkit.getLogger().info(":::: Live-Chat ID is Set ::::");
        }
        else{
            Bukkit.broadcastMessage("Incorrect Video ID. Please provide the Video ID of a Livestream.");
        }
    }

//    private static void setChannelId(){
//        CHANNEL_ID = getVideo("snippet").getSnippet().getChannelId();
//    }

    public static void updateVideoId(String videoId) {
        setAPI(API_KEY, videoId);
    }

    private static YouTube getYoutube() {
        try {
            final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

            return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                    .setApplicationName(APP_NAME).build();

        } catch (Exception e) {

            System.out.println(e);
            return null;
        }

    }

    private static Video getVideo(String parts) {
        try {
            YouTube.Videos.List req = getYoutube().videos().list(parts);
            req.setKey(API_KEY);
            req.setId(VIDEO_ID);

            VideoListResponse res = req.execute();
            Bukkit.getLogger().info(":::: GET-Video Youtube API called ::::");
            return res.getItems().get(0);
            
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private static void getSubscriberCount() {
        try{

            YouTube.Channels.List req = getYoutube().channels().list("statistics");


        } catch(Exception e){

        }
    }



 

    public static List<LiveChatMessage> getChats() {
        try {
            if(LIVE_CHAT_ID==null){
                Bukkit.broadcastMessage("Incorrect Video ID. Please provide the Video ID of a Livestream.");
                return null;
            }
            YouTube.LiveChatMessages.List req = getYoutube().liveChatMessages().list(LIVE_CHAT_ID,
                    "snippet,authorDetails");
            req.setKey(API_KEY);

            LiveChatMessageListResponse res = req.execute();
            Bukkit.getLogger().info(":::: GET-LiveChat YouTube API called ::::");

            return res.getItems();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    public static BigInteger getConcurrentViewers(){
        BigInteger concurrentViewers =  getVideo("liveStreamingDetails").getLiveStreamingDetails().getConcurrentViewers();
        Bukkit.getLogger().info(":::: GET-Viewers === "+concurrentViewers+"  ::::");
        return concurrentViewers;
    }


}

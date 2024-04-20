package mod.zenith.ytcraft;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

public class YoutubeAPI {
    private static String API_KEY = "";
    private static String VIDEO_ID = "";

    public static  void setAPI(String apiKey, String videoId){
        API_KEY=apiKey;
        VIDEO_ID=videoId;
    }

    public static void call(){
    
        try {

            final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
            final YouTube youtube = new YouTube(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null);
            
            YouTube.Videos.List req = youtube.videos().list("liveStreamingDetails,statistics");
            
            req.setKey(API_KEY);
            req.setId(VIDEO_ID);


            VideoListResponse res = req.execute();

            Video video = res.getItems().get(0);

            System.out.println("Like Count :"+ video.getStatistics().getLikeCount());
            // System.out.println("Views :"+ video.getLiveStreamingDetails().getConcurrentViewers());
            


        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}

package mod.zenith.ytcraft;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

public class YoutubeAPI {

    private static final String API_KEY="AIzaSyD7pHfmxDuY5neRWf7_npFnzWVHIXcWv84";
    private static final String VIDEO_ID="iYzZFernvh0";

    public static void main(String[] args[]){

        try {
            final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
            final YouTube youtube = new YouTube(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null);
            
            YouTube.Videos.List req = youtube.videos().list("liveStreamingDetails,statistics");
            
            req.setKey(API_KEY);
            req.setId(VIDEO_ID);


            VideoListResponse res = req.execute();

            Video video = res.getItems().get(0);

            System.out.println("Like Count :"+ video.getStatistics().getLikeCount());
            System.out.println("Views :"+ video.getLiveStreamingDetails().getConcurrentViewers());
            


        }catch (Exception e){
            System.out.println(e);
        }
    }
}

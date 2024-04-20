package mod.zenith.ytcraft;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Test {
    public static void main(String[] args){
        String API_KEY="AIzaSyD7pHfmxDuY5neRWf7_npFnzWVHIXcWv84";
        String VIDEO_ID="VJpvJXKm07Q";

        String URL_PATH = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet&part=statistics&part=liveStreamingDetails&id="+VIDEO_ID+"&key="+API_KEY;
        try{

            URL url = new URL(URL_PATH);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept","application/json");

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code:"+responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();


            // System.out.println("Response :"+ response.toString());

            Gson gson = new Gson();
            ResponseObject rObj = gson.fromJson(response.toString(),ResponseObject.class);


        }
        catch(Exception e){
            System.out.println("Error :\n"+e);
        }


    }
}

class ResponseObject {

}
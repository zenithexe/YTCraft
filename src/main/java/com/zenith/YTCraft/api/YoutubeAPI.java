package com.zenith.YTCraft.api;

import java.math.BigInteger;
import java.util.List;

import org.bukkit.Bukkit;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.LiveChatMessage;
import com.google.api.services.youtube.model.LiveChatMessageListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoLiveStreamingDetails;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class YoutubeAPI {
    private static final String APP_NAME = "YTCraft-MinecraftPlugin";
    private static String API_KEY;
    private static String VIDEO_ID;
    private static String LIVE_CHAT_ID;
    private static String CHANNEL_ID;

    public static void setAPI(String apiKey, String videoId) {
        API_KEY = apiKey;
        VIDEO_ID = videoId;
        setChannelId();
        setLiveChatId();
        if(LIVE_CHAT_ID==null){
            Bukkit.broadcast(Component.text("Error :: Can't Set ChatID.").color(NamedTextColor.RED));
            Bukkit.broadcast(Component.text("Make sure the Video-Id is of a Livestream.").color(NamedTextColor.YELLOW));
        }
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

    private static void setChannelId(){
        CHANNEL_ID = getVideo("snippet").getSnippet().getChannelId();
    }

    public static void updateVideoId(String videoId) {
        setAPI(API_KEY, videoId);
    }

    private static YouTube getYoutube() {
        try {
            final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

            return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                    .setApplicationName(APP_NAME).build();

        } catch (Exception e) {
            Bukkit.getLogger().info(e.getMessage());
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
            Bukkit.broadcast(Component.text("Error :: Can't Get Video").color(NamedTextColor.RED));
            Bukkit.broadcast(Component.text("Make sure the Video-Id is correct.").color(NamedTextColor.YELLOW));
            return null;
        }
    }

    public static BigInteger getSubscribers() {
        try{

            YouTube.Channels.List req = getYoutube().channels().list("statistics");
            req.setKey(API_KEY);
            req.setId(CHANNEL_ID);

            ChannelListResponse response =  req.execute();
            Channel channel = response.getItems().get(0);
            BigInteger subscriberCount = channel.getStatistics().getSubscriberCount();
            Bukkit.getLogger().info(":::: GET-Subscriber === "+subscriberCount+"  ::::");
            return subscriberCount;

        } catch(Exception e){
            Bukkit.broadcast(Component.text("Error :: Can't Get Subscriber Count.").color(NamedTextColor.RED));
            Bukkit.broadcast(Component.text("Make sure the Video-Id is correct.").color(NamedTextColor.YELLOW));
            return BigInteger.ZERO;
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
        try{
            BigInteger concurrentViewers =  getVideo("liveStreamingDetails").getLiveStreamingDetails().getConcurrentViewers();
            Bukkit.getLogger().info(":::: GET-Viewers === "+concurrentViewers+"  ::::");
            return concurrentViewers;
        }
        catch (Exception e){
            Bukkit.broadcast(Component.text("Error :: Can't Get Live-Watching Count.").color(NamedTextColor.RED));
            Bukkit.broadcast(Component.text("Make sure the Video-Id is of a Livestream.").color(NamedTextColor.YELLOW));
            return BigInteger.ZERO;
        }

    }


}

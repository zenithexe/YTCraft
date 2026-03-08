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

        if (LIVE_CHAT_ID == null) {
            Bukkit.broadcast(Component.text("Error :: Can't Set ChatID.").color(NamedTextColor.RED));
            Bukkit.broadcast(Component.text("Make sure the Video-Id is of a Livestream.").color(NamedTextColor.YELLOW));
        }
    }

    private static void setLiveChatId() {
        Video video = getVideo("liveStreamingDetails");
        if (video != null) {
            VideoLiveStreamingDetails stream = video.getLiveStreamingDetails();
            if (stream != null) {
                LIVE_CHAT_ID = stream.getActiveLiveChatId();
                Bukkit.getLogger().info(":::: Live-Chat ID is Set ::::");
            } else {
                Bukkit.broadcast(Component.text("Video is not a livestream or livestream is not active.").color(NamedTextColor.YELLOW));
                LIVE_CHAT_ID = null;
            }
        } else {
            Bukkit.broadcast(Component.text("Could not retrieve video - it may be private or invalid.").color(NamedTextColor.RED));
            LIVE_CHAT_ID = null;
        }
    }

    private static void setChannelId() {
        Video video = getVideo("snippet");
        if (video != null && video.getSnippet() != null) {
            CHANNEL_ID = video.getSnippet().getChannelId();
        } else {
            Bukkit.getLogger().warning("Could not retrieve channel ID - video may be private or invalid");
            CHANNEL_ID = null;
        }
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
        try {

            YouTube.Channels.List req = getYoutube().channels().list("statistics");
            req.setKey(API_KEY);
            req.setId(CHANNEL_ID);

            ChannelListResponse response = req.execute();
            Channel channel = response.getItems().get(0);
            BigInteger subscriberCount = channel.getStatistics().getSubscriberCount();
            Bukkit.getLogger().info(String.format(":::: GET-Subscriber === %s  ::::", subscriberCount));
            return subscriberCount;

        } catch (Exception e) {
            Bukkit.broadcast(Component.text("Error :: Can't Get Subscriber Count.").color(NamedTextColor.RED));
            Bukkit.broadcast(Component.text("Make sure the Video-Id is correct.").color(NamedTextColor.YELLOW));
            return BigInteger.ZERO;
        }
    }

    public static List<LiveChatMessage> getChats() {
        try {
            if (LIVE_CHAT_ID == null) {
                Bukkit.broadcast(Component.text("Incorrect Video ID. Please provide the Video ID of a Livestream.").color(NamedTextColor.RED));
                return null;
            }
            
            YouTube.LiveChatMessages.List req = getYoutube().liveChatMessages().list(LIVE_CHAT_ID,
                    "snippet,authorDetails");
            req.setKey(API_KEY);

            LiveChatMessageListResponse res = req.execute();
            Bukkit.getLogger().info(":::: GET-LiveChat YouTube API called ::::");

            return res.getItems();

        } catch (Exception e) {
            Bukkit.getLogger().warning(String.format("Error fetching YouTube chat: %s", e.getMessage()));
            return null;
        }
    }

    public static BigInteger getConcurrentViewers() {
        try {
            Video video = getVideo("liveStreamingDetails");
            if (video != null && video.getLiveStreamingDetails() != null) {
                BigInteger concurrentViewers = video.getLiveStreamingDetails().getConcurrentViewers();
                Bukkit.getLogger().info(String.format(":::: GET-Viewers === %s  ::::", concurrentViewers));
                return concurrentViewers;
            } else {
                Bukkit.getLogger().warning("Could not get concurrent viewers - video may not be live");
                return BigInteger.ZERO;
            }
        } catch (Exception e) {
            Bukkit.broadcast(Component.text("Error :: Can't Get Live-Watching Count.").color(NamedTextColor.RED));
            Bukkit.broadcast(Component.text("Make sure the Video-Id is of a Livestream.").color(NamedTextColor.YELLOW));
            return BigInteger.ZERO;
        }
    }

    public static boolean isConfigured() {
        return LIVE_CHAT_ID != null && CHANNEL_ID != null;
    }

}

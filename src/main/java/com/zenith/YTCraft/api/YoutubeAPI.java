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

    /**
     * Initialize API with key, channel id from config, and optional video id.
     * Does NOT call setLiveChatId here — call updateVideoId() or connect() to activate.
     */
    public static void setAPI(String apiKey, String channelId, String videoId) {
        API_KEY = apiKey;
        CHANNEL_ID = (channelId != null && !channelId.isEmpty()) ? channelId : null;
        VIDEO_ID = (videoId != null && !videoId.isEmpty()) ? videoId : null;
        LIVE_CHAT_ID = null;

        // Only resolve live chat if we have a video id
        if (VIDEO_ID != null) {
            setLiveChatId();
        }
    }

    /**
     * Update only the runtime video id and channel id (does not persist to config).
     */
    public static void updateVideoId(String videoId) {
        VIDEO_ID = videoId;
        LIVE_CHAT_ID = null;
        setLiveChatId();
    }

    /**
     * Update only the runtime channel id (does not persist to config).
     */
    public static void updateChannelId(String channelId) {
        CHANNEL_ID = channelId;
    }

    public static String getChannelId() {
        return CHANNEL_ID;
    }

    public static String getVideoId() {
        return VIDEO_ID;
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

    /**
     * Fetch active live broadcasts for the configured channel.
     * Returns a list of [videoId, title] pairs.
     */
    public static List<String[]> getLiveBroadcasts() {
        try {
            if (CHANNEL_ID == null) return null;

            YouTube youtube = getYoutube();
            if (youtube == null) return null;

            // Search for live broadcasts on the channel
            com.google.api.services.youtube.YouTube.Search.List req =
                    youtube.search().list("id,snippet");
            req.setKey(API_KEY);
            req.setChannelId(CHANNEL_ID);
            req.setEventType("live");
            req.setType("video");
            req.setMaxResults(10L);

            com.google.api.services.youtube.model.SearchListResponse res = req.execute();
            List<String[]> broadcasts = new java.util.ArrayList<>();
            for (com.google.api.services.youtube.model.SearchResult item : res.getItems()) {
                String id = item.getId().getVideoId();
                String title = item.getSnippet().getTitle();
                broadcasts.add(new String[]{id, title});
            }
            return broadcasts;
        } catch (Exception e) {
            Bukkit.getLogger().warning("Error fetching live broadcasts: " + e.getMessage());
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

    public static boolean hasChannelId() {
        return CHANNEL_ID != null && !CHANNEL_ID.isEmpty();
    }

    public static boolean hasVideoId() {
        return VIDEO_ID != null && !VIDEO_ID.isEmpty();
    }

    /**
     * Resolves a channel username/handle to a channel ID.
     * Tries forUsername first (legacy), then a search query as fallback for modern handles.
     * Returns the channel ID string, or null if not found.
     */
    public static String resolveChannelIdByUsername(String username) {
        try {
            YouTube youtube = getYoutube();
            if (youtube == null) return null;

            // Strip leading @ if present
            String handle = username.startsWith("@") ? username.substring(1) : username;

            // Try legacy forUsername
            YouTube.Channels.List req = youtube.channels().list("id,snippet");
            req.setKey(API_KEY);
            req.setForUsername(handle);
            ChannelListResponse res = req.execute();
            if (res.getItems() != null && !res.getItems().isEmpty()) {
                return res.getItems().get(0).getId();
            }

            // Fallback: search by channel name and return the first match
            com.google.api.services.youtube.YouTube.Search.List search =
                    youtube.search().list("id,snippet");
            search.setKey(API_KEY);
            search.setQ(handle);
            search.setType("channel");
            search.setMaxResults(1L);
            com.google.api.services.youtube.model.SearchListResponse searchRes = search.execute();
            if (searchRes.getItems() != null && !searchRes.getItems().isEmpty()) {
                return searchRes.getItems().get(0).getId().getChannelId();
            }

        } catch (Exception e) {
            Bukkit.getLogger().warning("Could not resolve channel username: " + e.getMessage());
        }
        return null;
    }

    /**
     * Returns the channel title for the configured CHANNEL_ID, or null on failure.
     */
    public static String getChannelTitle() {
        try {
            YouTube.Channels.List req = getYoutube().channels().list("snippet");
            req.setKey(API_KEY);
            req.setId(CHANNEL_ID);
            ChannelListResponse res = req.execute();
            if (res.getItems() != null && !res.getItems().isEmpty()) {
                return res.getItems().get(0).getSnippet().getTitle();
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("Could not fetch channel title: " + e.getMessage());
        }
        return null;
    }

    /**
     * Returns the video title for the configured VIDEO_ID, or null on failure.
     */
    public static String getVideoTitle() {
        try {
            Video video = getVideo("snippet");
            if (video != null && video.getSnippet() != null) {
                return video.getSnippet().getTitle();
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("Could not fetch video title: " + e.getMessage());
        }
        return null;
    }

}

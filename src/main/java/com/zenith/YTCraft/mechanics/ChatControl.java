package com.zenith.YTCraft.mechanics;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.api.YoutubeAPI;
import com.zenith.YTCraft.data.MobManager;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.util.MobUtils;

public class ChatControl implements Runnable {

    private static int viewers;
    private static int currentSubscriberCount;
    public static LocalDateTime ReadTimeStamp;

    public static void setTimeStamp(LocalDateTime TS) {
        ReadTimeStamp = TS;
    }

    @Override
    public void run() {

        if (PluginState.isChatControlEnabled()) {
            if (ReadTimeStamp == null) {
                ReadTimeStamp = MobUtils.getGMTTimeNow();
            }

            viewers = YoutubeAPI.getConcurrentViewers().intValue();
            PluginState.setSubscriberCount(YoutubeAPI.getSubscribers().intValue());

            SubscriberMechanics.spawnMob(PluginState.getSubscriberCount());

            List<LiveChatMessage> Chats = YoutubeAPI.getChats();

            if (Chats == null) {
                return;
            }

            for (LiveChatMessage message : Chats) {

                LocalDateTime MessageTimeStamp = MobUtils.getMessageTime(message);

                if (MessageTimeStamp.compareTo(ReadTimeStamp) > 0) {

                    String author = message.getAuthorDetails().getDisplayName();
                    String text = message.getSnippet().getDisplayMessage();
                    String channelId = message.getAuthorDetails().getChannelId();

                    Bukkit.getLogger().info(author + ">>" + text);
                    ReadTimeStamp = MessageTimeStamp;

                    if (text != null && text.startsWith("spawn")) {

                        String[] chatArgs = text.split(" +");

                        EntityType userArgEntityType = null;

                        if (chatArgs.length == 2) {
                            userArgEntityType = EntityType.valueOf(chatArgs[1].toUpperCase());
                        }

                        //!MobManager.getAliveAuthorMobChannelIds().contains(channelId)
                        if (!MobManager.getChannelIdToAuthorMob().containsKey(channelId) || viewers <= 10) {
                            if (userArgEntityType != null && MobUtils.isEntityType_To_NViewers(chatArgs, viewers)) {
                                MobSpawning.addMob(userArgEntityType, author, channelId);
                            }
                        }

                    } else if (text != null && text.startsWith("give") && PluginState.isItemSpawnEnabled()) {
                        String[] charArgs = text.split(" +");

                        Bukkit.getLogger().info("Args ::" + Arrays.toString(charArgs));

                        if (charArgs.length <= 3 && charArgs.length > 1) {

                            Material material = Material.getMaterial(charArgs[1].toUpperCase());

                            Bukkit.getLogger().info("Material ::" + material.toString());
                            int count = 1;
                            if (charArgs.length == 3) {
                                count = Math.min(Integer.parseInt(charArgs[2]), 16);
                            }
                            if (material.isItem()) {
                                Bukkit.getLogger().info("Passed Item Check");
                                ItemStack itemStack = new ItemStack(material, count);

                                ItemMeta meta = itemStack.getItemMeta();
                                PersistentDataContainer data = meta.getPersistentDataContainer();
                                data.set(new NamespacedKey(YTCraft.getPlugin(), "IsChatSpawned"), PersistentDataType.BOOLEAN, true);
                                itemStack.setItemMeta(meta);

                                if (PluginState.getStreamer().getInventory().addItem(itemStack).isEmpty()) {

                                } else {
                                    Location playerLocation = PluginState.getStreamer().getLocation();
                                    PluginState.getStreamer().getWorld().dropItemNaturally(playerLocation, itemStack);
                                }

                                MobUtils.sendAuthorItemSpawnMessage(itemStack, author);
                            }
                        }
                    }
                }
            }
        }
    }
}

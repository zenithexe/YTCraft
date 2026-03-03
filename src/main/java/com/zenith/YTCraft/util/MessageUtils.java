package com.zenith.YTCraft.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * Utility class for broadcasting messages to players
 */
public class MessageUtils {

    /**
     * Broadcast message when a viewer spawns a mob
     */
    public static void sendAuthorMobSpawnMessage(LivingEntity creature, String author) {
        Component broadcastMessage = Component.text(author)
                .color(NamedTextColor.YELLOW)
                .appendSpace()
                .append(Component.text("has spawned a").color(NamedTextColor.WHITE))
                .appendSpace()
                .append(Component.text(creature.getType().toString()).color(NamedTextColor.RED));

        Bukkit.getServer().broadcast(broadcastMessage);
    }

    /**
     * Broadcast message when a viewer gives an item
     */
    public static void sendAuthorItemSpawnMessage(ItemStack item, String author) {
        Component broadcastMessage = Component.text(author)
                .color(NamedTextColor.YELLOW)
                .appendSpace()
                .append(Component.text("gave you").color(NamedTextColor.WHITE))
                .appendSpace()
                .append(Component.text(item.getType().toString() + " X " + item.getAmount()).color(NamedTextColor.GREEN));

        Bukkit.getServer().broadcast(broadcastMessage);
    }
}

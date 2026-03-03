package com.zenith.YTCraft.chatactions.actions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.chatactions.ChatAction;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.util.MobUtils;

/**
 * Handles item giving from YouTube chat Viewers type "give <item> [count]" to
 * give items to the streamer
 */
public class GiveItemAction implements ChatAction {

    private static final int MAX_ITEM_COUNT = 16;

    @Override
    public String getTrigger() {
        return "give";
    }

    @Override
    public boolean execute(LiveChatMessage message, String[] args, int viewers) {

        // Check if item spawning is enabled
        if (!PluginState.isItemSpawnEnabled()) {
            return false;
        }

        String author = message.getAuthorDetails().getDisplayName();

        // Validate arguments (give <item> or give <item> <count>)
        if (args.length < 2 || args.length > 3) {
            Bukkit.getLogger().info("Invalid give from " + author + " - wrong number of arguments");
            return false;
        }

        // Parse material
        Material material = Material.getMaterial(args[1].toUpperCase());
        if (material == null) {
            Bukkit.getLogger().info("Invalid material from " + author + ": " + args[1]);
            return false;
        }

        if (!material.isItem()) {
            Bukkit.getLogger().info("Material is not an item from " + author + ": " + material);
            return false;
        }

        // Parse count (default 1)
        int count = 1;
        if (args.length == 3) {
            try {
                count = Integer.parseInt(args[2]);
                count = Math.min(count, MAX_ITEM_COUNT); // Cap at max
            } catch (NumberFormatException e) {
                Bukkit.getLogger().info("Invalid count from " + author + ": " + args[2]);
                return false;
            }
        }

        // Create item with metadata
        ItemStack itemStack = new ItemStack(material, count);
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(YTCraft.getPlugin(), "IsChatSpawned"),
                PersistentDataType.BOOLEAN, true);
        itemStack.setItemMeta(meta);

        // Give to streamer
        if (PluginState.getStreamer() == null) {
            Bukkit.getLogger().warning("Cannot give item - streamer is null");
            return false;
        }

        // Try to add to inventory, drop if full
        if (!PluginState.getStreamer().getInventory().addItem(itemStack).isEmpty()) {
            Location playerLocation = PluginState.getStreamer().getLocation();
            if (playerLocation != null) {
                PluginState.getStreamer().getWorld().dropItemNaturally(playerLocation, itemStack);
            }
        }

        // Send broadcast message
        MobUtils.sendAuthorItemSpawnMessage(itemStack, author);
        Bukkit.getLogger().info(author + " gave " + material + " x" + count);

        return true;
    }

    @Override
    public String getDescription() {
        return "Give an item to the streamer";
    }
}

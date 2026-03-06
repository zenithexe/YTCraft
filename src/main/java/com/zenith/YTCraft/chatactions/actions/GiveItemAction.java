package com.zenith.YTCraft.chatactions.actions;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.chatactions.ChatAction;

public class GiveItemAction implements ChatAction {

    @Override
    public String getKey() {
        return "give";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"gv", "giv"};
    }

    @Override
    public String getDescription() {
        return "Give an item to the streamer";
    }

    @Override
    public boolean execute(LiveChatMessage message, String[] args) {

        return true;

        // // Check if item spawning is enabled
        // if (!PluginState.isItemSpawnEnabled()) {
        //     return false;
        // }
        // String author = message.getAuthorDetails().getDisplayName();
        // // Validate arguments (give <item> or give <item> <count>)
        // if (args.length < 2 || args.length > 3) {
        //     Bukkit.getLogger().info(String.format("Invalid give from %s - wrong number of arguments", author));
        //     return false;
        // }
        // // Parse material
        // Material material = Material.getMaterial(args[1].toUpperCase());
        // if (material == null) {
        //     Bukkit.getLogger().info(String.format("Invalid material from %s: %s", author, args[1]));
        //     return false;
        // }
        // if (!material.isItem()) {
        //     Bukkit.getLogger().info(String.format("Material is not an item from %s: %s", author, material));
        //     return false;
        // }
        // // Parse count (default 1)
        // int count = 1;
        // if (args.length == 3) {
        //     try {
        //         count = Integer.parseInt(args[2]);
        //         count = Math.min(count, MAX_ITEM_COUNT); // Cap at max
        //     } catch (NumberFormatException e) {
        //         Bukkit.getLogger().info(String.format("Invalid count from %s: %s", author, args[2]));
        //         return false;
        //     }
        // }
        // // Create item with metadata
        // ItemStack itemStack = new ItemStack(material, count);
        // ItemMeta meta = itemStack.getItemMeta();
        // PersistentDataContainer data = meta.getPersistentDataContainer();
        // data.set(new NamespacedKey(YTCraft.getPlugin(), "IsChatSpawned"),
        //         PersistentDataType.BOOLEAN, true);
        // itemStack.setItemMeta(meta);
        // // Give to streamer
        // if (PluginState.getStreamer() == null) {
        //     Bukkit.getLogger().warning("Cannot give item - streamer is null");
        //     return false;
        // }
        // // Try to add to inventory, drop if full
        // if (!PluginState.getStreamer().getInventory().addItem(itemStack).isEmpty()) {
        //     Location playerLocation = PluginState.getStreamer().getLocation();
        //     if (playerLocation != null) {
        //         PluginState.getStreamer().getWorld().dropItemNaturally(playerLocation, itemStack);
        //     }
        // }
        // // Send broadcast message
        // MobUtils.sendAuthorItemSpawnMessage(itemStack, author);
        // Bukkit.getLogger().info(String.format("%s gave %s x%d", author, material, count));
        // return true;
    }

}

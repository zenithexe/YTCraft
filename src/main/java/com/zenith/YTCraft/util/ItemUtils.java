package com.zenith.YTCraft.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.ItemGiveSettings;
import com.zenith.YTCraft.config.types.ItemGiveSettings.ItemAlias;
import com.zenith.YTCraft.data.ItemGiveState;

/**
 * Utility class for item operations
 */
public class ItemUtils {

    private static final int MAX_ITEM_COUNT = 64;

    /**
     * Check if an item can be given based on settings
     */
    public static boolean canGiveItem(String itemName) {
        return ItemGiveState.canGiveItem(itemName);
    }

    /**
     * Check if an alias exists
     */
    public static boolean isAlias(String aliasName) {
        return ItemGiveState.isAlias(aliasName);
    }

    /**
     * Give an item from alias to player
     */
    public static boolean giveAliasItem(Player player, String aliasName, String author) {
        ItemGiveSettings settings = SettingsLoader.getSettings().getItemGiveSettings();

        if (!settings.isEnabled()) {
            return false;
        }

        // Get validated alias from cache
        ItemAlias alias = ItemGiveState.getAlias(aliasName.toLowerCase());
        if (alias == null) {
            return false;
        }

        Material material = Material.getMaterial(alias.getItem().toUpperCase());
        if (material == null || !material.isItem()) {
            // This shouldn't happen since we validated, but safety check
            Bukkit.getLogger().warning(String.format("Invalid material in validated alias '%s': %s",
                    aliasName, alias.getItem()));
            return false;
        }

        // Alias quantity overwrites MAX_ITEM_COUNT
        ItemStack itemStack = new ItemStack(material, alias.getQty());

        // Apply enchantments (all are pre-validated)
        if (alias.getEnchantments() != null && !alias.getEnchantments().isEmpty()) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                for (ItemGiveSettings.Enchantment ench : alias.getEnchantments()) {
                    @SuppressWarnings("deprecation")
                    Enchantment enchantment = Enchantment.getByName(ench.getEnchantment().toUpperCase());
                    if (enchantment != null) {
                        meta.addEnchant(enchantment, ench.getLevel(), true);
                    }
                }
                itemStack.setItemMeta(meta);
            }
        }

        // Mark as chat spawned
        setItemNBT(itemStack, author);

        // Give to player
        giveItemToPlayer(player, itemStack);

        // Broadcast message
        MessageUtils.sendAuthorItemGiveMessage(itemStack, author);

        Bukkit.getLogger().info(String.format("%s gave alias '%s' (%s x%d)",
                author, aliasName, material, alias.getQty()));

        return true;
    }

    /**
     * Give a regular item to player
     */
    public static boolean giveItem(Player player, String itemName, int quantity, String author) {
        ItemGiveSettings settings = SettingsLoader.getSettings().getItemGiveSettings();

        if (!settings.isEnabled()) {
            return false;
        }

        itemName = itemName.toUpperCase();

        Material material = Material.getMaterial(itemName);
        if (material == null || !material.isItem()) {
            Bukkit.getLogger().info(String.format("Invalid material from %s: %s", author, itemName));
            return false;
        }

        // Check if item is allowed
        if (!canGiveItem(itemName)) {
            Bukkit.getLogger().info(String.format("Item %s is not allowed from %s", itemName, author));
            return false;
        }

        // Cap quantity at MAX_ITEM_COUNT for regular items
        quantity = Math.min(quantity, MAX_ITEM_COUNT);

        ItemStack itemStack = new ItemStack(material, quantity);
        setItemNBT(itemStack, author);
        giveItemToPlayer(player, itemStack);

        // Broadcast message
        MessageUtils.sendAuthorItemGiveMessage(itemStack, author);

        Bukkit.getLogger().info(String.format("%s gave %s x%d", author, itemName, quantity));

        return true;
    }

    /**
     * Set NBT data on item to mark it as chat-spawned
     */
    public static void setItemNBT(ItemStack itemStack, String author) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(YTCraft.getPlugin(), "IsChatSpawned"),
                    PersistentDataType.BOOLEAN, true);
            data.set(new NamespacedKey(YTCraft.getPlugin(), "SpawnedBy"),
                    PersistentDataType.STRING, author);
            itemStack.setItemMeta(meta);
        }
    }

    /**
     * Give item to player, drop if inventory full
     */
    @SuppressWarnings("null")
    private static void giveItemToPlayer(Player player, ItemStack itemStack) {
        if (player == null) {
            Bukkit.getLogger().warning("Cannot give item - player is null");
            return;
        }

        // Try to add to inventory, drop if full
        if (!player.getInventory().addItem(itemStack).isEmpty()) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        }
    }
}

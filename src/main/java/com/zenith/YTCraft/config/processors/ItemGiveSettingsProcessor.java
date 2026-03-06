package com.zenith.YTCraft.config.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import com.zenith.YTCraft.config.types.ItemGiveSettings;
import com.zenith.YTCraft.config.types.ItemGiveSettings.ItemAlias;
import com.zenith.YTCraft.data.ItemGiveState;

/**
 * Processes ItemGiveSettings into runtime state
 */
public class ItemGiveSettingsProcessor {

    /**
     * Process item give settings into runtime state
     */
    public static void process(ItemGiveSettings settings) {
        if (settings == null) {
            Bukkit.getLogger().warning("ItemGiveSettings is null, skipping processing");
            return;
        }

        // Build and cache banned items set
        Set<String> bannedSet = buildItemSet(settings.getBannedItems());
        ItemGiveState.setBannedItems(bannedSet);

        // Build and cache allowed items set
        Set<String> allowedSet = buildItemSet(settings.getAllowedItems());
        ItemGiveState.setAllowedItems(allowedSet);

        // Build and cache validated aliases
        Map<String, ItemAlias> validatedAliases = buildValidatedAliases(settings.getItemAliases());
        ItemGiveState.setValidatedAliases(validatedAliases);
    }

    /**
     * Convert list to uppercase set for fast lookups
     */
    private static Set<String> buildItemSet(List<String> items) {
        Set<String> itemSet = new HashSet<>();

        if (items != null) {
            for (String item : items) {
                itemSet.add(item.toUpperCase());
            }
        }

        return itemSet;
    }

    /**
     * Build validated alias map, filtering out invalid materials and enchantments
     */
    private static Map<String, ItemAlias> buildValidatedAliases(Map<String, ItemAlias> aliases) {
        Map<String, ItemAlias> validatedMap = new HashMap<>();

        if (aliases == null || aliases.isEmpty()) {
            return validatedMap;
        }

        for (Map.Entry<String, ItemAlias> entry : aliases.entrySet()) {
            String aliasName = entry.getKey().toLowerCase();
            ItemAlias alias = entry.getValue();

            // Validate material
            Material material = Material.getMaterial(alias.getItem().toUpperCase());

            if (material == null || !material.isItem()) {
                Bukkit.getLogger().warning(String.format(
                        "[ItemGive] Skipping alias '%s': Invalid material '%s'",
                        aliasName, alias.getItem()));
                continue;
            }

            // Create a clean alias with validated enchantments
            ItemAlias cleanAlias = createCleanAlias(alias, aliasName);

            // Add to validated map
            validatedMap.put(aliasName, cleanAlias);

            Bukkit.getLogger().info(String.format(
                    "[ItemGive] Loaded alias: '%s' -> %s x%d with %d enchantment(s)",
                    aliasName, material, cleanAlias.getQty(),
                    cleanAlias.getEnchantments() != null ? cleanAlias.getEnchantments().size() : 0));
        }

        return validatedMap;
    }

    /**
     * Create a clean alias with only valid enchantments
     */
    private static ItemAlias createCleanAlias(ItemAlias original, String aliasName) {
        ItemAlias cleanAlias = new ItemAlias();
        cleanAlias.setItem(original.getItem());
        cleanAlias.setQty(original.getQty());

        // Filter enchantments
        if (original.getEnchantments() != null && !original.getEnchantments().isEmpty()) {
            List<ItemGiveSettings.Enchantment> validEnchantments = new ArrayList<>();

            for (ItemGiveSettings.Enchantment ench : original.getEnchantments()) {
                @SuppressWarnings("deprecation")
                Enchantment enchantment = Enchantment.getByName(ench.getEnchantment().toUpperCase());

                if (enchantment != null) {
                    validEnchantments.add(ench);
                } else {
                    Bukkit.getLogger().warning(String.format(
                            "[ItemGive] Skipping invalid enchantment '%s' in alias '%s'",
                            ench.getEnchantment(), aliasName));
                }
            }

            cleanAlias.setEnchantments(validEnchantments);
        } else {
            cleanAlias.setEnchantments(new ArrayList<>());
        }

        return cleanAlias;
    }
}

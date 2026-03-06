package com.zenith.YTCraft.config.types;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class ItemGiveSettings {

    private boolean enabled;
    private ModeSettings mode;
    private List<String> banned_items;
    private List<String> allowed_items;
    private Map<String, ItemAlias> item_aliases;

    public boolean isEnabled() {
        return enabled;
    }

    public ModeSettings getMode() {
        return mode;
    }

    public List<String> getBannedItems() {
        return banned_items;
    }

    public List<String> getAllowedItems() {
        return allowed_items;
    }

    public Map<String, ItemAlias> getItemAliases() {
        return item_aliases;
    }

    // Setters
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setMode(ModeSettings mode) {
        this.mode = mode;
    }

    public void setBannedItems(List<String> bannedItems) {
        this.banned_items = bannedItems;
    }

    public void setAllowedItems(List<String> allowedItems) {
        this.allowed_items = allowedItems;
    }

    public void setItemAliases(Map<String, ItemAlias> itemAliases) {
        this.item_aliases = itemAliases;
    }

    public static class ItemAlias {

        private String item;
        private int qty;
        private List<Enchantment> enchantments;

        public String getItem() {
            return item;
        }

        public int getQty() {
            return qty;
        }

        public List<Enchantment> getEnchantments() {
            return enchantments;
        }

        // Setters
        public void setItem(String item) {
            this.item = item;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public void setEnchantments(List<Enchantment> enchantments) {
            this.enchantments = enchantments;
        }
    }

    public static class Enchantment {

        private String enchantment;
        private int level;

        public String getEnchantment() {
            return enchantment;
        }

        public int getLevel() {
            return level;
        }

        // Setters
        public void setEnchantment(String enchantment) {
            this.enchantment = enchantment;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }

    public static enum ModeSettings {
        @SerializedName("all")
        ALL,
        @SerializedName("allowed_only")
        ALLOWED_ONLY
    }

}

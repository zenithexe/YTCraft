package com.zenith.YTCraft.chatactions.actions;

import org.bukkit.Bukkit;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.chatactions.ChatAction;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.util.ItemUtils;

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
        return "Give an item to the streamer. Usage: !give <item|alias> [count]";
    }

    @Override
    public boolean execute(LiveChatMessage message, String[] args) {

        if (!SettingsLoader.getSettings().getItemGiveSettings().isEnabled()) {
            Bukkit.getLogger().info(String.format(" :: Item-Giving is Disabled"));
            return false;
        }

        String author = message.getAuthorDetails().getDisplayName();

        // Validate arguments: give <item|alias> [count]
        if (args.length < 2) {
            Bukkit.getLogger().info(String.format("Invalid give from %s - wrong number of arguments", author));
            return false;
        }

        String itemOrAlias = args[1];

        if (PluginState.getStreamer() == null) {
            Bukkit.getLogger().warning("Cannot give item - streamer is null");
            return false;
        }

        // Check if it's an alias (case-insensitive)
        if (ItemUtils.isAlias(itemOrAlias)) {
            return ItemUtils.giveAliasItem(PluginState.getStreamer(), itemOrAlias, author);
        }

        // Regular item
        int quantity = 1;
        if (args.length >= 3) {
            try {
                quantity = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                Bukkit.getLogger().info(String.format("Invalid count from %s: %s", author, args[2]));
                return false;
            }
        }

        return ItemUtils.giveItem(PluginState.getStreamer(), itemOrAlias, quantity, author);
    }

}

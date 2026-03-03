package com.zenith.YTCraft.chatactions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.chatactions.actions.GiveItemAction;
import com.zenith.YTCraft.chatactions.actions.SpawnMobAction;

/**
 * Handles registration and execution of YouTube chat actions
 */
public class ChatActionHandler {

    private static final Map<String, ChatAction> Chat_Actions_Reg = new HashMap<>();

    static {
        // Register all chat actions
        registerChatAction(new SpawnMobAction());
        registerChatAction(new GiveItemAction());
    }

    // Register a chat action
    public static void registerChatAction(ChatAction action) {

        Chat_Actions_Reg.put(action.getKey().toLowerCase(), action);
        Bukkit.getLogger().info(String.format("Registered Chat Action: %s", action.getKey()));
    }

    // Get an action by trigger
    public static ChatAction getChatAction(String key) {
        return Chat_Actions_Reg.get(key.toLowerCase());
    }

    /**
     * ================ Handler =================
     */
    public static boolean handler(LiveChatMessage message) {

        String text = message.getSnippet().getDisplayMessage();

        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        // Parse trigger and arguments
        String[] args = text.trim().split("\\s+"); //Split by 'space'
        String key = args[0].toLowerCase();

        // Get action
        ChatAction action = getChatAction(key);

        if (action == null) {
            return false;
        }

        // Execute action
        try {
            return action.execute(message, args);
        } catch (Exception e) {

            Bukkit.getLogger().warning(String.format("Error executing action '%s' : %s", key, e.getMessage()));
            return false;
        }
    }
}

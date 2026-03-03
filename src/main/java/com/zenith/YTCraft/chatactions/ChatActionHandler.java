package com.zenith.YTCraft.chatactions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.chatactions.actions.GiveItemAction;
import com.zenith.YTCraft.chatactions.actions.SpawnMobAction;

/**
 * Handles registration and execution of YouTube chat actions
 */
public class ChatActionHandler {

    private static final Map<String, ChatAction> chatActionsReg = new HashMap<>();

    static {
        // Register all chat actions
        registerChatAction(new SpawnMobAction());
        registerChatAction(new GiveItemAction());
    }

    // Register a chat action
    public static void registerChatAction(ChatAction action) {
        chatActionsReg.put(action.getTrigger().toLowerCase(), action);

        Bukkit.getLogger().info("Registered chat action: " + action.getTrigger());
    }

    // Get an action by trigger
    public static ChatAction getChatAction(String trigger) {
        return chatActionsReg.get(trigger.toLowerCase());
    }

    // Check if an action exists
    public static boolean hasChatAction(String trigger) {
        return chatActionsReg.containsKey(trigger.toLowerCase());
    }

    // Get all registered action triggers    
    public static Set<String> getChatActionTriggers() {
        return chatActionsReg.keySet();
    }

    /**
     * Process a chat message and execute action if found
     *
     * @return true if an action was found and executed
     */
    public static boolean processMessage(LiveChatMessage message, int viewers) {
        String text = message.getSnippet().getDisplayMessage();

        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        // Parse trigger and arguments
        String[] args = text.trim().split("\\s+");
        String trigger = args[0].toLowerCase();

        // Get action
        ChatAction action = getChatAction(trigger);

        if (action == null) {
            return false; // Not an action
        }

        // Execute action
        try {
            return action.execute(message, args, viewers);
        } catch (Exception e) {

            Bukkit.getLogger().warning("Error executing action '" + trigger + "': " + e.getMessage());

            e.printStackTrace();

            return false;
        }
    }
}

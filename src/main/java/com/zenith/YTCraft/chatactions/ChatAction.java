package com.zenith.YTCraft.chatactions;

import com.google.api.services.youtube.model.LiveChatMessage;

/**
 * Interface for YouTube chat actions/interactions Represents actions that
 * viewers can trigger through YouTube chat
 */
public interface ChatAction {

    /**
     * Get the trigger keyword (e.g., "spawn", "give")
     */
    String getKey();

    String[] getAliases();

    /**
     * Get description of what this action does
     */
    String getDescription();

    /**
     * Execute the action based on chat message
     *
     * @param message The YouTube chat message
     * @param args Message split by spaces (including trigger at index 0)
     * @param viewers Current viewer count
     * @return true if action executed successfully
     */
    boolean execute(LiveChatMessage message, String[] args);

}

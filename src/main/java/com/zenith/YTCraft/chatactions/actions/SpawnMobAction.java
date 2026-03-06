package com.zenith.YTCraft.chatactions.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.chatactions.ChatAction;
import com.zenith.YTCraft.data.MobSpawnState;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.mechanics.MobSpawning;

/**
 * Handles mob spawning from YouTube chat Viewers type "spawn <mob>" to spawn a
 * mob
 */
public class SpawnMobAction implements ChatAction {

    @Override
    public String getKey() {
        return "spawn";
    }

    @Override
    public String getDescription() {
        return "Spawn a mob in the game";
    }

    @Override
    public boolean execute(LiveChatMessage message, String[] args) {

        int viewers = PluginState.getViewers();

        String author = message.getAuthorDetails().getDisplayName();
        String channelId = message.getAuthorDetails().getChannelId();
        String text = message.getSnippet().getDisplayMessage();

        // Validate arguments
        if (args.length != 2) {
            Bukkit.getLogger().info(String.format("Chat Action Error :: Invalid Format :: %s >> %s", author, text));
            return false;
        }

        // Parse entity type
        EntityType entityType;

        try {

            entityType = EntityType.valueOf(args[1].toUpperCase());

        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().info(String.format("Invalid entity type from %s: %s", author, args[1]));
            return false;
        }

        // Check if user already has a mob spawned (unless low viewers bypass)
        if (MobSpawnState.getChannelIdToAuthorMob().containsKey(channelId) && viewers > 10) {
            Bukkit.getLogger().info(String.format("%s already has a mob spawned", author));
            return false;
        }

        // Check viewer requirements for this entity type
        if (!MobSpawnState.isMobSpawnable(entityType)) {
            Bukkit.getLogger().info(String.format("Insufficient Viewers :: %s >> %s ", author, entityType));
            return false;
        }

        // Add to spawn queue
        MobSpawning.addMob(entityType, author, channelId);
        Bukkit.getLogger().info(String.format("%s queued %s for spawning", author, entityType));

        return true;
    }

}

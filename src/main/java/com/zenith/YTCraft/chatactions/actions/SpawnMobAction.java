package com.zenith.YTCraft.chatactions.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.chatactions.ChatAction;
import com.zenith.YTCraft.data.MobManager;
import com.zenith.YTCraft.mechanics.MobSpawning;
import com.zenith.YTCraft.util.MobUtils;

/**
 * Handles mob spawning from YouTube chat
 * Viewers type "spawn <mob>" to spawn a mob
 */
public class SpawnMobAction implements ChatAction {

    @Override
    public String getTrigger() {
        return "spawn";
    }

    @Override
    public boolean execute(LiveChatMessage message, String[] args, int viewers) {
        String author = message.getAuthorDetails().getDisplayName();
        String channelId = message.getAuthorDetails().getChannelId();

        // Validate arguments
        if (args.length != 2) {
            Bukkit.getLogger().info("Invalid spawn from " + author + " - wrong number of arguments");
            return false;
        }

        // Parse entity type
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().info("Invalid entity type from " + author + ": " + args[1]);
            return false;
        }

        // Check if user already has a mob spawned (unless low viewers bypass)
        if (MobManager.getChannelIdToAuthorMob().containsKey(channelId) && viewers > 10) {
            Bukkit.getLogger().info(author + " already has a mob spawned");
            return false;
        }

        // Check viewer requirements for this entity type
        if (!MobUtils.isEntityType_To_NViewers(args, viewers)) {
            Bukkit.getLogger().info(author + " doesn't meet viewer requirement for " + entityType);
            return false;
        }

        // Add to spawn queue
        MobSpawning.addMob(entityType, author, channelId);
        Bukkit.getLogger().info(author + " queued " + entityType + " for spawning");
        
        return true;
    }

    @Override
    public String getDescription() {
        return "Spawn a mob in the game";
    }
}

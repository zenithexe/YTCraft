package com.zenith.YTCraft.chatactions.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.chatactions.ChatAction;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.custommobs.CustomMobRegistry;
import com.zenith.YTCraft.data.MobSpawnState;
import com.zenith.YTCraft.mechanics.MobSpawning;
import com.zenith.YTCraft.types.AuthorMob;
import com.zenith.YTCraft.util.MobUtils;

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
    public String[] getAliases() {
        return new String[]{"spn", "spwn", "summon", "sp"};
    }

    @Override
    public String getDescription() {
        return "Spawn a mob in the game";
    }

    @Override
    public boolean execute(LiveChatMessage message, String[] args) {

        if (!SettingsLoader.getSettings().getMobSpawnSettings().isEnabled()) {
            Bukkit.getLogger().info(String.format(" No Mob Spawned :: Mob Spawn is Disabled"));
            return false;
        }

        String author = message.getAuthorDetails().getDisplayName();
        String channelId = message.getAuthorDetails().getChannelId();
        String text = message.getSnippet().getDisplayMessage();

        // Validate arguments
        if (args.length < 2) {
            Bukkit.getLogger().info(String.format("Spawn Mob Action Error :: Invalid Format >> %s :: %s", author, text));
            return false;
        }

        String mobName = args[1].toLowerCase();

        // Check if user already has a mob spawned - if so, kill it first
        // Check if it's a custom mob first
        if (CustomMobRegistry.isCustomMob(mobName)) {

            // Check if custom mobs are enabled
            if (!SettingsLoader.getSettings().getCustomMobSettings().isEnabled()) {
                Bukkit.getLogger().info(String.format(" :: Custom Mob Spawn is Disabled"));
                return false;
            }

            CustomMob customMob = CustomMobRegistry.getCustomMob(mobName);

            // Check viewer requirements for custom mob
            Bukkit.getLogger().info(String.format("Custom Mob >> %s :: %s [%s]", author, mobName, customMob.getEntityType()));
            if (!MobSpawnState.isMobSpawnable(customMob.getMobKey())) {
                return false;
            }

            // If viewer has already a mob spawned
            if (MobSpawnState.getChannelIdToAuthorMob().containsKey(channelId)) {
                AuthorMob existingMob = MobSpawnState.getChannelIdToAuthorMob().get(channelId);
                MobUtils.killAuthorMob(existingMob);
            }

            // Add custom mob to spawn queue
            MobSpawning.addCustomMob(customMob, author, channelId);
            Bukkit.getLogger().info(String.format("%s :: %s [%s] queued for spawning", author, mobName, customMob.getEntityType()));
            return true;
        }

        // Not a custom mob, try regular entity type
        EntityType entityType;

        try {
            entityType = EntityType.valueOf(mobName.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().info(String.format("Invalid entity type from %s: %s", author, mobName));
            return false;
        }

        // Check if the entity type is a living entity
        if (!LivingEntity.class.isAssignableFrom(entityType.getEntityClass())) {
            Bukkit.getLogger().info(String.format("Invalid entity type (not a living entity) from %s: %s", author, mobName));
            return false;
        }

        // Check viewer requirements for this entity type
        Bukkit.getLogger().info(String.format("Mob Spawn >> %s :: %s ", author, entityType));
        if (!MobSpawnState.isMobSpawnable(entityType.toString())) {
            return false;
        }

        // If viewer has already a mob spawned
        if (MobSpawnState.getChannelIdToAuthorMob().containsKey(channelId)) {
            AuthorMob existingMob = MobSpawnState.getChannelIdToAuthorMob().get(channelId);
            MobUtils.killAuthorMob(existingMob);
        }

        // Add to spawn queue
        MobSpawning.addMob(entityType, author, channelId);
        Bukkit.getLogger().info(String.format("%s :: %s queued for spawning", author, entityType));

        return true;
    }

}

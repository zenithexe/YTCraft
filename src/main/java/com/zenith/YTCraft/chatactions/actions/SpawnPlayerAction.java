package com.zenith.YTCraft.chatactions.actions;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.chatactions.ChatAction;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.data.MobSpawnState;
import com.zenith.YTCraft.mechanics.MobSpawning;
import com.zenith.YTCraft.types.AuthorMob;
import com.zenith.YTCraft.util.MobUtils;

public class SpawnPlayerAction implements ChatAction {

    @Override
    public String getKey() {
        return "spawnplayer";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"spawnp", "spp", "spnply", "spawnme", "spme", "spwnme"};
    }

    @Override
    public String getDescription() {
        return "Spawn a user in the game";
    }

    @Override
    public boolean execute(LiveChatMessage message, String[] args) {

        //Checking is Mob-Spawn is enabled
        if (!SettingsLoader.getSettings().getMobSpawnSettings().isEnabled()) {
            Bukkit.getLogger().info(String.format(" :: Mob Spawn is Disabled."));
        }

        // Check if username spawns are enabled
        if (!SettingsLoader.getSettings().getCustomMobSettings().isUsernameSpawnsEnabled()) {
            Bukkit.getLogger().info(String.format(" :: Username-Spawn is Disabled"));
            return false;
        }

        String author = message.getAuthorDetails().getDisplayName();
        String channelId = message.getAuthorDetails().getChannelId();
        String text = message.getSnippet().getDisplayMessage();

        if (args.length < 2) {
            Bukkit.getLogger().info(String.format("Spawn Player Action Error :: Invalid Format >> %s :: %s", author, text));
            return false;
        }

        String playerName = args[1];

        EntityType[] PlayerMobTypes = {EntityType.ZOMBIE, EntityType.SKELETON, EntityType.PILLAGER, EntityType.VINDICATOR};
        EntityType rEntityType = PlayerMobTypes[new Random().nextInt(PlayerMobTypes.length)];

        CustomMob customMob = new CustomMob("__PLAYER_MOB__", playerName, rEntityType, playerName, null);

        // If viewer has already a mob spawned
        if (MobSpawnState.getChannelIdToAuthorMob().containsKey(channelId)) {
            AuthorMob existingMob = MobSpawnState.getChannelIdToAuthorMob().get(channelId);
            MobUtils.killAuthorMob(existingMob);
        }

        MobSpawning.addCustomMob(customMob, author, channelId);
        Bukkit.getLogger().info(String.format("%s :: %s [%s] queued for spawning", author, playerName, customMob.getEntityType()));

        return true;
    }

}

package com.zenith.YTCraft.chatactions.actions;

import org.bukkit.Bukkit;

import com.google.api.services.youtube.model.LiveChatMessage;
import com.zenith.YTCraft.chatactions.ChatAction;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.data.MobSpawnState;
import com.zenith.YTCraft.types.AuthorMob;
import com.zenith.YTCraft.util.MessageUtils;
import com.zenith.YTCraft.util.MobUtils;

/**
 * Handles killing/despawning viewer's own spawned mob Viewers type "kill" to
 * remove their spawned mob
 */
public class DeSpawnMobAction implements ChatAction {

    @Override
    public String getKey() {
        return "despawn";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"dspw", "despwn", "dspwn", "kill", "kil"};
    }

    @Override
    public String getDescription() {
        return "Despawn your spawned mob";
    }

    @Override
    public boolean execute(LiveChatMessage message, String[] args) {

        String author = message.getAuthorDetails().getDisplayName();
        String channelId = message.getAuthorDetails().getChannelId();

        // Check if feature is enabled
        if (!SettingsLoader.getSettings().getMobSpawnSettings().isAllowViewerMobKill()) {
            Bukkit.getLogger().info(String.format("Viewer mob kill disabled :: %s attempted to kill mob", author));
            return false;
        }

        // Check if viewer has a spawned mob
        AuthorMob authorMob = MobSpawnState.getChannelIdToAuthorMob().get(channelId);

        if (authorMob == null) {
            Bukkit.getLogger().info(String.format("%s has no spawned mob to kill", author));
            return false;
        }

        MobUtils.killAuthorMob(authorMob);

        if (authorMob.isCustomMob() && authorMob.getCustomMob() != null) {
            MessageUtils.sendAuthorCustomMobDespawnMessage(authorMob.getCustomMob(), author);
        }
        else{

            MessageUtils.sendAuthorMobDespawnMessage(authorMob.getMob(), author);
        }
        // Broadcast despawn message

        Bukkit.getLogger().info(String.format("%s killed their spawned %s", author, authorMob.getMob().getType()));

        return true;
    }

}

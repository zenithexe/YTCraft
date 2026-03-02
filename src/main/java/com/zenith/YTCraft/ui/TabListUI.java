package com.zenith.YTCraft.ui;

import org.bukkit.entity.Player;

import com.zenith.YTCraft.data.MobManager;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.types.AuthorMob;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class TabListUI {

    public static void updateHeaderTabList() {
        Player streamer = PluginState.getStreamer();
        if (streamer == null) return;

        Component header = Component.text("Spawned Mobs :: 0").color(NamedTextColor.GREEN);

        if (!MobManager.getChannelIdToAuthorMob().isEmpty()) {
            header = Component.text("Spawned Mobs :: " + MobManager.getChannelIdToAuthorMob().size())
                    .color(NamedTextColor.GREEN);
        }
        
        streamer.sendPlayerListHeader(header);
    }

    public static void updateFooterTabList() {
        Player streamer = PluginState.getStreamer();
        if (streamer == null) return;

        if (MobManager.getChannelIdToAuthorMob().isEmpty()) {
            streamer.sendPlayerListFooter(Component.empty());
            return;
        }

        Component footer = Component.text("\n")
                .append(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━").color(NamedTextColor.DARK_GRAY))
                .append(Component.text("\n"))
                .append(Component.text("  Active Viewer Mobs").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD))
                .append(Component.text("\n"));

        int index = 0;
        for (AuthorMob authorMob : MobManager.getChannelIdToAuthorMob().values()) {
            String author = authorMob.getAuthor();
            String mobType = authorMob.getMob().getType().toString();

            // Alternate colors for better readability
            NamedTextColor authorColor = (index % 2 == 0) ? NamedTextColor.YELLOW : NamedTextColor.AQUA;

            footer = footer.append(Component.text("\n  ● ").color(NamedTextColor.GREEN))
                    .append(Component.text(author).color(authorColor).decorate(TextDecoration.BOLD))
                    .append(Component.text(" → ").color(NamedTextColor.DARK_GRAY))
                    .append(Component.text(mobType).color(NamedTextColor.WHITE));

            index++;
        }

        footer = footer.append(Component.text("\n"))
                .append(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━").color(NamedTextColor.DARK_GRAY))
                .append(Component.text("\n"));

        streamer.sendPlayerListFooter(footer);
    }

}

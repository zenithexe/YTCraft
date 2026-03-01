package com.zenith.YTCraft.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.data.MobManager;
import com.zenith.YTCraft.data.PluginState;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class TabList {

    private static List<NamedTextColor> colors = new ArrayList<NamedTextColor>() {
        {
            add(NamedTextColor.YELLOW);
            add(NamedTextColor.RED);
            add(NamedTextColor.GREEN);
        }
    };

    public static void updateHeaderTabList() {
        Component header = Component.text("Spawned Mobs :: 0").color(NamedTextColor.GREEN);
        if (!MobManager.getChannelIdToAuthorMobMap().isEmpty()) {
            header = Component.text("Spawned Mobs :: " + MobManager.getChannelIdToAuthorMobMap().values().toArray().length)
                    .color(NamedTextColor.GREEN);
        }
        YTCraft.getPlugin().adventure().player(PluginState.getStreamer()).sendPlayerListHeader(header);
    }

    public static void updateFooterTabList() {
        Component footer = Component.text("");

        if (!MobManager.getChannelIdToAuthorMobMap().isEmpty()) {
            String dataString = MobManager.getChannelIdToAuthorMobMap().values().toString();
            int length = dataString.length();
            dataString = dataString.substring(1, length - 1);

            int i = 0;
            String AuthorMobs[] = dataString.split(",");
            Bukkit.getLogger().info(Arrays.toString(AuthorMobs));

            for (String authorMob : AuthorMobs) {
                String displayString = authorMob.trim();
                Bukkit.getLogger().info("DisplayString >>>>>>>>>>>>>>>>> :: " + displayString);

                displayString = displayString.substring(1, displayString.length() - 1);
                Bukkit.getLogger().info("DisplayString After Sub >>>>>>>>>>>>>>>>> :: " + displayString);

                String[] elements = displayString.split("=");

                footer = footer.append(
                        Component.text(elements[0].trim()).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));
                footer = footer.append(Component.text(" : "));
                footer = footer.append(Component.text(elements[1].trim()).color(NamedTextColor.WHITE));

                if (i == AuthorMobs.length - 1) {
                    continue;
                }
                footer = footer.append(Component.text(" || ").color(NamedTextColor.RED));
                i++;
            }
        }
        YTCraft.getPlugin().adventure().player(PluginState.getStreamer()).sendPlayerListFooter(footer);
    }
}

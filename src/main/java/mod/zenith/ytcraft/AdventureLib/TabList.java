package mod.zenith.ytcraft.AdventureLib;

import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YTCraft;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabList {

    private static List<NamedTextColor> colors = new ArrayList<NamedTextColor>(){{
       add(NamedTextColor.YELLOW);
       add(NamedTextColor.RED);
       add(NamedTextColor.GREEN);
    }};

    public static void updateHeaderTabList(){
        Component header = Component.text("Spawned Mobs :: 0").color(NamedTextColor.GREEN);
        if(!Data.ChannelId_To_AuthorMob_List.isEmpty()){
            header = Component.text("Spawned Mobs :: "+Data.ChannelId_To_AuthorMob_List.values().toArray().length).color(NamedTextColor.GREEN);
        }
        YTCraft.getPlugin().adventure().player(Data.streamer).sendPlayerListHeader(header);
    }
    public static void updateFooterTabList(){
        Component footer = Component.text("");

        if(!Data.ChannelId_To_AuthorMob_List.isEmpty()){
            String dataString = Data.ChannelId_To_AuthorMob_List.values().toString();
            int length = dataString.length();
            dataString = dataString.substring(1,length-1);

            int i = 0;
            String AuthorMobs[] = dataString.split(",");
            Bukkit.getLogger().info(Arrays.toString(AuthorMobs));

            for(String authorMob: AuthorMobs){
                String displayString= authorMob.trim();
                Bukkit.getLogger().info("DisplayString >>>>>>>>>>>>>>>>> :: "+ displayString);

                displayString = displayString.substring(1,displayString.length()-1);
                Bukkit.getLogger().info("DisplayString After Sub >>>>>>>>>>>>>>>>> :: "+ displayString);

                String[] elements = displayString.split("=");

                footer=footer.append(Component.text(elements[0].trim()).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));
                footer=footer.append(Component.text(" : "));
                footer=footer.append(Component.text(elements[1].trim()).color(NamedTextColor.WHITE));

                if(i==AuthorMobs.length-1){
                    continue;
                }
                footer=footer.append(Component.text(" || ").color(NamedTextColor.RED));
                i++;
            }
        }
        YTCraft.getPlugin().adventure().player(Data.streamer).sendPlayerListFooter(footer);
    }
}

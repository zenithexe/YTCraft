package mod.zenith.ytcraft.AdventureLib;

import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YTCraft;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class TabList {

    public static void updateHeaderTabList(){
        Component header = Component.text("").color(NamedTextColor.GREEN);
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

            for(String am: AuthorMobs){
                if(i%2==0){
                    footer=footer.append(Component.text(am).color(NamedTextColor.RED));
                }
                else{
                    footer=footer.append(Component.text(am).color(NamedTextColor.BLUE));
                }
                i++;
            }


//            footer = Component.text(Data.ChannelId_To_AuthorMob_List.values().toString()).color(NamedTextColor.GREEN);
        }

        YTCraft.getPlugin().adventure().player(Data.streamer).sendPlayerListFooter(footer);
    }
}

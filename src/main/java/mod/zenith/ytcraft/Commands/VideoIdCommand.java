package mod.zenith.ytcraft.Commands;

import mod.zenith.ytcraft.YTCraft;
import mod.zenith.ytcraft.YoutubeAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VideoIdCommand implements CommandExecutor, TabExecutor {



    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args) {

        if(args[1]!=null && args.length==2){
            YTCraft.getPlugin().getConfig().set("VIDEO_ID",args[1]);
            YTCraft.getPlugin().saveConfig();

            YoutubeAPI.updateVideoId(args[1]);

            sender.sendMessage("Video Id set to "+args[1]);
            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length==1){
            return Arrays.asList("set");
        }

        return new ArrayList<>();
    }
}

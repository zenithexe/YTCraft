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


public class YTConfigCommand implements CommandExecutor, TabExecutor {



    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args) {

        if(args[0].equalsIgnoreCase("videoid") && args.length==2){
            YTCraft.getPlugin().getConfig().set("VIDEO_ID",args[1]);
            YTCraft.getPlugin().saveConfig();

            YoutubeAPI.updateVideoId(args[1]);

            sender.sendMessage("Video Id set to "+args[1]);
            return true;
        }

        if(args[0].equalsIgnoreCase("timer") && args.length>=2){
            if(args[1].equalsIgnoreCase("enable")){
                sender.sendMessage("Timer-Enaled");
                return true;
            }

            if(args[1].equalsIgnoreCase("disable")){
                sender.sendMessage("Timer-Disabled");
                return true;
            }

            if(args[1].equalsIgnoreCase("setRestTime") && args.length==4){
                sender.sendMessage("setRestTimer "+args[2]+args[3]);
                return true;
            }

            if(args[1].equalsIgnoreCase("setActiveTime") && args.length==4){
                sender.sendMessage("setActiveTimer "+args[2]+args[3]);
                return true;
            }

            if(args[1].equalsIgnoreCase("skip")){
                sender.sendMessage("Skip this!");
                return true;
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length==1){
            return Arrays.asList("videoId","timer");
        }

        if(args.length==2){
            if(args[0].equals("timer")){
                return Arrays.asList("enable","disable","setRestTime","setActiveTime","skip");
            }
        }

        return new ArrayList<>();
    }
}

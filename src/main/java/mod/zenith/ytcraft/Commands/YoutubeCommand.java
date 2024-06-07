package mod.zenith.ytcraft.Commands;

import mod.zenith.ytcraft.YTCraft;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import mod.zenith.ytcraft.ChatSpawn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YoutubeCommand implements CommandExecutor, TabExecutor {

    private static BukkitTask YoutubeTask;
    private static boolean isYoutubeTaskActive=false;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        if (commandSender instanceof Player){
            if (args.length==1 && args[0].equalsIgnoreCase("start")){
                if(!isYoutubeTaskActive){
                    YoutubeTask =  Bukkit.getScheduler().runTaskTimer(YTCraft.getPlugin(),new ChatSpawn(), 20L,20L*10);
                    ChatSpawn.streamer = (Player) commandSender;
                    Bukkit.broadcastMessage("Session Successfully Started.");
                    Bukkit.broadcastMessage(commandSender.getName()+" has been set as Streamer.");
                    isYoutubeTaskActive = true;
                }
                else{
                    Bukkit.broadcastMessage("Already Running.");
                }
                return true;

            }
            else if(args.length==1 && args[0].equals("end")){
                if(isYoutubeTaskActive){
                    YoutubeTask.cancel();
                    Bukkit.broadcastMessage("Session Successfully Ended.");
                }
                else{
                    Bukkit.broadcastMessage("No Running session.");
                }
                return true;
            }
        }
        else{
            commandSender.sendMessage("Only a Players can execute this command");
            return true;
        }
        
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return Arrays.asList("start","end");
        }

        return new ArrayList<>();
    }
}

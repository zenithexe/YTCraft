package mod.zenith.ytcraft.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mod.zenith.ytcraft.AdventureLib.TabList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mod.zenith.ytcraft.Board.BlankBoard;
import mod.zenith.ytcraft.Board.Board;
import mod.zenith.ytcraft.ChatControl;
import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.Timer.PluginTimer;
import mod.zenith.ytcraft.YTCraft;
import sun.tools.jconsole.Tab;

public class YoutubeCommand implements CommandExecutor, TabExecutor {

    private static BukkitTask YoutubeTask;
    private static BukkitTask TimerTask;
    private static boolean isYoutubeTaskActive=false;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) commandSender;

        if (commandSender instanceof Player){
            if (args.length==1 && args[0].equalsIgnoreCase("start")){
                if(!isYoutubeTaskActive) {
                    Data.streamer = (Player) commandSender;

                    YoutubeTask = Bukkit.getScheduler().runTaskTimer(YTCraft.getPlugin(), new ChatControl(), 20L, 20L * 10);
                    TimerTask = Bukkit.getScheduler().runTaskTimer(YTCraft.getPlugin(), new PluginTimer(), 0, 20);


                    Bukkit.broadcastMessage("Session Successfully Started.");
                    Bukkit.broadcastMessage(commandSender.getName() + " has been set as Streamer.");
                    isYoutubeTaskActive = true;

                    Board.createNewScoreBoard(Data.streamer);

                }
                else{
                    Bukkit.broadcastMessage("Already Running.");
                }
                return true;

            }
            else if(args.length==1 && args[0].equals("end")){
                if(isYoutubeTaskActive){

                    YoutubeTask.cancel();
                    TimerTask.cancel();
                    ChatControl.setTimeStamp(null);

                    BlankBoard.createBlankBoard();
                    Bukkit.broadcastMessage("Session Successfully Ended.");
                    isYoutubeTaskActive = false;
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

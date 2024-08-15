package mod.zenith.ytcraft.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mod.zenith.ytcraft.App.MobQueueSpawning;
import mod.zenith.ytcraft.App.SubscribeSpawnMechanics;
import mod.zenith.ytcraft.App.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mod.zenith.ytcraft.Board.BlankBoard;
import mod.zenith.ytcraft.Board.Board;
import mod.zenith.ytcraft.App.ChatControl;
import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.Timer.PluginTimer;
import mod.zenith.ytcraft.YTCraft;


public class YTCraftCommand implements CommandExecutor, TabExecutor {

    private static BukkitTask YoutubeTask;
    private static BukkitTask TimerTask;
    private static BukkitTask MobSpawnTask;
    private static boolean isYoutubeTaskActive=false;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) commandSender;

        if (commandSender instanceof Player) {
            if (args.length==1 && args[0].equalsIgnoreCase("start")){
                if(!isYoutubeTaskActive) {
                    Data.streamer = (Player) commandSender;

                    YoutubeTask = Bukkit.getScheduler().runTaskTimer(YTCraft.getPlugin(), new ChatControl(), 0, 20L * 3);
                    TimerTask = Bukkit.getScheduler().runTaskTimer(YTCraft.getPlugin(), new PluginTimer(), 0, 20);
                    MobSpawnTask = Bukkit.getScheduler().runTaskTimer(YTCraft.getPlugin(), new MobQueueSpawning(),0,20L);
                    isYoutubeTaskActive = true;

                    Bukkit.broadcast(Component.text("YTCraft Successfully Started.").color(NamedTextColor.GREEN));
                    Bukkit.broadcast(Component.text(commandSender.getName().toString()).color(NamedTextColor.YELLOW).append(Component.text(" has been set as Streamer.").color(NamedTextColor.WHITE)));

                    Board.createNewScoreBoard(Data.streamer);
                }
                else{
                    Bukkit.broadcast(Component.text("Already Running.").color(NamedTextColor.YELLOW));
                }
                return true;
            }
            else if(args.length==1 && args[0].equals("end")){
                if(isYoutubeTaskActive){

                    YoutubeTask.cancel();
                    TimerTask.cancel();
                    isYoutubeTaskActive = false;
                    ChatControl.setTimeStamp(null);

                    Utils.killAllAuthorMobs();
                    Utils.clearAllAuthorItems();
                    SubscribeSpawnMechanics.SubscriberCountLimit=0;
                    BlankBoard.createBlankBoard();
                    Bukkit.broadcast(Component.text("Session Successfully Ended.").color(NamedTextColor.RED));

                }
                else{
                    Bukkit.broadcast(Component.text("No Running session.").color(NamedTextColor.YELLOW)) ;
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

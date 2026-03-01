package com.zenith.YTCraft.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.mechanics.ChatControl;
import com.zenith.YTCraft.mechanics.MobSpawning;
import com.zenith.YTCraft.mechanics.SubscriberMechanics;
import com.zenith.YTCraft.timer.PluginTimer;
import com.zenith.YTCraft.ui.BlankBoard;
import com.zenith.YTCraft.ui.Pluginboard;
import com.zenith.YTCraft.util.MobUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;


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
                    PluginState.setStreamer((Player) commandSender);

                    YoutubeTask = Bukkit.getScheduler().runTaskTimer(YTCraft.getPlugin(), new ChatControl(), 0, 20L * 3);
                    TimerTask = Bukkit.getScheduler().runTaskTimer(YTCraft.getPlugin(), new PluginTimer(), 0, 20);
                    MobSpawnTask = Bukkit.getScheduler().runTaskTimer(YTCraft.getPlugin(), new MobSpawning(),0,20L);
                    isYoutubeTaskActive = true;

                    Bukkit.broadcast(Component.text("YTCraft Successfully Started.").color(NamedTextColor.GREEN));
                    Bukkit.broadcast(Component.text(commandSender.getName().toString()).color(NamedTextColor.YELLOW).append(Component.text(" has been set as Streamer. ///").color(NamedTextColor.WHITE)));

                    Pluginboard.createNewScoreBoard(PluginState.getStreamer());
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

                    MobUtils.killAllAuthorMobs();
                    MobUtils.clearAllAuthorItems();
                    SubscriberMechanics.SubscriberCountLimit=0;
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

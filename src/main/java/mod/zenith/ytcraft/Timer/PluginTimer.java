package mod.zenith.ytcraft.Timer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import mod.zenith.ytcraft.App.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mod.zenith.ytcraft.AdventureLib.Titles;
import mod.zenith.ytcraft.Board.Board;
import mod.zenith.ytcraft.App.ChatControl;
import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YTCraft;
import net.md_5.bungee.api.ChatColor;

public class PluginTimer implements Runnable {

    public static boolean isForceToggle = false;

    private static Player player;

    private static int activeMin;
    private static int activeSec;

    private static int restMin;
    private static int restSec;

    private static String displayTimerMode;

    private static String displayMin;
    private static String displaySec;

    public static void setActiveTimer(int aMin, int aSec){
        activeMin=aMin;
        activeSec=aSec;
    }

    public static void setRestTimer(int rMin, int rSec){
        restMin=rMin;
        restSec=rSec;
    }

    public PluginTimer() {

        player = Data.streamer;
        Data.isActiveTimerMode = false;

        int[] activeTime = Data.getActiveTime();
        activeMin = activeTime[0];
        activeSec = activeTime[1];

        int[] resTime = Data.getResTime();
        restMin = resTime[0];
        restSec = resTime[1];
    }

    private static void updateTimer() {

        if (Data.isActiveTimerMode) {
            if (activeSec == 0) {
                activeSec = 59;
                activeMin--;
            } else {
                activeSec--;
            }

            displayMin = ChatColor.RED+("0"+activeMin).substring(("0"+activeMin).length()-2);
            displaySec = ChatColor.RED+("0"+activeSec).substring(("0"+activeSec).length()-2);

        } else { //Rest Timer ::
            if (restSec == 0) {
                restSec = 59;
                restMin--;
            } else {
                restSec--;
            }
            
            displayMin = ChatColor.GREEN+("0"+restMin).substring(("0"+restMin).length()-2);
            displaySec = ChatColor.GREEN+("0"+restSec).substring(("0"+restSec).length()-2);
        }
    }

    private static void updateTimerMode(){
        if(Data.isActiveTimerMode){
            displayTimerMode = ChatColor.RED + "" +"Spawn";
        }
        else {
            displayTimerMode = ChatColor.YELLOW + "" +"Rest";
        }
    }

    private static void toggleTimer() {

        if (activeMin == 0 && activeSec == 0 && Data.isActiveTimerMode) {
            int[] resTime = Data.getResTime();
            restMin = resTime[0];
            restSec = resTime[1];

            Data.isActiveTimerMode = false;
            Utils.killAllAuthorMobs();
            Utils.clearAllAuthorItems();

            //Showing Rest Title
            Titles.showTimerRestTitle(YTCraft.getPlugin().adventure().player(player));
        }

        if (restMin == 0 && restSec == 0 && !Data.isActiveTimerMode) {
            int[] activeTime = Data.getActiveTime();
            activeMin = activeTime[0];
            activeSec = activeTime[1];

            Data.isActiveTimerMode = true;
            ChatControl.setTimeStamp(LocalDateTime.parse(ZonedDateTime.now(ZoneId.of("GMT")).toString().substring(0, 19)));
            Bukkit.getLogger().info("API Activated!!! at "+ ChatControl.ReadTimeStamp.toString() );
            //Showing Active Title
            Titles.showTimerActiveTitle(YTCraft.getPlugin().adventure().player(player));
        }
    }

    private static void forceToggleTimer(){
        if(Data.isActiveTimerMode){
            activeMin=0;
            activeSec=0;
        }
        else {
            restMin=0;
            restSec=0;
        }
    }

    @Override
    public void run() {

        if(isForceToggle){
            forceToggleTimer();
        }else{
            updateTimer();
        }
        isForceToggle=false;

        updateTimerMode();

        if(Data.streamer.getScoreboard().getObjective("YTCraftBoard") != null) {
            Board.updateScoreboard(Data.streamer, displayMin, displaySec, displayTimerMode);
        }

        toggleTimer();
    }
}

package mod.zenith.ytcraft.Board;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import mod.zenith.ytcraft.Data;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class Board implements Runnable {

    private final Player player;

    private static String displayTimerMode;

    private static int activeMin;
    private static int activeSec;

    private static int restMin;
    private static int restSec;

    private static String displayMin;
    private static String displaySec;

    public Board(Player player) {
        this.player = player;
    }

    private static void updateTimer(){

        if (Data.isActiveTimerMode) {
            if (activeSec == 0) {
                activeSec = 59;
                activeMin--;
            } else {
                activeSec--;
            }
            displayMin = ChatColor.RED+("0"+activeMin).substring(("0"+activeMin).length()-2);
            displaySec = ChatColor.RED+("0"+activeSec).substring(("0"+activeSec).length()-2);
        }
        else { //Rest Timer ::
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

    private static void toggleTimer(){

        if (activeMin == 0 && activeSec==0 && Data.isActiveTimerMode) {
            int[] resTime = Data.getResTime();
            restMin = resTime[0];
            restSec = resTime[1];

            Data.isActiveTimerMode = false;
        }

        if (restMin == 0 && restSec==0 && !Data.isActiveTimerMode) {
            int[] activeTime = Data.getActiveTime();
            activeMin = activeTime[0];
            activeSec = activeTime[1];

            Data.isActiveTimerMode = true;
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

    @Override
    public void run() {

        if (player.getScoreboard().getObjective("Board") != null) {
            
            updateTimerMode();
            updateTimer();
        
            updateScoreboard(player);
            
            toggleTimer();
        } 
        else {

            int[] activeTime = Data.getActiveTime();
            activeMin = activeTime[0];
            activeSec = activeTime[1];

            int[] resTime = Data.getResTime();
            restMin = resTime[0];
            restSec = resTime[1];


            createNewScoreBoard(player);
        }

    }

    private void createNewScoreBoard(Player player) {

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        //Objective is the topic of scoreboard = Title
        Objective objective = scoreboard.registerNewObjective("Board", "dummy", ChatColor.RED + "YoutubeSpawn");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        //One score obj is one line in scoreboard
        objective.getScore("").setScore(4);
        objective.getScore(ChatColor.WHITE + "This is Zen").setScore(4);
        objective.getScore("").setScore(3);

        Team timerMode = scoreboard.registerNewTeam("TimerMode");
        String timerModeKey = ChatColor.AQUA.toString();
        timerMode.addEntry(timerModeKey);
        timerMode.setPrefix("Mode: ");
        timerMode.setSuffix("None");

        objective.getScore(timerModeKey).setScore(2);
        objective.getScore("").setScore(1);

        //Update cause re-render, which may couse flicker, that's why we use team
        Team timer = scoreboard.registerNewTeam("Timer");
        String timerKey = ChatColor.GOLD.toString();
        timer.addEntry(timerKey);
        timer.setPrefix("Timer: ");
        timer.setSuffix("00:00");

        objective.getScore(timerKey).setScore(0);

        player.setScoreboard(scoreboard);
    }

    private void updateScoreboard(Player player) {

        Scoreboard scoreboard = player.getScoreboard();

        Team timerMode = scoreboard.getTeam("TimerMode");
        timerMode.setSuffix(displayTimerMode);
    

        Team timer = scoreboard.getTeam("Timer");
        timer.setSuffix(ChatColor.RED + "" + displayMin + ":" + displaySec );

    }

}

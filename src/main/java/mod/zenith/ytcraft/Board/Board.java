package mod.zenith.ytcraft.Board;

import mod.zenith.ytcraft.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class Board {

    
    public static void createNewScoreBoard(Player player) {

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        //Objective is the topic of scoreboard = Title
        Objective objective = scoreboard.registerNewObjective("YTCraftBoard", "dummy", ChatColor.RED+""+ChatColor.BOLD+"YTCraft");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        //One score obj is one line in scoreboard
        objective.getScore(" ").setScore(5);
        objective.getScore(ChatColor.WHITE + "Made by ZenithGG").setScore(4);
        objective.getScore(" ").setScore(4);

        //TimerMode
        Team timerMode = scoreboard.registerNewTeam("TimerMode");
        String timerModeKey = ChatColor.AQUA.toString();
        timerMode.addEntry(timerModeKey);
        timerMode.setPrefix("Mode: ");
        timerMode.setSuffix("None");

        objective.getScore(timerModeKey).setScore(3);
        objective.getScore("").setScore(2);

        //Update cause re-render, which may couse flicker, that's why we use team
        Team timer = scoreboard.registerNewTeam("Timer");
        String timerKey = ChatColor.GOLD.toString();
        timer.addEntry(timerKey);
        timer.setPrefix("Timer: ");
        timer.setSuffix("00:00");

        objective.getScore(timerKey).setScore(1);

        //Subscriber Count
        Team subscriberCount = scoreboard.registerNewTeam("SubscriberCount");
        String subcriberCountKey = org.bukkit.ChatColor.RED.toString();
        subscriberCount.addEntry(subcriberCountKey);
        subscriberCount.setPrefix("Subs: ");
        subscriberCount.setSuffix("0");

        objective.getScore(subcriberCountKey).setScore(0);

        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboard(Player player, String displayMin, String displaySec, String displayTimerMode) {

        Scoreboard scoreboard = player.getScoreboard();

        Team timerMode = scoreboard.getTeam("TimerMode");
        timerMode.setSuffix(displayTimerMode);
    

        Team timer = scoreboard.getTeam("Timer");
        timer.setSuffix(ChatColor.RED + "" + displayMin + ":" + displaySec );

        Team subscriberCount = scoreboard.getTeam("SubscriberCount");
        subscriberCount.setSuffix(""+Data.SubscriberCount);

    }

}

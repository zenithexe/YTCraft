package mod.zenith.ytcraft.Board;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;

public class Board implements Runnable {

    private final static Board instance = new Board();

    private Board() {

    }

    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getScoreboard() != null && p.getScoreboard().getObjective("Board") != null){
                updateScoreboard(p);
            }else{
                createNewScoreBoard(p);
            }
        }
    }

    private void createNewScoreBoard(Player player) {

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        //Objective is the topic of scoreboard = Title
        Objective objective = scoreboard.registerNewObjective("Board","dummy",ChatColor.RED+"YoutubeSpawn");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        //One score obj is one line in scoreboard
        Score score = objective.getScore(ChatColor.WHITE+"This is Zen");
        score.setScore(3);

        //Update cause re-render, which may couse flicker, that's why we use team
        Team team1 = scoreboard.registerNewTeam("team1");
        String teamKey = ChatColor.GOLD.toString();
        team1.addEntry(teamKey);
        team1.setPrefix("World :");
        team1.setSuffix("Zen");


        objective.getScore(teamKey).setScore(0);

        player.setScoreboard(scoreboard);
    }

    private void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Team team1 = scoreboard.getTeam("team1");
        
        team1.setSuffix(ChatColor.YELLOW+""+(player.getStatistic(Statistic.WALK_ONE_CM)));

    }


    public static Board getInstance() {
        return instance;
    }
}

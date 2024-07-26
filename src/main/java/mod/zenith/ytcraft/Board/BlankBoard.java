package mod.zenith.ytcraft.Board;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import mod.zenith.ytcraft.Data;

public class BlankBoard {

    public static void createBlankBoard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Data.streamer.setScoreboard(scoreboard);
    }

}


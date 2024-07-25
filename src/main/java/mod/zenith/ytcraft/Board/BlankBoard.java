package mod.zenith.ytcraft.Board;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class BlankBoard implements Runnable {

    private final Player player;

    public BlankBoard(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (player != null) {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard(scoreboard);
        }
    }

}

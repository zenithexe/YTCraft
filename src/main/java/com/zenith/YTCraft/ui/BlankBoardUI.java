package com.zenith.YTCraft.ui;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import com.zenith.YTCraft.data.PluginState;

public class BlankBoardUI {

    public static void createBlankBoard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        PluginState.getStreamer().setScoreboard(scoreboard);
    }

}

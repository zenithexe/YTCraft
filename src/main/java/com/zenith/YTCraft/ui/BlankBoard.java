package com.zenith.YTCraft.ui;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.data.MobManager;
import com.zenith.YTCraft.data.SpawnQueue;

public class BlankBoard {

    public static void createBlankBoard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        PluginState.getStreamer().setScoreboard(scoreboard);
    }

}


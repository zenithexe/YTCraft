package com.zenith.YTCraft.timer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.data.TimerState;
import com.zenith.YTCraft.mechanics.ChatControl;
import com.zenith.YTCraft.ui.BossBarUI;
import com.zenith.YTCraft.ui.ScoreboardUI;
import com.zenith.YTCraft.ui.TitlesUI;
import com.zenith.YTCraft.util.DateTimeUtils;
import com.zenith.YTCraft.util.ItemUtils;
import com.zenith.YTCraft.util.MobUtils;

import net.md_5.bungee.api.ChatColor;

public class PluginTimer implements Runnable {

    private static PluginTimer instance;
    public static boolean isForceSkip = false;

    private int activeSec;
    private int restSec;

    private int currActiveSec;
    private int currRestSec;

    public PluginTimer() {
        instance = this;

        this.activeSec = TimerState.getActiveTime();
        this.currActiveSec = this.activeSec;

        this.restSec = TimerState.getRestTime();
        this.currRestSec = this.restSec;
    }

    public static PluginTimer getInstance() {
        return instance;
    }

    public void setActiveTimer(int seconds) {
        this.activeSec = seconds;
        // Only reset current time if we're in active mode
        if (PluginState.isChatControlEnabled()) {
            this.currActiveSec = this.activeSec;
        }
    }

    public void setRestTimer(int seconds) {
        this.restSec = seconds;
        // Only reset current time if we're in rest mode
        if (!PluginState.isChatControlEnabled()) {
            this.currRestSec = this.restSec;
        }
    }

    public static void skipTimerMode() {
        isForceSkip = true;
    }

    private void updateTimer() {
        if (PluginState.isChatControlEnabled()) {
            currActiveSec--;
        } else {
            currRestSec--;
        }
    }

    private String getFormattedTime() {
        int totalSeconds = PluginState.isChatControlEnabled() ? currActiveSec : currRestSec;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds / 60) % 60;
        int seconds = totalSeconds % 60;

        ChatColor color = PluginState.isChatControlEnabled() ? ChatColor.RED : ChatColor.GREEN;

        if (hours > 0) {
            return color + String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return color + String.format("%02d:%02d", minutes, seconds);
        }
    }

    private String getDisplayTimerMode() {
        if (PluginState.isChatControlEnabled()) {
            return ChatColor.RED + "Spawn";
        } else {
            return ChatColor.YELLOW + "Rest";
        }
    }

    private void toggleTimer() {
        Player streamer = PluginState.getStreamer();

        if (streamer == null) {
            return;
        }

        if (currActiveSec == 0 && PluginState.isChatControlEnabled()) {
            this.currRestSec = this.restSec;

            PluginState.setChatControl(false);
            MobUtils.killAllAuthorMobs();
            ItemUtils.clearAllAuthorItems();

            //Showing Rest Title
            TitlesUI.showTimerRestTitle();

            //Update Boss Bar
            BossBarUI.updateBossBar(currRestSec, restSec);
        }

        if (currRestSec == 0 && !PluginState.isChatControlEnabled()) {
            this.currActiveSec = this.activeSec;

            PluginState.setChatControl(true);
            ChatControl.setTimeStamp(DateTimeUtils.getGMTTimeNow());
            Bukkit.getLogger().info("Chat Control Activated!");

            //Showing Active Title
            TitlesUI.showTimerActiveTitle();

            //Update Boss Bar
            BossBarUI.updateBossBar(currActiveSec, activeSec);
        }
    }

    private void forceToggleTimer() {
        if (PluginState.isChatControlEnabled()) {
            currActiveSec = 0;
        } else {
            currRestSec = 0;
        }

        isForceSkip = false;
    }

    @Override
    public void run() {
        Player player = PluginState.getStreamer();
        if (player == null) {
            return;
        }

        if (isForceSkip) {
            forceToggleTimer();
        } else {
            updateTimer();
        }

        if (player.getScoreboard().getObjective("YTCraftBoard") != null) {
            ScoreboardUI.updateScoreboard(player, getFormattedTime(), getDisplayTimerMode());
        }

        toggleTimer();

        //Update Boss Bar every second
        if (PluginState.isChatControlEnabled()) {
            BossBarUI.updateBossBar(currActiveSec, activeSec);
        } else {
            BossBarUI.updateBossBar(currRestSec, restSec);
        }
    }
}

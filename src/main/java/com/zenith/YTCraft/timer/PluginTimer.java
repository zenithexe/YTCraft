package com.zenith.YTCraft.timer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.mechanics.ChatControl;
import com.zenith.YTCraft.ui.BossBarUI;
import com.zenith.YTCraft.ui.ScoreboardUI;
import com.zenith.YTCraft.ui.TitlesUI;
import com.zenith.YTCraft.util.MobUtils;

import net.md_5.bungee.api.ChatColor;

public class PluginTimer implements Runnable {

    public static boolean isForceToggle = false;

    private static int activeMin;
    private static int activeSec;

    private static int restMin;
    private static int restSec;

    private static String displayTimerMode;

    private static String displayMin;
    private static String displaySec;

    public static void setActiveTimer(int aMin, int aSec) {
        activeMin = aMin;
        activeSec = aSec;
    }

    public static void setRestTimer(int rMin, int rSec) {
        restMin = rMin;
        restSec = rSec;
    }

    public static int getActiveMin() {
        return activeMin;
    }

    public static int getActiveSec() {
        return activeSec;
    }

    public static int getRestMin() {
        return restMin;
    }

    public static int getRestSec() {
        return restSec;
    }

    public PluginTimer() {
        PluginState.setChatControl(false);

        int[] activeTime = PluginState.getActiveTime();
        activeMin = activeTime[0];
        activeSec = activeTime[1];

        int[] resTime = PluginState.getRestTime();
        restMin = resTime[0];
        restSec = resTime[1];
    }

    private static void updateTimer() {

        if (PluginState.isChatControlEnabled()) {
            if (activeSec == 0) {
                activeSec = 59;
                activeMin--;
            } else {
                activeSec--;
            }

            displayMin = ChatColor.RED + ("0" + activeMin).substring(("0" + activeMin).length() - 2);
            displaySec = ChatColor.RED + ("0" + activeSec).substring(("0" + activeSec).length() - 2);

        } else { //Rest Timer ::
            if (restSec == 0) {
                restSec = 59;
                restMin--;
            } else {
                restSec--;
            }

            displayMin = ChatColor.GREEN + ("0" + restMin).substring(("0" + restMin).length() - 2);
            displaySec = ChatColor.GREEN + ("0" + restSec).substring(("0" + restSec).length() - 2);
        }
    }

    private static void updateTimerMode() {
        if (PluginState.isChatControlEnabled()) {
            displayTimerMode = ChatColor.RED + "" + "Spawn";
        } else {
            displayTimerMode = ChatColor.YELLOW + "" + "Rest";
        }
    }

    private static void toggleTimer() {
        Player streamer = PluginState.getStreamer();

        if (streamer == null) return;

        if (activeMin == 0 && activeSec == 0 && PluginState.isChatControlEnabled()) {
            int[] resTime = PluginState.getRestTime();
            restMin = resTime[0];
            restSec = resTime[1];

            PluginState.setChatControl(false);
            MobUtils.killAllAuthorMobs();
            MobUtils.clearAllAuthorItems();

            //Showing Rest Title
            TitlesUI.showTimerRestTitle();
            
            //Update Boss Bar
            BossBarUI.updateBossBar(restMin, restSec, PluginState.getRestTime()[0], PluginState.getRestTime()[1]);
        }

        if (restMin == 0 && restSec == 0 && !PluginState.isChatControlEnabled()) {
            int[] activeTime = PluginState.getActiveTime();
            activeMin = activeTime[0];
            activeSec = activeTime[1];

            PluginState.setChatControl(true);
            ChatControl.setTimeStamp(MobUtils.getGMTTimeNow());
            Bukkit.getLogger().info("Chat Control Activated!");
            
            //Showing Active Title
            TitlesUI.showTimerActiveTitle();
            
            //Update Boss Bar
            BossBarUI.updateBossBar(activeMin, activeSec, PluginState.getActiveTime()[0], PluginState.getActiveTime()[1]);
        }
    }

    private static void forceToggleTimer() {
        if (PluginState.isChatControlEnabled()) {
            activeMin = 0;
            activeSec = 0;
        } else {
            restMin = 0;
            restSec = 0;
        }
    }

    @Override
    public void run() {
        Player player = PluginState.getStreamer();
        if (player == null) return;

        if (isForceToggle) {
            forceToggleTimer();
        } else {
            updateTimer();
        }
        isForceToggle = false;

        updateTimerMode();

        if (player.getScoreboard().getObjective("YTCraftBoard") != null) {
            ScoreboardUI.updateScoreboard(player, displayMin, displaySec, displayTimerMode);
        }

        toggleTimer();
        
        //Update Boss Bar every second
        if (PluginState.isChatControlEnabled()) {
            BossBarUI.updateBossBar(activeMin, activeSec, PluginState.getActiveTime()[0], PluginState.getActiveTime()[1]);
        } else {
            BossBarUI.updateBossBar(restMin, restSec, PluginState.getRestTime()[0], PluginState.getRestTime()[1]);
        }
    }
}

package com.zenith.YTCraft.timer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.data.PluginState;
import com.zenith.YTCraft.mechanics.ChatControl;
import com.zenith.YTCraft.ui.Pluginboard;
import com.zenith.YTCraft.ui.Titles;
import com.zenith.YTCraft.util.MobUtils;

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

    public static void setActiveTimer(int aMin, int aSec) {
        activeMin = aMin;
        activeSec = aSec;
    }

    public static void setRestTimer(int rMin, int rSec) {
        restMin = rMin;
        restSec = rSec;
    }

    public PluginTimer() {

        player = PluginState.getStreamer();
        PluginState.setActiveTimerMode(false);

        int[] activeTime = PluginState.getActiveTime();
        activeMin = activeTime[0];
        activeSec = activeTime[1];

        int[] resTime = PluginState.getRestTime();
        restMin = resTime[0];
        restSec = resTime[1];
    }

    private static void updateTimer() {

        if (PluginState.isActiveTimerMode()) {
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
        if (PluginState.isActiveTimerMode()) {
            displayTimerMode = ChatColor.RED + "" + "Spawn";
        } else {
            displayTimerMode = ChatColor.YELLOW + "" + "Rest";
        }
    }

    private static void toggleTimer() {

        if (activeMin == 0 && activeSec == 0 && PluginState.isActiveTimerMode()) {
            int[] resTime = PluginState.getRestTime();
            restMin = resTime[0];
            restSec = resTime[1];

            PluginState.setActiveTimerMode(false);
            MobUtils.killAllAuthorMobs();
            MobUtils.clearAllAuthorItems();

            //Showing Rest Title
            Titles.showTimerRestTitle(YTCraft.getPlugin().adventure().player(player));
        }

        if (restMin == 0 && restSec == 0 && !PluginState.isActiveTimerMode()) {
            int[] activeTime = PluginState.getActiveTime();
            activeMin = activeTime[0];
            activeSec = activeTime[1];

            PluginState.setActiveTimerMode(true);
            ChatControl.setTimeStamp(LocalDateTime.parse(ZonedDateTime.now(ZoneId.of("GMT")).toString().substring(0, 19)));
            Bukkit.getLogger().info("API Activated!!! at " + ChatControl.ReadTimeStamp.toString());
            //Showing Active Title
            Titles.showTimerActiveTitle(YTCraft.getPlugin().adventure().player(player));
        }
    }

    private static void forceToggleTimer() {
        if (PluginState.isActiveTimerMode()) {
            activeMin = 0;
            activeSec = 0;
        } else {
            restMin = 0;
            restSec = 0;
        }
    }

    @Override
    public void run() {

        if (isForceToggle) {
            forceToggleTimer();
        } else {
            updateTimer();
        }
        isForceToggle = false;

        updateTimerMode();

        if (PluginState.getStreamer().getScoreboard().getObjective("YTCraftBoard") != null) {
            Pluginboard.updateScoreboard(PluginState.getStreamer(), displayMin, displaySec, displayTimerMode);
        }

        toggleTimer();
    }
}

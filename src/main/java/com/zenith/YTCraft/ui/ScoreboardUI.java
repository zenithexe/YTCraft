package com.zenith.YTCraft.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.zenith.YTCraft.data.MobSpawnState;
import com.zenith.YTCraft.data.PluginState;

import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class ScoreboardUI {

    // Team names
    private static final String TEAM_TIMER_MODE = "TimerMode";
    private static final String TEAM_TIMER = "Timer";
    private static final String TEAM_VIEWER_COUNT = "ViewerCount";
    private static final String TEAM_SUBSCRIBER_COUNT = "SubscriberCount";
    private static final String TEAM_ACTIVE_MOBS = "ActiveMobs";
    private static final String TEAM_DEATH_COUNT = "DeathCount";

    // Score positions
    private static final int SCORE_HEADER = 12;
    private static final int SCORE_SPACER_1 = 11;
    private static final int SCORE_TIMER_MODE = 10;
    private static final int SCORE_TIMER = 9;
    private static final int SCORE_SPACER_2 = 8;
    private static final int SCORE_VIEWER_COUNT = 7;
    private static final int SCORE_SUBSCRIBER_COUNT = 6;
    private static final int SCORE_SPACER_3 = 5;
    private static final int SCORE_ACTIVE_MOBS = 4;
    private static final int SCORE_DEATH_COUNT = 3;
    private static final int SCORE_SPACER_4 = 2;
    private static final int SCORE_CREDITS = 1;

    // Default values
    private static final String DEFAULT_MODE = "Idle";
    private static final String DEFAULT_TIMER = "00:00";
    private static final String DEFAULT_SUBS = "0";

    public static void createNewScoreBoard(Player player) {
        if (player == null) return;

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        // Create objective with a friendly title
        Objective objective = scoreboard.registerNewObjective(
                "YTCraftBoard",
                "dummy",
                ChatColor.GOLD + "✦ " + ChatColor.RED + ChatColor.BOLD + "YTCraft" + ChatColor.GOLD + " ✦"
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Header section
        objective.getScore(ChatColor.GRAY + "━━━━━━━━━━━━━━━━━━━━━━").setScore(SCORE_HEADER);
        objective.getScore(ChatColor.WHITE + " ").setScore(SCORE_SPACER_1);

        // // Timer Mode
        // Team timerMode = scoreboard.registerNewTeam(TEAM_TIMER_MODE);
        // String timerModeKey = ChatColor.AQUA.toString();
        // timerMode.addEntry(timerModeKey);
        // timerMode.setPrefix(ChatColor.AQUA + "⏱ Mode: " + ChatColor.WHITE);
        // timerMode.setSuffix(DEFAULT_MODE);
        // objective.getScore(timerModeKey).setScore(SCORE_TIMER_MODE);

        // // Timer (uses teams to avoid flicker on updates)
        // Team timer = scoreboard.registerNewTeam(TEAM_TIMER);
        // String timerKey = ChatColor.GOLD.toString();
        // timer.addEntry(timerKey);
        // timer.setPrefix(ChatColor.GOLD + "⏰ Time: " + ChatColor.WHITE);
        // timer.setSuffix(DEFAULT_TIMER);
        // objective.getScore(timerKey).setScore(SCORE_TIMER);

        // objective.getScore(ChatColor.WHITE + "  ").setScore(SCORE_SPACER_2);

        // Viewer Count
        Team viewerCount = scoreboard.registerNewTeam(TEAM_VIEWER_COUNT);
        String viewerCountKey = ChatColor.LIGHT_PURPLE.toString();
        viewerCount.addEntry(viewerCountKey);
        viewerCount.setPrefix(ChatColor.LIGHT_PURPLE + "👁 Watching: " + ChatColor.WHITE);
        viewerCount.setSuffix("0");
        objective.getScore(viewerCountKey).setScore(SCORE_VIEWER_COUNT);

        // Subscriber Count
        Team subscriberCount = scoreboard.registerNewTeam(TEAM_SUBSCRIBER_COUNT);
        String subscriberCountKey = ChatColor.RED.toString();
        subscriberCount.addEntry(subscriberCountKey);
        subscriberCount.setPrefix(ChatColor.RED + "❤ Subscriber: " + ChatColor.WHITE);
        subscriberCount.setSuffix(DEFAULT_SUBS);
        objective.getScore(subscriberCountKey).setScore(SCORE_SUBSCRIBER_COUNT);

        objective.getScore(ChatColor.WHITE + "   ").setScore(SCORE_SPACER_3);

        // Active Mobs Count
        Team activeMobs = scoreboard.registerNewTeam(TEAM_ACTIVE_MOBS);
        String activeMobsKey = ChatColor.GREEN.toString();
        activeMobs.addEntry(activeMobsKey);
        activeMobs.setPrefix(ChatColor.GREEN + "⚔ Spawned Mobs: " + ChatColor.WHITE);
        activeMobs.setSuffix("0");
        objective.getScore(activeMobsKey).setScore(SCORE_ACTIVE_MOBS);

        // Death Count
        Team deathCount = scoreboard.registerNewTeam(TEAM_DEATH_COUNT);
        String deathCountKey = ChatColor.DARK_RED.toString();
        deathCount.addEntry(deathCountKey);
        deathCount.setPrefix(ChatColor.DARK_RED + "💀 Death Count: " + ChatColor.WHITE);
        deathCount.setSuffix("0");
        objective.getScore(deathCountKey).setScore(SCORE_DEATH_COUNT);

        objective.getScore(ChatColor.WHITE + "    ").setScore(SCORE_SPACER_4);

        // Credits footer
        objective.getScore(ChatColor.GRAY + "Made by " + ChatColor.GOLD + "ZenithGG").setScore(SCORE_CREDITS);

        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboard(Player player, String formattedTime, String displayTimerMode) {
        if (player == null) return;

        Scoreboard scoreboard = player.getScoreboard();

        // Update timer mode
        Team timerMode = scoreboard.getTeam(TEAM_TIMER_MODE);
        if (timerMode != null) {
            timerMode.setSuffix(displayTimerMode != null ? displayTimerMode : DEFAULT_MODE);
        }

        // Update timer with formatted time (already includes color)
        Team timer = scoreboard.getTeam(TEAM_TIMER);
        if (timer != null) {
            timer.setSuffix(formattedTime);
        }

        // Update viewer count
        Team viewerCount = scoreboard.getTeam(TEAM_VIEWER_COUNT);
        if (viewerCount != null) {
            viewerCount.setSuffix(String.valueOf(PluginState.getViewers()));
        }

        // Update subscriber count
        Team subscriberCount = scoreboard.getTeam(TEAM_SUBSCRIBER_COUNT);
        if (subscriberCount != null) {
            subscriberCount.setSuffix(String.valueOf(PluginState.getSubscriberCount()));
        }

        // Update active mobs count
        Team activeMobs = scoreboard.getTeam(TEAM_ACTIVE_MOBS);
        if (activeMobs != null) {
            int mobCount = MobSpawnState.getChannelIdToAuthorMob().size();
            activeMobs.setSuffix(String.valueOf(mobCount));
        }

        // Update death count
        Team deathCount = scoreboard.getTeam(TEAM_DEATH_COUNT);
        if (deathCount != null) {
            deathCount.setSuffix(String.valueOf(PluginState.getSteamerDeathCount()));
        }
    }


}

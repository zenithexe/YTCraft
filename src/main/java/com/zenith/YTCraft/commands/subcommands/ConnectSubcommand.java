package com.zenith.YTCraft.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.api.YoutubeAPI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * /yt connect            - fetches live broadcasts; auto-selects if one, lists if multiple.
 * /yt connect use <no>   - selects broadcast by number from the last listed results.
 * /yt connect info       - shows current connected broadcast details.
 */
public class ConnectSubcommand implements Subcommand {

    private static final int TITLE_PREVIEW_LENGTH = 30;

    // Holds the last fetched broadcast list so the user can pick by number
    private static List<String[]> lastBroadcasts = null;

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        // /yt connect info
        if (args.length >= 1 && args[0].equalsIgnoreCase("info")) {
            handleInfo(sender);
            return true;
        }

        // /yt connect use <no>
        if (args.length >= 2 && args[0].equalsIgnoreCase("use")) {
            handleUse(sender, args[1]);
            return true;
        }

        // /yt connect — fetch broadcasts
        if (!YoutubeAPI.hasChannelId()) {
            sender.sendMessage(Component.text("[YTCraft] No channel ID configured.").color(NamedTextColor.RED));
            sender.sendMessage(Component.text("Use /yt config channel <id|@handle> to set one.").color(NamedTextColor.YELLOW));
            return true;
        }

        sender.sendMessage(Component.text("[YTCraft] Fetching live broadcasts...").color(NamedTextColor.GRAY));

        Bukkit.getScheduler().runTaskAsynchronously(YTCraft.getPlugin(), () -> {
            List<String[]> broadcasts = YoutubeAPI.getLiveBroadcasts();

            Bukkit.getScheduler().runTask(YTCraft.getPlugin(), () -> {
                if (broadcasts == null || broadcasts.isEmpty()) {
                    sender.sendMessage(Component.text("[YTCraft] No active live broadcasts found for this channel.").color(NamedTextColor.RED));
                    lastBroadcasts = null;
                    return;
                }

                if (broadcasts.size() == 1) {
                    applyBroadcast(sender, broadcasts.get(0)[0], broadcasts.get(0)[1]);
                    lastBroadcasts = null;
                } else {
                    lastBroadcasts = broadcasts;
                    sender.sendMessage(Component.text("[YTCraft] Multiple live broadcasts found:").color(NamedTextColor.YELLOW));
                    for (int i = 0; i < broadcasts.size(); i++) {
                        String preview = truncateTitle(broadcasts.get(i)[1]);
                        String videoId = broadcasts.get(i)[0];
                        sender.sendMessage(
                            Component.text("  " + (i + 1) + ". ").color(NamedTextColor.WHITE)
                                .append(Component.text(preview).color(NamedTextColor.AQUA))
                                .append(Component.text(" (" + videoId + ")").color(NamedTextColor.GRAY))
                        );
                    }
                    sender.sendMessage(Component.text("Use /yt connect use <no> to select one.").color(NamedTextColor.YELLOW));
                }
            });
        });

        return true;
    }

    private void handleUse(CommandSender sender, String numberArg) {
        try {
            int choice = Integer.parseInt(numberArg);
            if (lastBroadcasts == null || lastBroadcasts.isEmpty()) {
                sender.sendMessage(Component.text("[YTCraft] No broadcast list available. Run /yt connect first.").color(NamedTextColor.RED));
                return;
            }
            if (choice < 1 || choice > lastBroadcasts.size()) {
                sender.sendMessage(Component.text("[YTCraft] Invalid number. Choose between 1 and " + lastBroadcasts.size() + ".").color(NamedTextColor.RED));
                return;
            }
            String[] selected = lastBroadcasts.get(choice - 1);
            applyBroadcast(sender, selected[0], selected[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("[YTCraft] Usage: /yt connect use <number>").color(NamedTextColor.RED));
        }
    }

    private void handleInfo(CommandSender sender) {
        if (!YoutubeAPI.hasVideoId()) {
            sender.sendMessage(Component.text("[YTCraft] No broadcast connected yet.").color(NamedTextColor.RED));
            sender.sendMessage(Component.text("Use /yt connect to link a live broadcast.").color(NamedTextColor.YELLOW));
            return;
        }

        sender.sendMessage(Component.text("[YTCraft] Fetching broadcast info...").color(NamedTextColor.GRAY));

        Bukkit.getScheduler().runTaskAsynchronously(YTCraft.getPlugin(), () -> {
            String title = YoutubeAPI.getVideoTitle();
            boolean configured = YoutubeAPI.isConfigured();

            Bukkit.getScheduler().runTask(YTCraft.getPlugin(), () -> {
                if (title == null || !configured) {
                    sender.sendMessage(Component.text("[YTCraft] The connected broadcast appears to be invalid or no longer live.").color(NamedTextColor.RED));
                    sender.sendMessage(Component.text("Run /yt connect again to refresh.").color(NamedTextColor.YELLOW));
                    return;
                }
                sender.sendMessage(Component.text("[YTCraft] Connected broadcast:").color(NamedTextColor.GREEN));
                sender.sendMessage(Component.text("  Title: ").color(NamedTextColor.GRAY)
                        .append(Component.text(title).color(NamedTextColor.AQUA)));
                sender.sendMessage(Component.text("  Video ID: ").color(NamedTextColor.GRAY)
                        .append(Component.text(YoutubeAPI.getVideoId()).color(NamedTextColor.WHITE)));
            });
        });
    }

    private void applyBroadcast(CommandSender sender, String videoId, String title) {
        YoutubeAPI.updateVideoId(videoId);
        sender.sendMessage(Component.text("[YTCraft] Connected to: ").color(NamedTextColor.GREEN)
                .append(Component.text(title).color(NamedTextColor.AQUA)));
        sender.sendMessage(Component.text("  Video ID: " + videoId).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("You can now use /yt start.").color(NamedTextColor.GREEN));
    }

    private String truncateTitle(String title) {
        if (title == null) return "Unknown";
        if (title.length() <= TITLE_PREVIEW_LENGTH) return title;
        return title.substring(0, TITLE_PREVIEW_LENGTH) + "....";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("use", "info");
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "connect";
    }

    @Override
    public String getDescription() {
        return "Connect to a live broadcast from the configured channel";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

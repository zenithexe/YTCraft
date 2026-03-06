package com.zenith.YTCraft.commands.subcommands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.mechanics.ChatControl;
import com.zenith.YTCraft.mechanics.SubscriberMechanics;
import com.zenith.YTCraft.ui.BlankBoardUI;
import com.zenith.YTCraft.ui.BossBarUI;
import com.zenith.YTCraft.util.MobUtils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class EndSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!StartSubcommand.isRunning()) {
            sender.sendMessage(Component.text("No running session to end.").color(NamedTextColor.YELLOW));
            return true;
        }

        // Stop tasks
        StartSubcommand.stop();

        // Reset state
        ChatControl.setTimeStamp(null);
        MobUtils.killAllAuthorMobs();

        SubscriberMechanics.SubscriberCountLimit = 0;

        // Remove UI
        BlankBoardUI.createBlankBoard();
        BossBarUI.removeBossBar();

        Bukkit.broadcast(Component.text("Session Successfully Ended.").color(NamedTextColor.RED));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "end";
    }

    @Override
    public String getDescription() {
        return "End the YTCraft session";
    }
}

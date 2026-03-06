package com.zenith.YTCraft.commands.subcommands.mob.tier;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TierRemoveSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft mob tier remove <mob_type>").color(NamedTextColor.RED));
            return true;
        }

        String mobType = args[0].toUpperCase();
        List<MobSpawnSettings.MobTier> tiers = SettingsLoader.getSettings().getMobSpawnSettings().getMobTiers();
        
        boolean found = false;
        for (MobSpawnSettings.MobTier tier : tiers) {
            if (tier.getMobs().remove(mobType)) {
                found = true;
                break;
            }
        }

        if (found) {
            SettingsLoader.saveSettings();
            SettingsLoader.loadMobTiers();
            sender.sendMessage(Component.text("Removed " + mobType + " from tiers").color(NamedTextColor.GREEN));
        } else {
            sender.sendMessage(Component.text(mobType + " not found in any tier").color(NamedTextColor.YELLOW));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove mob from tiers";
    }
}

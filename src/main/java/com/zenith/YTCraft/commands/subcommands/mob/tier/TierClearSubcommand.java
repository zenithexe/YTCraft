package com.zenith.YTCraft.commands.subcommands.mob.tier;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;
import com.zenith.YTCraft.data.MobSpawnState;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TierClearSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        List<MobSpawnSettings.MobTier> tiers = SettingsLoader.getSettings().getMobSpawnSettings().getMobTiers();
        
        for (MobSpawnSettings.MobTier tier : tiers) {
            tier.getMobs().clear();
        }

        SettingsLoader.saveSettings();
        SettingsLoader.loadMobTiers();
        MobSpawnState.getEntityTypeToMinViewers().clear();
        
        sender.sendMessage(Component.text("Cleared all mob tiers").color(NamedTextColor.GREEN));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Clear all mob tiers";
    }
}

package com.zenith.YTCraft.commands.subcommands.mob.tier;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.MobSpawnSettings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TierListSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        List<MobSpawnSettings.MobTier> tiers = SettingsLoader.getSettings().getMobSpawnSettings().getMobTiers();
        
        if (tiers == null || tiers.isEmpty()) {
            sender.sendMessage(Component.text("No tiers configured").color(NamedTextColor.YELLOW));
            return true;
        }

        sender.sendMessage(Component.text("=== Mob Tiers ===").color(NamedTextColor.GOLD));
        for (MobSpawnSettings.MobTier tier : tiers) {
            sender.sendMessage(Component.text("Min Viewers: " + tier.getMinViewers()).color(NamedTextColor.YELLOW));
            sender.sendMessage(Component.text("  Mobs: " + String.join(", ", tier.getMobs())).color(NamedTextColor.GRAY));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Show all mob tiers";
    }
}

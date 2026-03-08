package com.zenith.YTCraft.commands.subcommands.custommob;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.custommobs.CustomMobRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CustomMobReloadSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // Reload settings
        SettingsLoader.loadSettings();
        SettingsLoader.loadCustomMobs();

        // Count unique mobs and total aliases
        int uniqueMobs = 0;
        int totalEntries = CustomMobRegistry.getAllCustomMobsRegistry().size();
        
        for (String key : CustomMobRegistry.getAllCustomMobsRegistry().keySet()) {
            if (CustomMobRegistry.getCustomMob(key).getMobKey().equals(key)) {
                uniqueMobs++;
            }
        }
        
        int aliases = totalEntries - uniqueMobs;

        sender.sendMessage(Component.text("✓ Reloaded ").color(NamedTextColor.GREEN)
                .append(Component.text(uniqueMobs).color(NamedTextColor.YELLOW))
                .append(Component.text(" custom mob(s) with ").color(NamedTextColor.GREEN))
                .append(Component.text(aliases).color(NamedTextColor.YELLOW))
                .append(Component.text(" alias(es)").color(NamedTextColor.GREEN)));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload custom mobs from config";
    }
}

package com.zenith.YTCraft.commands.subcommands.custommob;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.custommobs.CustomMobRegistry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CustomMobListSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Map<String, CustomMob> allMobs = CustomMobRegistry.getAllCustomMobs();
        
        if (allMobs.isEmpty()) {
            sender.sendMessage(Component.text("No custom mobs configured").color(NamedTextColor.YELLOW));
            return true;
        }

        // Get unique mobs (filter out aliases)
        List<CustomMob> uniqueMobs = new ArrayList<>();
        for (Map.Entry<String, CustomMob> entry : allMobs.entrySet()) {
            CustomMob mob = entry.getValue();
            if (entry.getKey().equals(mob.getMobKey())) {
                uniqueMobs.add(mob);
            }
        }

        sender.sendMessage(Component.text("=== Custom Mobs (" + uniqueMobs.size() + ") ===").color(NamedTextColor.GOLD));
        
        for (CustomMob mob : uniqueMobs) {
            Component message = Component.text("• " + mob.getMobKey()).color(NamedTextColor.YELLOW)
                    .append(Component.text(" (" + mob.getMobName() + ")").color(NamedTextColor.WHITE))
                    .append(Component.text(" - " + mob.getEntityType()).color(NamedTextColor.GRAY));
            
            sender.sendMessage(message);
            
            if (mob.hasAliases()) {
                sender.sendMessage(Component.text("  Aliases: " + String.join(", ", mob.getAliases())).color(NamedTextColor.DARK_GRAY));
            }
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
        return "List all custom mobs";
    }
}

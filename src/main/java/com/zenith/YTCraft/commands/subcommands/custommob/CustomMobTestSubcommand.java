package com.zenith.YTCraft.commands.subcommands.custommob;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.custommobs.CustomMob;
import com.zenith.YTCraft.custommobs.CustomMobRegistry;
import com.zenith.YTCraft.custommobs.SkinApplier;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CustomMobTestSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("This command can only be used by players").color(NamedTextColor.RED));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("Usage: /ytcraft custommob test <mob_key_or_alias>").color(NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;
        String mobKey = args[0].toLowerCase();
        CustomMob mob = CustomMobRegistry.getCustomMob(mobKey);

        if (mob == null) {
            sender.sendMessage(Component.text("Custom mob not found: " + mobKey).color(NamedTextColor.RED));
            return true;
        }

        // Spawn the mob at player's location
        Location spawnLoc = player.getLocation().add(player.getLocation().getDirection().multiply(2));
        LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(spawnLoc, mob.getEntityType());
        
        // Set custom name
        entity.customName(Component.text("Test: " + mob.getMobName()));
        entity.setCustomNameVisible(true);

        // Apply skin
        SkinApplier.applySkin(entity, mob, "Test: " + mob.getMobName(), YTCraft.getPlugin());

        sender.sendMessage(Component.text("✓ Spawned test mob '").color(NamedTextColor.GREEN)
                .append(Component.text(mob.getMobKey()).color(NamedTextColor.YELLOW))
                .append(Component.text("' at your location").color(NamedTextColor.GREEN)));

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (String key : CustomMobRegistry.getAllCustomMobs().keySet()) {
                if (key.startsWith(args[0].toLowerCase())) {
                    suggestions.add(key);
                }
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "Spawn a test custom mob";
    }
}

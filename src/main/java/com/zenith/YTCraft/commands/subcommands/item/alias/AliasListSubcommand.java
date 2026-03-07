package com.zenith.YTCraft.commands.subcommands.item.alias;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.config.types.ItemGiveSettings.ItemAlias;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class AliasListSubcommand implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Map<String, ItemAlias> aliases = SettingsLoader.getSettings().getItemGiveSettings().getItemAliases();

        if (aliases == null || aliases.isEmpty()) {
            sender.sendMessage(Component.text("No aliases defined!").color(NamedTextColor.YELLOW));
            return true;
        }

        sender.sendMessage(Component.text("=== Item Aliases ===").color(NamedTextColor.GOLD));
        for (Map.Entry<String, ItemAlias> entry : aliases.entrySet()) {
            ItemAlias alias = entry.getValue();
            sender.sendMessage(Component.text("  " + entry.getKey()).color(NamedTextColor.YELLOW)
                    .append(Component.text(" -> ").color(NamedTextColor.GRAY))
                    .append(Component.text(alias.getItem() + " x" + alias.getQty()).color(NamedTextColor.GREEN)));
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
        return "List all aliases";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }
}

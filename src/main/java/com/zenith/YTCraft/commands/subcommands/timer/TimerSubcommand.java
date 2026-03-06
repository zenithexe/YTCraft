package com.zenith.YTCraft.commands.subcommands.timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import com.zenith.YTCraft.commands.subcommands.Subcommand;
import com.zenith.YTCraft.config.SettingsLoader;
import com.zenith.YTCraft.data.TimerState;
import com.zenith.YTCraft.timer.PluginTimer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TimerSubcommand implements Subcommand {

    private final Map<String, SubAction> actions = new HashMap<>();

    public TimerSubcommand() {
        actions.put("alwaysactive", this::alwaysActive);
        actions.put("active", this::activeTime);
        actions.put("rest", this::restTime);
        actions.put("skip", this::skip);
        actions.put("reset", this::reset);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        SubAction action = actions.get(args[0].toLowerCase());
        if (action == null) {
            sender.sendMessage(Component.text("Unknown timer command: " + args[0]).color(NamedTextColor.RED));
            sendHelp(sender);
            return true;
        }

        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, args.length - 1);
        return action.execute(sender, subArgs);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (String name : actions.keySet()) {
                if (name.startsWith(args[0].toLowerCase())) {
                    suggestions.add(name);
                }
            }
            return suggestions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("alwaysactive")) {
            return Arrays.asList("true", "false");
        }

        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "timer";
    }

    @Override
    public String getDescription() {
        return "Manage timer settings";
    }

    @Override
    public String getPermission() {
        return "ytcraft.admin";
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("=== Timer Commands ===").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("  alwaysactive <true|false> - Set always active mode").color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  active <seconds> - Set active phase duration").color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  rest <seconds> - Set rest phase duration").color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  skip - Skip to next phase").color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text("  reset - Reset timer to 0").color(NamedTextColor.GRAY));
    }

    private boolean alwaysActive(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft timer alwaysactive <true|false>").color(NamedTextColor.RED));
            return true;
        }

        boolean value = Boolean.parseBoolean(args[0]);
        SettingsLoader.getSettings().getTimerSettings().setAlwaysActive(value);
        SettingsLoader.saveSettings();
        
        sender.sendMessage(Component.text("Always active mode: " + value).color(NamedTextColor.GREEN));
        return true;
    }

    private boolean activeTime(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft timer active <seconds>").color(NamedTextColor.RED));
            return true;
        }

        try {
            int seconds = Integer.parseInt(args[0]);
            if (seconds < 0) {
                sender.sendMessage(Component.text("Seconds must be positive!").color(NamedTextColor.RED));
                return true;
            }

            SettingsLoader.getSettings().getTimerSettings().setActiveSeconds(seconds);
            SettingsLoader.saveSettings();
            
            TimerState.setActiveTime(seconds);
            if (PluginTimer.getInstance() != null) {
                PluginTimer.getInstance().setActiveTimer(seconds);
            }
            
            sender.sendMessage(Component.text("Active phase set to: " + seconds + " seconds").color(NamedTextColor.GREEN));
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid number!").color(NamedTextColor.RED));
        }
        
        return true;
    }

    private boolean restTime(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /ytcraft timer rest <seconds>").color(NamedTextColor.RED));
            return true;
        }

        try {
            int seconds = Integer.parseInt(args[0]);
            if (seconds < 0) {
                sender.sendMessage(Component.text("Seconds must be positive!").color(NamedTextColor.RED));
                return true;
            }

            SettingsLoader.getSettings().getTimerSettings().setRestSeconds(seconds);
            SettingsLoader.saveSettings();
            
            TimerState.setRestTime(seconds);
            if (PluginTimer.getInstance() != null) {
                PluginTimer.getInstance().setRestTimer(seconds);
            }
            
            sender.sendMessage(Component.text("Rest phase set to: " + seconds + " seconds").color(NamedTextColor.GREEN));
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid number!").color(NamedTextColor.RED));
        }
        
        return true;
    }

    private boolean skip(CommandSender sender, String[] args) {
        PluginTimer.skipTimerMode();
        sender.sendMessage(Component.text("Skipping to next phase...").color(NamedTextColor.GREEN));
        return true;
    }

    private boolean reset(CommandSender sender, String[] args) {
        if (PluginTimer.getInstance() != null) {
            PluginTimer.getInstance().setActiveTimer(TimerState.getActiveTime());
            PluginTimer.getInstance().setRestTimer(TimerState.getRestTime());
        }
        sender.sendMessage(Component.text("Timer reset!").color(NamedTextColor.GREEN));
        return true;
    }

    @FunctionalInterface
    private interface SubAction {
        boolean execute(CommandSender sender, String[] args);
    }
}

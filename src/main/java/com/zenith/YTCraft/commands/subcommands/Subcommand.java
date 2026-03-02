package com.zenith.YTCraft.commands.subcommands;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface Subcommand {
    
    /**
     * Execute the subcommand
     * @param sender The command sender
     * @param args The command arguments (excluding the subcommand name itself)
     * @return true if command was handled successfully
     */
    boolean execute(CommandSender sender, String[] args);
    
    /**
     * Provide tab completion suggestions
     * @param sender The command sender
     * @param args The current arguments
     * @return List of suggestions
     */
    List<String> tabComplete(CommandSender sender, String[] args);
    
    /**
     * Get the name of this subcommand
     * @return The subcommand name
     */
    String getName();
    
    /**
     * Get the permission required to use this subcommand
     * @return Permission node, or null if no permission required
     */
    default String getPermission() {
        return null;
    }
    
    /**
     * Get the description of this subcommand
     * @return Description text
     */
    String getDescription();
}

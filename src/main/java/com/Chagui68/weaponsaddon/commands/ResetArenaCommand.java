package com.Chagui68.weaponsaddon.commands;

import com.Chagui68.weaponsaddon.listeners.BossAIHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to manually reset/destroy the Heavy Gunner arena.
 * Usage: /resetarena
 * Permission: militaryarsenal.admin
 */
public class ResetArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check permission
        if (!sender.hasPermission("militaryarsenal.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        // Execute arena reset
        try {
            BossAIHandler.destroyArena();
            sender.sendMessage(ChatColor.GREEN + "✓ Arena has been successfully reset!");
            sender.sendMessage(ChatColor.GRAY + "All arena blocks have been restored to their original state.");

            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.getWorld().setTime(1000); // Restore daytime
                sender.sendMessage(ChatColor.GRAY + "Time has been restored to day.");
            }

        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Error resetting arena: " + e.getMessage());
            return false;
        }

        return true;
    }
}

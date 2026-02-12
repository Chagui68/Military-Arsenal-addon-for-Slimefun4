package com.Chagui68.weaponsaddon.commands;

import com.Chagui68.weaponsaddon.listeners.BossAIHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main command for WeaponsAddon.
 * Usage: /weapons delete <arena|turrets>
 */
public class WeaponsCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label,
            String[] args) {
        if (!sender.hasPermission("militaryarsenal.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length < 2 || !args[0].equalsIgnoreCase("delete")) {
            sender.sendMessage(ChatColor.RED + "Usage: /weapons delete <arena|turrets>");
            return true;
        }

        String type = args[1].toLowerCase();

        switch (type) {
            case "arena":
                try {
                    BossAIHandler.destroyArena();
                    sender.sendMessage(ChatColor.GREEN + "✓ Arena has been successfully reset!");
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Error resetting arena: " + e.getMessage());
                }
                break;

            case "turrets":
                int count = 0;
                int ghostCount = 0;

                // 1. Remove ALL PVZ entities in the entire world
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        for (String tag : entity.getScoreboardTags()) {
                            if (tag.startsWith("PVZ_")) {
                                try {
                                    if (tag.startsWith("PVZ_TURRET_")) {
                                        String[] parts = tag.split("_");
                                        if (parts.length == 5) {
                                            int x = Integer.parseInt(parts[2]);
                                            int y = Integer.parseInt(parts[3]);
                                            int z = Integer.parseInt(parts[4]);
                                            Location loc = new Location(world, x, y, z);
                                            loc.getBlock().setType(Material.AIR);
                                            me.mrCookieSlime.Slimefun.api.BlockStorage.clearBlockInfo(loc);
                                        }
                                    }
                                } catch (Exception ignored) {
                                }
                                entity.remove();
                                count++;
                                break;
                            }
                        }
                    }
                }

                // 2. Remove orphans (ghosts) by scanning Slimefun storage near player
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    Location center = p.getLocation();
                    int radius = 100;

                    for (int x = -radius; x <= radius; x += 1) {
                        for (int z = -radius; z <= radius; z += 1) {
                            // Check all Y levels efficiently by only checking blocks known to Slimefun
                            Location column = center.clone().add(x, 0, z);
                            World w = center.getWorld();
                            for (int y = w.getMinHeight(); y < w.getMaxHeight(); y++) {
                                column.setY(y);
                                if (me.mrCookieSlime.Slimefun.api.BlockStorage.hasBlockInfo(column)) {
                                    String id = me.mrCookieSlime.Slimefun.api.BlockStorage.getLocationInfo(column,
                                            "id");
                                    if ("ATTACK_TURRET".equals(id)) {
                                        column.getBlock().setType(Material.AIR);
                                        me.mrCookieSlime.Slimefun.api.BlockStorage.clearBlockInfo(column);
                                        ghostCount++;
                                    }
                                }
                            }
                        }
                    }
                }

                sender.sendMessage(ChatColor.GREEN + "✓ Removed " + count + " active turret entities.");
                if (ghostCount > 0) {
                    sender.sendMessage(ChatColor.YELLOW + "⚠ Cleaned up " + ghostCount
                            + " persistent 'ghost' block data in radius.");
                }
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown type. Use 'arena' or 'turrets'.");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias,
            String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("delete");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            completions.add("arena");
            completions.add("turrets");
        }

        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}

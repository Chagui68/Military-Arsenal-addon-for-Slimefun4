package com.Chagui68.weaponsaddon.commands;

import org.bukkit.ChatColor;
import com.Chagui68.weaponsaddon.handlers.MilitaryMobHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

        if (args.length < 2 || (!args[0].equalsIgnoreCase("delete") && !args[0].equalsIgnoreCase("summon")
                && !args[0].equalsIgnoreCase("give"))) {
            sender.sendMessage(ChatColor.RED + "Usage: /weapons <delete|summon|give> <args>");
            return true;
        }

        String cmdType = args[0].toLowerCase();

        switch (cmdType) {
            case "delete":
                String deleteType = args[1].toLowerCase();
                if (deleteType.equals("arena")) {
                    try {
                        com.Chagui68.weaponsaddon.listeners.BossAIHandler.destroyArena();
                        sender.sendMessage(ChatColor.GREEN + "✓ Arena has been successfully reset!");
                        sender.sendMessage(
                                ChatColor.GRAY + "All arena blocks have been restored to their original state.");

                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            player.getWorld().setTime(1000); // Restore daytime
                            sender.sendMessage(ChatColor.GRAY + "Time has been restored to day.");
                        }
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "Error resetting arena: " + e.getMessage());
                    }
                } else if (deleteType.equals("turrets")) {
                    // Logic for turrets... (Keeping it simple for now as it's a large block)
                    sender.sendMessage(ChatColor.YELLOW + "Turret cleanup logic integration...");
                }
                break;

            case "summon":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /weapons summon <mob_type>");
                    return true;
                }

                Player p = (Player) sender;
                String mobType = args[1].toLowerCase();
                Location loc = p.getLocation();
                World world = loc.getWorld();

                try {
                    switch (mobType) {
                        case "crab":
                            PigZombie pigZombie = (PigZombie)  world
                                    .spawnEntity(loc,EntityType.ZOMBIFIED_PIGLIN);
                            MilitaryMobHandler.equipPigman(pigZombie);
                            sender.sendMessage(ChatColor.GREEN + "✓ Summoned Rusty Crab!");
                            break;
                        case "juan":
                            Horse horse = (Horse) world
                                    .spawnEntity(loc, EntityType.HORSE);
                            MilitaryMobHandler.equipHorseJuan(horse);
                            sender.sendMessage(ChatColor.GREEN + "✓ Summoned Juan!");
                            break;
                        case "king":
                            ZombieVillager king = (ZombieVillager) world
                                    .spawnEntity(loc, EntityType.ZOMBIE_VILLAGER);
                            MilitaryMobHandler.equipKing(king);
                            sender.sendMessage(ChatColor.GREEN + "✓ Summoned The King!");
                            break;
                        case "warrior":
                            Zombie warrior = (Zombie) world
                                    .spawnEntity(loc, EntityType.ZOMBIE);
                            MilitaryMobHandler.equipWarrior(warrior);
                            sender.sendMessage(ChatColor.GREEN + "✓ Summoned a Warrior!");
                            break;
                        case "pusher":
                            Zombie pusher = (Zombie) world
                                    .spawnEntity(loc, EntityType.ZOMBIE);
                            MilitaryMobHandler.equipPusher(pusher);
                            sender.sendMessage(ChatColor.GREEN + "✓ Summoned a Pusher!");
                            break;
                        case "elite_killer":
                            Zombie killer = (Zombie) world
                                    .spawnEntity(loc, EntityType.ZOMBIE);
                            MilitaryMobHandler.equipEliteKiller(killer);
                            sender.sendMessage(ChatColor.GREEN + "✓ Summoned an Elite Killer!");
                            break;
                        case "heavy_gunner":
                            Skeleton boss = (Skeleton) world
                                    .spawnEntity(loc, EntityType.SKELETON);
                            MilitaryMobHandler.equipHeavyGunner(boss);
                            sender.sendMessage(ChatColor.GREEN + "✓ Summoned the Heavy Gunner boss!");
                            break;
                        case "elite_ranger":
                            Skeleton ranger = (Skeleton) world
                                    .spawnEntity(loc, EntityType.SKELETON);
                            MilitaryMobHandler.equipEliteRanger(ranger);
                            sender.sendMessage(ChatColor.GREEN + "✓ Summoned an Elite Ranger!");
                            break;
                        case "battle_witch":
                            Witch witch = (Witch) world
                                    .spawnEntity(loc, EntityType.WITCH);
                            MilitaryMobHandler.equipBattleWitch(witch);
                            sender.sendMessage(ChatColor.GREEN + "✓ Summoned a Battle Witch!");
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED
                                    + "Unknown mob type. Use: king, warrior, pusher, elite_killer, heavy_gunner, elite_ranger, battle_witch.");
                            break;
                    }
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Error summoning " + mobType + ": " + e.getMessage());
                }
                break;

            case "give":
                if (!(sender instanceof Player) && args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /weapons give <item> <player>");
                    return true;
                }

                Player target;
                if (args.length >= 3) {
                    target = org.bukkit.Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + "Player not found!");
                        return true;
                    }
                } else {
                    target = (Player) sender;
                }

                String itemType = args[1].toLowerCase();
                ItemStack itemToGive = null;

                switch (itemType) {
                    case "pushers_piston":
                        itemToGive = MilitaryMobHandler.getPusherStick();
                        break;
                    case "kings_sword":
                        itemToGive = MilitaryMobHandler.getKingsSword();
                        break;
                    case "kings_crown":
                        itemToGive = MilitaryMobHandler.getKingsCrown();
                        break;
                    default:
                        sender.sendMessage(ChatColor.RED + "Unknown item type. Use: kings_sword, kings_crown.");
                        return true;
                }

                if (itemToGive != null) {
                    target.getInventory().addItem(itemToGive);
                    sender.sendMessage(ChatColor.GREEN + "✓ Given " + itemType + " to " + target.getName() + "!");
                }
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown type. Use 'delete', 'summon', or 'give'.");
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
            completions.add("summon");
            completions.add("give");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete")) {
                completions.add("arena");
                completions.add("turrets");
            } else if (args[0].equalsIgnoreCase("summon")) {
                completions.add("king");
                completions.add("warrior");
                completions.add("pusher");
                completions.add("elite_killer");
                completions.add("heavy_gunner");
                completions.add("elite_ranger");
                completions.add("battle_witch");
                completions.add("juan");
                completions.add("crab");
            } else if (args[0].equalsIgnoreCase("give")) {
                completions.add("kings_sword");
                completions.add("kings_crown");
                completions.add("pushers_piston");
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return null; // Return null to show player names
        }

        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}

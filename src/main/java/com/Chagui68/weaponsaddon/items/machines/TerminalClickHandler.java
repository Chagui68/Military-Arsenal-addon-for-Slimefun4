package com.Chagui68.weaponsaddon.items.machines;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import com.Chagui68.weaponsaddon.items.machines.energy.EnergyManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TerminalClickHandler implements Listener {

    private static final Map<UUID, Location> awaitingCoordinates = new HashMap<>();
    private static final Map<UUID, Inventory> playerInventories = new HashMap<>();
    private static final Map<UUID, Location> playerTerminalLocations = new HashMap<>();
    private static JavaPlugin plugin;

    public static void setPlugin(JavaPlugin p) {
        plugin = p;
    }

    public static void registerInventory(Player p, Inventory inv, Location blockLoc) {
        playerInventories.put(p.getUniqueId(), inv);
        playerTerminalLocations.put(p.getUniqueId(), blockLoc);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        String title = e.getView().getTitle();
        if (!title.equals(ChatColor.DARK_RED + "Bombardment Terminal")) return;

        Player p = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();

        if (slot == 11 || slot == 16) {
            return;
        }

        if (slot == 22) {
            e.setCancelled(true);

            Location terminalLoc = playerTerminalLocations.get(p.getUniqueId());
            if (terminalLoc == null) {
                p.sendMessage(ChatColor.RED + "[Terminal] Error: location not found");
                return;
            }

            int currentEnergy = EnergyManager.getCharge(terminalLoc);
            Inventory inv = e.getInventory();
            ItemStack tntSlot = inv.getItem(11);
            ItemStack starSlot = inv.getItem(16);

            int tntCount = (tntSlot != null && tntSlot.getType() == Material.TNT) ? tntSlot.getAmount() : 0;
            int starCount = (starSlot != null && starSlot.getType() == Material.NETHER_STAR) ? starSlot.getAmount() : 0;

            if (currentEnergy < BombardmentTerminal.getEnergyRequired()) {
                p.sendMessage(ChatColor.RED + "✗ [Terminal] Insufficient energy!");
                p.sendMessage(ChatColor.GRAY + "You need: " + formatEnergy(BombardmentTerminal.getEnergyRequired() - currentEnergy) + " J more");
                p.sendMessage(ChatColor.YELLOW + "Connect to Slimefun power grid");
                return;
            }

            if (tntCount >= 10 && starCount >= 5) {
                if (tntCount == 10) {
                    inv.setItem(11, new ItemStack(Material.AIR));
                } else {
                    tntSlot.setAmount(tntCount - 10);
                }

                if (starCount == 5) {
                    inv.setItem(16, new ItemStack(Material.AIR));
                } else {
                    starSlot.setAmount(starCount - 5);
                }

                EnergyManager.removeCharge(terminalLoc, BombardmentTerminal.getEnergyRequired());
                p.closeInventory();
                p.sendMessage(ChatColor.GREEN + "✓ [Terminal] Resources consumed:");
                p.sendMessage(ChatColor.GRAY + " • 10 TNT");
                p.sendMessage(ChatColor.GRAY + " • 5 Nether Stars");
                p.sendMessage(ChatColor.AQUA + " • 2,000,000 J energy");
                p.sendMessage(ChatColor.YELLOW + "→ [Terminal] Enter coordinates: X Y Z");
                p.sendMessage(ChatColor.GRAY + "Example: 100 64 -200");
                awaitingCoordinates.put(p.getUniqueId(), terminalLoc);
            } else {
                p.sendMessage(ChatColor.RED + "✗ [Terminal] Insufficient resources!");
                p.sendMessage(ChatColor.GRAY + "You need:");
                if (tntCount < 10) p.sendMessage(ChatColor.YELLOW + " • " + (10 - tntCount) + " more TNT");
                if (starCount < 5) p.sendMessage(ChatColor.YELLOW + " • " + (5 - starCount) + " more Nether Stars");
            }

            return;
        }

        if (slot < 27) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;

        String title = e.getView().getTitle();
        if (!title.equals(ChatColor.DARK_RED + "Bombardment Terminal")) return;

        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();

        ItemStack tntSlot = inv.getItem(11);
        ItemStack starSlot = inv.getItem(16);

        if (tntSlot != null && tntSlot.getType() == Material.TNT) {
            p.getInventory().addItem(tntSlot);
        }

        if (starSlot != null && starSlot.getType() == Material.NETHER_STAR) {
            p.getInventory().addItem(starSlot);
        }

        playerInventories.remove(p.getUniqueId());
        playerTerminalLocations.remove(p.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        Location loc = b.getLocation();

        if (BlockStorage.check(loc, "BOMBARDMENT_TERMINAL")) {
            BombardmentTerminal.removeSatelliteModel(loc);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (!awaitingCoordinates.containsKey(p.getUniqueId())) return;

        e.setCancelled(true);
        String message = e.getMessage();
        String[] parts = message.split(" ");

        if (parts.length != 3) {
            p.sendMessage(ChatColor.RED + "[Terminal] Invalid format. Use: X Y Z");
            p.sendMessage(ChatColor.GRAY + "Example: 100 64 -200");
            return;
        }

        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);

            if (y < -64 || y > 320) {
                p.sendMessage(ChatColor.RED + "[Terminal] Invalid Y coordinate (range: -64 to 320)");
                return;
            }

            Location target = new Location(p.getWorld(), x, y, z);
            awaitingCoordinates.remove(p.getUniqueId());

            p.sendMessage(ChatColor.GREEN + "✓ [Terminal] Coordinates confirmed: " + x + " " + y + " " + z);
            p.sendMessage(ChatColor.DARK_RED + "⚠ [Terminal] BOMBARDMENT INITIATED");
            p.sendMessage(ChatColor.GRAY + "Impact in 3 seconds...");

            Bukkit.getScheduler().runTask(WeaponsAddon.getInstance(), () -> {
                AirstrikeExecutor.executeBombardment(target, p);
            });

        } catch (NumberFormatException ex) {
            p.sendMessage(ChatColor.RED + "[Terminal] Invalid coordinates. Use whole numbers.");
            p.sendMessage(ChatColor.GRAY + "Example: 100 64 -200");
        }
    }

    private String formatEnergy(int energy) {
        if (energy >= 1000000) {
            return String.format("%.1fM", energy / 1000000.0);
        } else if (energy >= 1000) {
            return String.format("%.1fK", energy / 1000.0);
        }
        return String.valueOf(energy);
    }
}

package com.Chagui68.weaponsaddon.items.turrets;

import com.Chagui68.weaponsaddon.items.machines.energy.EnergyManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.createInventory;

public class TurretUpgradeGUI implements Listener {
    private static final Map<UUID, TurretSession> openSessions = new HashMap<>();

    public static void open(Player player, String turretId, String turretName, org.bukkit.Location loc, double baseRange, double baseDamage) {
        int currentLevel = TurretUpgradeManager.getCurrentLevel(loc);
        int maxLevel = TurretUpgradeManager.getMaxLevel(turretId);
        int energy = EnergyManager.getCharge(loc);
        String prefix = turretId.contains("ATTACK") ? "attack_tower" : "rapid_tower";
        double currentRange = TurretUpgradeManager.getRangeForLevel(baseRange, currentLevel);
        double currentDamage = TurretUpgradeManager.getDamageForLevel(baseDamage, currentLevel);
        Inventory inv = createInventory(null, 54, ChatColor.DARK_RED + turretName + " " + ChatColor.GRAY + "Lv." + currentLevel);
        ItemStack bg = item(Material.BLACK_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, bg);
        }
        inv.setItem(4, item(Material.NETHERITE_BLOCK, ChatColor.GOLD + turretName, "", ChatColor.YELLOW + "Level: " + ChatColor.GREEN + currentLevel + ChatColor.GRAY + "/" + maxLevel, ChatColor.YELLOW + "Energy: " + ChatColor.AQUA + energy + " J"));
        inv.setItem(19, item(Material.ARROW, ChatColor.AQUA + "Range", "", ChatColor.WHITE + "Current: " + ChatColor.GREEN + String.format("%.1f", currentRange) + " blocks"));
        inv.setItem(21, item(Material.REDSTONE, ChatColor.RED + "Damage", "", ChatColor.WHITE + "Current: " + ChatColor.GREEN + String.format("%.1f", currentDamage) + " HP"));
        inv.setItem(23, item(Material.LIGHTNING_ROD, ChatColor.YELLOW + "Energy/Shot", "", ChatColor.WHITE + "Cost: " + ChatColor.AQUA + EnergyManager.getCharge(loc) + " J"));
        inv.setItem(25, item(Material.EXPERIENCE_BOTTLE, ChatColor.LIGHT_PURPLE + "Level", "", ChatColor.WHITE + "Player XP: " + ChatColor.GREEN + player.getLevel()));
        if (currentLevel < maxLevel) {
            TurretUpgradeManager.UpgradeRequirement req = TurretUpgradeManager.getRequirementForLevel(turretId, currentLevel);
            if (req != null) {
                boolean hasSpace = TurretUpgradeManager.hasSpaceForUpgrade(loc, prefix, currentLevel);
                boolean canUpgrade = TurretUpgradeManager.canUpgrade(player, turretId, currentLevel);
                List<String> reqLore = new ArrayList<>();
                reqLore.add("");
                reqLore.add(ChatColor.GOLD + "Requirements for Level " + (currentLevel + 1) + ":");
                reqLore.add(ChatColor.YELLOW + "XP Levels: " + ChatColor.WHITE + req.xpLevels + (player.getLevel() >= req.xpLevels ? ChatColor.GREEN + " ✓" : ChatColor.RED + " ✗"));
                reqLore.add(ChatColor.YELLOW + "Range Bonus: " + ChatColor.GREEN + "+" + req.rangeBonus + " blocks");
                reqLore.add("");
                for (ItemStack item : req.items) {
                    boolean has = TurretUpgradeManager.hasItemInInventory(player, item);
                    String itemName = item.getItemMeta() != null && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : formatMaterialName(item.getType());
                    reqLore.add(ChatColor.GRAY + "- " + ChatColor.WHITE + itemName + " x" + item.getAmount() + (has ? ChatColor.GREEN + " ✓" : ChatColor.RED + " ✗"));
                }
                reqLore.add("");
                if (!hasSpace) {
                    reqLore.add(ChatColor.RED + "No space above for upgrade!");
                } else {
                    reqLore.add(ChatColor.GREEN + "Space available ✓");
                }
                Material upgradeMat = canUpgrade && hasSpace ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
                ChatColor upgradeColor = canUpgrade && hasSpace ? ChatColor.GREEN : ChatColor.RED;
                String upgradeText = canUpgrade && hasSpace ? "Click to Upgrade!" : "Requirements not met";
                List<String> upgradeLore = new ArrayList<>(reqLore);
                upgradeLore.add("");
                upgradeLore.add(upgradeColor + upgradeText);
                inv.setItem(40, item(upgradeMat, upgradeColor + "⬆ UPGRADE", upgradeLore.toArray(new String[0])));
            }
        } else {
            inv.setItem(40, item(Material.NETHER_STAR, ChatColor.GOLD + "MAX LEVEL", "", ChatColor.GREEN + "This turret is fully upgraded!"));
        }
        inv.setItem(49, item(Material.BARRIER, ChatColor.RED + "✖ Close"));
        player.openInventory(inv);
        openSessions.put(player.getUniqueId(), new TurretSession(turretId, turretName, loc, baseRange, baseDamage));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        String title = e.getView().getTitle();
        if (!title.contains("Lv.")) return;
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();
        if (slot == 49) {
            player.closeInventory();
            return;
        }
        if (slot == 40) {
            TurretSession session = openSessions.get(player.getUniqueId());
            if (session == null) return;
            int currentLevel = TurretUpgradeManager.getCurrentLevel(session.loc);
            int maxLevel = TurretUpgradeManager.getMaxLevel(session.turretId);
            if (currentLevel >= maxLevel) return;
            if (!TurretUpgradeManager.canUpgrade(player, session.turretId, currentLevel)) {
                player.sendMessage(ChatColor.RED + "You don't meet the upgrade requirements!");
                player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
                return;
            }
            String prefix = session.turretId.contains("ATTACK") ? "attack_tower" : "rapid_tower";
            if (!TurretUpgradeManager.hasSpaceForUpgrade(session.loc, prefix, currentLevel)) {
                player.sendMessage(ChatColor.RED + "Not enough space above the turret!");
                player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
                return;
            }
            TurretUpgradeManager.applyUpgrade(player, session.loc, session.turretId, prefix);
            player.sendMessage(ChatColor.GREEN + "Turret upgraded to level " + (currentLevel + 1) + "!");
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.5f);
            player.closeInventory();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        String title = e.getView().getTitle();
        if (title.contains("Lv.")) {
            openSessions.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        String title = e.getView().getTitle();
        if (title.contains("Lv.")) {
            e.setCancelled(true);
        }
    }

    private static String formatMaterialName(Material mat) {
        String name = mat.name().toLowerCase().replace("_", " ");
        StringBuilder sb = new StringBuilder();
        for (String word : name.split(" ")) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }

    private static ItemStack item(Material material, String name, String... lore) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        if (lore.length > 0) {
            List<String> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(line);
            }
            meta.setLore(loreList);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    private static class TurretSession {
        final String turretId;
        final String turretName;
        final org.bukkit.Location loc;
        final double baseRange;
        final double baseDamage;

        TurretSession(String turretId, String turretName, org.bukkit.Location loc, double baseRange, double baseDamage) {
            this.turretId = turretId;
            this.turretName = turretName;
            this.loc = loc;
            this.baseRange = baseRange;
            this.baseDamage = baseDamage;
        }
    }
}


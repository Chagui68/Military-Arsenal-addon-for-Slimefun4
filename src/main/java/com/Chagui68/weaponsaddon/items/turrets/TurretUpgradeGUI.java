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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.createInventory;

public class TurretUpgradeGUI implements Listener {
    private static final Map<UUID, TurretSession> OPEN_SESSIONS = new HashMap<>();

    public static void open(
            Player player,
            String turretId,
            String turretName,
            org.bukkit.Location loc,
            double baseRange,
            double baseDamage,
            int baseCapacity,
            int baseEnergyCost
    ) {
        int currentLevel = TurretUpgradeManager.getCurrentLevel(loc);
        int maxLevel = TurretUpgradeManager.getMaxLevel(turretId);
        int energy = EnergyManager.getCharge(loc);
        String prefix = turretId.contains("ATTACK") ? "attack_tower" : "rapid_tower";

        Inventory inv = createInventory(
                null,
                54,
                ChatColor.DARK_RED + turretName + " " + ChatColor.GRAY + "Lv." + currentLevel
        );

        ItemStack background = item(Material.BLACK_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, background);
        }

        inv.setItem(4, item(
                Material.NETHERITE_BLOCK,
                ChatColor.GOLD + turretName,
                "",
                ChatColor.YELLOW + "Level: " + ChatColor.GREEN + currentLevel + ChatColor.GRAY + "/" + maxLevel,
                ChatColor.YELLOW + "Energy: " + ChatColor.AQUA + energy + " / "
                        + TurretUpgradeManager.getCapacityForLevel(baseCapacity, currentLevel) + " J",
                ChatColor.YELLOW + "Energy/Shot: " + ChatColor.AQUA
                        + TurretUpgradeManager.getEnergyCostForLevel(baseEnergyCost, currentLevel) + " J"
        ));

        inv.setItem(11, item(Material.LIGHT_BLUE_STAINED_GLASS_PANE, ChatColor.AQUA + "⬆ Progression"));
        for (int i = 0; i < 4; i++) {
            int level = i + 1;
            if (level <= maxLevel) {
                inv.setItem(
                        12 + i,
                        buildLevelCard(
                                turretId,
                                baseRange,
                                baseDamage,
                                baseCapacity,
                                baseEnergyCost,
                                level,
                                currentLevel,
                                player
                        )
                );
            }
        }
        inv.setItem(16, item(Material.CYAN_STAINED_GLASS_PANE, ChatColor.AQUA + "📊 Stats"));

        double currentRange = TurretUpgradeManager.getRangeForLevel(baseRange, currentLevel);
        double currentDamage = TurretUpgradeManager.getDamageForLevel(baseDamage, currentLevel);

        List<String> rangeLore = new ArrayList<>();
        rangeLore.add(ChatColor.WHITE + "Current: " + ChatColor.GREEN + String.format("%.1f", currentRange) + " blocks");
        if (currentLevel < maxLevel) {
            rangeLore.add(ChatColor.WHITE + "Next: " + ChatColor.AQUA
                    + String.format("%.1f", TurretUpgradeManager.getRangeForLevel(baseRange, currentLevel + 1))
                    + " blocks");
        }
        inv.setItem(19, item(Material.ARROW, ChatColor.AQUA + "Range", rangeLore.toArray(new String[0])));

        List<String> damageLore = new ArrayList<>();
        damageLore.add(ChatColor.WHITE + "Current: " + ChatColor.GREEN + String.format("%.1f", currentDamage) + " HP");
        if (currentLevel < maxLevel) {
            damageLore.add(ChatColor.WHITE + "Next: " + ChatColor.AQUA
                    + String.format("%.1f", TurretUpgradeManager.getDamageForLevel(baseDamage, currentLevel + 1))
                    + " HP");
        }
        inv.setItem(20, item(Material.REDSTONE, ChatColor.RED + "Damage", damageLore.toArray(new String[0])));

        List<String> costLore = new ArrayList<>();
        costLore.add(ChatColor.WHITE + "Current: " + ChatColor.AQUA
                + TurretUpgradeManager.getEnergyCostForLevel(baseEnergyCost, currentLevel) + " J");
        if (currentLevel < maxLevel) {
            costLore.add(ChatColor.WHITE + "Next: " + ChatColor.GREEN
                    + TurretUpgradeManager.getEnergyCostForLevel(baseEnergyCost, currentLevel + 1) + " J");
        }
        inv.setItem(21, item(Material.LIGHTNING_ROD, ChatColor.YELLOW + "Energy/Shot", costLore.toArray(new String[0])));

        List<String> capacityLore = new ArrayList<>();
        capacityLore.add(ChatColor.WHITE + "Current: " + ChatColor.AQUA
                + TurretUpgradeManager.getCapacityForLevel(baseCapacity, currentLevel) + " J");
        if (currentLevel < maxLevel) {
            capacityLore.add(ChatColor.WHITE + "Next: " + ChatColor.GREEN
                    + TurretUpgradeManager.getCapacityForLevel(baseCapacity, currentLevel + 1) + " J");
        }
        inv.setItem(22, item(Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE + "Capacity", capacityLore.toArray(new String[0])));

        inv.setItem(23, item(
                Material.EXPERIENCE_BOTTLE,
                ChatColor.LIGHT_PURPLE + "Player XP",
                "",
                ChatColor.WHITE + "Your XP: " + ChatColor.GREEN + player.getLevel()
        ));

        if (currentLevel < maxLevel) {
            TurretUpgradeManager.UpgradeRequirement requirement =
                    TurretUpgradeManager.getRequirementForLevel(turretId, currentLevel);

            if (requirement != null) {
                boolean hasSpace = TurretUpgradeManager.hasSpaceForUpgrade(loc, prefix, currentLevel);
                boolean canUpgrade = TurretUpgradeManager.canUpgrade(player, turretId, currentLevel);

                inv.setItem(27, item(
                        Material.RED_STAINED_GLASS_PANE,
                        ChatColor.RED + "Requirements for Level " + (currentLevel + 1)
                ));

                List<String> xpLore = new ArrayList<>();
                xpLore.add(ChatColor.WHITE + "Needed: " + ChatColor.GOLD + requirement.xpLevels);
                xpLore.add(player.getLevel() >= requirement.xpLevels
                        ? ChatColor.GREEN + "You have enough ✓"
                        : ChatColor.RED + "You need " + (requirement.xpLevels - player.getLevel()) + " more");
                inv.setItem(29, item(Material.EXPERIENCE_BOTTLE, ChatColor.LIGHT_PURPLE + "XP Levels", xpLore.toArray(new String[0])));

                int slot = 30;
                for (ItemStack required : requirement.items) {
                    boolean has = TurretUpgradeManager.hasItemInInventory(player, required);
                    String itemName = required.getItemMeta() != null && required.getItemMeta().hasDisplayName()
                            ? required.getItemMeta().getDisplayName()
                            : formatMaterialName(required.getType());

                    List<String> itemLore = new ArrayList<>();
                    itemLore.add(ChatColor.WHITE + "Amount: " + ChatColor.GOLD + required.getAmount());
                    itemLore.add(has ? ChatColor.GREEN + "You have it ✓" : ChatColor.RED + "Missing ✗");
                    inv.setItem(slot, item(required.getType(), ChatColor.GOLD + itemName, itemLore.toArray(new String[0])));
                    slot++;
                }

                List<String> spaceLore = new ArrayList<>();
                spaceLore.add(hasSpace
                        ? ChatColor.GREEN + "Space available ✓"
                        : ChatColor.RED + "The next tower level would collide with another block!");
                inv.setItem(34, item(Material.OAK_SAPLING, ChatColor.GREEN + "Growth Space", spaceLore.toArray(new String[0])));

                Material upgradeMaterial = canUpgrade && hasSpace
                        ? Material.LIME_STAINED_GLASS_PANE
                        : Material.RED_STAINED_GLASS_PANE;
                ChatColor upgradeColor = canUpgrade && hasSpace ? ChatColor.GREEN : ChatColor.RED;
                String upgradeText = canUpgrade && hasSpace ? "Click to Upgrade!" : "Requirements not met";

                inv.setItem(40, item(
                        upgradeMaterial,
                        upgradeColor + "⬆ UPGRADE TO LEVEL " + (currentLevel + 1),
                        "",
                        ChatColor.GRAY + "The tower will grow taller",
                        upgradeColor + upgradeText
                ));
            }
        } else {
            inv.setItem(40, item(
                    Material.NETHER_STAR,
                    ChatColor.GOLD + "MAX LEVEL",
                    "",
                    ChatColor.GREEN + "This turret is fully upgraded!"
            ));
        }

        inv.setItem(49, item(Material.BARRIER, ChatColor.RED + "✖ Close"));
        OPEN_SESSIONS.put(
                player.getUniqueId(),
                new TurretSession(turretId, turretName, loc, baseRange, baseDamage, baseCapacity, baseEnergyCost)
        );
        player.openInventory(inv);
    }

    private static ItemStack buildLevelCard(
            String turretId,
            double baseRange,
            double baseDamage,
            int baseCapacity,
            int baseEnergyCost,
            int level,
            int currentLevel,
            Player player
    ) {
        boolean upgraded = level < currentLevel;
        boolean current = level == currentLevel;
        Material material = current
                ? Material.GOLD_BLOCK
                : (upgraded ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
        ChatColor color = current ? ChatColor.GOLD : (upgraded ? ChatColor.GREEN : ChatColor.DARK_GRAY);
        String name = color + "Lv." + level
                + (current ? " ◀ CURRENT" : (upgraded ? " ✓ UPGRADED" : " 🔒 LOCKED"));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Range: " + ChatColor.GREEN
                + String.format("%.1f", TurretUpgradeManager.getRangeForLevel(baseRange, level)) + " blocks");
        lore.add(ChatColor.WHITE + "Damage: " + ChatColor.GREEN
                + String.format("%.1f", TurretUpgradeManager.getDamageForLevel(baseDamage, level)) + " HP");
        lore.add(ChatColor.WHITE + "Capacity: " + ChatColor.GREEN
                + TurretUpgradeManager.getCapacityForLevel(baseCapacity, level) + " J");
        lore.add(ChatColor.WHITE + "Energy/Shot: " + ChatColor.GREEN
                + TurretUpgradeManager.getEnergyCostForLevel(baseEnergyCost, level) + " J");

        if (level > 1) {
            TurretUpgradeManager.UpgradeRequirement requirement =
                    TurretUpgradeManager.getRequirementForLevel(turretId, level - 1);
            if (requirement != null) {
                lore.add("");
                lore.add(ChatColor.GOLD + "Cost to reach this level:");
                lore.add(ChatColor.YELLOW + "XP: " + ChatColor.WHITE + requirement.xpLevels);
                for (ItemStack required : requirement.items) {
                    String itemName = required.getItemMeta() != null && required.getItemMeta().hasDisplayName()
                            ? required.getItemMeta().getDisplayName()
                            : formatMaterialName(required.getType());
                    boolean has = player != null && TurretUpgradeManager.hasItemInInventory(player, required);
                    lore.add(ChatColor.GRAY + "- " + ChatColor.WHITE + itemName + " x" + required.getAmount()
                            + (has ? ChatColor.GREEN + " ✓" : ChatColor.RED + " ✗"));
                }
            }
        }

        return item(material, name, lore.toArray(new String[0]));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) {
            return;
        }

        TurretSession session = OPEN_SESSIONS.get(player.getUniqueId());
        if (session == null || !e.getView().getTitle().contains("Lv.")) {
            return;
        }

        e.setCancelled(true);
        int slot = e.getRawSlot();
        if (slot == 49) {
            player.closeInventory();
            return;
        }
        if (slot != 40) {
            return;
        }

        int currentLevel = TurretUpgradeManager.getCurrentLevel(session.loc);
        int maxLevel = TurretUpgradeManager.getMaxLevel(session.turretId);
        if (currentLevel >= maxLevel) {
            return;
        }

        if (!TurretUpgradeManager.canUpgrade(player, session.turretId, currentLevel)) {
            player.sendMessage(ChatColor.RED + "You don't meet the upgrade requirements!");
            player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
            return;
        }

        String prefix = session.turretId.contains("ATTACK") ? "attack_tower" : "rapid_tower";
        if (!TurretUpgradeManager.hasSpaceForUpgrade(session.loc, prefix, currentLevel)) {
            player.sendMessage(ChatColor.RED + "The next turret level would collide with another block!");
            player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
            return;
        }

        if (!TurretUpgradeManager.applyUpgrade(player, session.loc, session.turretId, prefix)) {
            player.sendMessage(ChatColor.RED + "The turret upgrade could not be applied safely.");
            player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
            return;
        }

        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.5f);
        open(
                player,
                session.turretId,
                session.turretName,
                session.loc,
                session.baseRange,
                session.baseDamage,
                session.baseCapacity,
                session.baseEnergyCost
        );
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player player && e.getView().getTitle().contains("Lv.")) {
            OPEN_SESSIONS.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getWhoClicked() instanceof Player player
                && OPEN_SESSIONS.containsKey(player.getUniqueId())
                && e.getView().getTitle().contains("Lv.")) {
            e.setCancelled(true);
        }
    }

    private static String formatMaterialName(Material material) {
        String name = material.name().toLowerCase().replace("_", " ");
        StringBuilder sb = new StringBuilder();
        for (String word : name.split(" ")) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(' ');
            }
        }
        return sb.toString().trim();
    }

    private static ItemStack item(Material material, String name, String... lore) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        if (lore.length > 0) {
            meta.setLore(List.of(lore));
        }
        stack.setItemMeta(meta);
        return stack;
    }

    private static final class TurretSession {
        private final String turretId;
        private final String turretName;
        private final org.bukkit.Location loc;
        private final double baseRange;
        private final double baseDamage;
        private final int baseCapacity;
        private final int baseEnergyCost;

        private TurretSession(
                String turretId,
                String turretName,
                org.bukkit.Location loc,
                double baseRange,
                double baseDamage,
                int baseCapacity,
                int baseEnergyCost
        ) {
            this.turretId = turretId;
            this.turretName = turretName;
            this.loc = loc;
            this.baseRange = baseRange;
            this.baseDamage = baseDamage;
            this.baseCapacity = baseCapacity;
            this.baseEnergyCost = baseEnergyCost;
        }
    }
}

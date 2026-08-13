package com.Chagui68.weaponsaddon.items.turrets;

import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TurretUpgradeManager {
    private static final Map<String, UpgradeRequirement[]> UPGRADE_REQUIREMENTS = new HashMap<>();

    static {
        UPGRADE_REQUIREMENTS.put("MA_ATTACK_TURRET", new UpgradeRequirement[]{new UpgradeRequirement
                (5, new ItemStack[]{MilitaryComponents.TARGETING_SYSTEM},
                        2), new UpgradeRequirement(10, new ItemStack[]
                {MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.ENERGY_MATRIX},
                4), new UpgradeRequirement(15, new ItemStack[]
                {MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.ENERGY_MATRIX,
                        MilitaryComponents.QUANTUM_PROCESSOR}, 6)});
        UPGRADE_REQUIREMENTS.put("MA_MACHINE_GUN_TURRET",
                new UpgradeRequirement[]{new UpgradeRequirement(5,
                        new ItemStack[]{MilitaryComponents.MOVEMENT_CIRCUIT},
                        2), new UpgradeRequirement(10,
                        new ItemStack[]{MilitaryComponents.MOVEMENT_CIRCUIT,
                                MilitaryComponents.KINETIC_STABILIZER},
                        4), new UpgradeRequirement(15,
                        new ItemStack[]{MilitaryComponents.MOVEMENT_CIRCUIT,
                                MilitaryComponents.KINETIC_STABILIZER,
                                MilitaryComponents.QUANTUM_PROCESSOR}, 6)});
    }

    public static int getCurrentLevel(Location loc) {
        String levelStr = BlockStorage.getLocationInfo(loc, "turret-level");
        if (levelStr != null) {
            try {
                return Integer.parseInt(levelStr);
            } catch (NumberFormatException e) {
                return 1;
            }
        }
        return 1;
    }

    public static void setLevel(Location loc, int level) {
        BlockStorage.addBlockInfo(loc, "turret-level", String.valueOf(level));
    }

    public static double getRangeForLevel(double baseRange, int level) {
        return baseRange + (level - 1) * 2.0;
    }

    public static double getDamageForLevel(double baseDamage, int level) {
        return baseDamage * (1.0 + (level - 1) * 0.15);
    }

    public static int getCapacityForLevel(int baseCapacity, int level) {
        return (int) Math.round(baseCapacity * (1.0 + (level - 1) * 0.25));
    }

    public static int getEnergyCostForLevel(int baseCost, int level) {
        return Math.max(1, (int) Math.round(baseCost * (1.0 - (level - 1) * 0.10)));
    }

    public static int getShotCooldownForLevel(int baseCooldown, int level) {
        return Math.max(0, baseCooldown - (level - 1));
    }

    public static UpgradeRequirement getRequirementForLevel(String turretId, int currentLevel) {
        UpgradeRequirement[] reqs = UPGRADE_REQUIREMENTS.get(turretId);
        if (reqs == null || currentLevel < 1 || currentLevel > reqs.length) {
            return null;
        }
        return reqs[currentLevel - 1];
    }

    public static int getMaxLevel(String turretId) {
        UpgradeRequirement[] reqs = UPGRADE_REQUIREMENTS.get(turretId);
        return reqs != null ? reqs.length + 1 : 1;
    }

    public static boolean canUpgrade(Player player, String turretId, int currentLevel) {
        UpgradeRequirement req = getRequirementForLevel(turretId, currentLevel);
        if (req == null) return false;
        if (player.getLevel() < req.xpLevels) return false;
        for (ItemStack required : req.items) {
            if (!hasItemInInventory(player, required)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasItemInInventory(Player player, ItemStack required) {
        int needed = required.getAmount();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(required)) {
                needed -= item.getAmount();
                if (needed <= 0) return true;
            }
        }
        return false;
    }

    public static void consumeUpgradeItems(Player player, UpgradeRequirement req) {
        player.setLevel(player.getLevel() - req.xpLevels);
        for (ItemStack required : req.items) {
            int needed = required.getAmount();
            for (int i = 0; i < player.getInventory().getSize() && needed > 0; i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item != null && item.isSimilar(required)) {
                    int take = Math.min(item.getAmount(), needed);
                    item.setAmount(item.getAmount() - take);
                    needed -= take;
                }
            }
        }
    }

    public static boolean hasSpaceForUpgrade(Location baseLoc, String prefix, int currentLevel) {
        int maxHeight = TurretStructureManager.getMaxHeight(prefix);
        World world = baseLoc.getWorld();
        if (world == null) return false;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = 0; y <= maxHeight; y++) {
                    Material type = baseLoc.clone().add(x, y, z).getBlock().getType();
                    if (type != Material.AIR && type != Material.CAVE_AIR && type != Material.VOID_AIR && type != Material.LIGHT && type != Material.STRUCTURE_BLOCK) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void applyUpgrade(Player player, Location loc, String turretId, String prefix) {
        int currentLevel = getCurrentLevel(loc);
        UpgradeRequirement req = getRequirementForLevel(turretId, currentLevel);
        if (req == null) return;
        consumeUpgradeItems(player, req);
        TurretStructureManager.removeStructure(loc, TurretStructureManager.getMaxHeight(prefix));
        setLevel(loc, currentLevel + 1);
        String newStructure = TurretStructureManager.getStructureName(prefix, currentLevel + 1);
        TurretStructureManager.placeStructure(loc, newStructure);
    }

    public static class UpgradeRequirement {
        public final int xpLevels;
        public final ItemStack[] items;
        public final int rangeBonus;

        public UpgradeRequirement(int xpLevels, ItemStack[] items, int rangeBonus) {
            this.xpLevels = xpLevels;
            this.items = items;
            this.rangeBonus = rangeBonus;
        }
    }
}


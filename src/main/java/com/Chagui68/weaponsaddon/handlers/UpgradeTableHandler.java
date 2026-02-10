package com.Chagui68.weaponsaddon.handlers;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.machines.energy.EnergyManager;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UpgradeTableHandler implements Listener {

    private static final int UPGRADE_COST = 50000;
    private static final int MAX_UPGRADE_LEVEL = 5;
    private static final double DAMAGE_PER_LEVEL = 2.0; // +2 damage per level
    private static final double SPEED_PER_LEVEL = 0.15; // +0.15 speed per level
    private static final Map<UUID, Location> openTables = new HashMap<>();

    // Valid weapon/tool materials
    private static final Set<Material> VALID_UPGRADE_ITEMS = EnumSet.of(
            Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
            Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
            Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
            Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE,
            Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE,
            Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL,
            Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL,
            Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE,
            Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE,
            Material.BOW, Material.CROSSBOW, Material.TRIDENT);

    // Base damage values
    private static final Map<Material, Double> BASE_DAMAGE = new HashMap<>();
    static {
        BASE_DAMAGE.put(Material.WOODEN_SWORD, 4.0);
        BASE_DAMAGE.put(Material.GOLDEN_SWORD, 4.0);
        BASE_DAMAGE.put(Material.STONE_SWORD, 5.0);
        BASE_DAMAGE.put(Material.IRON_SWORD, 6.0);
        BASE_DAMAGE.put(Material.DIAMOND_SWORD, 7.0);
        BASE_DAMAGE.put(Material.NETHERITE_SWORD, 8.0);
        BASE_DAMAGE.put(Material.WOODEN_AXE, 7.0);
        BASE_DAMAGE.put(Material.GOLDEN_AXE, 7.0);
        BASE_DAMAGE.put(Material.STONE_AXE, 9.0);
        BASE_DAMAGE.put(Material.IRON_AXE, 9.0);
        BASE_DAMAGE.put(Material.DIAMOND_AXE, 9.0);
        BASE_DAMAGE.put(Material.NETHERITE_AXE, 10.0);
        BASE_DAMAGE.put(Material.TRIDENT, 9.0);
    }

    // Base attack speed values (Minecraft default is 4.0 for hand)
    private static final Map<Material, Double> BASE_SPEED = new HashMap<>();
    static {
        // Swords: 1.6 attack speed
        BASE_SPEED.put(Material.WOODEN_SWORD, 1.6);
        BASE_SPEED.put(Material.STONE_SWORD, 1.6);
        BASE_SPEED.put(Material.IRON_SWORD, 1.6);
        BASE_SPEED.put(Material.GOLDEN_SWORD, 1.6);
        BASE_SPEED.put(Material.DIAMOND_SWORD, 1.6);
        BASE_SPEED.put(Material.NETHERITE_SWORD, 1.6);
        // Axes: 0.8-1.0 attack speed
        BASE_SPEED.put(Material.WOODEN_AXE, 0.8);
        BASE_SPEED.put(Material.STONE_AXE, 0.8);
        BASE_SPEED.put(Material.IRON_AXE, 0.9);
        BASE_SPEED.put(Material.GOLDEN_AXE, 1.0);
        BASE_SPEED.put(Material.DIAMOND_AXE, 1.0);
        BASE_SPEED.put(Material.NETHERITE_AXE, 1.0);
        // Pickaxes: 1.2 attack speed
        BASE_SPEED.put(Material.WOODEN_PICKAXE, 1.2);
        BASE_SPEED.put(Material.STONE_PICKAXE, 1.2);
        BASE_SPEED.put(Material.IRON_PICKAXE, 1.2);
        BASE_SPEED.put(Material.GOLDEN_PICKAXE, 1.2);
        BASE_SPEED.put(Material.DIAMOND_PICKAXE, 1.2);
        BASE_SPEED.put(Material.NETHERITE_PICKAXE, 1.2);
        // Shovels: 1.0 attack speed
        BASE_SPEED.put(Material.WOODEN_SHOVEL, 1.0);
        BASE_SPEED.put(Material.STONE_SHOVEL, 1.0);
        BASE_SPEED.put(Material.IRON_SHOVEL, 1.0);
        BASE_SPEED.put(Material.GOLDEN_SHOVEL, 1.0);
        BASE_SPEED.put(Material.DIAMOND_SHOVEL, 1.0);
        BASE_SPEED.put(Material.NETHERITE_SHOVEL, 1.0);
        // Hoes: variable
        BASE_SPEED.put(Material.WOODEN_HOE, 1.0);
        BASE_SPEED.put(Material.STONE_HOE, 2.0);
        BASE_SPEED.put(Material.IRON_HOE, 3.0);
        BASE_SPEED.put(Material.GOLDEN_HOE, 1.0);
        BASE_SPEED.put(Material.DIAMOND_HOE, 4.0);
        BASE_SPEED.put(Material.NETHERITE_HOE, 4.0);
        // Trident
        BASE_SPEED.put(Material.TRIDENT, 1.1);
    }

    private static final int INFO_SLOT = 4;
    private static final int WEAPON_SLOT = 10;
    private static final int MODULE_SLOT = 16;
    private static final int UPGRADE_BUTTON = 13;
    private static final int OUTPUT_SLOT = 22;

    public static void openUpgradeGui(Player p, Location loc, int energy) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Weapon Upgrade Table");

        ItemStack darkBg = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, " ");
        ItemStack lightBg = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ");

        for (int i = 0; i < 27; i++) {
            inv.setItem(i, darkBg);
        }

        int[] frameSlots = { 0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 23, 24, 25, 26 };
        for (int slot : frameSlots) {
            inv.setItem(slot, lightBg);
        }

        inv.setItem(11, new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, "&6→"));
        inv.setItem(12, new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE, "&e→"));
        inv.setItem(14, new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE, "&e←"));
        inv.setItem(15, new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE, "&6←"));

        inv.setItem(WEAPON_SLOT, null);
        inv.setItem(MODULE_SLOT, null);
        inv.setItem(OUTPUT_SLOT, null);

        String energyColor = energy >= UPGRADE_COST ? "&a" : "&c";
        inv.setItem(INFO_SLOT, new CustomItemStack(Material.ANVIL, "&e&l⚒ Weapon Upgrade Table",
                "",
                "&7Place a &bweapon/tool &7on the left",
                "&7Place a &dmodule &7on the right",
                "&7Click &aUPGRADE &7to enhance!",
                "",
                "&6Energy: " + energyColor + formatEnergy(energy) + "&7/&e50K J",
                "",
                "&eMax upgrade level: &f5"));

        updateButton(inv, energy);

        p.openInventory(inv);
        openTables.put(p.getUniqueId(), loc);
    }

    private static String formatEnergy(int energy) {
        if (energy >= 1000000)
            return String.format("%.1fM", energy / 1000000.0);
        if (energy >= 1000)
            return String.format("%.1fK", energy / 1000.0);
        return String.valueOf(energy);
    }

    private static void updateButton(Inventory inv, int energy) {
        if (energy >= UPGRADE_COST) {
            inv.setItem(UPGRADE_BUTTON, new CustomItemStack(Material.LIME_STAINED_GLASS_PANE,
                    "&a&l⚡ UPGRADE ⚡", "", "&7Cost: &e50,000 J", "&aClick to upgrade!"));
        } else {
            inv.setItem(UPGRADE_BUTTON, new CustomItemStack(Material.RED_STAINED_GLASS_PANE,
                    "&c&l✘ NO ENERGY ✘", "", "&7Cost: &e50,000 J", "&cInsufficient energy!"));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Weapon Upgrade Table"))
            return;

        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();
        int slot = e.getRawSlot();

        if (slot >= 0 && slot < 27) {
            if (slot != WEAPON_SLOT && slot != MODULE_SLOT && slot != OUTPUT_SLOT) {
                e.setCancelled(true);
            }
            if (slot == UPGRADE_BUTTON) {
                e.setCancelled(true);
                handleUpgrade(p, inv);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!e.getView().getTitle().equals(ChatColor.DARK_GRAY + "Weapon Upgrade Table"))
            return;

        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();

        returnItemToPlayer(p, inv.getItem(WEAPON_SLOT));
        returnItemToPlayer(p, inv.getItem(MODULE_SLOT));
        returnItemToPlayer(p, inv.getItem(OUTPUT_SLOT));
        openTables.remove(p.getUniqueId());
    }

    private void returnItemToPlayer(Player p, ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            HashMap<Integer, ItemStack> leftover = p.getInventory().addItem(item);
            for (ItemStack drop : leftover.values()) {
                p.getWorld().dropItemNaturally(p.getLocation(), drop);
            }
        }
    }

    private void handleUpgrade(Player p, Inventory inv) {
        ItemStack weaponItem = inv.getItem(WEAPON_SLOT);
        ItemStack moduleItem = inv.getItem(MODULE_SLOT);
        ItemStack outputItem = inv.getItem(OUTPUT_SLOT);
        Location loc = openTables.get(p.getUniqueId());

        if (outputItem != null && outputItem.getType() != Material.AIR) {
            p.sendMessage(ChatColor.RED + "✕ Output slot not empty!");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        if (weaponItem == null || weaponItem.getType() == Material.AIR) {
            p.sendMessage(ChatColor.RED + "✕ Place a weapon in the left slot!");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        if (!isValidUpgradeItem(weaponItem)) {
            p.sendMessage(ChatColor.RED + "✕ Only weapons and tools can be upgraded!");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        if (moduleItem == null || moduleItem.getType() == Material.AIR) {
            p.sendMessage(ChatColor.RED + "✕ Place a module in the right slot!");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        if (loc == null)
            return;

        int energy = EnergyManager.getCharge(loc);
        if (energy < UPGRADE_COST) {
            p.sendMessage(ChatColor.RED + "✕ Not enough energy! Need 50,000 J");
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.5f, 0.5f);
            return;
        }

        ItemStack upgradedWeapon = weaponItem.clone();
        boolean success = false;
        String upgradeType = "";

        if (MilitaryComponents.DAMAGE_MODULE_I.isSimilar(moduleItem)) {
            int currentLevel = getUpgradeLevel(upgradedWeapon, "damage");
            if (currentLevel >= MAX_UPGRADE_LEVEL) {
                p.sendMessage(ChatColor.RED + "✕ Max damage level reached! (5/5)");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }
            success = applyDamageUpgrade(upgradedWeapon, currentLevel + 1);
            upgradeType = "DAMAGE Lv." + (currentLevel + 1);
        } else if (MilitaryComponents.SPEED_MODULE_I.isSimilar(moduleItem)) {
            int currentLevel = getUpgradeLevel(upgradedWeapon, "speed");
            if (currentLevel >= MAX_UPGRADE_LEVEL) {
                p.sendMessage(ChatColor.RED + "✕ Max speed level reached! (5/5)");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }
            success = applySpeedUpgrade(upgradedWeapon, currentLevel + 1);
            upgradeType = "SPEED Lv." + (currentLevel + 1);
        } else {
            p.sendMessage(ChatColor.RED + "✕ Invalid upgrade module!");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        if (success) {
            inv.setItem(WEAPON_SLOT, null);
            moduleItem.setAmount(moduleItem.getAmount() - 1);
            inv.setItem(OUTPUT_SLOT, upgradedWeapon);
            EnergyManager.removeCharge(loc, UPGRADE_COST);

            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.2f);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.5f);
            p.sendMessage(ChatColor.GREEN + "✓ Upgrade successful! " + ChatColor.YELLOW + upgradeType);

            refreshEnergyDisplay(inv, loc);
        }
    }

    private void refreshEnergyDisplay(Inventory inv, Location loc) {
        int newEnergy = EnergyManager.getCharge(loc);
        updateButton(inv, newEnergy);
        String energyColor = newEnergy >= UPGRADE_COST ? "&a" : "&c";
        inv.setItem(INFO_SLOT, new CustomItemStack(Material.ANVIL, "&e&l⚒ Weapon Upgrade Table",
                "", "&7Place a &bweapon/tool &7on the left", "&7Place a &dmodule &7on the right",
                "&7Click &aUPGRADE &7to enhance!", "",
                "&6Energy: " + energyColor + formatEnergy(newEnergy) + "&7/&e50K J", "",
                "&eMax upgrade level: &f5"));
    }

    private int getUpgradeLevel(ItemStack item, String type) {
        if (item == null || !item.hasItemMeta())
            return 0;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(WeaponsAddon.getInstance(), "upgrade_" + type + "_level");
        return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    private boolean applyDamageUpgrade(ItemStack item, int newLevel) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return false;

        // Store new level
        NamespacedKey levelKey = new NamespacedKey(WeaponsAddon.getInstance(), "upgrade_damage_level");
        meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, newLevel);

        // Remove old damage modifiers from our upgrade
        removeOurModifiers(meta, Attribute.GENERIC_ATTACK_DAMAGE, "military_damage");

        // Add single modifier with ACCUMULATED damage
        double totalBonus = DAMAGE_PER_LEVEL * newLevel;
        AttributeModifier modifier = new AttributeModifier(
                UUID.randomUUID(), "military_damage",
                totalBonus, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);

        // Hide vanilla attributes
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);

        // Update lore
        updateDamageLore(item, newLevel, totalBonus);
        return true;
    }

    private boolean applySpeedUpgrade(ItemStack item, int newLevel) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return false;

        // Store new level
        NamespacedKey levelKey = new NamespacedKey(WeaponsAddon.getInstance(), "upgrade_speed_level");
        meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, newLevel);

        // Remove old speed modifiers
        removeOurModifiers(meta, Attribute.GENERIC_ATTACK_SPEED, "military_speed");

        // Add single modifier with ACCUMULATED speed
        double totalBonus = SPEED_PER_LEVEL * newLevel;
        AttributeModifier modifier = new AttributeModifier(
                UUID.randomUUID(), "military_speed",
                totalBonus, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);

        // Update lore
        updateSpeedLore(item, newLevel, totalBonus);
        return true;
    }

    private void removeOurModifiers(ItemMeta meta, Attribute attr, String namePrefix) {
        if (!meta.hasAttributeModifiers())
            return;
        Collection<AttributeModifier> mods = meta.getAttributeModifiers(attr);
        if (mods == null)
            return;

        for (AttributeModifier mod : new ArrayList<>(mods)) {
            if (mod.getName().startsWith(namePrefix)) {
                meta.removeAttributeModifier(attr, mod);
            }
        }
    }

    private void updateDamageLore(ItemStack item, int level, double bonus) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

        // Remove old damage upgrade lines
        lore.removeIf(line -> line.contains("⚔ Damage") || line.contains("DAMAGE:"));

        // Calculate total damage
        double totalDamage = calculateTotalDamage(item);

        // Add new damage line at beginning
        String damageBar = createProgressBar(level, MAX_UPGRADE_LEVEL);
        lore.add(0, "");
        lore.add(1, ChatColor.translateAlternateColorCodes('&',
                "&c⚔ DAMAGE: &f" + String.format("%.1f", totalDamage) + " &7(+" + String.format("%.1f", bonus) + ")"));
        lore.add(2, ChatColor.translateAlternateColorCodes('&',
                "&7Upgrade: " + damageBar + " &f" + level + "/" + MAX_UPGRADE_LEVEL));

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private void updateSpeedLore(ItemStack item, int level, double bonus) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

        // Remove old speed upgrade lines
        lore.removeIf(line -> line.contains("⚡ SPEED") || line.contains("SPEED:"));

        // Calculate total attack speed
        double totalSpeed = calculateTotalSpeed(item);

        // Add speed line
        String speedBar = createProgressBar(level, MAX_UPGRADE_LEVEL);
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&',
                "&b⚡ SPEED: &f" + String.format("%.2f", totalSpeed) + " &7(+" + String.format("%.2f", bonus) + ")"));
        lore.add(ChatColor.translateAlternateColorCodes('&',
                "&7Upgrade: " + speedBar + " &f" + level + "/" + MAX_UPGRADE_LEVEL));

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private double calculateTotalSpeed(ItemStack item) {
        double speed = BASE_SPEED.getOrDefault(item.getType(), 4.0); // Default hand speed is 4.0

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasAttributeModifiers()) {
            Collection<AttributeModifier> mods = meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_SPEED);
            if (mods != null) {
                for (AttributeModifier mod : mods) {
                    if (mod.getOperation() == AttributeModifier.Operation.ADD_NUMBER) {
                        speed += mod.getAmount();
                    }
                }
            }
        }

        return speed;
    }

    private String createProgressBar(int current, int max) {
        StringBuilder bar = new StringBuilder("&a");
        for (int i = 0; i < current; i++)
            bar.append("▮");
        bar.append("&8");
        for (int i = current; i < max; i++)
            bar.append("▯");
        return bar.toString();
    }

    private double calculateTotalDamage(ItemStack item) {
        double damage = BASE_DAMAGE.getOrDefault(item.getType(), 1.0);

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasAttributeModifiers()) {
            Collection<AttributeModifier> mods = meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE);
            if (mods != null) {
                for (AttributeModifier mod : mods) {
                    if (mod.getOperation() == AttributeModifier.Operation.ADD_NUMBER) {
                        damage += mod.getAmount();
                    }
                }
            }
        }

        if (item.containsEnchantment(Enchantment.SHARPNESS)) {
            int level = item.getEnchantmentLevel(Enchantment.SHARPNESS);
            damage += 0.5 + (0.5 * level);
        }

        return damage;
    }

    private boolean isValidUpgradeItem(ItemStack item) {
        if (item == null)
            return false;
        if (VALID_UPGRADE_ITEMS.contains(item.getType()))
            return true;
        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        return sfItem != null && VALID_UPGRADE_ITEMS.contains(item.getType());
    }
}

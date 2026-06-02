package com.Chagui68.weaponsaddon.items.vouchers;

import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import com.Chagui68.weaponsaddon.WeaponsAddon;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class MilitaryVouchers {

        private static final SlimefunItemStack BOSS_DROP_DISPLAY = new SlimefunItemStack(
                        "MA_BOSS_DROP_DISPLAY",
                        Material.WITHER_SKELETON_SKULL,
                        "&4☠ &cBoss Drop",
                        "",
                        "&7Dropped by Military Bosses");

        private static RecipeType bossDropRecipeType;

        public static RecipeType getBossDropRecipeType() {
                if (bossDropRecipeType == null) {
                        bossDropRecipeType = new RecipeType(
                                        new NamespacedKey(WeaponsAddon.getInstance(), "boss_drop"),
                                        BOSS_DROP_DISPLAY);
                }
                return bossDropRecipeType;
        }

        // --- Vouchers ---

        public static final SlimefunItemStack VOUCHER_WEAPON_UPGRADE = new SlimefunItemStack(
                        "MA_VOUCHER_WEAPON_UPGRADE",
                        Material.PAPER,
                        "&c📜 Special Weapon Upgrade",
                        "",
                        "&7★ Boss Reward Voucher",
                        "&7Bring this to the Military Workbench",
                        "",
                        "&eAn important part of something more powerful");

        public static final SlimefunItemStack VOUCHER_TANK_PART = new SlimefunItemStack(
                        "MA_VOUCHER_TANK_PART",
                        Material.PAPER,
                        "&8⚙ Heavy Tank Part",
                        "",
                        "&7★ Boss Reward Voucher",
                        "&7A piece of a heavy war machine",
                        "",
                        "&eAn important part of something more powerful");

        public static final SlimefunItemStack VOUCHER_BATTERY = new SlimefunItemStack(
                        "MA_VOUCHER_BATTERY",
                        Material.PAPER,
                        "&e⚡ Experimental Battery",
                        "",
                        "&7★ Boss Reward Voucher",
                        "&7Highly unstable energy source",
                        "",
                        "&eAn important part of something more powerful");

        public static final SlimefunItemStack VOUCHER_DOCUMENT = new SlimefunItemStack(
                        "MA_VOUCHER_DOCUMENT",
                        Material.PAPER,
                        "&b📂 Classified Document",
                        "",
                        "&7★ Boss Reward Voucher",
                        "&7Unlock secret military knowledge",
                        "",
                        "&eAn important part of something more powerful");

        public static final SlimefunItemStack VOUCHER_KEY = new SlimefunItemStack(
                        "MA_VOUCHER_KEY",
                        Material.PAPER,
                        "&6🔑 Supply Crate Key",
                        "",
                        "&7★ Boss Reward Voucher",
                        "&7Used to open military supply drops",
                        "",
                        "&eAn important part of something more powerful");

        public static final SlimefunItemStack VOUCHER_COMMENDATION = new SlimefunItemStack(
                        "MA_VOUCHER_COMMENDATION",
                        Material.PAPER,
                        "&b✉ Official Military Commendation",
                        "",
                        "&7★ Boss Reward Voucher",
                        "&7Signed by the High Command",
                        "",
                        "&eAn important part of something more powerful");

        public static final SlimefunItemStack VOUCHER_EMBLEM = new SlimefunItemStack(
                        "MA_VOUCHER_EMBLEM",
                        Material.PAPER,
                        "&4☠ Heavy Gunner Merciless Emblem",
                        "",
                        "&7★ Boss Reward Voucher",
                        "&7Proof of your combat supremacy",
                        "",
                        "&eAn important part of something more powerful");

        public static void register(SlimefunAddon addon, ItemGroup category) {
                RecipeType bossDropType = getBossDropRecipeType();
                new SlimefunItem(category, VOUCHER_WEAPON_UPGRADE, bossDropType, new ItemStack[9]).register(addon);
                new SlimefunItem(category, VOUCHER_TANK_PART, bossDropType, new ItemStack[9]).register(addon);
                new SlimefunItem(category, VOUCHER_BATTERY, bossDropType, new ItemStack[9]).register(addon);
                new SlimefunItem(category, VOUCHER_DOCUMENT, bossDropType, new ItemStack[9]).register(addon);
                new SlimefunItem(category, VOUCHER_KEY, bossDropType, new ItemStack[9]).register(addon);
                new SlimefunItem(category, VOUCHER_COMMENDATION, bossDropType, new ItemStack[9]).register(addon);
                new SlimefunItem(category, VOUCHER_EMBLEM, bossDropType, new ItemStack[9]).register(addon);
        }
}

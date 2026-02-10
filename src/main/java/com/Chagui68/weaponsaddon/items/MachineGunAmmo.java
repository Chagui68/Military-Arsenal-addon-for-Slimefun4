package com.Chagui68.weaponsaddon.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MachineGunAmmo {

        public static final SlimefunItemStack MACHINE_GUN_AMMO = new SlimefunItemStack(
                        "MACHINE_GUN_AMMO",
                        Material.IRON_NUGGET,
                        "&eMachine Gun Bullets",
                        "",
                        "&7High-velocity ammunition",
                        "&7for automatic weapons",
                        "",
                        "&6Damage: &c5 HP per shot",
                        "&6Stack: &764",
                        "",
                        "&7Crafted at the Ammunition Workshop");

        public static void register(SlimefunAddon addon, ItemGroup category) {
                ItemStack[] recipe = new ItemStack[] {
                                null, new ItemStack(Material.COPPER_INGOT), null,
                                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.GUNPOWDER),
                                new ItemStack(Material.IRON_INGOT),
                                null, new ItemStack(Material.IRON_NUGGET), null
                };

                SlimefunItem ammo = new SlimefunItem(
                                category,
                                MACHINE_GUN_AMMO,
                                MilitaryRecipeTypes.getAmmunitionWorkshop(),
                                recipe);
                ammo.register(addon);
        }
}
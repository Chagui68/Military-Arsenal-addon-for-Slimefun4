package com.Chagui68.weaponsaddon.items;

import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.machines.MilitaryCraftingHandler;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MachineGun {

    public static final SlimefunItemStack MACHINE_GUN = new SlimefunItemStack(
            "MACHINE_GUN",
            Material.DIAMOND_HOE,
            "&4⚔ &cMachine Gun",
            "",
            "&7Automatic rapid-fire weapon",
            "&7Fires 5-shot bursts",
            "",
            "&6Damage: &c5 HP per shot",
            "&6Fire Rate: &e0.1s between shots",
            "&6Cooldown: &e0.5s",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK",
            "&e to view FULL 4×4 recipe",
            "",
            "&eRight-Click to fire burst",
            "&cRequires Machine Gun Bullets"
    );

    static {
        ItemMeta meta = MACHINE_GUN.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(true);
            MACHINE_GUN.setItemMeta(meta);
        }
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        // Receta completa 4x4 (16 items)
        ItemStack[] fullRecipe = new ItemStack[]{
                MilitaryComponents.WEAPON_BARREL, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.RADAR_MODULE, MilitaryComponents.WEAPON_BARREL,

                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.TRIGGER_MECHANISM,
                MilitaryComponents.STABILIZER_UNIT, MilitaryComponents.TARGETING_SYSTEM,

                MilitaryComponents.REINFORCED_FRAME, MilitaryComponents.POWER_CORE,
                MilitaryComponents.POWER_CORE, MilitaryComponents.REINFORCED_FRAME,

                MilitaryComponents.REINFORCED_FRAME, SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.ELECTRIC_MOTOR, MilitaryComponents.REINFORCED_FRAME
        };


        ItemStack[] guideRecipe = new ItemStack[]{
                MilitaryComponents.WEAPON_BARREL, MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.WEAPON_BARREL,
                MilitaryComponents.TRIGGER_MECHANISM, MilitaryComponents.POWER_CORE, MilitaryComponents.STABILIZER_UNIT,
                MilitaryComponents.REINFORCED_FRAME, SlimefunItems.ELECTRIC_MOTOR, MilitaryComponents.REINFORCED_FRAME
        };


        CustomRecipeItem machineGunItem = new CustomRecipeItem(
                category,
                MACHINE_GUN,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                fullRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );

        machineGunItem.register(addon);


        MilitaryCraftingHandler.registerRecipe(machineGunItem);
    }
}

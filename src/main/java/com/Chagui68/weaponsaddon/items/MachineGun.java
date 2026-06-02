package com.Chagui68.weaponsaddon.items;

import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.machines.MilitaryCraftingHandler;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.implementation.SlimefunItems;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MachineGun extends CustomRecipeItem {

    public static final SlimefunItemStack MACHINE_GUN = new SlimefunItemStack(
            "MA_MACHINE_GUN",
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
            "&eRight-Click to fire burst",
            "&cRequires Machine Gun Bullets",
            "",
            "&7Click in guide to view recipe");

    static {
        ItemMeta meta = MACHINE_GUN.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(true);

            // Add damage bonus for dynamic calculation
            meta.getPersistentDataContainer().set(
                    new NamespacedKey(com.Chagui68.weaponsaddon.WeaponsAddon.getInstance(), "boss_damage_bonus"),
                    org.bukkit.persistence.PersistentDataType.DOUBLE, 7.0); // 5 (base) + 7 = 12 total

            MACHINE_GUN.setItemMeta(meta);
            com.Chagui68.weaponsaddon.handlers.UpgradeTableHandler.updateWeaponLore(MACHINE_GUN);
        }
    }

    public MachineGun(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe, RecipeGridSize gridSize) {
        super(itemGroup, item, MilitaryRecipeTypes.getMilitaryCraftingTable(), recipe, gridSize);
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] fullRecipe = new ItemStack[] {
                MilitaryComponents.WEAPON_BARREL, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.RADAR_MODULE, MilitaryComponents.WEAPON_BARREL,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.TRIGGER_MECHANISM,
                MilitaryComponents.STABILIZER_UNIT, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.REINFORCED_FRAME, MilitaryComponents.POWER_CORE,
                MilitaryComponents.POWER_CORE, MilitaryComponents.REINFORCED_FRAME,
                MilitaryComponents.REINFORCED_FRAME, SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.ELECTRIC_MOTOR, MilitaryComponents.REINFORCED_FRAME
        };

        MachineGun machineGun = new MachineGun(category, MACHINE_GUN, fullRecipe, RecipeGridSize.GRID_4x4);
        machineGun.register(addon);
        MilitaryCraftingHandler.registerRecipe(machineGun);
    }
}

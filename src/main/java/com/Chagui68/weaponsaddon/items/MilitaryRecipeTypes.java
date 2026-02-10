package com.Chagui68.weaponsaddon.items;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import com.Chagui68.weaponsaddon.items.machines.AmmunitionWorkshop;
import com.Chagui68.weaponsaddon.items.machines.MilitaryCraftingTable;
import com.Chagui68.weaponsaddon.items.machines.MilitaryMachineFabricator;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.NamespacedKey;

/**
 * Lazy-initialized recipe types to avoid circular dependencies during class
 * loading.
 * Use getter methods instead of static fields.
 */
public class MilitaryRecipeTypes {

        private static RecipeType ammunitionWorkshop;
        private static RecipeType militaryCraftingTable;
        private static RecipeType militaryMachineFabricator;

        public static RecipeType getAmmunitionWorkshop() {
                if (ammunitionWorkshop == null) {
                        ammunitionWorkshop = new RecipeType(
                                        new NamespacedKey(WeaponsAddon.getInstance(), "ammunition_workshop"),
                                        AmmunitionWorkshop.AMMUNITION_WORKSHOP);
                }
                return ammunitionWorkshop;
        }

        public static RecipeType getMilitaryCraftingTable() {
                if (militaryCraftingTable == null) {
                        militaryCraftingTable = new RecipeType(
                                        new NamespacedKey(WeaponsAddon.getInstance(), "military_crafting_table"),
                                        MilitaryCraftingTable.MILITARY_CRAFTING_TABLE);
                }
                return militaryCraftingTable;
        }

        public static RecipeType getMilitaryMachineFabricator() {
                if (militaryMachineFabricator == null) {
                        militaryMachineFabricator = new RecipeType(
                                        new NamespacedKey(WeaponsAddon.getInstance(), "military_machine_fabricator"),
                                        MilitaryMachineFabricator.MILITARY_MACHINE_FABRICATOR);
                }
                return militaryMachineFabricator;
        }
}

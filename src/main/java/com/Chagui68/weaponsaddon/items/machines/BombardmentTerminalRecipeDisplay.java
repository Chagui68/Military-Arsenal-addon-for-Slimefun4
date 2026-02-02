package com.Chagui68.weaponsaddon.items.machines;

import com.Chagui68.weaponsaddon.items.CustomRecipeItem;
import com.Chagui68.weaponsaddon.items.MilitaryRecipeTypes;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BombardmentTerminalRecipeDisplay {

    public static final SlimefunItemStack BOMBARDMENT_TERMINAL_RECIPE = new SlimefunItemStack(
            "BOMBARDMENT_TERMINAL_RECIPE",
            Material.KNOWLEDGE_BOOK,
            "&4💣 &cBombardment Terminal Recipe",
            "",
            "&7Recipe viewer for",
            "&7Bombardment Terminal",
            "",
            "&c⚠ Requires 6×6 Machine Fabricator",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK",
            "&e to view FULL 6×6 recipe",
            "",
            "&8This is a recipe display item"
    );

    public static void register(SlimefunAddon addon, ItemGroup category) {
        // RECETA COMPLETA 6x6 (36 items)
        ItemStack[] fullRecipe = new ItemStack[]{
                // FILA 1
                MilitaryComponents.REINFORCED_FRAME, MilitaryComponents.QUANTUM_PROCESSOR,
                MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX,
                MilitaryComponents.QUANTUM_PROCESSOR, MilitaryComponents.REINFORCED_FRAME,

                // FILA 2
                MilitaryComponents.QUANTUM_PROCESSOR, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.QUANTUM_PROCESSOR,

                // FILA 3
                MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.EXPLOSIVE_CORE, MilitaryComponents.EXPLOSIVE_CORE,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.ENERGY_MATRIX,

                // FILA 4
                MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.EXPLOSIVE_CORE, MilitaryComponents.EXPLOSIVE_CORE,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.ENERGY_MATRIX,

                // FILA 5
                MilitaryComponents.QUANTUM_PROCESSOR, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.QUANTUM_PROCESSOR,

                // FILA 6
                MilitaryComponents.REINFORCED_FRAME, MilitaryComponents.QUANTUM_PROCESSOR,
                MilitaryComponents.HYDRAULIC_SYSTEM, MilitaryComponents.COOLANT_SYSTEM,
                MilitaryComponents.QUANTUM_PROCESSOR, MilitaryComponents.REINFORCED_FRAME
        };

        CustomRecipeItem recipeDisplay = new CustomRecipeItem(
                category,
                BOMBARDMENT_TERMINAL_RECIPE,
                MilitaryRecipeTypes.MILITARY_MACHINE_FABRICATOR,
                fullRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_6x6
        );

        recipeDisplay.setCustomResult(BombardmentTerminal.BOMBARDMENT_TERMINAL);
        recipeDisplay.register(addon);
    }
}

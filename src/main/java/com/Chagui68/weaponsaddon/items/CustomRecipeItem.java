package com.Chagui68.weaponsaddon.items;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

public class CustomRecipeItem extends SlimefunItem {

    public enum RecipeGridSize {
        GRID_4x4,
        GRID_6x6
    }

    private final ItemStack[] fullRecipe;
    private final RecipeGridSize gridSize;

    public CustomRecipeItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType,
                            ItemStack[] recipe, RecipeGridSize gridSize) {
        super(itemGroup, item, recipeType, new ItemStack[9]); 
        this.fullRecipe = recipe;
        this.gridSize = gridSize;
    }

    public ItemStack[] getFullRecipe() {
        return fullRecipe;
    }

    public RecipeGridSize getGridSize() {
        return gridSize;
    }

    public SlimefunItemStack getResultItem() {
        return (SlimefunItemStack) getItem();
    }
}

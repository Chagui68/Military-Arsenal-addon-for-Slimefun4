package com.Chagui68.weaponsaddon.items.components;

import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactive;
import com.github.drakescraft_labs.slimefun4.core.attributes.Radioactivity; // Clase correcta
import org.bukkit.inventory.ItemStack;
import javax.annotation.Nonnull;

public class RadioactiveComponent extends SlimefunItem implements Radioactive {

    private final Radioactivity radioactivity;

    public RadioactiveComponent(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Radioactivity radioactivity) {
        super(itemGroup, item, recipeType, recipe);
        this.radioactivity = radioactivity;
    }

    @Nonnull
    @Override
    public Radioactivity getRadioactivity() {
        return radioactivity;
    }
}

package com.Chagui68.weaponsaddon.items.components;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactivity; // Clase correcta
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
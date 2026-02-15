package com.Chagui68.weaponsaddon.items.components;

import com.Chagui68.weaponsaddon.core.attributes.CustomEffectEmitter;
import com.Chagui68.weaponsaddon.core.attributes.VoidProtection;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VoidComponents extends SlimefunItem implements CustomEffectEmitter {

    public VoidComponents(ItemGroup it, SlimefunItemStack item, RecipeType rt, ItemStack[] recipe) {
        super(it, item, rt, recipe);
    }

    /*
     * Asignacion de los efectos de pocion por tipo de item si este es de tipo void
     * dara
     * los siguientes efectos, su duracion funciona en ticks
     */

    @Override
    public void applyEffect(Player player) {
        // Check if player has full protection set
        if (hasFullProtection(player)) {
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 160, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 160, 5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 160, 5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 160, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 160, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 160, 5));
    }

    private boolean hasFullProtection(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chest = player.getInventory().getChestplate();
        ItemStack legs = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        return isProtected(helmet) && isProtected(chest) && isProtected(legs) && isProtected(boots);
    }

    private boolean isProtected(ItemStack item) {
        if (item == null)
            return false;
        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        return sfItem instanceof VoidProtection;
    }
}

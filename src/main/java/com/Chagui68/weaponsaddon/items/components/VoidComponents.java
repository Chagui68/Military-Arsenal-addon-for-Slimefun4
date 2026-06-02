package com.Chagui68.weaponsaddon.items.components;

import com.Chagui68.weaponsaddon.core.attributes.CustomEffectEmitter;
import com.Chagui68.weaponsaddon.core.attributes.VoidProtection;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.Chagui68.weaponsaddon.utils.VersionSafe;

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

        player.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("NAUSEA"), 160, 3));
        player.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("HUNGER"), 160, 5));
        player.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("SLOWNESS"), 160, 5));
        player.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("WEAKNESS"), 160, 2));
        player.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("BLINDNESS"), 160, 2));
        player.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("DARKNESS"), 160, 3));
        player.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("POISON"), 160, 5));
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

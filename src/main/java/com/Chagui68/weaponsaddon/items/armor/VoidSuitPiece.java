package com.Chagui68.weaponsaddon.items.armor;

import com.Chagui68.weaponsaddon.core.attributes.CustomEffectEmitter;
import com.Chagui68.weaponsaddon.core.attributes.VoidProtection;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VoidSuitPiece extends SlimefunItem implements VoidProtection, CustomEffectEmitter {

    public VoidSuitPiece(ItemGroup it, SlimefunItemStack item, RecipeType rt, ItemStack[] recipe) {
        super(it, item, rt, recipe);
    }

    public static final SlimefunItemStack VOID_HELMET = new SlimefunItemStack(
            "MA_VOID_HELMET",
            Material.LEATHER_HELMET, Color.PURPLE,
            "&d⚛ &5Void Tactical Helmet",
            "",
            "&7Specially reinforced tactical helmet.",
            "&7Coated with void-resistant polymers.",
            "",
            "&8⇨ Full set required for void protection");
    public static final SlimefunItemStack VOID_CHESTPLATE = new SlimefunItemStack(
            "MA_VOID_CHESTPLATE",
            Material.LEATHER_CHESTPLATE, Color.PURPLE,
            "&d⚛ &5Void Tactical Vest",
            "",
            "&7Heavy-duty vacuum-sealed vest.",
            "&7Contains internal cooling and",
            "&7void-filtration systems.",
            "",
            "&8⇨ Full set required for void protection");
    public static final SlimefunItemStack VOID_LEGGINGS = new SlimefunItemStack(
            "MA_VOID_LEGGINGS",
            Material.LEATHER_LEGGINGS, Color.PURPLE,
            "&d⚛ &5Void Tactical Leggings",
            "",
            "&7Flexible pressure-resistant greaves.",
            "&7Reinforced joints for mobility",
            "&7in high-pressure environments.",
            "",
            "&8⇨ Full set required for void protection");
    public static final SlimefunItemStack VOID_BOOTS = new SlimefunItemStack(
            "MA_VOID_BOOTS",
            Material.LEATHER_BOOTS, Color.PURPLE,
            "&d⚛ &5Void Tactical Boots",
            "",
            "&7Anti-gravitational magnetic boots.",
            "&7Neutralizes environmental",
            "&7kinetic interference.",
            "",
            "&8⇨ Full set required for void protection");

    public static void register(SlimefunAddon addon, ItemGroup category) {
        // Helmet Recipe
        new VoidSuitPiece(category, VOID_HELMET, RecipeType.ARMOR_FORGE, new ItemStack[] {
                MilitaryComponents.ANTIMATTER_PARTICLE,MilitaryComponents.ANTIMATTER_PARTICLE,MilitaryComponents.ANTIMATTER_PARTICLE,
                MilitaryComponents.ANTIMATTER_PARTICLE,null,MilitaryComponents.ANTIMATTER_PARTICLE,
                null,null,null
        }).register(addon);

        // Chestplate Recipe
        new VoidSuitPiece(category, VOID_CHESTPLATE, RecipeType.ARMOR_FORGE, new ItemStack[] {
                MilitaryComponents.ANTIMATTER_PARTICLE,null,MilitaryComponents.ANTIMATTER_PARTICLE,
                MilitaryComponents.ANTIMATTER_PARTICLE, MilitaryComponents.ANTIMATTER_PARTICLE, MilitaryComponents.ANTIMATTER_PARTICLE,
                MilitaryComponents.ANTIMATTER_PARTICLE, MilitaryComponents.ANTIMATTER_PARTICLE, MilitaryComponents.ANTIMATTER_PARTICLE
        }).register(addon);

        // Leggings Recipe
        new VoidSuitPiece(category, VOID_LEGGINGS, RecipeType.ARMOR_FORGE, new ItemStack[] {
                MilitaryComponents.ANTIMATTER_PARTICLE, MilitaryComponents.ANTIMATTER_PARTICLE, MilitaryComponents.ANTIMATTER_PARTICLE,
                MilitaryComponents.ANTIMATTER_PARTICLE,null, MilitaryComponents.ANTIMATTER_PARTICLE,
                MilitaryComponents.ANTIMATTER_PARTICLE,null, MilitaryComponents.ANTIMATTER_PARTICLE
        }).register(addon);

        // Boots Recipe
        new VoidSuitPiece(category, VOID_BOOTS, RecipeType.ARMOR_FORGE, new ItemStack[] {
                null,null,null,
                MilitaryComponents.ANTIMATTER_PARTICLE,null, MilitaryComponents.ANTIMATTER_PARTICLE,
                MilitaryComponents.ANTIMATTER_PARTICLE,null, MilitaryComponents.ANTIMATTER_PARTICLE
        }).register(addon);
    }

    @Override
    public void applyEffect(Player player) {
        // Solo aplicamos efectos desde una sola pieza (ej: el casco) para evitar spam
        // de efectos
        if (!getId().equals("MA_VOID_HELMET")) {
            return;
        }

        if (hasFullSet(player)) {
            // Efectos beneficiosos del traje completo
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 300, 1, true, false, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 2, true, false, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 300, 3, true, false, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,300,2,true,false,true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,300,1,true,false,true));
        }
    }

    private boolean hasFullSet(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chest = player.getInventory().getChestplate();
        ItemStack legs = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        return isVoidPiece(helmet) && isVoidPiece(chest) && isVoidPiece(legs) && isVoidPiece(boots);
    }

    private boolean isVoidPiece(ItemStack item) {
        if (item == null)
            return false;
        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        return sfItem instanceof VoidProtection;
    }
}

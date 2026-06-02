package com.Chagui68.weaponsaddon.items;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import com.Chagui68.weaponsaddon.items.machines.AntimatterRitual;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.api.recipes.RecipeType;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AntimatterRifle extends SlimefunItem {

    public static final SlimefunItemStack ANTIMATTER_RIFLE = new SlimefunItemStack(
            "MA_ANTIMATTER_RIFLE",
            Material.NETHERITE_SWORD,
            "&4☢ &cAntimatter Rifle",
            "",
            "&7Ultimate annihilation weapon",
            "&7Destroys any entity instantly",
            "",
            "&6Damage: &c999,999 HP (Instant Kill)",
            "&6Range: &e50 blocks",
            "&6Cooldown: &e8 seconds",
            "",
            "&c⚠ EXTREME DANGER",
            "&c⚠ Use with caution",
            "",
            "&eRight-Click to fire antimatter beam",
            "&7Crafted at Antimatter Ritual Altar");

    static {
        ItemMeta meta = ANTIMATTER_RIFLE.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(true);

            // Add damage bonus for dynamic calculation (Instant Kill)
            meta.getPersistentDataContainer().set(
                    new NamespacedKey(com.Chagui68.weaponsaddon.WeaponsAddon.getInstance(), "boss_damage_bonus"),
                    org.bukkit.persistence.PersistentDataType.DOUBLE, 999991.0); // 8 (base) + 999991 = 999999 total

            ANTIMATTER_RIFLE.setItemMeta(meta);
            com.Chagui68.weaponsaddon.handlers.UpgradeTableHandler.updateWeaponLore(ANTIMATTER_RIFLE);
        }
    }

    public AntimatterRifle(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType) {
        super(itemGroup, item, recipeType, new ItemStack[9]);
    }

    @Override
    public void preRegister() {
        // Handlers moved to AntimatterRifleHandler for better compatibility with
        // upgrades
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        RecipeType ritualRecipe = new RecipeType(
                new NamespacedKey(WeaponsAddon.getInstance(), "antimatter_ritual"),
                AntimatterRitual.ANTIMATTER_RITUAL_CORE);

        new AntimatterRifle(category, ANTIMATTER_RIFLE, ritualRecipe).register(addon);
    }
}

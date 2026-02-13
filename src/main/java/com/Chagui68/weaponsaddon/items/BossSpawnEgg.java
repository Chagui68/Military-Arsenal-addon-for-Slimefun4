package com.Chagui68.weaponsaddon.items;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import com.Chagui68.weaponsaddon.handlers.MilitaryMobHandler;
import com.Chagui68.weaponsaddon.listeners.BossAIHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getPluginManager;

public class BossSpawnEgg extends SlimefunItem {

    public static final SlimefunItemStack HEAVY_GUNNER_EGG = new SlimefunItemStack(
            "HEAVY_GUNNER_EGG",
            Material.WITHER_SKELETON_SPAWN_EGG,
            "&4☠ &cSummon: Heavy Gunner",
            "",
            "&7Spawns a powerful military boss",
            "&7Armed with a Machine Gun",
            "",
            "&eRight-Click on ground to spawn",
            "&c⚠ EXTREME DANGER");

    public BossSpawnEgg(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler((ItemUseHandler) e -> {
            Player p = e.getPlayer();
            e.cancel();

            // Spawnear el jefe en la ubicación clickeada
            if (e.getClickedBlock().isPresent()) {
                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                spawnBoss(e.getClickedBlock().get().getLocation().add(0.5, 1, 0.5));
                p.sendMessage(ChatColor.RED + "⚠ Heavy Gunner has been summoned!");
            }
        });
    }

    private void spawnBoss(Location loc) {
        Skeleton boss = (Skeleton) loc.getWorld().spawnEntity(loc,
                EntityType.SKELETON);

        MilitaryMobHandler.equipHeavyGunner(boss);

        // Construir arena física y activar Boss Bar
        BossAIHandler.buildArena(loc,
                getPluginManager().getPlugin("WeaponsAddon"));

        loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] recipe = new ItemStack[] {
                new ItemStack(Material.NETHERITE_BLOCK), MachineGun.MACHINE_GUN,
                new ItemStack(Material.NETHERITE_BLOCK),
                new ItemStack(Material.WITHER_SKELETON_SKULL), new ItemStack(Material.BEACON),
                new ItemStack(Material.WITHER_SKELETON_SKULL),
                new ItemStack(Material.NETHERITE_BLOCK), new ItemStack(Material.DIAMOND_BLOCK),
                new ItemStack(Material.NETHERITE_BLOCK)
        };

        new BossSpawnEgg(category, HEAVY_GUNNER_EGG, RecipeType.ENHANCED_CRAFTING_TABLE, recipe).register(addon);
    }
}

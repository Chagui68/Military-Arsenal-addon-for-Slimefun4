package com.Chagui68.weaponsaddon.items;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import com.Chagui68.weaponsaddon.items.machines.AntimatterRitual;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AntimatterRifle extends SlimefunItem {

    public static final SlimefunItemStack ANTIMATTER_RIFLE = new SlimefunItemStack(
            "ANTIMATTER_RIFLE",
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
            "&7Crafted at Antimatter Ritual Altar"
    );

    static {
        ItemMeta meta = ANTIMATTER_RIFLE.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(true);
            ANTIMATTER_RIFLE.setItemMeta(meta);
        }
    }

    public AntimatterRifle(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType) {
        super(itemGroup, item, recipeType, new ItemStack[9]);
    }

    @Override
    public void preRegister() {
        addItemHandler((ToolUseHandler) (e, tool, fortune, drops) -> {
            e.setCancelled(true);
        });

        addItemHandler((ItemUseHandler) e -> {
            e.cancel();
            Player p = e.getPlayer();

            if (p.hasCooldown(Material.NETHERITE_SWORD)) {
                p.sendMessage(ChatColor.RED + "⚠ Rifle cooling down...");
                return;
            }

            Vector direction = p.getEyeLocation().getDirection();
            RayTraceResult result = p.getWorld().rayTraceEntities(
                    p.getEyeLocation(),
                    direction,
                    50.0,
                    0.5,
                    entity -> entity instanceof LivingEntity && entity != p
            );

            if (result != null && result.getHitEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) result.getHitEntity();

                for (double i = 0; i < result.getHitPosition().distance(p.getEyeLocation().toVector()); i += 0.5) {
                    Vector point = p.getEyeLocation().toVector().add(direction.clone().multiply(i));
                    p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK,
                            point.getX(), point.getY(), point.getZ(),
                            1, 0, 0, 0, 0);
                    p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,
                            point.getX(), point.getY(), point.getZ(),
                            2, 0.1, 0.1, 0.1, 0.01);
                }

                target.getWorld().spawnParticle(Particle.EXPLOSION, target.getLocation().add(0, 1, 0), 50, 1, 1, 1, 0.3);
                target.getWorld().spawnParticle(Particle.FLASH, target.getLocation().add(0, 1, 0), 10);
                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.5f);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.5f);

                target.damage(999999, p); //Daño del arma xd

                p.sendMessage(ChatColor.DARK_RED + "☢ " + ChatColor.RED + "ANTIMATTER ANNIHILATION!");
                p.sendMessage(ChatColor.GRAY + "Target eliminated at " +
                        String.format("%.1f", result.getHitPosition().distance(p.getEyeLocation().toVector())) + " blocks");

                p.setCooldown(Material.NETHERITE_SWORD, 320); // Intervalo de disparo del arma en ticks
            } else {
                p.sendMessage(ChatColor.RED + "✗ No target in range");
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.0f, 1.0f);
            }
        });
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        RecipeType ritualRecipe = new RecipeType(
                new NamespacedKey(WeaponsAddon.getInstance(), "antimatter_ritual"),
                AntimatterRitual.ANTIMATTER_RITUAL_CORE
        );

        new AntimatterRifle(category, ANTIMATTER_RIFLE, ritualRecipe).register(addon);
    }
}

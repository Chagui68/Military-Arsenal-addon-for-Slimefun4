package com.Chagui68.weaponsaddon.items.machines;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static org.bukkit.Bukkit.getScheduler;

public class AirstrikeExecutor {

    public static void executeBombardment(Location target, Player p) {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(AirstrikeExecutor.class);

        new BukkitRunnable() {
            int wave = 0;

            @Override
            public void run() {
                if (wave >= 2) {
                    p.sendMessage(ChatColor.GREEN + "✓ [Terminal] Bombardment complete");
                    cancel();
                    return;
                }

                wave++;
                p.sendMessage(ChatColor.DARK_RED + "💣 [Terminal] Wave " + wave + "/2 - Target Locked!");

                for (int i = 0; i < 4; i++) {
                    double offsetX = (Math.random() - 0.5) * 15;
                    double offsetZ = (Math.random() - 0.5) * 15;
                    Location bombTarget = target.clone().add(offsetX, 0, offsetZ);

                    // Asegurar que el target esté en el suelo
                    bombTarget = bombTarget.getWorld().getHighestBlockAt(bombTarget).getLocation().add(0, 1, 0);
                    Location finalTarget = bombTarget;

                    getScheduler().runTaskLater(plugin, () -> {
                        spawnMissileSequence(finalTarget, plugin);
                    }, i * 20L + (wave * 40L));
                }
            }
        }.runTaskTimer(plugin, 20L, 100L);
    }

    private static void spawnMissileSequence(Location target, JavaPlugin plugin) {
        // 1. LASER TARGETING (3 seconds before impact)
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks > 60) {
                    cancel();
                    return;
                }
                // Red laser beam from sky to ground
                for (int y = 0; y < 40; y += 2) {
                    target.getWorld().spawnParticle(Particle.DUST, target.clone().add(0, y, 0), 1,
                            new Particle.DustOptions(Color.RED, 1.0f));
                }
                // Ground circle
                for (double angle = 0; angle < 360; angle += 30) {
                    double x = Math.cos(Math.toRadians(angle)) * 2;
                    double z = Math.sin(Math.toRadians(angle)) * 2;
                    target.getWorld().spawnParticle(Particle.DUST, target.clone().add(x, 0.1, z), 1,
                            new Particle.DustOptions(Color.RED, 1.5f));
                }
                ticks += 5;
            }
        }.runTaskTimer(plugin, 0L, 5L);

        // 2. MISSILE SPAWN AND TRAVEL
        getScheduler().runTaskLater(plugin, () -> {
            Location missileStart = target.clone().add(0, 60, 0);
            BlockDisplay missile = (BlockDisplay) target.getWorld().spawnEntity(missileStart, EntityType.BLOCK_DISPLAY);
            missile.setBlock(Material.COAL_BLOCK.createBlockData());

            // Scaled and rotated for a missile look
            missile.setTransformation(new Transformation(
                    new Vector3f(-0.25f, -1.0f, -0.25f),
                    new Quaternionf(),
                    new Vector3f(0.5f, 2.0f, 0.5f),
                    new Quaternionf()));

            target.getWorld().playSound(missileStart, Sound.ENTITY_WITHER_SHOOT, 5.0f, 0.5f);

            new BukkitRunnable() {
                int elapsed = 0;
                Location current = missileStart.clone();
                Vector step = new Vector(0, -1.5, 0);

                @Override
                public void run() {
                    if (current.getY() <= target.getY() + 1 || elapsed > 100) {
                        impact(target);
                        missile.remove();
                        cancel();
                        return;
                    }

                    current.add(step);
                    missile.teleport(current);

                    // Smoke and fire trail
                    target.getWorld().spawnParticle(Particle.LARGE_SMOKE, current, 5, 0.1, 0.5, 0.1, 0.02);
                    target.getWorld().spawnParticle(Particle.FLAME, current, 3, 0.1, 0.5, 0.1, 0.02);

                    if (elapsed % 5 == 0) {
                        target.getWorld().playSound(current, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 2.0f, 0.5f);
                    }

                    elapsed++;
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }, 60L); // Starts after 3s of laser targeting
    }

    private static void impact(Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5.0f, 0.5f);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, loc, 5, 1, 1, 1, 0.1);
        loc.getWorld().spawnParticle(Particle.FLASH, loc.add(0, 1, 0), 10, 2, 2, 2, 0.05);

        TNTPrimed tnt = loc.getWorld().spawn(loc, TNTPrimed.class);
        tnt.setFuseTicks(0); // Instant boom
    }
}

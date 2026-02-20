package com.Chagui68.weaponsaddon.utils;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.Vector;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CinematicUtils {

    private static final Map<UUID, GameMode> originalGameModes = new HashMap<>();
    private static final Map<UUID, Location> originalLocations = new HashMap<>();
    private static final Map<UUID, ArmorStand> activeNpcs = new HashMap<>();
    private static final Map<UUID, LivingEntity> activeBosses = new HashMap<>();

    /**
     * Inicia la cinemática de "The Shadow Realm Trap" para el Purple Guy.
     */
    public static void startPurpleGuyCinematic(Plugin plugin, Player player, LivingEntity purpleGuy, double damage) {
        UUID uuid = player.getUniqueId();
        if (activeNpcs.containsKey(uuid))
            return;

        // 1. Guardar estado original
        originalGameModes.put(uuid, player.getGameMode());
        originalLocations.put(uuid, player.getLocation().clone());
        activeBosses.put(uuid, purpleGuy);

        // Freeze Purple Guy
        if (purpleGuy != null) {
            purpleGuy.setAI(false);
        }

        // 2. Crear el "Cuerpo Falso" (NPC)
        Location npcLoc = player.getLocation().clone().add(0, 3.0, 0); // Elevate 3 blocks
        ArmorStand npc = (ArmorStand) player.getWorld().spawnEntity(npcLoc, EntityType.ARMOR_STAND);
        npc.setVisible(false); // Hide the wooden parts (the "sticks") to look more like a spirit/player
        npc.setGravity(false);
        npc.setBasePlate(false);
        npc.setArms(true);
        npc.setCustomName(player.getName());
        npc.setCustomNameVisible(true);

        // Initial state: Only the player's head (vulnerable/trapped)
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwnerProfile(player.getPlayerProfile());
            head.setItemMeta(meta);
        }
        npc.getEquipment().setHelmet(head);

        // Hide armor at start (will appear during sequence)
        npc.getEquipment().setChestplate(null);
        npc.getEquipment().setLeggings(null);
        npc.getEquipment().setBoots(null);

        npc.getEquipment().setItemInMainHand(player.getInventory().getItemInMainHand());

        activeNpcs.put(uuid, npc);

        // 3. Preparar al jugador (Cámara)
        player.setGameMode(GameMode.SPECTATOR);

        // 4. Secuencia de la cinemática
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (!player.isOnline() || player.isDead() || (purpleGuy != null && purpleGuy.isDead())) {
                    cleanup(player);
                    this.cancel();
                    return;
                }

                ticks++;

                // Floating oscillation effect (Onda senoidal suave)
                double offset = Math.sin(ticks * 0.15) * 0.2;
                Location floatingLoc = npcLoc.clone().add(0, offset, 0);
                npc.teleport(floatingLoc);

                // Position Purple Guy to "watch" (Behind the NPC at ground level)
                if (purpleGuy != null && ticks % 5 == 0) {
                    Location watchLoc = npcLoc.clone().add(npcLoc.getDirection().multiply(-2.5)).add(0, -3.0, 0);
                    Vector lookDir = npcLoc.toVector().subtract(watchLoc.toVector());
                    watchLoc.setDirection(lookDir);
                    purpleGuy.teleport(watchLoc);
                }

                // Cámara estable y bloqueada - VISTA FRONTAL (multiply 3.5 instead of -3)
                Location camLoc = floatingLoc.clone().add(floatingLoc.getDirection().multiply(3.5)).add(0, 1.0, 0);
                camLoc.setDirection(floatingLoc.toVector().subtract(camLoc.toVector()));
                player.teleport(camLoc);

                // Tock 1: Títulos y Sonido inicial
                if (ticks == 1) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
                    player.sendTitle(ChatColor.DARK_PURPLE + "SPRINGLOCK FAILURE", ChatColor.RED + "Don't Move...",
                            10, 80, 10);
                }

                // Tock 30: Boots
                if (ticks == 30) {
                    npc.getEquipment().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
                    player.playSound(floatingLoc, Sound.BLOCK_ANVIL_LAND, 1.0f, 1.2f);
                    Particle crit = VersionSafe.getParticle("CRIT");
                    if (crit != null) {
                        player.getWorld().spawnParticle(crit, floatingLoc.clone().add(0, 0.2, 0), 10);
                    }
                }

                // Tock 60: Leggings
                if (ticks == 60) {
                    npc.getEquipment().setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
                    player.playSound(floatingLoc, Sound.BLOCK_ANVIL_LAND, 1.0f, 1.1f);
                    Particle crit = VersionSafe.getParticle("CRIT");
                    if (crit != null) {
                        player.getWorld().spawnParticle(crit, floatingLoc.clone().add(0, 0.8, 0), 10);
                    }
                }

                // Tock 90: Chestplate
                if (ticks == 90) {
                    npc.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
                    player.playSound(floatingLoc, Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                    Particle crit = VersionSafe.getParticle("CRIT");
                    if (crit != null) {
                        player.getWorld().spawnParticle(crit, floatingLoc.clone().add(0, 1.2, 0), 10);
                    }
                }

                // Tock 120: The Springtrap Head
                if (ticks == 120) {
                    npc.getEquipment().setHelmet(getSpringtrapHead());
                    player.playSound(floatingLoc, Sound.BLOCK_ANVIL_LAND, 1.2f, 0.8f);
                    Particle smoke = VersionSafe.getParticle("LARGE_SMOKE");
                    if (smoke != null) {
                        player.getWorld().spawnParticle(smoke, floatingLoc.clone().add(0, 1.8, 0), 15);
                    }
                }

                // Tock 140: The Final Crunch
                if (ticks == 140) {
                    player.playSound(floatingLoc, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1.5f, 0.5f);
                    player.playSound(floatingLoc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.5f, 0.5f);
                    Particle explode = VersionSafe.getParticle("EXPLOSION_EMITTER");
                    if (explode != null) {
                        player.getWorld().spawnParticle(explode, floatingLoc, 5);
                    }
                    Particle block = VersionSafe.getParticle("BLOCK");
                    if (block != null) {
                        player.getWorld().spawnParticle(block, floatingLoc.clone().add(0, 1, 0), 50,
                                Material.REDSTONE_BLOCK.createBlockData());
                    }

                    cleanup(player);

                    // Delay damage by 2 ticks and clear effects to ensure configured damage
                    // registers
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        if (player.isOnline()) {
                            // Clear all effects to bypass Resistance/mitigations
                            player.getActivePotionEffects()
                                    .forEach(effect -> player.removePotionEffect(effect.getType()));

                            player.setInvulnerable(false);
                            player.setNoDamageTicks(0);
                            player.damage(damage, purpleGuy);
                        }
                    }, 2L);

                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private static void cleanup(Player player) {
        UUID uuid = player.getUniqueId();

        // Restaurar Purple Guy
        LivingEntity purpleGuy = activeBosses.remove(uuid);
        if (purpleGuy != null && purpleGuy.isValid()) {
            purpleGuy.setAI(true);
        }

        // Eliminar NPC
        ArmorStand npc = activeNpcs.remove(uuid);
        if (npc != null)
            npc.remove();

        // Restaurar posición y modo
        Location loc = originalLocations.remove(uuid);
        if (loc != null)
            player.teleport(loc);

        GameMode gm = originalGameModes.remove(uuid);
        if (gm != null)
            player.setGameMode(gm);

        player.removePotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS);
    }

    private static ItemStack getSpringtrapHead() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta == null)
            return head;

        // Texture URL:
        // http://textures.minecraft.net/texture/39d6eb1ba297d6c8e08d5cc4584c5819e79b2d5d3421e5a0ed0308487b010a71
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.fromString("39d6eb1b-a297-d6c8-e08d-5cc4584c5819"),
                "Springtrap");
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL(
                    "http://textures.minecraft.net/texture/39d6eb1ba297d6c8e08d5cc4584c5819e79b2d5d3421e5a0ed0308487b010a71"));
        } catch (MalformedURLException e) {
            // Fallback to name if URL fails
        }
        profile.setTextures(textures);
        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }
}

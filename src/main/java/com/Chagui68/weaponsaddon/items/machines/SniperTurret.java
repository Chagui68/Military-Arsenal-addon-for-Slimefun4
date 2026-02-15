package com.Chagui68.weaponsaddon.items.machines;

import com.Chagui68.weaponsaddon.items.CustomRecipeItem;
import com.Chagui68.weaponsaddon.items.MilitaryRecipeTypes;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.machines.energy.EnergyManager;
import com.Chagui68.weaponsaddon.items.vouchers.MilitaryVouchers;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Hoglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.bukkit.util.RayTraceResult;
import org.bukkit.metadata.FixedMetadataValue;
import com.Chagui68.weaponsaddon.WeaponsAddon;
import com.Chagui68.weaponsaddon.utils.TurretUtils;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getWorlds;

public class SniperTurret extends CustomRecipeItem implements EnergyNetComponent, Listener {

    private static final int ENERGY_CAPACITY = 6000;
    private static final int ENERGY_PER_SHOT = 250;
    private static final double RANGE = 55.0;
    private static final double DAMAGE = 100.0;
    private static final int SHOT_COOLDOWN = 3; // Cooldown in Slimefun ticks

    // --- VISUAL CONFIGURATION CONSTANTS ---
    private static final float STEM_SCALE_X = 0.25f;
    private static final float STEM_SCALE_Y = 1.1f;
    private static final float STEM_SCALE_Z = 0.25f;
    private static final float STEM_OFFSET_X = -0.125f;

    private static final float HEAD_OFFSET_Y = 1.1f;
    private static final float HEAD_SCALE = 0.5f;
    private static final float HEAD_INT_OFFSET = -0.25f;

    private static final float BARREL_OFFSET_Z = 0.1f;
    private static final float BARREL_SCALE_X = 0.12f;
    private static final float BARREL_SCALE_Y = 0.12f;
    private static final float BARREL_SCALE_Z = 1.4f;

    private static final float SENSOR_OFFSET_X = 0.1f;
    private static final float SENSOR_OFFSET_Y = 0.20f;
    private static final float SENSOR_OFFSET_Z = 0.20f;
    private static final float SENSOR_SCALE = 0.12f;

    private static final Material STEM_MATERIAL = Material.BLACK_CONCRETE;
    private static final Material HEAD_MATERIAL = Material.GOLD_BLOCK;
    private static final Material BARREL_MATERIAL = Material.GRAY_CONCRETE;
    private static final Material SENSOR_MATERIAL = Material.RED_STAINED_GLASS;
    // -----------------------------------------------------------------

    public static final SlimefunItemStack SNIPER_TURRET = new SlimefunItemStack(
            "MA_SNIPER_TURRET",
            Material.RED_STAINED_GLASS,
            "&1🎯 &9Long-Range Sniper Turret",
            "",
            "&7Precision-engineered for long-range",
            "&7elimination of hostile targets.",
            "",
            "&6Maximum Range: &e45 Blocks",
            "&6Precision Damage: &e30.0 HP",
            "&6Fire Rate: &eVery Slow (3s)",
            "&6Energy: &e250 J per shot",
            "&6Capacity: &e6000 J",
            "",
            "&eRight-Click to place",
            "&8(Animated 3D Model)");

    public SniperTurret(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
        super(itemGroup, item, MilitaryRecipeTypes.getMilitaryMachineFabricator(), recipe, RecipeGridSize.GRID_6x6);
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return ENERGY_CAPACITY;
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                e.getBlock().setType(Material.LIGHT);
                BlockStorage.addBlockInfo(e.getBlock(), "id", "MA_SNIPER_TURRET");
                spawnPvzModel(e.getBlock().getLocation());
            }
        });

        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                removePvzModel(e.getBlock().getLocation());
            }

            @Override
            public void onExplode(Block b, List<ItemStack> drops) {
                removePvzModel(b.getLocation());
            }
        });

        addItemHandler(new BlockTicker() {
            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                SniperTurret.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    private void tick(Block b) {
        Location loc = b.getLocation();

        // --- Persistence Fix: Auto-Respawn Model if missing ---
        String tag = "PVZ_SNIPER_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        Entity modelPart = loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 1.5, 1.5)
                .stream().filter(e -> e.getScoreboardTags().contains(tag)).findFirst().orElse(null);

        if (modelPart == null) {
            spawnPvzModel(loc);
        }
        // -----------------------------------------------------

        // Handle Cooldown using BlockStorage
        String cooldownStr = BlockStorage.getLocationInfo(loc, "cooldown");
        int cooldown = cooldownStr == null ? 0 : Integer.parseInt(cooldownStr);

        if (cooldown > 0) {
            BlockStorage.addBlockInfo(loc, "cooldown", String.valueOf(cooldown - 1));
            // Rotate towards target even during cooldown
            LivingEntity target = findTarget(loc.clone().add(0.5, 0.5, 0.5));
            updateModelRotation(loc, target);
            return;
        }

        int charge = EnergyManager.getCharge(loc);

        LivingEntity target = findTarget(loc);
        updateModelRotation(loc, target);

        if (target == null)
            return;

        if (charge < ENERGY_PER_SHOT) {
            return;
        }

        shoot(loc.clone().add(0.5, 0.5, 0.5), target);
        EnergyManager.removeCharge(loc, ENERGY_PER_SHOT);
        BlockStorage.addBlockInfo(loc, "cooldown", String.valueOf(SHOT_COOLDOWN));
    }

    private LivingEntity findTarget(Location loc) {
        Location center = loc.clone().add(0.5, 0.5, 0.5);
        Collection<Entity> nearby = loc.getWorld().getNearbyEntities(center, RANGE, RANGE, RANGE);
        LivingEntity closest = null;
        double closestDist = Double.MAX_VALUE;

        for (Entity e : nearby) {
            // Target all Hostile Entities (Monsters, Slimes, etc.)
            boolean isHostile = e instanceof Monster || e instanceof Slime || e instanceof Ghast || e instanceof Phantom
                    || e instanceof Shulker || e instanceof Hoglin;

            if (isHostile && !e.isDead()
                    && !e.hasMetadata("no_target")
                    && !e.getScoreboardTags().contains("PVZ_HEAD")
                    && !e.getScoreboardTags().contains("PVZ_GUARDIAN")) {
                double dist = e.getLocation().distanceSquared(center);
                if (dist < closestDist && dist <= RANGE * RANGE) {
                    if (hasLineOfSight(loc, (LivingEntity) e)) {
                        closestDist = dist;
                        closest = (LivingEntity) e;
                    }
                }
            }
        }
        return closest;
    }

    private boolean hasLineOfSight(Location loc, LivingEntity target) {
        // Standardized LOS height: 1.1
        Location start = loc.clone().add(0.5, 1.1, 0.5);
        Location end = target.getEyeLocation();
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = direction.length();

        // Raytrace to check for solid blocks
        RayTraceResult result = loc.getWorld().rayTraceBlocks(start, direction.normalize(), distance,
                FluidCollisionMode.NEVER, true);

        // If result is null, it means nothing was hit, so LOS is clear
        return result == null || result.getHitBlock() == null;
    }

    private void shoot(Location start, LivingEntity target) {
        Location targetLoc = target.getEyeLocation();
        Vector direction = targetLoc.toVector().subtract(start.toVector()).normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));

        double radYaw = Math.toRadians(yaw);
        Location muzzle = start.clone().add(
                -Math.sin(radYaw) * (BARREL_SCALE_Z + 0.1),
                0.4 + (HEAD_OFFSET_Y - 0.95),
                Math.cos(radYaw) * (BARREL_SCALE_Z + 0.1));

        muzzle.getWorld().playSound(muzzle, Sound.ENTITY_EGG_THROW, 1.5f, 0.8f);
        muzzle.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, muzzle, 5, 0.1, 0.1, 0.1, 0.05);

        Location bullet = muzzle.clone();
        for (int i = 0; i < 20; i++) {
            bullet.add(direction.clone().multiply(0.5));
            if (bullet.distanceSquared(muzzle) > RANGE * RANGE)
                break;
            start.getWorld().spawnParticle(Particle.COMPOSTER, bullet, 1, 0, 0, 0, 0);
        }

        target.setNoDamageTicks(0);
        target.damage(DAMAGE);

        target.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, targetLoc, 5, 0.2, 0.2, 0.2, 0.05);
        target.getWorld().playSound(targetLoc, Sound.ENTITY_SLIME_ATTACK, 1.0f, 1.2f);
    }

    private void spawnPvzModel(Location loc) {
        Location center = loc.clone().add(0.5, 0, 0.5);
        World world = loc.getWorld();
        String tag = "PVZ_SNIPER_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();

        BlockDisplay stem = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        stem.setBlock(STEM_MATERIAL.createBlockData());
        stem.setTransformation(new Transformation(
                new Vector3f(STEM_OFFSET_X, 0.0f, STEM_OFFSET_X),
                new Quaternionf(),
                new Vector3f(STEM_SCALE_X, STEM_SCALE_Y, STEM_SCALE_Z),
                new Quaternionf()));
        stem.addScoreboardTag(tag);

        BlockDisplay frame = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        frame.setBlock(Material.IRON_BARS.createBlockData());
        frame.setTransformation(new Transformation(
                new Vector3f(-0.2f, 0.0f, -0.2f),
                new Quaternionf(),
                new Vector3f(0.4f, STEM_SCALE_Y * 0.9f, 0.4f),
                new Quaternionf()));
        frame.addScoreboardTag(tag);

        BlockDisplay head = (BlockDisplay) world.spawnEntity(center.clone().add(0, HEAD_OFFSET_Y, 0),
                EntityType.BLOCK_DISPLAY);
        head.setBlock(HEAD_MATERIAL.createBlockData());
        head.setTransformation(new Transformation(
                new Vector3f(HEAD_INT_OFFSET, -0.15f, HEAD_INT_OFFSET),
                new Quaternionf(),
                new Vector3f(HEAD_SCALE, HEAD_SCALE, HEAD_SCALE),
                new Quaternionf()));
        head.addScoreboardTag(tag);
        head.addScoreboardTag("PVZ_HEAD");

        BlockDisplay mouth = (BlockDisplay) world.spawnEntity(center.clone().add(0, HEAD_OFFSET_Y, 0),
                EntityType.BLOCK_DISPLAY);
        mouth.setBlock(BARREL_MATERIAL.createBlockData());
        mouth.setTransformation(new Transformation(
                new Vector3f(-BARREL_SCALE_X / 2f, 0.0f, BARREL_OFFSET_Z),
                new Quaternionf(),
                new Vector3f(BARREL_SCALE_X, BARREL_SCALE_Y, BARREL_SCALE_Z),
                new Quaternionf()));
        mouth.addScoreboardTag(tag);
        mouth.addScoreboardTag("PVZ_MOUTH");

        BlockDisplay sensor = (BlockDisplay) world.spawnEntity(center.clone().add(0, HEAD_OFFSET_Y, 0),
                EntityType.BLOCK_DISPLAY);
        sensor.setBlock(SENSOR_MATERIAL.createBlockData());
        sensor.setTransformation(new Transformation(
                new Vector3f(SENSOR_OFFSET_X, SENSOR_OFFSET_Y, SENSOR_OFFSET_Z),
                new Quaternionf(),
                new Vector3f(SENSOR_SCALE, SENSOR_SCALE, SENSOR_SCALE),
                new Quaternionf()));
        sensor.addScoreboardTag(tag);
        sensor.addScoreboardTag("PVZ_SENSOR");

        Interaction interaction = (Interaction) world.spawnEntity(center, EntityType.INTERACTION);
        interaction.setInteractionWidth(1.2f);
        interaction.setInteractionHeight(HEAD_OFFSET_Y + HEAD_SCALE);
        interaction.addScoreboardTag(tag);
        interaction.addScoreboardTag("SNIPER_HITBOX");
    }

    @EventHandler
    public void onHitboxAttack(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Interaction))
            return;
        Interaction interaction = (Interaction) e.getEntity();
        if (!interaction.getScoreboardTags().contains("SNIPER_HITBOX"))
            return;

        handleDismantle(interaction, e.getDamager());
        e.setCancelled(true);
    }

    @EventHandler
    public void onHitboxInteract(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Interaction))
            return;
        Interaction interaction = (Interaction) e.getRightClicked();
        if (!interaction.getScoreboardTags().contains("SNIPER_HITBOX"))
            return;

        handleDismantle(interaction, e.getPlayer());
        e.setCancelled(true);
    }

    private void handleDismantle(Interaction interaction, Entity damager) {
        if (!(damager instanceof Player))
            return;

        // Layer 1: Global Location Lock
        if (!TurretUtils.beginDismantle(interaction.getLocation())) {
            return;
        }

        // Layer 2: Metadata Lock
        if (interaction.hasMetadata("MA_DISMANTLED") || !interaction.isValid()) {
            return;
        }

        for (String tag : interaction.getScoreboardTags()) {
            if (tag.startsWith("PVZ_SNIPER_")) {
                String[] parts = tag.split("_");
                if (parts.length == 5) {
                    try {
                        int x = Integer.parseInt(parts[2]);
                        int y = Integer.parseInt(parts[3]);
                        int z = Integer.parseInt(parts[4]);
                        Location loc = new Location(interaction.getWorld(), x, y, z);

                        // Layer 3: Block State Validation
                        String id = BlockStorage.getLocationInfo(loc, "id");
                        if (id != null && id.equals("MA_SNIPER_TURRET")) {
                            // Atomic DUPLICATION PROTECTION: Set metadata immediately
                            interaction.setMetadata("MA_DISMANTLED",
                                    new FixedMetadataValue(WeaponsAddon.getInstance(), true));

                            // Race condition protection: Clear info BEFORE dropping/removing
                            BlockStorage.clearBlockInfo(loc);
                            loc.getBlock().setType(Material.AIR);

                            interaction.getWorld().playSound(loc, Sound.BLOCK_LANTERN_BREAK, 1f, 1f);
                            interaction.getWorld().dropItemNaturally(loc, SNIPER_TURRET.clone());
                            removePvzModel(loc);
                            interaction.remove();
                        } else {
                            // If it's a "ghost" model, just remove the entities
                            removePvzModel(loc);
                            interaction.remove();
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
                break;
            }
        }

    }

    public static void cleanupAllModels() {
        for (World world : getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getScoreboardTags().stream().anyMatch(tag -> tag.startsWith("PVZ_"))) {
                    entity.remove();
                }
            }
        }
    }

    private void removePvzModel(Location loc) {
        String tag = "PVZ_SNIPER_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        for (Entity entity : loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 1.5, 1.5)) {
            if (entity.getScoreboardTags().contains(tag)) {
                entity.remove();
            }
        }
    }

    private void updateModelRotation(Location loc, LivingEntity target) {
        String tag = "PVZ_SNIPER_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        Location center = loc.clone().add(0.5, 0.6, 0.5);

        float yaw = 0;
        if (target != null) {
            Vector dir = target.getLocation().toVector().subtract(center.toVector());
            yaw = (float) Math.toDegrees(Math.atan2(-dir.getX(), dir.getZ()));
        }

        for (Entity entity : loc.getWorld().getNearbyEntities(center, 1.5, 1.5, 1.5)) {
            if (entity.getScoreboardTags().contains(tag) &&
                    (entity.getScoreboardTags().contains("PVZ_HEAD")
                            || entity.getScoreboardTags().contains("PVZ_MOUTH")
                            || entity.getScoreboardTags().contains("PVZ_SENSOR"))) {

                Location eloc = entity.getLocation();
                eloc.setYaw(yaw);
                entity.teleport(eloc);
            }
        }
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] recipe = new ItemStack[] {
                MilitaryComponents.FIREARM_BARREL, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.FIREARM_BARREL,
                MilitaryComponents.KINETIC_STABILIZER, MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.KINETIC_STABILIZER,
                MilitaryComponents.MOVEMENT_CIRCUIT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryVouchers.VOUCHER_DOCUMENT, MilitaryComponents.TURRET_SHELL, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.MOVEMENT_CIRCUIT,
                MilitaryComponents.MOVEMENT_CIRCUIT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TURRET_SHELL, MilitaryVouchers.VOUCHER_WEAPON_UPGRADE, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.MOVEMENT_CIRCUIT,
                MilitaryComponents.KINETIC_STABILIZER, MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.KINETIC_STABILIZER,
                MilitaryComponents.FIREARM_BARREL, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.FIREARM_BARREL
        };
        SniperTurret turret = new SniperTurret(category, SNIPER_TURRET, recipe);
        turret.register(addon);

        if (addon instanceof Plugin) {
            getPluginManager().registerEvents(turret, (Plugin) addon);
        }
    }
}

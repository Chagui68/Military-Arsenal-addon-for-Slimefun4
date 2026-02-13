package com.Chagui68.weaponsaddon.items.machines;

import com.Chagui68.weaponsaddon.items.CustomRecipeItem;
import com.Chagui68.weaponsaddon.items.MilitaryRecipeTypes;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.machines.energy.EnergyManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.FluidCollisionMode;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Hoglin;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getWorlds;

public class AttackTurret extends CustomRecipeItem implements EnergyNetComponent, Listener {

    private static final int ENERGY_CAPACITY = 5000;
    private static final int ENERGY_PER_SHOT = 100;
    private static final double RANGE = 15.0;
    private static final double DAMAGE = 30.0;

    // --- VISUAL CONFIGURATION CONSTANTS (Tweak these for design) ---
    private static final float STEM_SCALE_X = 0.3f;
    private static final float STEM_SCALE_Y = 0.95f;
    private static final float STEM_SCALE_Z = 0.3f;
    private static final float STEM_OFFSET_X = -0.15f;

    private static final float HEAD_OFFSET_Y = 0.95f; // Elevation of the head
    private static final float HEAD_SCALE = 0.7f;
    private static final float HEAD_INT_OFFSET = -0.35f; // Internal centering

    private static final float BARREL_OFFSET_Z = 0.1f;
    private static final float BARREL_SCALE_X = 0.2f;
    private static final float BARREL_SCALE_Y = 0.2f;
    private static final float BARREL_SCALE_Z = 0.6f; // Shortened from 1.0/0.8

    private static final float SENSOR_OFFSET_X = 0.15f;
    private static final float SENSOR_OFFSET_Y = 0.3f;
    private static final float SENSOR_OFFSET_Z = 0.3f; // Forward position
    private static final float SENSOR_SCALE = 0.15f;

    private static final Material STEM_MATERIAL = Material.GRAY_CONCRETE;
    private static final Material HEAD_MATERIAL = Material.NETHERITE_BLOCK;
    private static final Material BARREL_MATERIAL = Material.IRON_BLOCK;
    private static final Material SENSOR_MATERIAL = Material.CYAN_STAINED_GLASS;
    // -----------------------------------------------------------------

    public static final SlimefunItemStack ATTACK_TURRET = new SlimefunItemStack(
            "ATTACK_TURRET",
            Material.NETHERITE_BLOCK,
            "&1🛡 &9Industrial Attack Turret",
            "",
            "&7Automated robotic defense system.",
            "&7Advanced AI with targeting sensors.",
            "",
            "&6Range: &e30 Blocks",
            "&6Damage: &e15.0 HP",
            "&6Energy: &e100 J per shot",
            "&6Capacity: &e5000 J",
            "",
            "&eRight-Click to place",
            "&8(Invisible anchor block)");

    public AttackTurret(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
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
                // Change the block to LIGHT to act as an anchor
                e.getBlock().setType(Material.LIGHT);
                BlockStorage.addBlockInfo(e.getBlock(), "id", "ATTACK_TURRET");
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
                AttackTurret.this.tick(b);
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
        String tag = "PVZ_ATTACK_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        Entity modelPart = loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 1.5, 1.5)
                .stream().filter(e -> e.getScoreboardTags().contains(tag)).findFirst().orElse(null);

        if (modelPart == null) {
            spawnPvzModel(loc);
        }
        // -----------------------------------------------------

        Location centerLoc = loc.clone().add(0.5, 0.5, 0.5);
        int charge = EnergyManager.getCharge(b.getLocation());

        LivingEntity target = findTarget(centerLoc);
        updateModelRotation(b.getLocation(), target);

        if (target == null)
            return;

        if (charge < ENERGY_PER_SHOT) {
            return;
        }

        shoot(centerLoc, target);
        EnergyManager.removeCharge(b.getLocation(), ENERGY_PER_SHOT);
    }

    private LivingEntity findTarget(Location loc) {
        Collection<Entity> nearby = loc.getWorld().getNearbyEntities(loc, RANGE, RANGE, RANGE);
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
                double dist = e.getLocation().distanceSquared(loc);
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
        Location start = loc.clone().add(0, HEAD_OFFSET_Y, 0);
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

        // Calculate muzzle location based on current turret rotation
        Vector direction = targetLoc.toVector().subtract(start.toVector()).normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));

        // Offset starting point to the muzzle
        double radYaw = Math.toRadians(yaw);
        Location muzzle = start.clone().add(
                -Math.sin(radYaw) * (BARREL_SCALE_Z + 0.1),
                0.4 + (HEAD_OFFSET_Y - 0.95), // Adjust based on head elevation
                Math.cos(radYaw) * (BARREL_SCALE_Z + 0.1));
        // Visual and Sound effects
        muzzle.getWorld().playSound(muzzle, Sound.ENTITY_EGG_THROW, 1.5f, 0.8f);
        muzzle.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, muzzle, 5, 0.1, 0.1, 0.1, 0.05);

        // Visual "Bullet" (Fast moving particle)
        Location bullet = muzzle.clone();
        for (int i = 0; i < 15; i++) {
            bullet.add(direction.clone().multiply(0.5));
            if (bullet.distanceSquared(muzzle) > RANGE * RANGE)
                break;
            start.getWorld().spawnParticle(Particle.COMPOSTER, bullet, 1, 0, 0, 0, 0);
        }

        // Direct damage with no-damage-ticks bypass
        target.setNoDamageTicks(0);
        target.damage(DAMAGE);

        target.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, targetLoc, 5, 0.2, 0.2, 0.2, 0.05);
        target.getWorld().playSound(targetLoc, Sound.ENTITY_SLIME_ATTACK, 1.0f, 1.2f);
    }

    private void spawnPvzModel(Location loc) {
        Location center = loc.clone().add(0.5, 0, 0.5);
        World world = loc.getWorld();
        String tag = "PVZ_ATTACK_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();

        // 1. HYDRAULIC STEM (Industrial base)
        BlockDisplay stem = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        stem.setBlock(STEM_MATERIAL.createBlockData());
        stem.setTransformation(new Transformation(
                new Vector3f(STEM_OFFSET_X, 0.0f, STEM_OFFSET_X),
                new Quaternionf(),
                new Vector3f(STEM_SCALE_X, STEM_SCALE_Y, STEM_SCALE_Z),
                new Quaternionf()));
        stem.addScoreboardTag(tag);

        // 2. SUPPORT FRAME (Mechanical detail)
        BlockDisplay frame = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        frame.setBlock(Material.IRON_BARS.createBlockData());
        frame.setTransformation(new Transformation(
                new Vector3f(-0.2f, 0.0f, -0.2f),
                new Quaternionf(),
                new Vector3f(0.4f, STEM_SCALE_Y * 0.9f, 0.4f),
                new Quaternionf()));
        frame.addScoreboardTag(tag);

        // 3. ARMORED HEAD
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

        // 4. RAILGUN MUZZLE
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

        // 5. OPTICAL SENSOR (Targeting)
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

        // 6. DYNAMIC HITBOX (Interaction Entity)
        Interaction interaction = (Interaction) world.spawnEntity(center, EntityType.INTERACTION);
        interaction.setInteractionWidth(1.2f);
        interaction.setInteractionHeight(HEAD_OFFSET_Y + HEAD_SCALE);
        interaction.addScoreboardTag(tag);
        interaction.addScoreboardTag("PVZ_HITBOX");
    }

    @EventHandler
    public void onHitboxAttack(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Interaction))
            return;

        Interaction interaction = (Interaction) e.getEntity();
        if (!interaction.getScoreboardTags().contains("PVZ_HITBOX"))
            return;

        for (String tag : interaction.getScoreboardTags()) {
            if (tag.startsWith("PVZ_ATTACK_")) {
                String[] parts = tag.split("_");
                if (parts.length == 5) {
                    try {
                        int x = Integer.parseInt(parts[2]);
                        int y = Integer.parseInt(parts[3]);
                        int z = Integer.parseInt(parts[4]);
                        Location loc = new Location(interaction.getWorld(), x, y, z);

                        if (e.getDamager() instanceof Player) {
                            // Visual and sound feedback
                            interaction.getWorld().playSound(interaction.getLocation(), Sound.BLOCK_LANTERN_BREAK, 1f,
                                    1f);
                            interaction.getWorld().spawnParticle(Particle.WAX_OFF,
                                    interaction.getLocation().add(0, 0.5, 0), 15, 0.2, 0.2, 0.2, 0.1);

                            // Force drop the item manually
                            interaction.getWorld().dropItemNaturally(loc, ATTACK_TURRET.clone());

                            // Clear block and Slimefun data COMPLETELY
                            loc.getBlock().setType(Material.AIR);
                            BlockStorage.clearBlockInfo(loc);
                            removePvzModel(loc);

                            e.setCancelled(true);
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
        String tag = "PVZ_ATTACK_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        for (Entity entity : loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 1.5, 1.5)) {
            if (entity.getScoreboardTags().contains(tag)) {
                entity.remove();
            }
        }
    }

    private void updateModelRotation(Location loc, LivingEntity target) {
        String tag = "PVZ_ATTACK_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
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
        ItemStack[] recipe = new ItemStack[36];
        for (int i = 0; i < 36; i++)
            recipe[i] = MilitaryComponents.REINFORCED_PLATING;

        // Customizing some parts of the heavy recipe
        recipe[14] = MilitaryComponents.TARGETING_SYSTEM;
        recipe[15] = MilitaryComponents.TARGETING_SYSTEM;
        recipe[20] = MilitaryComponents.TARGETING_SYSTEM;
        recipe[21] = MilitaryComponents.TARGETING_SYSTEM;
        recipe[2] = MilitaryComponents.ADVANCED_CIRCUIT;
        recipe[3] = MilitaryComponents.ADVANCED_CIRCUIT;
        recipe[32] = MilitaryComponents.ENERGY_MATRIX;
        recipe[33] = MilitaryComponents.ENERGY_MATRIX;

        AttackTurret turret = new AttackTurret(category, ATTACK_TURRET, recipe);
        turret.register(addon);

        // Register the hitbox listener
        if (addon instanceof Plugin) {
            getPluginManager().registerEvents(turret, (Plugin) addon);
        }
    }
}

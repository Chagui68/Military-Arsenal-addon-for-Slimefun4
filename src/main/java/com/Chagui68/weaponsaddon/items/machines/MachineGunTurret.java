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
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.FluidCollisionMode;
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
import org.bukkit.plugin.*;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getWorlds;

public class MachineGunTurret extends CustomRecipeItem implements EnergyNetComponent, Listener {

    private static final int ENERGY_CAPACITY = 3000;
    private static final int ENERGY_PER_SHOT = 20;
    private static final double RANGE = 10.0;
    private static final double DAMAGE = 4.0; // Lower damage but higher fire rate

    // --- VISUAL CONFIGURATION ---
    private static final float HEAD_OFFSET_Y = 0.8f;
    private static final float HEAD_SCALE = 0.6f;

    private static final float BARREL_SCALE_Z = 0.5f;

    public static final SlimefunItemStack MACHINE_GUN_TURRET = new SlimefunItemStack(
            "MACHINE_GUN_TURRET",
            Material.IRON_BLOCK,
            "&4🔫 &cMachine Gun Turret",
            "",
            "&7A rapid-fire automated defense system.",
            "&7Equipped with 4 rotary mini-guns.",
            "",
            "&6Range: &e10 Blocks",
            "&6Damage: &e4.0 HP (High fire-rate)",
            "&6Energy: &e20 J per shot",
            "&6Capacity: &e3000 J",
            "",
            "&eRight-Click to place",
            "&8(Invisible anchor block)");

    public MachineGunTurret(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
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
                BlockStorage.addBlockInfo(e.getBlock(), "id", "MACHINE_GUN_TURRET");
                spawnModel(e.getBlock().getLocation());
            }
        });

        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                removeModel(e.getBlock().getLocation());
            }

            @Override
            public void onExplode(Block b, List<ItemStack> drops) {
                removeModel(b.getLocation());
            }
        });

        addItemHandler(new BlockTicker() {
            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                MachineGunTurret.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    private void tick(Block b) {
        Location loc = b.getLocation();
        String tag = "MG_TURRET_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();

        Entity modelPart = loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 1.5, 1.5)
                .stream().filter(e -> e.getScoreboardTags().contains(tag)).findFirst().orElse(null);

        if (modelPart == null) {
            spawnModel(loc);
        }

        Location centerLoc = loc.clone().add(0.5, 0.5, 0.5);
        int charge = EnergyManager.getCharge(b.getLocation());

        LivingEntity target = findTarget(centerLoc);
        updateModelRotation(b.getLocation(), target);

        if (target == null || charge < ENERGY_PER_SHOT) {
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
        RayTraceResult result = loc.getWorld().rayTraceBlocks(start, direction.normalize(), direction.length(),
                FluidCollisionMode.NEVER, true);
        return result == null || result.getHitBlock() == null;
    }

    private void shoot(Location start, LivingEntity target) {
        Location targetLoc = target.getEyeLocation();
        Vector direction = targetLoc.toVector().subtract(start.toVector()).normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));
        double radYaw = Math.toRadians(yaw);

        // Fire from one of the 4 barrels (randomly or sequentially)
        // For simplicity, fire from the center area with multiple particles
        Location muzzle = start.clone().add(
                -Math.sin(radYaw) * (BARREL_SCALE_Z + 0.1),
                0.4 + (HEAD_OFFSET_Y - 0.7),
                Math.cos(radYaw) * (BARREL_SCALE_Z + 0.1));

        muzzle.getWorld().playSound(muzzle, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.5f);
        muzzle.getWorld().spawnParticle(Particle.FLASH, muzzle, 1, 0.05, 0.05, 0.05, 0.05);

        Location bullet = muzzle.clone();
        for (int i = 0; i < 10; i++) {
            bullet.add(direction.clone().multiply(1.0));
            if (bullet.distanceSquared(muzzle) > RANGE * RANGE)
                break;
            start.getWorld().spawnParticle(Particle.CRIT, bullet, 1, 0, 0, 0, 0);
        }

        target.setNoDamageTicks(0);
        target.damage(DAMAGE);
    }

    private void spawnModel(Location loc) {
        Location center = loc.clone().add(0.5, 0, 0.5);
        World world = loc.getWorld();
        String tag = "MG_TURRET_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();

        // 1. BLACK BASE (Image 1 bottom)
        BlockDisplay base = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        base.setBlock(Material.BLACK_CONCRETE.createBlockData());
        base.setTransformation(new Transformation(
                new Vector3f(-0.35f, 0.0f, -0.35f),
                new Quaternionf(),
                new Vector3f(0.7f, 0.2f, 0.7f),
                new Quaternionf()));
        base.addScoreboardTag(tag);

        // 2. VERTICAL POST (Image 1 center)
        BlockDisplay post = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        post.setBlock(Material.LIGHT_GRAY_CONCRETE.createBlockData());
        post.setTransformation(new Transformation(
                new Vector3f(-0.1f, 0.2f, -0.1f),
                new Quaternionf(),
                new Vector3f(0.2f, 0.7f, 0.2f),
                new Quaternionf()));
        post.addScoreboardTag(tag);

        // 3. MAIN HEAD CUBE (Image 1 top)
        BlockDisplay head = (BlockDisplay) world.spawnEntity(center.clone().add(0, 0.9, 0), EntityType.BLOCK_DISPLAY);
        head.setBlock(Material.LIGHT_GRAY_CONCRETE.createBlockData());
        head.setTransformation(new Transformation(
                new Vector3f(-0.25f, -0.15f, -0.25f),
                new Quaternionf(),
                new Vector3f(0.5f, 0.5f, 0.5f),
                new Quaternionf()));
        head.addScoreboardTag(tag);
        head.addScoreboardTag("MG_HEAD");

        // 4. 4 BLACK BARRELS (Faithful to Image 1 vertical spacing)
        // Two on left, two on right, with vertical gap
        float[][] barrelOffsets = {
                { -0.55f, 0.15f, -0.1f }, // Left Top
                { -0.55f, -0.15f, -0.1f }, // Left Bottom
                { 0.25f, 0.15f, -0.1f }, // Right Top
                { 0.25f, -0.15f, -0.1f } // Right Bottom
        };

        for (int i = 0; i < 4; i++) {
            BlockDisplay barrel = (BlockDisplay) world.spawnEntity(center.clone().add(0, 0.9, 0),
                    EntityType.BLOCK_DISPLAY);
            barrel.setBlock(Material.BLACK_CONCRETE.createBlockData());
            barrel.setTransformation(new Transformation(
                    new Vector3f(barrelOffsets[i][0], barrelOffsets[i][1], 0.05f), // Slightly further forward
                    new Quaternionf(),
                    new Vector3f(0.25f, 0.18f, 0.9f), // Increased Z-scale from 0.4 to 0.9
                    new Quaternionf()));
            barrel.addScoreboardTag(tag);
            barrel.addScoreboardTag("MG_BARREL");
        }

        // 5. HITBOX
        Interaction interaction = (Interaction) world.spawnEntity(center, EntityType.INTERACTION);
        interaction.setInteractionWidth(1.2f);
        interaction.setInteractionHeight(HEAD_OFFSET_Y + HEAD_SCALE);
        interaction.addScoreboardTag(tag);
        interaction.addScoreboardTag("MG_HITBOX");
    }

    @EventHandler
    public void onHitboxAttack(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Interaction))
            return;
        Interaction interaction = (Interaction) e.getEntity();
        if (!interaction.getScoreboardTags().contains("MG_HITBOX"))
            return;

        for (String tag : interaction.getScoreboardTags()) {
            if (tag.startsWith("MG_TURRET_")) {
                String[] parts = tag.split("_");
                if (parts.length == 5) {
                    try {
                        int x = Integer.parseInt(parts[2]);
                        int y = Integer.parseInt(parts[3]);
                        int z = Integer.parseInt(parts[4]);
                        Location loc = new Location(interaction.getWorld(), x, y, z);
                        if (e.getDamager() instanceof Player) {
                            interaction.getWorld().dropItemNaturally(loc, MACHINE_GUN_TURRET.clone());
                            loc.getBlock().setType(Material.AIR);
                            BlockStorage.clearBlockInfo(loc);
                            removeModel(loc);
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
                if (entity.getScoreboardTags().stream().anyMatch(tag -> tag.startsWith("MG_"))) {
                    entity.remove();
                }
            }
        }
    }

    private void removeModel(Location loc) {
        String tag = "MG_TURRET_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        for (Entity entity : loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 1.5, 1.5)) {
            if (entity.getScoreboardTags().contains(tag)) {
                entity.remove();
            }
        }
    }

    private void updateModelRotation(Location loc, LivingEntity target) {
        String tag = "MG_TURRET_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        Location center = loc.clone().add(0.5, 0.6, 0.5);
        float yaw = 0;
        if (target != null) {
            Vector dir = target.getLocation().toVector().subtract(center.toVector());
            yaw = (float) Math.toDegrees(Math.atan2(-dir.getX(), dir.getZ()));
        }

        for (Entity entity : loc.getWorld().getNearbyEntities(center, 1.5, 1.5, 1.5)) {
            if (entity.getScoreboardTags().contains(tag) &&
                    (entity.getScoreboardTags().contains("MG_HEAD") ||
                            entity.getScoreboardTags().contains("MG_BARREL") ||
                            entity.getScoreboardTags().contains("MG_SENSOR"))) {
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
        recipe[14] = MilitaryComponents.TARGETING_SYSTEM;
        recipe[15] = MilitaryComponents.TARGETING_SYSTEM;
        recipe[2] = MilitaryComponents.ADVANCED_CIRCUIT;
        recipe[32] = MilitaryComponents.ENERGY_MATRIX;

        MachineGunTurret turret = new MachineGunTurret(category, MACHINE_GUN_TURRET, recipe);
        turret.register(addon);
        if (addon instanceof Plugin) {
            getPluginManager().registerEvents(turret, (Plugin) addon);
        }
    }
}

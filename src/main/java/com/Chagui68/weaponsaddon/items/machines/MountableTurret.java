package com.Chagui68.weaponsaddon.items.machines;

import com.Chagui68.weaponsaddon.items.CustomRecipeItem;
import com.Chagui68.weaponsaddon.items.MachineGun;
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
import org.bukkit.FluidCollisionMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.bukkit.metadata.FixedMetadataValue;
import com.Chagui68.weaponsaddon.WeaponsAddon;
import com.Chagui68.weaponsaddon.utils.TurretUtils;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.List;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getWorlds;

public class MountableTurret extends CustomRecipeItem implements EnergyNetComponent, Listener {

    private static final int ENERGY_CAPACITY = 50000;
    private static final int ENERGY_PER_SHOT = 150;
    private static final double DAMAGE = 10.0;
    private static final double RANGE = 35.0;

    // --- VISUAL CONFIGURATION (New Design) ---
    private static final Material PRIMARY_MATERIAL = Material.BLACK_CONCRETE;
    private static final Material SECONDARY_MATERIAL = Material.PURPLE_CONCRETE;
    private static final Material GLOW_MATERIAL = Material.SEA_LANTERN;
    private static final Material BARREL_MATERIAL = Material.NETHERITE_BLOCK;

    public static final SlimefunItemStack MOUNTABLE_TURRET = new SlimefunItemStack(
            "MA_MOUNTABLE_TURRET",
            Material.OBSERVER,
            "&4💣 &cWraith-Class War Turret",
            "",
            "&7A heavy player-controlled plasma turret.",
            "&7Advanced energy-based war machine.",
            "",
            "&6Target: &eAny (Including Players)",
            "&6Damage: &e10.0 HP",
            "&6Range: &e35 Blocks",
            "&6Energy: &e150 J per shot",
            "",
            "&eRight-Click the base to mount",
            "&eLeft-Click while mounted to fire");

    public MountableTurret(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
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
                BlockStorage.addBlockInfo(e.getBlock(), "id", "MA_MOUNTABLE_TURRET");
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
                MountableTurret.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    private void tick(Block b) {
        Location loc = b.getLocation();
        String tag = "MOUNT_TURRET_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();

        Entity modelPart = loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 1.5, 1.5)
                .stream().filter(e -> e.getScoreboardTags().contains(tag)).findFirst().orElse(null);

        if (modelPart == null) {
            spawnModel(loc);
        }

        // Update rotation based on passenger
        updateModelRotation(loc);
    }

    private void spawnModel(Location loc) {
        Location center = loc.clone().add(0.5, 0, 0.5);
        World world = loc.getWorld();
        String tag = "MOUNT_TURRET_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();

        // 1. ROUNDED RING BASE (Image 2)
        // Main donut-shaped platform
        BlockDisplay base = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        base.setBlock(PRIMARY_MATERIAL.createBlockData());
        base.setTransformation(new Transformation(
                new Vector3f(-0.5f, 0.15f, -0.5f),
                new Quaternionf(),
                new Vector3f(1.0f, 0.25f, 1.0f),
                new Quaternionf()));
        base.addScoreboardTag(tag);

        // 2. 4 LEG PODS / FEET (Image 2)
        float[][] legOffsets = {{0.45f, 0.45f}, {0.45f, -0.45f}, {-0.45f, 0.45f}, {-0.45f, -0.45f}};
        for (float[] offset : legOffsets) {
            BlockDisplay leg = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
            leg.setBlock(SECONDARY_MATERIAL.createBlockData());
            leg.setTransformation(new Transformation(
                    new Vector3f(offset[0] - 0.2f, 0.0f, offset[1] - 0.2f),
                    new Quaternionf(),
                    new Vector3f(0.4f, 0.3f, 0.4f),
                    new Quaternionf()));
            leg.addScoreboardTag(tag);
        }

        // 3. GLOWING CYAN HIGHLIGHTS
        BlockDisplay glow = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        glow.setBlock(GLOW_MATERIAL.createBlockData());
        glow.setTransformation(new Transformation(
                new Vector3f(-0.3f, 0.18f, -0.3f),
                new Quaternionf(),
                new Vector3f(0.6f, 0.05f, 0.6f),
                new Quaternionf()));
        glow.addScoreboardTag(tag);

        // 4. SEAT (ArmorStand)
        ArmorStand seat = (ArmorStand) world.spawnEntity(center.clone().add(0, 0.6, 0), EntityType.ARMOR_STAND);
        seat.setVisible(false);
        seat.setGravity(false);
        seat.setMarker(true);
        seat.addScoreboardTag(tag);
        seat.addScoreboardTag("MOUNT_SEAT");

        // 5. CENTRAL ROUNDED COCKPIT POD (Image 2)
        BlockDisplay head = (BlockDisplay) world.spawnEntity(center.clone().add(0, 0.9, 0), EntityType.BLOCK_DISPLAY);
        head.setBlock(PRIMARY_MATERIAL.createBlockData());
        head.setTransformation(new Transformation(
                new Vector3f(-0.4f, -0.3f, -0.4f),
                new Quaternionf(),
                new Vector3f(0.8f, 0.7f, 0.8f),
                new Quaternionf()));
        head.addScoreboardTag(tag);
        head.addScoreboardTag("MOUNT_HEAD");

        // 6. TWIN HEAVY CANNONS WITH VERTICAL WINGS (Image 2)
        float[] sideOffsets = {-0.65f, 0.45f};
        for (float x : sideOffsets) {
            // Barrel
            BlockDisplay b = (BlockDisplay) world.spawnEntity(center.clone().add(0, 1.1, 0), EntityType.BLOCK_DISPLAY);
            b.setBlock(BARREL_MATERIAL.createBlockData());
            b.setTransformation(new Transformation(
                    new Vector3f(x + 0.05f, 0.0f, 0.4f),
                    new Quaternionf(),
                    new Vector3f(0.15f, 0.15f, 1.4f),
                    new Quaternionf()));
            b.addScoreboardTag(tag);
            b.addScoreboardTag("MOUNT_BARREL");

            // Vertical Wings/Shields (Image 2 characteristic)
            BlockDisplay w = (BlockDisplay) world.spawnEntity(center.clone().add(0, 1.1, 0), EntityType.BLOCK_DISPLAY);
            w.setBlock(SECONDARY_MATERIAL.createBlockData());
            w.setTransformation(new Transformation(
                    new Vector3f(x - 0.1f, -0.3f, 0.0f),
                    new Quaternionf(),
                    new Vector3f(0.4f, 1.0f, 0.6f),
                    new Quaternionf()));
            w.addScoreboardTag(tag);
            w.addScoreboardTag("MOUNT_BARREL");

            // Wing Glow
            BlockDisplay wg = (BlockDisplay) world.spawnEntity(center.clone().add(0, 1.1, 0), EntityType.BLOCK_DISPLAY);
            wg.setBlock(GLOW_MATERIAL.createBlockData());
            wg.setTransformation(new Transformation(
                    new Vector3f(x - 0.11f, 0.1f, 0.2f),
                    new Quaternionf(),
                    new Vector3f(0.42f, 0.1f, 0.2f),
                    new Quaternionf()));
            wg.addScoreboardTag(tag);
            wg.addScoreboardTag("MOUNT_BARREL");
        }

        // 7. LARGE REAR RING / ARCH (Image 2)
        BlockDisplay ring = (BlockDisplay) world.spawnEntity(center.clone().add(0, 1.3, 0), EntityType.BLOCK_DISPLAY);
        ring.setBlock(SECONDARY_MATERIAL.createBlockData());
        ring.setTransformation(new Transformation(
                new Vector3f(-0.55f, -0.7f, -0.65f),
                new Quaternionf(),
                new Vector3f(1.1f, 1.5f, 0.2f),
                new Quaternionf()));
        ring.addScoreboardTag(tag);
        ring.addScoreboardTag("MOUNT_HEAD");

        // 8. LARGE HITBOX
        Interaction interaction = (Interaction) world.spawnEntity(center, EntityType.INTERACTION);
        interaction.setInteractionWidth(1.6f);
        interaction.setInteractionHeight(2.0f);
        interaction.addScoreboardTag(tag);
        interaction.addScoreboardTag("MOUNT_HITBOX");
    }

    @EventHandler
    public void onHitboxInteract(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Interaction))
            return;
        Interaction interaction = (Interaction) e.getRightClicked();
        if (!interaction.getScoreboardTags().contains("MOUNT_HITBOX"))
            return;

        // If it's being dismantled, cancel right-click
        if (interaction.hasMetadata("MA_DISMANTLED") || !interaction.isValid()) {
            e.setCancelled(true);
            return;
        }

        // Otherwise, allow standard interaction (onPlayerInteract will handle mounting)
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof Interaction))
            return;
        Interaction interaction = (Interaction) e.getRightClicked();
        if (!interaction.getScoreboardTags().contains("MOUNT_HITBOX"))
            return;

        String tag = interaction.getScoreboardTags().stream()
                .filter(t -> t.startsWith("MOUNT_TURRET_")).findFirst().orElse(null);
        if (tag == null)
            return;

        ArmorStand seat = (ArmorStand) interaction.getWorld()
                .getNearbyEntities(interaction.getLocation(), 1.5, 1.5, 1.5)
                .stream()
                .filter(ent -> ent.getScoreboardTags().contains(tag) && ent.getScoreboardTags().contains("MOUNT_SEAT"))
                .findFirst().orElse(null);

        if (seat != null) {
            seat.addPassenger(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerShoot(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Interaction))
            return;
        Interaction interaction = (Interaction) e.getEntity();
        if (!interaction.getScoreboardTags().contains("MOUNT_HITBOX"))
            return;

        if (!(e.getDamager() instanceof Player))
            return;
        Player player = (Player) e.getDamager();

        // If player is mounting this turret, allow shooting
        if (isMounting(player, interaction)) {
            shoot(player, interaction);
            e.setCancelled(true);
            return;
        }

        // If NOT mounting, trigger dismantle with protection
        handleDismantle(interaction, player);
        e.setCancelled(true);
    }

    private void handleDismantle(Interaction interaction, Player player) {
        // Layer 1: Global Location Lock
        if (!TurretUtils.beginDismantle(interaction.getLocation())) {
            return;
        }

        // Layer 2: Metadata Lock
        if (interaction.hasMetadata("MA_DISMANTLED") || !interaction.isValid()) {
            return;
        }

        for (String tag : interaction.getScoreboardTags()) {
            if (tag.startsWith("MOUNT_TURRET_")) {
                String[] parts = tag.split("_");
                if (parts.length == 5) {
                    try {
                        int x = Integer.parseInt(parts[2]);
                        int y = Integer.parseInt(parts[3]);
                        int z = Integer.parseInt(parts[4]);
                        Location loc = new Location(interaction.getWorld(), x, y, z);

                        // Layer 3: Block State Validation
                        String id = BlockStorage.getLocationInfo(loc, "id");
                        if (id != null && id.equals("MA_MOUNTABLE_TURRET")) {
                            // Atomic DUPLICATION PROTECTION: Set metadata immediately
                            interaction.setMetadata("MA_DISMANTLED",
                                    new FixedMetadataValue(WeaponsAddon.getInstance(), true));

                            // Race condition protection: Clear info BEFORE dropping/removing
                            BlockStorage.clearBlockInfo(loc);
                            loc.getBlock().setType(Material.AIR);

                            interaction.getWorld().playSound(loc, Sound.BLOCK_LANTERN_BREAK, 1f, 1f);
                            interaction.getWorld().dropItemNaturally(loc, MOUNTABLE_TURRET.clone());
                            removeModel(loc);
                            interaction.remove();
                        } else {
                            // If it's a "ghost" model, just remove the entities
                            removeModel(loc);
                            interaction.remove();
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
                break;
            }
        }
    }

    private boolean isMounting(Player player, Interaction interaction) {
        if (player.getVehicle() instanceof ArmorStand
                && player.getVehicle().getScoreboardTags().contains("MOUNT_SEAT")) {
            String turretTag = interaction.getScoreboardTags().stream()
                    .filter(t -> t.startsWith("MOUNT_TURRET_")).findFirst().orElse(null);
            return turretTag != null && player.getVehicle().getScoreboardTags().contains(turretTag);
        }
        return false;
    }

    private void shoot(Player shooter, Interaction interaction) {
        String tag = interaction.getScoreboardTags().stream()
                .filter(t -> t.startsWith("MOUNT_TURRET_")).findFirst().orElse(null);
        if (tag == null)
            return;

        String[] parts = tag.split("_");
        Location loc = new Location(interaction.getWorld(), Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
        int charge = EnergyManager.getCharge(loc);

        if (charge < ENERGY_PER_SHOT) {
            shooter.sendMessage("§cNot enough energy!");
            shooter.playSound(shooter.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
            return;
        }

        Location start = interaction.getLocation().add(0, 1.1, 0);
        Vector direction = shooter.getEyeLocation().getDirection();
        World world = start.getWorld();

        world.playSound(start, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.5f, 0.5f);
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, start.clone().add(direction.clone().multiply(1.5)), 20, 0.2, 0.2,
                0.2, 0.1);
        world.spawnParticle(Particle.FLASH, start.clone().add(direction.clone().multiply(1.0)), 2);

        // Efficient Raytrace: Check for blocks AND entities
        RayTraceResult blockResult = world.rayTraceBlocks(start, direction, RANGE, FluidCollisionMode.NEVER, true);
        double maxDist = (blockResult != null && blockResult.getHitBlock() != null)
                ? blockResult.getHitBlock().getLocation().distance(start)
                : RANGE;

        RayTraceResult entityResult = world.rayTraceEntities(start, direction, maxDist, 0.5,
                ent -> ent instanceof LivingEntity && ent != shooter && !(ent instanceof ArmorStand)
                        && !(ent instanceof Interaction));

        if (entityResult != null && entityResult.getHitEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) entityResult.getHitEntity();
            target.damage(DAMAGE, shooter);
            target.getWorld().spawnParticle(Particle.SONIC_BOOM, target.getLocation().add(0, 1, 0), 1);
        }

        // Particle trail (limited by block collision)
        Location bullet = start.clone();
        for (int i = 0; i < 25; i++) {
            bullet.add(direction.clone().multiply(1.4));
            if (bullet.distanceSquared(start) > maxDist * maxDist)
                break;
            world.spawnParticle(Particle.SOUL, bullet, 1, 0, 0, 0, 0);
        }
    }

    private void updateModelRotation(Location loc) {
        String tag = "MOUNT_TURRET_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        ArmorStand seat = (ArmorStand) loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 1.5, 1.5)
                .stream()
                .filter(e -> e.getScoreboardTags().contains(tag) && e.getScoreboardTags().contains("MOUNT_SEAT"))
                .findFirst().orElse(null);

        if (seat == null || seat.getPassengers().isEmpty())
            return;
        if (!(seat.getPassengers().get(0) instanceof Player))
            return;

        Player p = (Player) seat.getPassengers().get(0);
        float yaw = p.getLocation().getYaw();

        for (Entity entity : loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 1.5, 1.5)) {
            if (entity.getScoreboardTags().contains(tag) &&
                    (entity.getScoreboardTags().contains("MOUNT_HEAD")
                            || entity.getScoreboardTags().contains("MOUNT_BARREL"))) {
                Location eloc = entity.getLocation();
                eloc.setYaw(yaw);
                entity.teleport(eloc);
            }
        }
    }

    private void removeModel(Location loc) {
        String tag = "MOUNT_TURRET_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        for (Entity entity : loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 2.0, 2.0, 2.0)) {
            if (entity.getScoreboardTags().contains(tag)) {
                entity.remove();
            }
        }
    }

    public static void cleanupAllModels() {
        for (World world : getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getScoreboardTags().stream().anyMatch(tag -> tag.startsWith("MOUNT_"))) {
                    entity.remove();
                }
            }
        }
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] recipe = new ItemStack[]{
                MilitaryComponents.FIREARM_BARREL, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.FIREARM_BARREL,
                MilitaryComponents.KINETIC_STABILIZER, MilitaryComponents.ADVANCED_MOVEMENT_CIRCUIT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.KINETIC_STABILIZER,
                MilitaryComponents.MOVEMENT_CIRCUIT, MilitaryComponents.TUNGSTEN_INGOT, new ItemStack(Material.SADDLE), new ItemStack(Material.SADDLE), MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.MOVEMENT_CIRCUIT,
                MilitaryComponents.MOVEMENT_CIRCUIT, MilitaryComponents.TUNGSTEN_INGOT, new ItemStack(Material.SADDLE), new ItemStack(Material.SADDLE), MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.MOVEMENT_CIRCUIT,
                MilitaryComponents.KINETIC_STABILIZER, MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.ADVANCED_MOVEMENT_CIRCUIT, MilitaryComponents.KINETIC_STABILIZER,
                MilitaryComponents.FIREARM_BARREL, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.FIREARM_BARREL
        };

            MountableTurret turret = new MountableTurret(category, MOUNTABLE_TURRET, recipe);
            turret.register(addon);
            if (addon instanceof Plugin) {
                getPluginManager().registerEvents(turret, (Plugin) addon);
            }
        }
    }

package com.Chagui68.weaponsaddon.items.machines;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import com.Chagui68.weaponsaddon.items.CustomRecipeItem;
import com.Chagui68.weaponsaddon.items.MilitaryRecipeTypes;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.machines.energy.EnergyManager;
import com.Chagui68.weaponsaddon.items.vouchers.MilitaryVouchers;
import com.Chagui68.weaponsaddon.utils.TurretUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MeleeTurret extends CustomRecipeItem implements EnergyNetComponent, Listener {

    private static final int ENERGY_CAPACITY = 5000;
    private static final int ENERGY_PER_ATTACK = 150;
    private static final double RANGE = 4.0;
    private static final double DAMAGE = 50.0;
    private static final int ATTACK_COOLDOWN = 2; // Slimefun ticks (approx 2s)
    private static final Random RANDOM = new Random();

    public static final SlimefunItemStack MELEE_TURRET = new SlimefunItemStack(
            "MA_MELEE_TURRET",
            Material.WHITE_BANNER,
            "&e⚔ &6Guardian Melee Turret",
            "",
            "&7Advanced sentinel programmed for",
            "&7lethal close-quarters combat.",
            "",
            "&6Range: &e4.0 Blocks",
            "&6Damage: &e20.0 HP",
            "&6Animations: &e8 Distinct Styles",
            "&6Energy: &e150 J per attack",
            "&6Capacity: &e5000 J",
            "",
            "&eRight-Click to place",
            "&8(Animated Guardian Stand)");

    public MeleeTurret(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
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
                BlockStorage.addBlockInfo(e.getBlock(), "id", "MA_MELEE_TURRET");
                spawnGuardianModel(e.getBlock().getLocation());
            }
        });

        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                removeGuardianModel(e.getBlock().getLocation());
            }

            @Override
            public void onExplode(Block b, List<ItemStack> drops) {
                removeGuardianModel(b.getLocation());
            }
        });

        addItemHandler(new BlockTicker() {
            @Override
            public void tick(Block b, SlimefunItem item, me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config data) {
                MeleeTurret.this.tick(b);
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
        String tag = "MELEE_GUARDIAN_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        Entity modelPart = loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 2.0, 1.5)
                .stream().filter(e -> e.getScoreboardTags().contains(tag)).findFirst().orElse(null);

        if (modelPart == null) {
            spawnGuardianModel(loc);
        }
        // -----------------------------------------------------

        // Handle Cooldown
        String cooldownStr = BlockStorage.getLocationInfo(loc, "cooldown");
        int cooldown = cooldownStr == null ? 0 : Integer.parseInt(cooldownStr);

        if (cooldown > 0) {
            BlockStorage.addBlockInfo(loc, "cooldown", String.valueOf(cooldown - 1));
            return;
        }

        int charge = EnergyManager.getCharge(loc);

        LivingEntity target = findTarget(loc);
        if (target == null)
            return;

        if (charge < ENERGY_PER_ATTACK)
            return;

        // Start Attack
        performAttack(loc, target);
        EnergyManager.removeCharge(loc, ENERGY_PER_ATTACK);
        BlockStorage.addBlockInfo(loc, "cooldown", String.valueOf(ATTACK_COOLDOWN));
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
        // Correct Centering: loc is the block location (corner)
        Location start = loc.clone().add(0.5, 1.1, 0.5);
        Location end = target.getEyeLocation();
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = direction.length();

        RayTraceResult result = loc.getWorld().rayTraceBlocks(start, direction.normalize(), distance,
                FluidCollisionMode.NEVER, true);
        return result == null || result.getHitBlock() == null;
    }

    private void performAttack(Location loc, LivingEntity target) {
        String tag = "MELEE_GUARDIAN_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        ArmorStand stand = null;

        for (Entity e : loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 1.5, 1.5)) {
            if (e instanceof ArmorStand && e.getScoreboardTags().contains(tag)) {
                stand = (ArmorStand) e;
                break;
            }
        }

        if (stand == null)
            return;

        // Face target
        Vector dir = target.getLocation().toVector().subtract(stand.getLocation().toVector());
        Location standLoc = stand.getLocation();
        standLoc.setYaw((float) Math.toDegrees(Math.atan2(-dir.getX(), dir.getZ())));
        stand.teleport(standLoc);

        int animationIndex = RANDOM.nextInt(8);
        final ArmorStand finalStand = stand;
        final LivingEntity finalTarget = target;

        new BukkitRunnable() {
            int frame = 0;

            @Override
            public void run() {
                if (finalStand.isDead() || !finalStand.isValid()) {
                    this.cancel();
                    return;
                }

                switch (animationIndex) {
                    case 0:
                        animatePowerOverstrike(finalStand, frame);
                        break;
                    case 1:
                        animateHorizontalSwipe(finalStand, frame);
                        break;
                    case 2:
                        animateStab(finalStand, frame);
                        break;
                    case 3:
                        animateUppercut(finalStand, frame);
                        break;
                    case 4:
                        animateDualStrike(finalStand, frame);
                        break;
                    case 5:
                        animateSpinStrike(finalStand, frame);
                        break;
                    case 6:
                        animateGroundSlam(finalStand, frame);
                        break;
                    case 7:
                        animateXSlash(finalStand, frame);
                        break;
                }

                if (frame == 5) { // Impact frame
                    if (finalTarget.isValid() && !finalTarget.isDead()
                            && finalTarget.getLocation().distanceSquared(finalStand.getLocation()) <= RANGE * RANGE) {
                        finalTarget.damage(DAMAGE, finalStand);
                        finalTarget.getWorld().playSound(finalTarget.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT,
                                1.0f, 1.0f);
                        finalTarget.getWorld().spawnParticle(Particle.SWEEP_ATTACK,
                                finalTarget.getLocation().add(0, 1, 0), 1);
                    }
                }

                if (frame >= 10) {
                    resetPose(finalStand);
                    this.cancel();
                }
                frame++;
            }
        }.runTaskTimer(WeaponsAddon.getInstance(), 0L, 1L);
    }

    // --- ANIMATIONS ---

    private void animatePowerOverstrike(ArmorStand stand, int frame) {
        if (frame < 5) { // Wind up
            stand.setRightArmPose(new EulerAngle(Math.toRadians(-120), 0, 0));
        } else { // Strike
            stand.setRightArmPose(new EulerAngle(Math.toRadians(30), 0, 0));
        }
    }

    private void animateHorizontalSwipe(ArmorStand stand, int frame) {
        if (frame < 5) { // Wind up side
            stand.setRightArmPose(new EulerAngle(Math.toRadians(-90), Math.toRadians(-90), 0));
        } else { // Horizontal sweep
            stand.setRightArmPose(new EulerAngle(Math.toRadians(-90), Math.toRadians(90), 0));
        }
    }

    private void animateStab(ArmorStand stand, int frame) {
        if (frame < 5) { // Pull back
            stand.setRightArmPose(new EulerAngle(Math.toRadians(-90), 0, Math.toRadians(-30)));
        } else { // Thrust
            stand.setRightArmPose(new EulerAngle(Math.toRadians(-90), 0, Math.toRadians(30)));
        }
    }

    @EventHandler
    public void onHitboxAttack(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Interaction))
            return;
        Interaction interaction = (Interaction) e.getEntity();
        if (!interaction.getScoreboardTags().contains("MELEE_HITBOX"))
            return;

        handleDismantle(interaction, e.getDamager());
        e.setCancelled(true);
    }

    @EventHandler
    public void onHitboxInteract(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Interaction))
            return;
        Interaction interaction = (Interaction) e.getRightClicked();
        if (!interaction.getScoreboardTags().contains("MELEE_HITBOX"))
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
            if (tag.startsWith("MELEE_GUARDIAN_")) {
                String[] parts = tag.split("_");
                if (parts.length == 5) {
                    try {
                        int x = Integer.parseInt(parts[2]);
                        int y = Integer.parseInt(parts[3]);
                        int z = Integer.parseInt(parts[4]);
                        Location loc = new Location(interaction.getWorld(), x, y, z);

                        // Layer 3: Block State Validation
                        String id = BlockStorage.getLocationInfo(loc, "id");
                        if (id != null && id.equals("MA_MELEE_TURRET")) {
                            // Atomic DUPLICATION PROTECTION: Set metadata immediately
                            interaction.setMetadata("MA_DISMANTLED",
                                    new FixedMetadataValue(WeaponsAddon.getInstance(), true));

                            // Race condition protection: Clear info BEFORE dropping/removing
                            BlockStorage.clearBlockInfo(loc);
                            loc.getBlock().setType(Material.AIR);

                            interaction.getWorld().playSound(interaction.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1f,
                                    1f);
                            interaction.getWorld().dropItemNaturally(loc, MELEE_TURRET.clone());
                            removeGuardianModel(loc);
                            interaction.remove();
                        } else {
                            // If it's a "ghost" model, just remove the entities
                            removeGuardianModel(loc);
                            interaction.remove();
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
                break;
            }
        }
    }

    private void animateUppercut(ArmorStand stand, int frame) {
        if (frame < 5) { // Lower
            stand.setRightArmPose(new EulerAngle(Math.toRadians(30), 0, 0));
        } else { // Rise
            stand.setRightArmPose(new EulerAngle(Math.toRadians(-150), 0, 0));
        }
    }

    private void animateDualStrike(ArmorStand stand, int frame) {
        if (frame < 5) {
            stand.setRightArmPose(new EulerAngle(Math.toRadians(-100), 0, 0));
            stand.setLeftArmPose(new EulerAngle(Math.toRadians(30), 0, 0));
        } else {
            stand.setRightArmPose(new EulerAngle(Math.toRadians(30), 0, 0));
            stand.setLeftArmPose(new EulerAngle(Math.toRadians(-100), 0, 0));
        }
    }

    private void animateSpinStrike(ArmorStand stand, int frame) {
        stand.setRightArmPose(new EulerAngle(Math.toRadians(-90), 0, Math.toRadians(90)));
        Location loc = stand.getLocation();
        loc.setYaw(loc.getYaw() + 36); // Spin 36 degrees per frame (360 total in 10 frames)
        stand.teleport(loc);
    }

    private void animateGroundSlam(ArmorStand stand, int frame) {
        if (frame < 5) { // Raise high
            stand.setRightArmPose(new EulerAngle(Math.toRadians(-150), Math.toRadians(-20), 0));
            stand.setLeftArmPose(new EulerAngle(Math.toRadians(-150), Math.toRadians(20), 0));
        } else { // Slam down
            stand.setRightArmPose(new EulerAngle(Math.toRadians(45), Math.toRadians(-20), 0));
            stand.setLeftArmPose(new EulerAngle(Math.toRadians(45), Math.toRadians(20), 0));
        }
    }

    private void animateXSlash(ArmorStand stand, int frame) {
        if (frame < 5) { // Prepare cross
            stand.setRightArmPose(new EulerAngle(Math.toRadians(-120), Math.toRadians(-45), 0));
            stand.setLeftArmPose(new EulerAngle(Math.toRadians(-120), Math.toRadians(45), 0));
        } else { // Cross strike
            stand.setRightArmPose(new EulerAngle(Math.toRadians(20), Math.toRadians(45), 0));
            stand.setLeftArmPose(new EulerAngle(Math.toRadians(20), Math.toRadians(-45), 0));
        }
    }

    private void resetPose(ArmorStand stand) {
        stand.setRightArmPose(new EulerAngle(Math.toRadians(-15), 0, Math.toRadians(10)));
        stand.setLeftArmPose(new EulerAngle(Math.toRadians(-15), 0, Math.toRadians(-10)));
    }

    private void spawnGuardianModel(Location loc) {
        Location center = loc.clone().add(0.5, 0, 0.5);
        World world = loc.getWorld();
        String tag = "MELEE_GUARDIAN_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();

        ArmorStand stand = (ArmorStand) world.spawnEntity(center, EntityType.ARMOR_STAND);
        stand.setBasePlate(false);
        stand.setArms(true);
        stand.setSmall(false);
        stand.setVisible(true);
        stand.setGravity(false);
        stand.setMarker(true);
        stand.setInvulnerable(true);
        stand.addScoreboardTag(tag);
        stand.addScoreboardTag("PVZ_GUARDIAN");

        // Equipment
        stand.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
        stand.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        stand.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        stand.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
        stand.getEquipment().setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD));
        stand.getEquipment().setItemInOffHand(new ItemStack(Material.NETHERITE_SWORD));

        resetPose(stand);

        // Interaction Entity for Hitbox
        Interaction interaction = (Interaction) world.spawnEntity(center, EntityType.INTERACTION);
        interaction.setInteractionWidth(1.2f);
        interaction.setInteractionHeight(2.0f);
        interaction.addScoreboardTag(tag);
        interaction.addScoreboardTag("MELEE_HITBOX");
    }

    public static void cleanupAllModels() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getScoreboardTags().stream()
                        .anyMatch(tag -> tag.contains("GUARDIAN") || tag.contains("PVZ_"))) {
                    entity.remove();
                }
            }
        }
    }

    private void removeGuardianModel(Location loc) {
        String tag = "MELEE_GUARDIAN_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        for (Entity entity : loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 1.5, 2.0, 1.5)) {
            if (entity.getScoreboardTags().contains(tag)) {
                entity.remove();
            }
        }
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] recipe = new ItemStack[] {
                MilitaryComponents.FIREARM_BARREL, MilitaryComponents.ENERGY_MATRIX, new ItemStack(Material.NETHERITE_HELMET), new ItemStack(Material.NETHERITE_HELMET), MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.FIREARM_BARREL,
                MilitaryComponents.TURRET_SHELL, MilitaryComponents.QUANTUM_PROCESSOR, new ItemStack(Material.NETHERITE_CHESTPLATE), new ItemStack(Material.NETHERITE_CHESTPLATE), MilitaryComponents.QUANTUM_PROCESSOR, MilitaryComponents.TURRET_SHELL,
                MilitaryComponents.IMPACT_PISTON, MilitaryComponents.ADVANCED_MOVEMENT_CIRCUIT, new ItemStack(Material.NETHERITE_LEGGINGS), new ItemStack(Material.NETHERITE_LEGGINGS), MilitaryComponents.ADVANCED_MOVEMENT_CIRCUIT, MilitaryComponents.IMPACT_PISTON,
                MilitaryComponents.IMPACT_PISTON, MilitaryComponents.ADVANCED_MOVEMENT_CIRCUIT, new ItemStack(Material.NETHERITE_BOOTS), new ItemStack(Material.NETHERITE_BOOTS), MilitaryComponents.ADVANCED_MOVEMENT_CIRCUIT, MilitaryComponents.IMPACT_PISTON,
                MilitaryComponents.TURRET_SHELL, MilitaryComponents.QUANTUM_PROCESSOR, MilitaryComponents.TUNGSTEN_BLADE , MilitaryComponents.TUNGSTEN_BLADE, MilitaryComponents.QUANTUM_PROCESSOR, MilitaryComponents.TURRET_SHELL,
                MilitaryComponents.FIREARM_BARREL, MilitaryComponents.ENERGY_MATRIX, MilitaryVouchers.VOUCHER_COMMENDATION, MilitaryVouchers.VOUCHER_EMBLEM, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.FIREARM_BARREL
        };
        MeleeTurret turret = new MeleeTurret(category, MELEE_TURRET, recipe);
        turret.register(addon);

        if (addon instanceof Plugin) {
            Bukkit.getPluginManager().registerEvents(turret, (Plugin) addon);
        }
    }
}

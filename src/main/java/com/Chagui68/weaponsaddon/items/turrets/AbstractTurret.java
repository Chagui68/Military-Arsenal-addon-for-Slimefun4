package com.Chagui68.weaponsaddon.items.turrets;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import com.Chagui68.weaponsaddon.items.CustomRecipeItem;
import com.Chagui68.weaponsaddon.items.machines.energy.EnergyManager;
import com.Chagui68.weaponsaddon.utils.TurretUtils;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import com.github.drakescraft_labs.slimefun4.core.attributes.EnergyNetComponent;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockBreakHandler;
import com.github.drakescraft_labs.slimefun4.core.handlers.BlockPlaceHandler;
import com.github.drakescraft_labs.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import com.github.drakescraft_labs.slimefun4.legacy.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

import static org.bukkit.Bukkit.getWorlds;

public abstract class AbstractTurret extends CustomRecipeItem implements EnergyNetComponent, Listener {
    protected AbstractTurret(com.github.drakescraft_labs.slimefun4.api.items.ItemGroup itemGroup,
                             SlimefunItemStack item, ItemStack[] recipe)
    {
        super(itemGroup, item, com.Chagui68.weaponsaddon.items.MilitaryRecipeTypes.getMilitaryMachineFabricator()
                , recipe, RecipeGridSize.GRID_6x6);
    }

    protected abstract String getTurretId();

    protected abstract String getHitboxTag();

    protected abstract String getStructurePrefix();

    protected abstract double getBaseRange();

    protected abstract double getBaseDamage();

    protected abstract int getBaseEnergyCapacity();

    protected abstract int getEnergyPerShot();

    protected abstract SlimefunItemStack getTurretItem();

    protected abstract int getShotCooldown();

    protected abstract void onShootEffects(Location muzzle, LivingEntity target, double range);

    protected abstract void onStructurePlaced(Location loc);

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return getBaseEnergyCapacity();
    }

    public double getCurrentRange(Location loc) {
        int level = TurretUpgradeManager.getCurrentLevel(loc);
        return TurretUpgradeManager.getRangeForLevel(getBaseRange(), level);
    }

    public double getCurrentDamage(Location loc) {
        int level = TurretUpgradeManager.getCurrentLevel(loc);
        return TurretUpgradeManager.getDamageForLevel(getBaseDamage(), level);
    }

    public int getCurrentEnergyCost(Location loc) {
        int level = TurretUpgradeManager.getCurrentLevel(loc);
        return TurretUpgradeManager.getEnergyCostForLevel(getEnergyPerShot(), level);
    }

    public int getCurrentCapacity(Location loc) {
        int level = TurretUpgradeManager.getCurrentLevel(loc);
        return TurretUpgradeManager.getCapacityForLevel(getBaseEnergyCapacity(), level);
    }

    public int getCurrentShotCooldown(Location loc) {
        int level = TurretUpgradeManager.getCurrentLevel(loc);
        return TurretUpgradeManager.getShotCooldownForLevel(getShotCooldown(), level);
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                e.getBlock().setType(Material.LIGHT);
                BlockStorage.addBlockInfo(e.getBlock(), "id", getTurretId());
                TurretUpgradeManager.setLevel(e.getBlock().getLocation(), 1);
                String structure = TurretStructureManager.getStructureName(getStructurePrefix(), 1);
                TurretStructureManager.placeStructure(e.getBlock().getLocation(), structure);
                onStructurePlaced(e.getBlock().getLocation());
            }
        });
        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                dismantle(e.getBlock().getLocation());
            }

            @Override
            public void onExplode(Block b, List<ItemStack> drops) {
                dismantle(b.getLocation());
            }
        });
        addItemHandler(new BlockTicker() {
            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                AbstractTurret.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    protected void tick(Block b) {
        Location loc = b.getLocation();
        int level = TurretUpgradeManager.getCurrentLevel(loc);
        String structure = TurretStructureManager.getStructureName(getStructurePrefix(), level);
        int maxHeight = TurretStructureManager.getMaxHeight(getStructurePrefix());
        int highestPoint = TurretStructureManager.findHighestPoint(loc, maxHeight);
        if (highestPoint == 0) {
            TurretStructureManager.placeStructure(loc, structure);
        }
        int cooldownStr = 0;
        String cd = BlockStorage.getLocationInfo(loc, "cooldown");
        if (cd != null) {
            try {
                cooldownStr = Integer.parseInt(cd);
            } catch (NumberFormatException ignored) {
            }
        }
        if (cooldownStr > 0) {
            BlockStorage.addBlockInfo(loc, "cooldown", String.valueOf(cooldownStr - 1));
            LivingEntity target = findTarget(loc);
            updateModelRotation(loc, target);
            return;
        }
        int charge = EnergyManager.getCharge(loc);
        LivingEntity target = findTarget(loc);
        updateModelRotation(loc, target);
        if (target == null) return;
        int energyCost = getCurrentEnergyCost(loc);
        if (charge < energyCost) return;
        double range = getCurrentRange(loc);
        Location muzzle = loc.clone().add(0.5, highestPoint + 0.5, 0.5);
        onShootEffects(muzzle, target, range);
        EnergyManager.removeCharge(loc, energyCost);
        int shotCooldown = getCurrentShotCooldown(loc);
        if (shotCooldown > 0) {
            BlockStorage.addBlockInfo(loc, "cooldown", String.valueOf(shotCooldown));
        }
    }

    protected LivingEntity findTarget(Location loc) {
        double range = getCurrentRange(loc);
        Location center = loc.clone().add(0.5, 0.5, 0.5);
        Collection<Entity> nearby = loc.getWorld().getNearbyEntities(center, range, range, range);
        LivingEntity closest = null;
        double closestDist = Double.MAX_VALUE;
        for (Entity e : nearby) {
            boolean isHostile = e instanceof Monster || e instanceof Slime || e instanceof Ghast || e instanceof Phantom || e instanceof Shulker || e instanceof Hoglin;
            if (isHostile && !e.isDead() && !e.hasMetadata("no_target") && !e.getScoreboardTags().contains("PVZ_HEAD") && !e.getScoreboardTags().contains("PVZ_GUARDIAN")) {
                double dist = e.getLocation().distanceSquared(center);
                if (dist < closestDist && dist <= range * range) {
                    if (hasLineOfSight(loc, (LivingEntity) e)) {
                        closestDist = dist;
                        closest = (LivingEntity) e;
                    }
                }
            }
        }
        return closest;
    }

    protected boolean hasLineOfSight(Location loc, LivingEntity target) {
        Location start = loc.clone().add(0.5, 1.1, 0.5);
        Location end = target.getEyeLocation();
        Vector direction = end.toVector().subtract(start.toVector());
        double distance = direction.length();
        RayTraceResult result = loc.getWorld().rayTraceBlocks(start, direction.normalize(), distance, FluidCollisionMode.NEVER, true);
        return result == null || result.getHitBlock() == null;
    }

    protected void damageTarget(LivingEntity target, double damage) {
        target.setNoDamageTicks(0);
        target.damage(damage);
        target.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, target.getEyeLocation(), 5, 0.2, 0.2, 0.2, 0.05);
        target.getWorld().playSound(target.getEyeLocation(), Sound.ENTITY_SLIME_ATTACK, 1.0f, 1.2f);
    }

    protected void updateModelRotation(Location loc, LivingEntity target) {
        String tag = getStructurePrefix().toUpperCase().replace("_", "") + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        Location center = loc.clone().add(0.5, 0.6, 0.5);
        float yaw = 0;
        if (target != null) {
            Vector dir = target.getLocation().toVector().subtract(center.toVector());
            yaw = (float) Math.toDegrees(Math.atan2(-dir.getX(), dir.getZ()));
        }
        for (Entity entity : loc.getWorld().getNearbyEntities(center, 1.5, 1.5, 1.5)) {
            if (entity.getScoreboardTags().contains(tag) && (entity.getScoreboardTags().contains("TURRET_HEAD") || entity.getScoreboardTags().contains("TURRET_MOUTH") || entity.getScoreboardTags().contains("TURRET_SENSOR"))) {
                Location eloc = entity.getLocation();
                eloc.setYaw(yaw);
                entity.teleport(eloc);
            }
        }
    }

    @EventHandler
    public void onHitboxAttack(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Interaction)) return;
        Interaction interaction = (Interaction) e.getEntity();
        if (!interaction.getScoreboardTags().contains(getHitboxTag())) return;
        handleDismantle(interaction, e.getDamager());
        e.setCancelled(true);
    }

    @EventHandler
    public void onHitboxInteract(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Interaction)) return;
        Interaction interaction = (Interaction) e.getRightClicked();
        if (!interaction.getScoreboardTags().contains(getHitboxTag())) return;
        Player player = e.getPlayer();
        if (player.isSneaking()) {
            for (String tag : interaction.getScoreboardTags()) {
                if (tag.startsWith(getTagPrefix())) {
                    String[] parts = tag.split("_");
                    if (parts.length >= 4) {
                        try {
                            int x = Integer.parseInt(parts[parts.length - 3]);
                            int y = Integer.parseInt(parts[parts.length - 2]);
                            int z = Integer.parseInt(parts[parts.length - 1]);
                            Location loc = new Location(interaction.getWorld(), x, y, z);
                            String id = BlockStorage.getLocationInfo(loc, "id");
                            if (id != null && id.equals(getTurretId())) {
                                TurretUpgradeGUI.open(player, getTurretId(), getTurretItem().getDisplayName(), loc, getBaseRange(), getBaseDamage(), getBaseEnergyCapacity(), getEnergyPerShot());
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    break;
                }
            }
            e.setCancelled(true);
            return;
        }
        handleDismantle(interaction, e.getPlayer());
        e.setCancelled(true);
    }

    protected void handleDismantle(Interaction interaction, Entity damager) {
        if (!(damager instanceof Player)) return;
        if (!TurretUtils.beginDismantle(interaction.getLocation())) return;
        if (interaction.hasMetadata("MA_DISMANTLED") || !interaction.isValid()) return;
        for (String tag : interaction.getScoreboardTags()) {
            if (tag.startsWith(getTagPrefix())) {
                String[] parts = tag.split("_");
                if (parts.length >= 4) {
                    try {
                        int x = Integer.parseInt(parts[parts.length - 3]);
                        int y = Integer.parseInt(parts[parts.length - 2]);
                        int z = Integer.parseInt(parts[parts.length - 1]);
                        Location loc = new Location(interaction.getWorld(), x, y, z);
                        String id = BlockStorage.getLocationInfo(loc, "id");
                        if (id != null && id.equals(getTurretId())) {
                            interaction.setMetadata("MA_DISMANTLED", new FixedMetadataValue(WeaponsAddon.getInstance(), true));
                            dismantle(loc);
                            interaction.getWorld().playSound(interaction.getLocation(), Sound.BLOCK_LANTERN_BREAK, 1f, 1f);
                            interaction.getWorld().dropItemNaturally(loc, getTurretItem().clone());
                            interaction.remove();
                        } else {
                            dismantle(loc);
                            interaction.remove();
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
                break;
            }
        }
    }

    protected void dismantle(Location loc) {
        int level = TurretUpgradeManager.getCurrentLevel(loc);
        int maxHeight = TurretStructureManager.getMaxHeight(getStructurePrefix());
        TurretStructureManager.removeStructure(loc, maxHeight);
        BlockStorage.clearBlockInfo(loc);
        loc.getBlock().setType(Material.AIR);
        String tag = getTagPrefix() + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        for (Entity entity : loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), 2.0, 2.0, 2.0)) {
            if (entity.getScoreboardTags().contains(tag)) {
                entity.remove();
            }
        }
    }

    protected String getTagPrefix() {
        return getStructurePrefix().toUpperCase().replace("_", "") + "_";
    }

    public static void cleanupAllModels() {
        for (org.bukkit.World world : getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getScoreboardTags().stream().anyMatch(tag -> tag.startsWith("TURRET_") || tag.startsWith("TURRETHITBOX"))) {
                    entity.remove();
                }
            }
        }
    }
}


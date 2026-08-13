package com.Chagui68.weaponsaddon.items.turrets;

import com.Chagui68.weaponsaddon.items.MachineGun;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.github.drakescraft_labs.slimefun4.api.SlimefunAddon;
import com.github.drakescraft_labs.slimefun4.api.items.ItemGroup;
import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import static org.bukkit.Bukkit.getPluginManager;

public class MachineGunTurret extends AbstractTurret {
    public static final SlimefunItemStack MACHINE_GUN_TURRET = new SlimefunItemStack(
            "MA_MACHINE_GUN_TURRET",
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
            "&bLevels: &f4 upgradeable stages",
            "&bThe tower grows taller with each level",
            "&bEach level: &f+2 range, +15% damage, +25% capacity, -10% energy cost",
            "",
            "&eRight-Click to place",
            "&eSneak + Right-Click to upgrade",
            "&8(NBT Structure Model)"
    );

    public MachineGunTurret(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
        super(itemGroup, item, recipe);
    }

    @Override
    protected String getTurretId() {
        return "MA_MACHINE_GUN_TURRET";
    }

    @Override
    protected String getHitboxTag() {
        return "MG_HITBOX";
    }

    @Override
    protected String getStructurePrefix() {
        return "rapid_tower";
    }

    @Override
    protected double getBaseRange() {
        return 10.0;
    }

    @Override
    protected double getBaseDamage() {
        return 4.0;
    }

    @Override
    protected int getBaseEnergyCapacity() {
        return 3000;
    }

    @Override
    protected int getEnergyPerShot() {
        return 20;
    }

    @Override
    protected SlimefunItemStack getTurretItem() {
        return MACHINE_GUN_TURRET;
    }

    @Override
    protected int getShotCooldown() {
        return 0;
    }

    @Override
    protected void onStructurePlaced(Location loc) {
    }

    @Override
    protected void onShootEffects(Location baseLoc, Location muzzle, LivingEntity target, double range) {
        Location targetLoc = target.getEyeLocation();
        Vector direction = targetLoc.toVector().subtract(muzzle.toVector()).normalize();

        muzzle.getWorld().playSound(muzzle, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.5f);
        muzzle.getWorld().spawnParticle(Particle.FLASH, muzzle, 1, 0.05, 0.05, 0.05, 0.05);

        Location bullet = muzzle.clone();
        int particleSteps = Math.max(1, (int) Math.ceil(range));
        for (int i = 0; i < particleSteps; i++) {
            bullet.add(direction.clone());
            if (bullet.distanceSquared(muzzle) > range * range) {
                break;
            }
            muzzle.getWorld().spawnParticle(Particle.CRIT, bullet, 1, 0, 0, 0, 0);
        }

        damageTarget(target, getCurrentDamage(baseLoc));
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] recipe = new ItemStack[]{
                MilitaryComponents.FIREARM_BARREL, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.FIREARM_BARREL,
                MilitaryComponents.KINETIC_STABILIZER, MilitaryComponents.ADVANCED_MOVEMENT_CIRCUIT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.KINETIC_STABILIZER,
                MilitaryComponents.MOVEMENT_CIRCUIT, MilitaryComponents.TUNGSTEN_INGOT, MachineGun.MACHINE_GUN, MachineGun.MACHINE_GUN, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.MOVEMENT_CIRCUIT,
                MilitaryComponents.MOVEMENT_CIRCUIT, MilitaryComponents.TUNGSTEN_INGOT, MachineGun.MACHINE_GUN, MachineGun.MACHINE_GUN, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.MOVEMENT_CIRCUIT,
                MilitaryComponents.KINETIC_STABILIZER, MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.ADVANCED_MOVEMENT_CIRCUIT, MilitaryComponents.KINETIC_STABILIZER,
                MilitaryComponents.FIREARM_BARREL, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.FIREARM_BARREL
        };

        MachineGunTurret turret = new MachineGunTurret(category, MACHINE_GUN_TURRET, recipe);
        turret.register(addon);
        if (addon instanceof Plugin plugin) {
            getPluginManager().registerEvents(turret, plugin);
        }
    }
}

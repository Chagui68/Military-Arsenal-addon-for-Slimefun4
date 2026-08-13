package com.Chagui68.weaponsaddon.items.turrets;

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

public class AttackTurret extends AbstractTurret {
    public static final SlimefunItemStack ATTACK_TURRET = new SlimefunItemStack(
            "MA_ATTACK_TURRET",
            Material.NETHERITE_BLOCK,
            "&1🛡 &9Industrial Attack Turret",
            "",
            "&7Automated robotic defense system.",
            "&7Advanced AI with targeting sensors.",
            "",
            "&6Range: &e15 Blocks",
            "&6Damage: &e30.0 HP",
            "&6Energy: &e100 J per shot",
            "&6Capacity: &e5000 J",
            "",
            "&bLevels: &f4 upgradeable stages",
            "&bThe tower grows taller with each level",
            "&bEach level: &f+2 range, +15% damage, +25% capacity, -10% energy cost",
            "",
            "&eRight-Click to place",
            "&eSneak + Right-Click to upgrade",
            "&8(NBT Structure Model)"
    );

    public AttackTurret(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe) {
        super(itemGroup, item, recipe);
    }

    @Override
    protected String getTurretId() {
        return "MA_ATTACK_TURRET";
    }

    @Override
    protected String getHitboxTag() {
        return "ATTACK_HITBOX";
    }

    @Override
    protected String getStructurePrefix() {
        return "attack_tower";
    }

    @Override
    protected double getBaseRange() {
        return 15.0;
    }

    @Override
    protected double getBaseDamage() {
        return 30.0;
    }

    @Override
    protected int getBaseEnergyCapacity() {
        return 5000;
    }

    @Override
    protected int getEnergyPerShot() {
        return 100;
    }

    @Override
    protected SlimefunItemStack getTurretItem() {
        return ATTACK_TURRET;
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

        muzzle.getWorld().playSound(muzzle, Sound.ENTITY_EGG_THROW, 1.5f, 0.8f);
        muzzle.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, muzzle, 5, 0.1, 0.1, 0.1, 0.05);

        Location bullet = muzzle.clone();
        int particleSteps = Math.max(1, (int) Math.ceil(range * 2.0));
        for (int i = 0; i < particleSteps; i++) {
            bullet.add(direction.clone().multiply(0.5));
            if (bullet.distanceSquared(muzzle) > range * range) {
                break;
            }
            muzzle.getWorld().spawnParticle(Particle.COMPOSTER, bullet, 1, 0, 0, 0, 0);
        }

        damageTarget(target, getCurrentDamage(baseLoc));
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] recipe = new ItemStack[]{
                MilitaryComponents.FIREARM_BARREL, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.FIREARM_BARREL,
                MilitaryComponents.KINETIC_STABILIZER, MilitaryComponents.ADVANCED_MOVEMENT_CIRCUIT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.KINETIC_STABILIZER,
                MilitaryComponents.MOVEMENT_CIRCUIT, MilitaryComponents.TUNGSTEN_INGOT, new ItemStack(Material.BOW), new ItemStack(Material.BOW), MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.MOVEMENT_CIRCUIT,
                MilitaryComponents.MOVEMENT_CIRCUIT, MilitaryComponents.TUNGSTEN_INGOT, new ItemStack(Material.BOW), new ItemStack(Material.BOW), MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.MOVEMENT_CIRCUIT,
                MilitaryComponents.KINETIC_STABILIZER, MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.TUNGSTEN_INGOT, MilitaryComponents.ADVANCED_MOVEMENT_CIRCUIT, MilitaryComponents.KINETIC_STABILIZER,
                MilitaryComponents.FIREARM_BARREL, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.FIREARM_BARREL
        };

        AttackTurret turret = new AttackTurret(category, ATTACK_TURRET, recipe);
        turret.register(addon);
        if (addon instanceof Plugin plugin) {
            getPluginManager().registerEvents(turret, plugin);
        }
    }
}

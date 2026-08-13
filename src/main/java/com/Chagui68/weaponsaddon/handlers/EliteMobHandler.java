package com.Chagui68.weaponsaddon.handlers;

import com.Chagui68.weaponsaddon.utils.VersionSafe;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static org.bukkit.Bukkit.getWorlds;

public class EliteMobHandler implements org.bukkit.event.Listener {

    private final Plugin plugin;

    public EliteMobHandler(Plugin plugin) {
        this.plugin = plugin;
        startEliteTask();
    }

    private void startEliteTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                scanElites();
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    private void scanElites() {
        for (World world : getWorlds()) {
            for (Zombie zombie : world.getEntitiesByClass(Zombie.class)) {
                if (zombie.isDead())
                    continue;
                if (zombie.getScoreboardTags().contains("MA_ShockTrooper")) {
                    handleShockTrooper(zombie);
                } else if (zombie.getScoreboardTags().contains("MA_FlameRaider")) {
                    handleFlameRaider(zombie);
                } else if (zombie.getScoreboardTags().contains("MA_SiegeBreaker")) {
                    handleSiegeBreaker(zombie);
                } else if (zombie.getScoreboardTags().contains("MA_MoltenColossus")) {
                    handleMoltenColossus(zombie);
                }
            }
            for (Witch witch : world.getEntitiesByClass(Witch.class)) {
                if (witch.isDead())
                    continue;
                if (witch.getScoreboardTags().contains("MA_VenomReaper")) {
                    handleVenomReaper(witch);
                }
            }
            for (Stray stray : world.getEntitiesByClass(Stray.class)) {
                if (stray.isDead())
                    continue;
                if (stray.getScoreboardTags().contains("MA_FrostWarden")) {
                    handleFrostWarden(stray);
                }
            }
            for (Skeleton skeleton : world.getEntitiesByClass(Skeleton.class)) {
                if (skeleton.isDead())
                    continue;
                if (skeleton.getScoreboardTags().contains("MA_PhantomScout")) {
                    handlePhantomScout(skeleton);
                }
            }
            for (Husk husk : world.getEntitiesByClass(Husk.class)) {
                if (husk.isDead())
                    continue;
                if (husk.getScoreboardTags().contains("MA_ThunderTitan")) {
                    handleThunderTitan(husk);
                }
            }
            for (Enderman enderman : world.getEntitiesByClass(Enderman.class)) {
                if (enderman.isDead())
                    continue;
                if (enderman.getScoreboardTags().contains("MA_RiftWalker")) {
                    handleRiftWalker(enderman);
                }
            }
            for (CaveSpider spider : world.getEntitiesByClass(CaveSpider.class)) {
                if (spider.isDead())
                    continue;
                if (spider.getScoreboardTags().contains("MA_DreadWeaver")) {
                    handleDreadWeaver(spider);
                }
            }
        }
    }

    private Player nearestTarget(LivingEntity elite, double radius) {
        Player nearest = null;
        double best = Double.MAX_VALUE;
        for (Player p : elite.getWorld().getPlayers()) {
            if (p.isDead() || p.getGameMode() == org.bukkit.GameMode.CREATIVE
                    || p.getGameMode() == org.bukkit.GameMode.SPECTATOR)
                continue;
            double dist = p.getLocation().distance(elite.getLocation());
            if (dist < best && dist < radius) {
                best = dist;
                nearest = p;
            }
        }
        return nearest;
    }

    private boolean onCooldown(LivingEntity elite, String key, long ms) {
        if (elite.hasMetadata(key)) {
            return System.currentTimeMillis() < elite.getMetadata(key).get(0).asLong();
        }
        return false;
    }

    private void startCooldown(LivingEntity elite, String key, long ms) {
        elite.setMetadata(key, new FixedMetadataValue(plugin, System.currentTimeMillis() + ms));
    }

    private void handleShockTrooper(Zombie trooper) {
        if (onCooldown(trooper, "shock_dash_cd", 5000))
            return;
        Player target = nearestTarget(trooper, 12);
        if (target == null)
            return;
        Vector dir = target.getLocation().toVector().subtract(trooper.getLocation().toVector()).normalize();
        trooper.setVelocity(dir.multiply(1.6).setY(0.3));
        trooper.getWorld().spawnParticle(VersionSafe.getParticle("FLASH"), trooper.getLocation().add(0, 1, 0), 5, 0.2,
                0.2, 0.2, 0.1);
        trooper.getWorld().playSound(trooper.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.5f);
        startCooldown(trooper, "shock_dash_cd", 5000);
    }

    private void handleFlameRaider(Zombie raider) {
        Player target = nearestTarget(raider, 8);
        if (target == null)
            return;
        target.setFireTicks(Math.max(target.getFireTicks(), 60));
        raider.getWorld().spawnParticle(VersionSafe.getParticle("FLAME"), raider.getLocation().add(0, 1, 0), 10, 0.4,
                0.4, 0.4, 0.02);
        if (onCooldown(raider, "flame_trail_cd", 4000))
            return;
        for (Entity e : raider.getNearbyEntities(3, 3, 3)) {
            if (e instanceof LivingEntity && !(e instanceof Player)) {
                ((LivingEntity) e).setFireTicks(80);
            }
        }
        startCooldown(raider, "flame_trail_cd", 4000);
    }

    private void handleVenomReaper(Witch reaper) {
        Player target = nearestTarget(reaper, 6);
        if (target == null)
            return;
        if (onCooldown(reaper, "venom_aura_cd", 3000))
            return;
        for (Entity e : reaper.getNearbyEntities(5, 5, 5)) {
            if (e instanceof LivingEntity) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(
                        VersionSafe.getPotionEffectType("POISON"), 100, 1));
            }
        }
        reaper.getWorld().spawnParticle(VersionSafe.getParticle("WITCH"), reaper.getLocation().add(0, 1, 0), 25, 0.5,
                0.5, 0.5, 0.05);
        startCooldown(reaper, "venom_aura_cd", 3000);
    }

    private void handleSiegeBreaker(Zombie breaker) {
        Player target = nearestTarget(breaker, 5);
        if (target == null)
            return;
        if (onCooldown(breaker, "siege_wave_cd", 6000))
            return;
        for (Entity e : breaker.getNearbyEntities(6, 3, 6)) {
            if (e instanceof LivingEntity) {
                Vector push = e.getLocation().toVector().subtract(breaker.getLocation().toVector()).normalize();
                ((LivingEntity) e).setVelocity(push.multiply(1.4).setY(0.5));
            }
        }
        breaker.getWorld().playSound(breaker.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f);
        breaker.getWorld().spawnParticle(VersionSafe.getParticle("ANGRY_VILLAGER"), breaker.getLocation(), 20, 0.5,
                0.5, 0.5, 0.1);
        startCooldown(breaker, "siege_wave_cd", 6000);
    }

    private void handleFrostWarden(Stray warden) {
        if (onCooldown(warden, "frost_nova_cd", 7000))
            return;
        Player target = nearestTarget(warden, 10);
        if (target == null)
            return;
        for (Entity e : warden.getNearbyEntities(7, 4, 7)) {
            if (e instanceof LivingEntity) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(
                        VersionSafe.getPotionEffectType("SLOWNESS"), 140, 2));
            }
        }
        warden.getWorld().spawnParticle(VersionSafe.getParticle("SNOWFLAKE"), warden.getLocation().add(0, 1, 0), 40,
                1.5, 0.5, 1.5, 0.1);
        warden.getWorld().playSound(warden.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 0.5f);
        startCooldown(warden, "frost_nova_cd", 7000);
    }

    private void handlePhantomScout(Skeleton scout) {
        if (onCooldown(scout, "cloak_cd", 10000))
            return;
        scout.addPotionEffect(new PotionEffect(
                VersionSafe.getPotionEffectType("INVISIBILITY"), 120, 0));
        scout.addPotionEffect(new PotionEffect(
                VersionSafe.getPotionEffectType("SPEED"), 120, 1));
        Player target = nearestTarget(scout, 20);
        if (target != null && scout.getTarget() == null) {
            scout.setTarget(target);
        }
        scout.getWorld().spawnParticle(VersionSafe.getParticle("LARGE_SMOKE"), scout.getLocation().add(0, 1, 0), 10,
                0.3, 0.3, 0.3, 0.02);
        startCooldown(scout, "cloak_cd", 10000);
    }

    private void handleThunderTitan(Husk titan) {
        if (onCooldown(titan, "thunder_cd", 8000))
            return;
        Player target = nearestTarget(titan, 14);
        if (target == null)
            return;
        titan.getWorld().strikeLightningEffect(target.getLocation());
        titan.getWorld().playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
        titan.getWorld().spawnParticle(VersionSafe.getParticle("FLASH"), target.getLocation().add(0, 1, 0), 8, 0.3,
                0.3, 0.3, 0.05);
        startCooldown(titan, "thunder_cd", 8000);
    }

    private void handleRiftWalker(Enderman walker) {
        if (onCooldown(walker, "rift_cd", 7000))
            return;
        Player target = nearestTarget(walker, 18);
        if (target == null)
            return;
        Location behind = target.getLocation().clone().add(target.getLocation().getDirection().multiply(-3));
        walker.teleport(behind);
        walker.getWorld().spawnParticle(VersionSafe.getParticle("PORTAL"), walker.getLocation().add(0, 1, 0), 30, 0.5,
                0.5, 0.5, 0.1);
        walker.getWorld().playSound(walker.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
        startCooldown(walker, "rift_cd", 7000);
    }

    private void handleMoltenColossus(Zombie colossus) {
        Player target = nearestTarget(colossus, 4);
        if (target == null)
            return;
        target.setFireTicks(Math.max(target.getFireTicks(), 100));
        colossus.getWorld().spawnParticle(VersionSafe.getParticle("LAVA"), colossus.getLocation().add(0, 1, 0), 8, 0.3,
                0.3, 0.3, 0.05);
    }

    private void handleDreadWeaver(CaveSpider weaver) {
        if (onCooldown(weaver, "web_cd", 6000))
            return;
        Player target = nearestTarget(weaver, 10);
        if (target == null)
            return;
        Location webLoc = target.getLocation();
        if (webLoc.getBlock().getType() == Material.AIR || webLoc.getBlock().getType() == Material.CAVE_AIR) {
            webLoc.getBlock().setType(Material.COBWEB);
        }
        weaver.getWorld().playSound(weaver.getLocation(), Sound.ENTITY_SPIDER_STEP, 1.0f, 1.0f);
        weaver.getWorld().spawnParticle(VersionSafe.getParticle("CRIT"), target.getLocation().add(0, 1, 0), 15, 0.3,
                0.3, 0.3, 0.05);
        startCooldown(weaver, "web_cd", 6000);
    }
}

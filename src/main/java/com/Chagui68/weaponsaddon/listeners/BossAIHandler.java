package com.Chagui68.weaponsaddon.listeners;

import com.Chagui68.weaponsaddon.handlers.MilitaryMobHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BossAIHandler implements Listener {

    private final Plugin plugin;

    public BossAIHandler(Plugin plugin) {
        this.plugin = plugin;
        startAITask();
    }

    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Snowball) {
            Snowball bullet = (Snowball) e.getDamager();

            if (bullet.getShooter() instanceof Skeleton) {
                Skeleton shooter = (Skeleton) bullet.getShooter();

                if (shooter.getScoreboardTags().contains("HeavyGunner")) {
                    // Aplicar daño real (4.0 = 2 corazones por bala) (12.0 = 6 corazones)
                    e.setDamage(24.0);
                }
            }
        }

    }

    private void startAITask() {
        // Tarea repetitiva cada 100 ticks (5 segundos)
        new BukkitRunnable() {
            @Override
            public void run() {
                scanAndShoot();
            }
        }.runTaskTimer(plugin, 20L, 100L);
    }

    private void scanAndShoot() {
        for (World world : Bukkit.getWorlds()) {
            // Escanear SKELETONS (Heavy Gunner)
            for (Skeleton skeleton : world.getEntitiesByClass(Skeleton.class)) {
                if (skeleton.getScoreboardTags().contains("HeavyGunner") && !skeleton.isDead()) {
                    handleShooting(skeleton);
                }
            }

            // Escanear ZOMBIES (Elite Killer)
            for (Zombie zombie : world.getEntitiesByClass(Zombie.class)) {
                if (zombie.getScoreboardTags().contains("EliteKiller") && !zombie.isDead()) {
                    handleEliteKillerAI(zombie);
                }
            }
        }
    }

    private void handleEliteKillerAI(Zombie killer) {
        LivingEntity target = killer.getTarget();
        if (target == null || target.isDead() || target.getWorld() != killer.getWorld())
            return;

        double distance = killer.getLocation().distance(target.getLocation());

        // 1. Verificar RANGO (Radio de 20 bloques)
        if (distance > 20)
            return;

        // 2. Verificar COOLDOWN (30 segundos)
        if (killer.hasMetadata("killer_cooldown")) {
            long cooldownTime = killer.getMetadata("killer_cooldown").get(0).asLong();
            if (System.currentTimeMillis() < cooldownTime) {
                return; // Todavía en tiempo de espera
            }
        }

        // Habilidad: Aparecer 3 "Pushers" detrás del jugador
        for (int i = 0; i < 3; i++) {
            spawnPusherBehind(target);
        }

        // Establecer nuevo cooldown (30 segundos)
        killer.setMetadata("killer_cooldown", new FixedMetadataValue(plugin, System.currentTimeMillis() + 30000));
    }

    private void spawnPusherBehind(LivingEntity target) {
        Location targetLoc = target.getLocation();
        Vector direction = targetLoc.getDirection();

        // Calcular ubicación 2 bloques atrás
        Location spawnLoc = targetLoc.clone().add(direction.multiply(-2));

        Zombie pusher = (Zombie) target.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);

        MilitaryMobHandler.equipPusher(pusher);

        pusher.setTarget(target);

        target.getWorld().playSound(spawnLoc, Sound.ENTITY_ZOMBIE_AMBIENT, 1.0f, 2.0f);
    }

    private void handleShooting(Skeleton boss) {
        LivingEntity target = boss.getTarget();

        if (target == null || target.isDead() || target.getWorld() != boss.getWorld())
            return;

        double distance = boss.getLocation().distance(target.getLocation());

        if (distance > 25 || distance < 2)
            return;

        // Congelar al jefe mientras dispara (Slowness muy alto por 2 segundos)
        boss.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 40, 255));

        fireBurst(boss, target);

        boss.getWorld().playSound(boss.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1.0f, 2.0f);
    }

    private void fireBurst(LivingEntity shooter, LivingEntity target) {
        new BukkitRunnable() {
            int shots = 0;

            @Override
            public void run() {
                if (shooter.isDead() || target.isDead() || shots >= 5) { // 5 Disparos
                    this.cancel();
                    return;
                }

                fireBullet(shooter, target);
                shots++;
            }
        }.runTaskTimer(plugin, 10L, 4L); // Retraso inicial de 0.5s para que se detenga, luego cada 4 ticks
    }

    private void fireBullet(LivingEntity shooter, LivingEntity target) {
        Location origin = shooter.getEyeLocation();
        Location targetLoc = target.getEyeLocation(); // Apuntar a la cabeza/pecho

        Vector dir = targetLoc.clone().subtract(origin).toVector().normalize();
        dir.add(new Vector(
                (Math.random() - 0.5) * 0.15,
                (Math.random() - 0.5) * 0.15,
                (Math.random() - 0.5) * 0.15));

        // Lanzar proyectil (Snowball hace daño al impactar en Vanilla o Spigot custom)
        // Nota: Para que haga daño real sin código extra, podemos usar Arrow o usar un
        // listener de daño.
        // Usaremos Arrow (Flecha) pero invisible o muy rápida para simular bala, o
        // Snowball y manejamos el daño.
        // Por simplicidad y efectividad visual de "bala": Snowball.

        Snowball bullet = shooter.launchProjectile(Snowball.class, dir.multiply(3.5));
        bullet.setShooter(shooter);

        // Sonido de disparo
        shooter.getWorld().playSound(shooter.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.0f, 0.5f);
    }
}

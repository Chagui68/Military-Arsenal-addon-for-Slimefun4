package com.Chagui68.weaponsaddon.listeners;

import com.Chagui68.weaponsaddon.handlers.MilitaryMobHandler;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;

public class BossAIHandler implements Listener {

    private final Plugin plugin;

    // --- CONFIGURABLE PARAMETERS ---
    private static final double AIRSTRIKE_DAMAGE = 150.0;
    private static final int FLASHBANG_DURATION = 100;
    private static final double COVER_HP_THRESHOLD = 0.10;
    private static final double ARENA_RADIUS = 25.0;
    private static final long IDLE_DESPAWN_TIME = 60000;
    private static final int ARENA_HEIGHT = 19;
    private static final double ARENA_MARGIN = 0.5;

    /*
     La altura y tamaño se define aquí en las lineas 33 para la altura y 31 para el radio
     (private static final int Arena_HEIGHT)
     (private static final double ARENA_RADIUS)
     En las lineas donde se usan estas variables se explica mas a fondo como funciona la
     creacion de la arena y su uso de los bloques
     */

    private static final Set<Location> arenaBlocks = new HashSet<>();
    private static final Map<Location, Material> originalBlocks = new HashMap<>();
    private static BossBar activeBossBar = null;

   /*
   El arena center se usa para calcular el tamaño de la arena y colocar una particula 
    */

    private static Location arenaCenter = null;
    private static int currentBossPhase = 1;

    public BossAIHandler(Plugin plugin) {
        this.plugin = plugin;
        startAITask();
    }

    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent e) {
        // --- BULLET DAMAGE (Progressive by phase) ---
        if (e.getDamager() instanceof Snowball) {
            Snowball bullet = (Snowball) e.getDamager();
            if (bullet.getShooter() instanceof Skeleton) {
                Skeleton shooter = (Skeleton) bullet.getShooter();
                if (shooter.getScoreboardTags().contains("HeavyGunner")) {
                    // Base damage + phase bonus
                    double baseDamage = 80.0;
                    double phaseDamage = baseDamage + (currentBossPhase * 3.0);
                    e.setDamage(phaseDamage); // Phase 1: 15, Phase 7: 33
                }
            }
        }

        // --- TRACK DAMAGE RECEIVED BY BOSS + DAMAGE CAP ---
        if (e.getEntity() instanceof Skeleton && e.getEntity().getScoreboardTags().contains("HeavyGunner")) {
            e.getEntity().setMetadata("last_damage_taken", new FixedMetadataValue(plugin, System.currentTimeMillis()));

            // CAP DAMAGE TO 1000 (500 hearts) to prevent one-shots
            if (e.getDamage() > 1000.0) {
                e.setDamage(1000.0);
            }

        }
    }

    private void startAITask() {
        // Tarea repetitiva cada 20 ticks (1 segundo) para mayor precisión en la arena
        new BukkitRunnable() {
            @Override
            public void run() {
                scanAndShoot();
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    private void scanAndShoot() {
        for (World world : Bukkit.getWorlds()) {
            // Escanear SKELETONS (Heavy Gunner)
            for (Skeleton skeleton : world.getEntitiesByClass(Skeleton.class)) {
                if (skeleton.getScoreboardTags().contains("HeavyGunner") && !skeleton.isDead()) {
                    // 1. Verificar Inactividad
                    long lastDamage = skeleton.hasMetadata("last_damage_taken")
                            ? skeleton.getMetadata("last_damage_taken").get(0).asLong()
                            : System.currentTimeMillis();

                    if (System.currentTimeMillis() - lastDamage > IDLE_DESPAWN_TIME) {
                        // Destroy arena BEFORE removing the boss
                        destroyArena();

                        // Cleanup boss bar
                        if (activeBossBar != null) {
                            activeBossBar.removeAll();
                            activeBossBar = null;
                        }
                        currentBossPhase = 1;

                        // Restore time
                        skeleton.getWorld().setTime(1000);

                        // Remove the boss
                        skeleton.remove();

                        Bukkit.broadcastMessage(
                                org.bukkit.ChatColor.GRAY + "The Heavy Gunner has retreated due to lack of combat.");
                        continue;
                    }

                    // Auto-setup Boss Bar if not exists
                    if (activeBossBar == null) {
                        setupBossBar(skeleton, plugin);
                    }

                    handleShooting(skeleton);
                    handleArena(skeleton);
                }
            }

            // Scan ZOMBIES (Elite Killer and Warrior)
            for (Zombie zombie : world.getEntitiesByClass(Zombie.class)) {
                if (zombie.isDead())
                    continue;

                if (zombie.getScoreboardTags().contains("EliteKiller")) {
                    handleEliteKillerAI(zombie);
                } else if (zombie.getScoreboardTags().contains("Warrior")) {
                    handleWarriorAI(zombie);
                }
            }

            // Scan WITCHES (Battle Witch)
            for (Witch witch : world.getEntitiesByClass(Witch.class)) {
                if (witch.isDead())
                    continue;

                if (witch.getScoreboardTags().contains("BattleWitch")) {
                    handleBattleWitchAI(witch);
                }
            }
        }
    }

    private void handleWarriorAI(Zombie warrior) {
        LivingEntity target = warrior.getTarget();
        if (target == null || target.isDead() || target.getWorld() != warrior.getWorld())
            return;

        double distance = warrior.getLocation().distance(target.getLocation());
        if (distance > 15 || distance < 2)
            return;

        // Cooldown check (30s)
        if (warrior.hasMetadata("warrior_dash_cd")) {
            long cd = warrior.getMetadata("warrior_dash_cd").get(0).asLong();
            if (System.currentTimeMillis() < cd)
                return;
        }

        // --- PHASE 1: CHARGING (1s) ---
        // Freeze warrior (Slowness 255)
        warrior.addPotionEffect(
                new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 25, 255));
        warrior.getWorld().playSound(warrior.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.0f, 0.5f);
        warrior.getWorld().spawnParticle(org.bukkit.Particle.CRIT, warrior.getLocation().add(0, 1, 0), 10, 0.3, 0.3,
                0.3,
                0.1);

        // --- PHASE 2: DASH (after 20 ticks) ---
        new BukkitRunnable() {
            @Override
            public void run() {
                if (warrior.isDead() || target.isDead())
                    return;

                Vector dashDir = target.getLocation().toVector().subtract(warrior.getLocation().toVector()).normalize();
                warrior.setVelocity(dashDir.multiply(1.5).setY(0.2));

                warrior.getWorld().playSound(warrior.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, 0.8f);
                warrior.getWorld().spawnParticle(org.bukkit.Particle.CLOUD, warrior.getLocation(), 15, 0.5, 0.5, 0.5,
                        0.05);
            }
        }.runTaskLater(plugin, 20L);

        // Set cooldown (30s)
        warrior.setMetadata("warrior_dash_cd", new FixedMetadataValue(plugin, System.currentTimeMillis() + 30000));
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

    private void handleBattleWitchAI(Witch witch) {
        // Find nearest player
        Player nearestPlayer = null;
        double nearestDist = Double.MAX_VALUE;

        for (Player p : witch.getWorld().getPlayers()) {
            if (p.isDead() || p.getGameMode() == org.bukkit.GameMode.CREATIVE
                    || p.getGameMode() == org.bukkit.GameMode.SPECTATOR)
                continue;

            double dist = p.getLocation().distance(witch.getLocation());
            if (dist < nearestDist && dist < 25) {
                nearestDist = dist;
                nearestPlayer = p;
            }
        }

        if (nearestPlayer == null)
            return;

        // Cooldown check (8 seconds to allow for dice animation)
        if (witch.hasMetadata("witch_potion_cd")) {
            long cd = witch.getMetadata("witch_potion_cd").get(0).asLong();
            if (System.currentTimeMillis() < cd)
                return;
        }

        // Check if dice roll is in progress
        if (witch.hasMetadata("witch_rolling")) {
            return;
        }

        Location witchLoc = witch.getLocation();
        World world = witch.getWorld();

        // Mark as rolling
        witch.setMetadata("witch_rolling", new FixedMetadataValue(plugin, true));

        // Dice roll animation (2 seconds)
        new BukkitRunnable() {
            int timer = 0;
            final int rollDuration = 40; // 2 seconds

            @Override
            public void run() {
                if (witch.isDead()) {
                    this.cancel();
                    return;
                }

                if (timer < rollDuration) {
                    // Show random numbers as animation to nearby players
                    int displayNum = (int) (Math.random() * 6) + 1;
                    for (Player p : world.getPlayers()) {
                        if (p.getLocation().distance(witchLoc) < 30) {
                            p.sendTitle(ChatColor.DARK_PURPLE + "🎲 " + displayNum + " 🎲",
                                    ChatColor.LIGHT_PURPLE + "Battle Witch casting...", 0, 10, 0);
                        }
                    }
                    world.playSound(witchLoc, Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, 1.0f + (timer / 40f));
                    timer += 4;
                } else {
                    // Final roll
                    int finalRoll = (int) (Math.random() * 6) + 1;
                    world.playSound(witchLoc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);

                    // Determine potion name
                    String potionName;
                    switch (finalRoll) {
                        case 1:
                            potionName = ChatColor.DARK_GREEN + "Starvation Brew";
                            break;
                        case 2:
                            potionName = ChatColor.DARK_PURPLE + "Decay Elixir";
                            break;
                        case 3:
                            potionName = ChatColor.GRAY + "Shadow Curse";
                            break;
                        case 4:
                            potionName = ChatColor.RED + "Inferno Draught";
                            break;
                        case 5:
                            potionName = ChatColor.AQUA + "Frost Bane";
                            break;
                        case 6:
                            potionName = ChatColor.DARK_RED + "Soul Drain";
                            break;
                        default:
                            potionName = ChatColor.WHITE + "Unknown Brew";
                            break;
                    }

                    // Show final result
                    for (Player p : world.getPlayers()) {
                        if (p.getLocation().distance(witchLoc) < 30) {
                            p.sendTitle(ChatColor.DARK_PURPLE + "🎲 " + finalRoll + " 🎲",
                                    potionName, 10, 40, 10);
                        }
                    }

                    // Throw potion after brief delay
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        if (witch.isDead())
                            return;

                        // Find target again
                        Player target = null;
                        double nearDist = Double.MAX_VALUE;
                        for (Player p : world.getPlayers()) {
                            if (p.isDead() || p.getGameMode() == org.bukkit.GameMode.CREATIVE)
                                continue;
                            double dist = p.getLocation().distance(witch.getLocation());
                            if (dist < nearDist && dist < 30) {
                                nearDist = dist;
                                target = p;
                            }
                        }

                        if (target == null)
                            return;

                        throwWitchPotion(witch, target, finalRoll);
                    }, 10L);

                    // Clear rolling flag and set cooldown
                    witch.removeMetadata("witch_rolling", plugin);
                    witch.setMetadata("witch_potion_cd",
                            new FixedMetadataValue(plugin, System.currentTimeMillis() + 15000)); // 8 segundos de casteo entre cada uso

                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 4L);
    }

    private void throwWitchPotion(Witch witch, Player target, int diceRoll) {
        Location witchLoc = witch.getLocation().add(0, 1.5, 0);
        Location targetLoc = target.getLocation();

        org.bukkit.inventory.ItemStack potionItem = new org.bukkit.inventory.ItemStack(Material.SPLASH_POTION);
        org.bukkit.inventory.meta.PotionMeta potionMeta = (org.bukkit.inventory.meta.PotionMeta) potionItem
                .getItemMeta();

        switch (diceRoll) {
            case 1: // Hunger V + Nausea II (10 seconds)
                potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.HUNGER, 200, 4), true);
                potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.NAUSEA, 200, 1), true);
                potionMeta.setDisplayName(ChatColor.DARK_GREEN + "Starvation Brew");
                break;
            case 2: // Poison III + Wither I (20 seconds)
                potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.POISON, 400, 2), true);
                potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.WITHER, 400, 0), true);
                potionMeta.setDisplayName(ChatColor.DARK_PURPLE + "Decay Elixir");
                break;
            case 3: // Blindness I + Weakness I (15 seconds)
                potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.BLINDNESS, 300, 0), true);
                potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.WEAKNESS, 300, 0), true);
                potionMeta.setDisplayName(ChatColor.GRAY + "Shadow Curse");
                break;
            case 4: // Fire + Slowness III (12 seconds) - NEW
                potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.SLOWNESS, 240, 2), true);
                potionMeta.setDisplayName(ChatColor.RED + "Inferno Draught");
                break;
            case 5: // Mining Fatigue III + Slowness II (15 seconds) - NEW
                potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.MINING_FATIGUE, 300, 2), true);
                potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.SLOWNESS, 300, 1), true);
                potionMeta.setDisplayName(ChatColor.AQUA + "Frost Bane");
                break;
            case 6: // Instant Damage II + Levitation I (5 seconds) - NEW
                potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.INSTANT_DAMAGE, 1, 1), true);
                potionMeta.addCustomEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.LEVITATION, 100, 0), true);
                potionMeta.setDisplayName(ChatColor.DARK_RED + "Soul Drain");
                break;
        }
        potionItem.setItemMeta(potionMeta);

        // Spawn and throw the potion
        ThrownPotion potion = witch.getWorld().spawn(witchLoc, ThrownPotion.class);
        potion.setItem(potionItem);

        // Calculate trajectory
        Vector direction = targetLoc.toVector().subtract(witchLoc.toVector()).normalize();
        double distance = witchLoc.distance(targetLoc);
        double velocity = Math.min(1.5, 0.5 + (distance / 20.0));
        potion.setVelocity(direction.multiply(velocity).add(new Vector(0, 0.3, 0)));

        // Fire effect for Inferno Draught
        if (diceRoll == 4) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                target.setFireTicks(240); // 12 seconds of fire
            }, 20L);
        }

        // Effects
        witch.getWorld().playSound(witchLoc, Sound.ENTITY_WITCH_THROW, 1.5f, 1.0f);
        witch.getWorld().spawnParticle(org.bukkit.Particle.WITCH, witchLoc, 15, 0.5, 0.5, 0.5, 0);
    }

    private void handleArena(Skeleton boss) {
        if (arenaCenter == null) return;

        double limit = ARENA_RADIUS;
        double minX = arenaCenter.getX() - limit;
        double maxX = arenaCenter.getX() + limit;
        double minZ = arenaCenter.getZ() - limit;
        double maxZ = arenaCenter.getZ() + limit;

        for (Player p : arenaCenter.getWorld().getPlayers()) {
            Location loc = p.getLocation();

            /*
            Aqui se encuentra el margen de la arena si alguien trata de escapar o pasa ese margen
            va a ser devuelto al centro de la arena a 10 bloques de altura soltando a su vez
            un mensaje donde dice que no puedes escapar de la batalla
             */
            boolean outside = loc.getX() < minX - ARENA_MARGIN || loc.getX() > maxX + ARENA_MARGIN ||
                              loc.getZ() < minZ - ARENA_MARGIN || loc.getZ() > maxZ + ARENA_MARGIN;
            
            boolean nearby = loc.getX() > minX - 15 && loc.getX() < maxX + 15 &&
                             loc.getZ() > minZ - 15 && loc.getZ() < maxZ + 15;

            if (outside && nearby) {
                Location safeLoc = arenaCenter.clone().add(0, 10, 0);
                p.teleport(safeLoc);
                p.sendMessage(org.bukkit.ChatColor.RED + "YOU CANNOT ESCAPE THE BATTLEFIELD!");
                p.playSound(p.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 0.5f);
            }

            // 2. Warning Layer (Square Particles inside the glass)
            double distToMinX = Math.abs(loc.getX() - minX);
            double distToMaxX = Math.abs(loc.getX() - maxX);
            double distToMinZ = Math.abs(loc.getZ() - minZ);
            double distToMaxZ = Math.abs(loc.getZ() - maxZ);
            double minDist = Math.min(Math.min(distToMinX, distToMaxX), Math.min(distToMinZ, distToMaxZ));

            if (minDist < 10) {
                drawSquareParticles(arenaCenter, limit);
            }
        }
    }

    private void drawSquareParticles(Location center, double radius) {
        World world = center.getWorld();
        // Visual offset to be inside the glass
        double offset = 0.2;
        double minX = center.getX() - radius + offset;
        double maxX = center.getX() + radius - offset;
        double minZ = center.getZ() - radius + offset;
        double maxZ = center.getZ() + radius - offset;

        // Draw horizontal lines (X)
        for (double x = minX; x <= maxX; x += 1.0) {
            for (int y = 0; y <= 3; y++) {
                world.spawnParticle(org.bukkit.Particle.FLAME, new Location(world, x, center.getY() + y, minZ), 1, 0, 0, 0, 0);
                world.spawnParticle(org.bukkit.Particle.FLAME, new Location(world, x, center.getY() + y, maxZ), 1, 0, 0, 0, 0);
            }
        }
        // Draw horizontal lines (Z)
        for (double z = minZ; z <= maxZ; z += 1.0) {
            for (int y = 0; y <= 3; y++) {
                world.spawnParticle(org.bukkit.Particle.FLAME, new Location(world, minX, center.getY() + y, z), 1, 0, 0, 0, 0);
                world.spawnParticle(org.bukkit.Particle.FLAME, new Location(world, maxX, center.getY() + y, z), 1, 0, 0, 0, 0);
            }
        }
    }

    private void handleShooting(Skeleton boss) {
        LivingEntity target = boss.getTarget();

        if (target == null || target.isDead() || target.getWorld() != boss.getWorld())
            return;

        // COOLDOWN DE DISPARO (Ahora que el loop es cada 1s, limitamos a cada 5s)
        if (boss.hasMetadata("shooting_cd")) {
            if (System.currentTimeMillis() < boss.getMetadata("shooting_cd").get(0).asLong())
                return;
        }
        boss.setMetadata("shooting_cd", new FixedMetadataValue(plugin, System.currentTimeMillis() + 5000));

        double distance = boss.getLocation().distance(target.getLocation());

        if (distance > 25 || distance < 2)
            return;

        // --- PHASE LOGIC AND STATS ---
        double maxHealth = boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double currentHealth = boss.getHealth();
        double healthPercent = (currentHealth / maxHealth) * 100.0;

        int currentPhase;
        if (healthPercent < 5) {
            currentPhase = 7; // Final Stand
        } else if (healthPercent < 15) {
            currentPhase = 6; // Last Resistance
        } else if (healthPercent < 30) {
            currentPhase = 5; // Desperation
        } else if (healthPercent < 50) {
            currentPhase = 4; // Berserk
        } else if (healthPercent < 70) {
            currentPhase = 3; // Overdrive
        } else if (healthPercent < 85) {
            currentPhase = 2; // Aggressive
        } else {
            currentPhase = 1; // Normal
        }
        int lastPhase = boss.hasMetadata("boss_phase") ? boss.getMetadata("boss_phase").get(0).asInt() : 1;

        // 1. TRIGGER: FLASHBANG (Phase Change)
        if (currentPhase != lastPhase) {
            executeFlashbang(boss);
            executeReinforcementCall(boss, plugin);
            boss.setMetadata("boss_phase", new FixedMetadataValue(plugin, currentPhase));
            currentBossPhase = currentPhase;
            updateBossBarColor();

            // Reset cooldown immediately after phase change trigger
            boss.setMetadata("reinforce_call_cd",
                    new FixedMetadataValue(plugin, System.currentTimeMillis() + getReinforceCooldown(currentPhase)));
            boss.removeMetadata("reinforce_warned", plugin);
        }

        // 2. TRIGGER: DEPLOYABLE COVER (Every 10% HP)
        double lastSpawnHealth = boss.hasMetadata("last_cover_hp") ? boss.getMetadata("last_cover_hp").get(0).asDouble()
                : maxHealth;
        if (currentHealth <= lastSpawnHealth - (maxHealth * COVER_HP_THRESHOLD)) {
            spawnDeployableCover(boss);
            boss.setMetadata("last_cover_hp", new FixedMetadataValue(plugin, currentHealth));
        }

        // 3. TRIGGER: AIR STRIKE (Phase 3+, every 30s)
        if (currentPhase >= 3) {
            long lastAirstrike = boss.hasMetadata("airstrike_cd") ? boss.getMetadata("airstrike_cd").get(0).asLong()
                    : 0;
            if (System.currentTimeMillis() > lastAirstrike) {
                executeAirStrike(target);
                // More frequent in higher phases
                int cooldown = (currentPhase >= 5) ? 10000 : (currentPhase >= 4) ? 20000 : 30000;
                boss.setMetadata("airstrike_cd", new FixedMetadataValue(plugin, System.currentTimeMillis() + cooldown));
            }
        }

        int reinforceCooldown = getReinforceCooldown(currentPhase);
        long lastReinforceCall = boss.hasMetadata("reinforce_call_cd")
                ? boss.getMetadata("reinforce_call_cd").get(0).asLong()
                : 0;
        long timeUntilCall = lastReinforceCall - System.currentTimeMillis();

        // Warning 10 seconds before
        if (timeUntilCall > 0 && timeUntilCall <= 10000 && !boss.hasMetadata("reinforce_warned")) {
            for (Player p : boss.getWorld().getPlayers()) {
                p.sendTitle(ChatColor.DARK_RED + "⚠ WARNING ⚠",
                        ChatColor.GOLD + "Reinforcement Call incoming!", 10, 60, 10);
                p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.5f);
            }
            boss.setMetadata("reinforce_warned", new FixedMetadataValue(plugin, true));
        }

        if (System.currentTimeMillis() > lastReinforceCall) {
            executeReinforcementCall(boss, plugin);
            boss.setMetadata("reinforce_call_cd",
                    new FixedMetadataValue(plugin, System.currentTimeMillis() + reinforceCooldown));
            boss.removeMetadata("reinforce_warned", plugin);
        }

        // --- BASE SHOOTING ---
        int burstShots = 5;
        long burstInterval = 4L;

        if (currentPhase == 7) {
            // PHASE 7: FINAL STAND (<5% HP)
            burstShots = 25;
            burstInterval = 1L;
            boss.getWorld().spawnParticle(org.bukkit.Particle.END_ROD, boss.getLocation().add(0, 1, 0), 40, 0.7, 0.7,
                    0.7, 0.2);
            boss.addPotionEffect(
                    new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, 100, 4));
            boss.addPotionEffect(
                    new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 100, 4));
            boss.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 100, 2));
            // Slow regeneration
            if (boss.getHealth() < maxHealth * 0.10) {
                boss.setHealth(Math.min(boss.getHealth() + 10, maxHealth * 0.10));
            }
        } else if (currentPhase == 6) {
            // PHASE 6: LAST RESISTANCE (15-5% HP)
            burstShots = 20;
            burstInterval = 1L;
            boss.getWorld().spawnParticle(org.bukkit.Particle.SOUL_FIRE_FLAME, boss.getLocation().add(0, 1, 0), 30, 0.6,
                    0.6, 0.6, 0.15);
            boss.addPotionEffect(
                    new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, 100, 3));
            boss.addPotionEffect(
                    new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 100, 3));
            boss.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 100, 1));
        } else if (currentPhase == 5) {
            // PHASE 5: DESPERATION (30-15% HP)
            burstShots = 16;
            burstInterval = 2L;
            boss.getWorld().spawnParticle(org.bukkit.Particle.DRAGON_BREATH, boss.getLocation().add(0, 1, 0), 25, 0.5,
                    0.5, 0.5, 0.1);
            boss.addPotionEffect(
                    new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, 100, 2));
            boss.addPotionEffect(
                    new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 100, 2));
            // Spawn reinforcements every 20s
            long lastReinforce = boss.hasMetadata("reinforce_cd") ? boss.getMetadata("reinforce_cd").get(0).asLong()
                    : 0;
            if (System.currentTimeMillis() > lastReinforce) {
                spawnReinforcements(boss);
                boss.setMetadata("reinforce_cd", new FixedMetadataValue(plugin, System.currentTimeMillis() + 20000));
            }
        } else if (currentPhase == 4) {
            // PHASE 4: BERSERK (50-30% HP)
            burstShots = 12;
            burstInterval = 2L;
            boss.getWorld().spawnParticle(org.bukkit.Particle.FLAME, boss.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5,
                    0.1);
            boss.addPotionEffect(
                    new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, 100, 1));
            boss.addPotionEffect(
                    new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 100, 1));
        } else if (currentPhase == 3) {
            // PHASE 3: OVERDRIVE (70-50% HP)
            burstShots = 10;
            burstInterval = 3L;
            boss.getWorld().spawnParticle(org.bukkit.Particle.DUST, boss.getLocation().add(0, 1, 0), 15, 0.4, 0.4, 0.4,
                    0,
                    new org.bukkit.Particle.DustOptions(org.bukkit.Color.ORANGE, 1));
        } else if (currentPhase == 2) {
            // PHASE 2: AGGRESSIVE (85-70% HP)
            burstShots = 7;
            burstInterval = 3L;
            boss.getWorld().spawnParticle(org.bukkit.Particle.DUST, boss.getLocation().add(0, 1, 0), 10, 0.3, 0.3, 0.3,
                    0,
                    new org.bukkit.Particle.DustOptions(org.bukkit.Color.YELLOW, 1));
        }

        // Freeze boss while shooting
        long freezeTicks = (burstShots * burstInterval) + 10;
        boss.addPotionEffect(
                new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, (int) freezeTicks,
                        255));

        fireBurst(boss, target, burstShots, burstInterval);
        boss.getWorld().playSound(boss.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1.0f, 2.0f);
    }

    private void spawnReinforcements(LivingEntity boss) {
        boss.getWorld().playSound(boss.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 2.0f, 0.5f);
        Bukkit.broadcastMessage(
                ChatColor.DARK_RED + "☠ " + ChatColor.GOLD + "The Heavy Gunner calls for reinforcements!");

        Location bossLoc = boss.getLocation();
        for (int i = 0; i < 2; i++) {
            double angle = Math.random() * 360;
            double x = Math.cos(Math.toRadians(angle)) * 5;
            double z = Math.sin(Math.toRadians(angle)) * 5;
            Location spawnLoc = bossLoc.clone().add(x, 0, z);

            Zombie reinforcement = (Zombie) boss.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
            MilitaryMobHandler.equipPusher(reinforcement);
            reinforcement.setTarget(boss instanceof Skeleton ? ((Skeleton) boss).getTarget() : null);
        }
    }

    private void executeReinforcementCall(Skeleton boss, Plugin plugin) {
        Location bossLoc = boss.getLocation();
        World world = boss.getWorld();

        // Freeze all players and the boss for 10 seconds
        int freezeDuration = 200; // 10 seconds = 200 ticks
        boss.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.SLOWNESS, freezeDuration, 255));
        boss.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.JUMP_BOOST, freezeDuration, 250)); // Prevent jumping

        for (Player p : world.getPlayers()) {
            if (p.getLocation().distance(bossLoc) < ARENA_RADIUS + 10) {
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.SLOWNESS, freezeDuration, 255));
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(
                        org.bukkit.potion.PotionEffectType.JUMP_BOOST, freezeDuration, 250));
            }
        }

        // Initial announcement
        world.playSound(bossLoc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0f, 0.5f);
        for (Player p : world.getPlayers()) {
            p.sendTitle(ChatColor.DARK_RED + "⚔ REINFORCEMENT CALL ⚔",
                    ChatColor.YELLOW + "Rolling the dice...", 10, 40, 10);
        }

        // Dice roll animation (5 seconds of rolling)
        new BukkitRunnable() {
            int timer = 0;
            final int rollDuration = 100; // 5 seconds of rolling animation

            @Override
            public void run() {
                if (timer < rollDuration) {
                    // Show random numbers as animation
                    int displayNum = (int) (Math.random() * 6) + 1;
                    for (Player p : world.getPlayers()) {
                        p.sendTitle(ChatColor.GOLD + "🎲 " + displayNum + " 🎲",
                                ChatColor.GRAY + "Rolling...", 0, 10, 0);
                    }
                    world.playSound(bossLoc, Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f + (timer / 100f));
                    timer += 5;
                } else {
                    // Final roll
                    int finalRoll = (int) (Math.random() * 6) + 1;
                    world.playSound(bossLoc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0f, 1.0f);

                    // Determine entity based on dice roll
                    String entityName;
                    switch (finalRoll) {
                        case 1:
                            entityName = "Warrior";
                            break;
                        case 2:
                            entityName = "Pusher";
                            break;
                        case 3:
                            entityName = "The King";
                            break;
                        case 4:
                            entityName = "Elite Killer";
                            break;
                        case 5:
                            entityName = "Elite Ranger";
                            break;
                        case 6:
                            entityName = "Battle Witch";
                            break;
                        default:
                            entityName = "Warrior";
                            break;
                    }

                    // Show final result
                    for (Player p : world.getPlayers()) {
                        p.sendTitle(ChatColor.RED + "🎲 " + finalRoll + " 🎲",
                                ChatColor.GOLD + "Reinforcement: " + ChatColor.WHITE + entityName, 10, 60, 10);
                    }

                    // Spawn 2 entities, one on each side
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Vector dir = bossLoc.getDirection().normalize();
                        Vector leftOffset = new Vector(-dir.getZ(), 0, dir.getX()).multiply(4);
                        Vector rightOffset = new Vector(dir.getZ(), 0, -dir.getX()).multiply(4);

                        Location leftSpawn = bossLoc.clone().add(leftOffset);
                        Location rightSpawn = bossLoc.clone().add(rightOffset);

                        spawnReinforcementEntity(world, leftSpawn, finalRoll, boss);
                        spawnReinforcementEntity(world, rightSpawn, finalRoll, boss);

                        world.playSound(bossLoc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.5f);
                        Bukkit.broadcastMessage(ChatColor.DARK_RED + "☠ " + ChatColor.GOLD +
                                "The Heavy Gunner called reinforcement: " + ChatColor.WHITE + entityName + " x2!");
                    }, 20L); // 1-second delay after result

                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

    private void spawnReinforcementEntity(World world, Location loc, int diceRoll, Skeleton boss) {
        LivingEntity target = boss.getTarget();

        switch (diceRoll) {
            case 1: {
                Zombie warrior = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
                MilitaryMobHandler.equipWarrior(warrior);
                if (target != null)
                    warrior.setTarget(target);
                break;
            }
            case 2: {
                Zombie pusher = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
                MilitaryMobHandler.equipPusher(pusher);
                if (target != null)
                    pusher.setTarget(target);
                break;
            }
            case 3: {
                ZombieVillager king = (ZombieVillager) world.spawnEntity(loc, EntityType.ZOMBIE_VILLAGER);
                MilitaryMobHandler.equipKing(king);
                if (target != null)
                    king.setTarget(target);
                break;
            }
            case 4: {
                Zombie eliteKiller = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
                MilitaryMobHandler.equipEliteKiller(eliteKiller);
                if (target != null)
                    eliteKiller.setTarget(target);
                break;
            }
            case 5: {
                Skeleton eliteRanger = (Skeleton) world.spawnEntity(loc, EntityType.SKELETON);
                MilitaryMobHandler.equipEliteRanger(eliteRanger);
                if (target != null)
                    eliteRanger.setTarget(target);
                break;
            }
            case 6: {
                Witch battleWitch = (Witch) world.spawnEntity(loc, EntityType.WITCH);
                MilitaryMobHandler.equipBattleWitch(battleWitch);
                // Battle Witch AI handled separately
                break;
            }
        }

        // Spawn particles at location
        world.spawnParticle(org.bukkit.Particle.PORTAL, loc.add(0, 1, 0), 50, 0.5, 1, 0.5, 0.1);
    }

    private void executeFlashbang(LivingEntity boss) {
        boss.getWorld().playSound(boss.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 3.0f, 0.5f);
        boss.getWorld().spawnParticle(org.bukkit.Particle.FLASH, boss.getLocation().add(0, 1, 0), 5);

        for (Entity nearby : boss.getNearbyEntities(8, 8, 8)) {
            if (nearby instanceof Player) {
                Player p = (Player) nearby;
                p.addPotionEffect(
                        new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS,
                                FLASHBANG_DURATION, 1));
                p.addPotionEffect(
                        new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS,
                                FLASHBANG_DURATION, 2));
                p.sendMessage(org.bukkit.ChatColor.RED + "BLINDED BY TACTICAL GRENADE!");
            }
        }
    }

    private void spawnDeployableCover(LivingEntity boss) {
        boss.getWorld().playSound(boss.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.5f);
        Location bossLoc = boss.getLocation();
        Vector dir = bossLoc.getDirection().normalize();

        // Spawn 3 pushers in arc in front of him
        for (int i = -1; i <= 1; i++) {
            Vector offset = new Vector(-dir.getZ(), 0, dir.getX()).multiply(i * 1.5); // Perpendicular
            Location spawnLoc = bossLoc.clone().add(dir.multiply(2)).add(offset);
            Zombie pusher = (Zombie) boss.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
            MilitaryMobHandler.equipPusher(pusher);
            pusher.setTarget(boss instanceof Skeleton ? ((Skeleton) boss).getTarget() : null);
        }
    }

    private void executeAirStrike(LivingEntity target) {
        Location targetLoc = target.getLocation();
        target.sendMessage(org.bukkit.ChatColor.DARK_RED + "¡AVISO DE ATAQUE AÉREO DETECTADO!");
        target.getWorld().playSound(targetLoc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 0.5f);

        // Marcar zona con partículas
        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                if (timer >= 60) { // 3 segundos (20 ticks * 3)
                    targetLoc.getWorld().spawnParticle(org.bukkit.Particle.EXPLOSION, targetLoc, 1);
                    targetLoc.getWorld().playSound(targetLoc, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.5f);

                    // Daño configurable en área
                    for (Entity e : targetLoc.getWorld().getNearbyEntities(targetLoc, 4, 4, 4)) {
                        if (e instanceof LivingEntity) {
                            ((LivingEntity) e).damage(AIRSTRIKE_DAMAGE);
                        }
                    }
                    this.cancel();
                    return;
                }
                targetLoc.getWorld().spawnParticle(org.bukkit.Particle.LARGE_SMOKE, targetLoc, 5, 0.5, 0.1, 0.5, 0.05);
                timer += 5;
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

    private void fireBurst(LivingEntity shooter, LivingEntity target, int totalShots, long interval) {
        new BukkitRunnable() {
            int shots = 0;

            @Override
            public void run() {
                if (shooter.isDead() || target.isDead() || shots >= totalShots) {
                    this.cancel();
                    return;
                }

                fireBullet(shooter, target);
                shots++;
            }
        }.runTaskTimerAsynchronously(plugin, 10L, interval);
    }

    private void fireBullet(LivingEntity shooter, LivingEntity target) {
        Location origin = shooter.getEyeLocation();
        Location targetLoc = target.getEyeLocation();

        Vector dir = targetLoc.clone().subtract(origin).toVector().normalize();
        dir.add(new Vector(
                (Math.random() - 0.5) * 0.15,
                (Math.random() - 0.5) * 0.15,
                (Math.random() - 0.5) * 0.15));

        org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> {
            if (!shooter.isDead() && !target.isDead()) {
                Snowball bullet = shooter.launchProjectile(Snowball.class, dir.multiply(2.0));
                bullet.setShooter(shooter);
                bullet.addScoreboardTag("BossBullet");
                // Shot sound (now on main thread)
                shooter.getWorld().playSound(shooter.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.0f, 0.5f);
            }
        });
    }

    // ========== PHYSICAL ARENA ==========

    public static void buildArena(Location center, Plugin plugin) {
        arenaCenter = center;
        World world = center.getWorld();

        // Clear previous arena if exists
        destroyArena();


        /*
          Radio de la arena, este funciona de tal
          manera que si el radio fuera 20 tomaria
          20 bloques para cada lado sin contar el centro
          en otras palabras se toma desde un lado hasta
          otro lado en otras palabras el radio va desde
          -20 a 20 siendo asi que el 0 esta entre esos
          valores siendo asi que el radio real es de 21
          si por ejemplo el radio fuera 1 en realidad este
          seria de 3 bloques puesto que se toma desde -1 a 1
          siendo asi que los valores tomados son -1 , 0 , 1
         */
        int size = (int) ARENA_RADIUS;

        /*
         * Lo mismo aplica para la altura de la arena
         * pero en vez de ir desde -20 esta empieza desde
         * 0 en otras palabras esta comienza desde 0 a 4
         * siendo que si la altura fuera de 4 en realidad
         * seria de 5
         */
        int height = ARENA_HEIGHT;
        int startY = (int) center.getY() - 1; // Start 1 block below for floor
        int centerX = (int) center.getX();
        int centerZ = (int) center.getZ();

        // Build enclosed cube (walls, floor and ceiling)
        for (int x = centerX - size; x <= centerX + size; x++) {
            for (int z = centerZ - size; z <= centerZ + size; z++) {
                for (int y = startY; y <= startY + height; y++) {
                    // Only place blocks on edges (walls, floor, ceiling)
                    boolean isWall = (x == centerX - size || x == centerX + size ||
                            z == centerZ - size || z == centerZ + size);
                    boolean isFloor = (y == startY);
                    boolean isCeiling = (y == startY + height);

                    if (isWall || isFloor || isCeiling) {
                        Location blockLoc = new Location(world, x, y, z);
                        Block block = blockLoc.getBlock();

                        // Save original block
                        originalBlocks.put(blockLoc, block.getType());
                        arenaBlocks.add(blockLoc);

                        // Place red glass
                        block.setType(Material.RED_STAINED_GLASS);
                    }
                }
            }
        }

        // Ambient effect: Sunset time (reddish)
        world.setTime(13000);

        // Epic sound
        world.playSound(center, Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0f, 0.5f);
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "☠ " + ChatColor.RED
                + "A Heavy Gunner has appeared! The combat arena has been activated!");
    }

    public static void destroyArena() {
        // Restore original blocks
        for (Map.Entry<Location, Material> entry : originalBlocks.entrySet()) {
            entry.getKey().getBlock().setType(entry.getValue());
        }

        // Clear sets
        arenaBlocks.clear();
        originalBlocks.clear();
        arenaCenter = null;

        // Remove Boss Bar
        if (activeBossBar != null) {
            activeBossBar.removeAll();
            activeBossBar = null;
        }
    }

    private static void updateBossBarColor() {
        if (activeBossBar == null)
            return;

        switch (currentBossPhase) {
            case 1:
                activeBossBar.setColor(BarColor.GREEN);
                activeBossBar.setTitle(ChatColor.GREEN + "☠ Heavy Gunner - Phase 1: Normal ☠");
                break;
            case 2:
                activeBossBar.setColor(BarColor.YELLOW);
                activeBossBar.setTitle(ChatColor.YELLOW + "☠ Heavy Gunner - Phase 2: Aggressive ☠");
                break;
            case 3:
                activeBossBar.setColor(BarColor.YELLOW);
                activeBossBar.setTitle(ChatColor.GOLD + "☠ Heavy Gunner - Phase 3: Overdrive ☠");
                break;
            case 4:
                activeBossBar.setColor(BarColor.RED);
                activeBossBar.setTitle(ChatColor.RED + "☠ Heavy Gunner - Phase 4: Berserk ☠");
                break;
            case 5:
                activeBossBar.setColor(BarColor.RED);
                activeBossBar.setTitle(ChatColor.DARK_RED + "☠ Heavy Gunner - Phase 5: Desperation ☠");
                break;
            case 6:
                activeBossBar.setColor(BarColor.PURPLE);
                activeBossBar.setTitle(ChatColor.DARK_PURPLE + "☠ Heavy Gunner - Phase 6: Last Resistance ☠");
                break;
            case 7:
                activeBossBar.setColor(BarColor.WHITE);
                activeBossBar.setTitle(ChatColor.WHITE + "☠ Heavy Gunner - Phase 7: FINAL STAND ☠");
                break;
        }
    }

    public void setupBossBar(LivingEntity boss, Plugin plugin) {
        if (activeBossBar != null) {
            activeBossBar.removeAll();
        }

        activeBossBar = Bukkit.createBossBar(
                ChatColor.DARK_RED + "☠ Heavy Gunner ☠",
                BarColor.GREEN,
                BarStyle.SEGMENTED_10);

        activeBossBar.setVisible(true);

        // Add nearby players
        for (Player p : boss.getWorld().getPlayers()) {
            if (p.getLocation().distance(boss.getLocation()) < ARENA_RADIUS + 10) {
                activeBossBar.addPlayer(p);
            }
        }

        // Continuously update health, phases, and color
        new BukkitRunnable() {
            @Override
            public void run() {
                if (boss.isDead() || activeBossBar == null) {
                    if (activeBossBar != null) {
                        activeBossBar.removeAll();
                        activeBossBar = null;
                    }
                    this.cancel();
                    return;
                }

                double maxHealth = boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                double currentHealth = boss.getHealth();
                double healthPercent = (currentHealth / maxHealth) * 100.0;

                // Calculate phase from health
                int calculatedPhase;
                if (healthPercent > 85)
                    calculatedPhase = 1;
                else if (healthPercent > 70)
                    calculatedPhase = 2;
                else if (healthPercent > 50)
                    calculatedPhase = 3;
                else if (healthPercent > 30)
                    calculatedPhase = 4;
                else if (healthPercent > 15)
                    calculatedPhase = 5;
                else if (healthPercent > 5)
                    calculatedPhase = 6;
                else
                    calculatedPhase = 7;

                // Update phase if changed
                if (calculatedPhase != currentBossPhase) {
                    currentBossPhase = calculatedPhase;
                    updateBossBarColor();

                    // Announce phase change
                    for (Player p : boss.getWorld().getPlayers()) {
                        if (p.getLocation().distance(boss.getLocation()) < ARENA_RADIUS + 10) {
                            p.sendTitle(ChatColor.RED + "PHASE " + currentBossPhase,
                                    getPhaseTitle(currentBossPhase), 10, 40, 10);
                        }
                    }
                }

                // Update progress bar
                activeBossBar.setProgress(Math.max(0, Math.min(1, currentHealth / maxHealth)));

                // Update players
                for (Player p : boss.getWorld().getPlayers()) {
                    if (p.getLocation().distance(boss.getLocation()) < ARENA_RADIUS + 10) {
                        if (!activeBossBar.getPlayers().contains(p)) {
                            activeBossBar.addPlayer(p);
                        }
                    } else {
                        activeBossBar.removePlayer(p);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    private static String getPhaseTitle(int phase) {
        switch (phase) {
            case 1:
                return ChatColor.GREEN + "Normal";
            case 2:
                return ChatColor.YELLOW + "Aggressive";
            case 3:
                return ChatColor.GOLD + "Overdrive";
            case 4:
                return ChatColor.RED + "Berserk";
            case 5:
                return ChatColor.DARK_RED + "Desperation";
            case 6:
                return ChatColor.DARK_PURPLE + "Last Resistance";
            case 7:
                return ChatColor.WHITE + "FINAL STAND";
            default:
                return "Unknown";
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        // Protect arena blocks
        if (arenaBlocks.contains(e.getBlock().getLocation())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot break the arena barrier!");
        }
    }

    private int getReinforceCooldown(int phase) {
        switch (phase) {
            case 7:
                return 50000; // 50s
            case 6:
            case 5:
                return 60000; // 1 min
            case 4:
            case 3:
                return 120000; // 2 min
            default:
                return 180000; // 3 min
        }
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) e.getEntity();
            if (skeleton.getScoreboardTags().contains("HeavyGunner")) {
                // Destroy arena and restore blocks
                destroyArena();

                // Cleanup boss bar
                if (activeBossBar != null) {
                    activeBossBar.removeAll();
                    activeBossBar = null;
                }
                currentBossPhase = 1;

                // Restore time of day
                skeleton.getWorld().setTime(1000);

                // Check if this was a despawn death (no drops, different message)
                if (skeleton.hasMetadata("despawn_death")) {
                    // Clear all drops
                    e.getDrops().clear();
                    e.setDroppedExp(0);

                    Bukkit.broadcastMessage(ChatColor.GRAY + "The Heavy Gunner has retreated due to lack of combat.");
                } else {
                    // Normal death - give reward to a random nearby player
                    rewardRandomPlayer(skeleton.getLocation());

                    Bukkit.broadcastMessage(ChatColor.GREEN + "☠ " + ChatColor.YELLOW
                            + "The Heavy Gunner has been defeated! The arena has been deactivated.");
                }
            }
        }
    }

    /**
     * Selects a random player within 10 blocks, or the closest player if none
     * within 10.
     */
    private void rewardRandomPlayer(Location deathLoc) {
        List<Player> nearbyPlayers = new ArrayList<>();
        Player closestPlayer = null;
        double minDistance = Double.MAX_VALUE;

        // Find players
        for (Player p : deathLoc.getWorld().getPlayers()) {
            if (p.getGameMode() == GameMode.SPECTATOR)
                continue;

            double dist = p.getLocation().distance(deathLoc);

            // Collect players within 10 blocks
            if (dist <= 10.0) {
                nearbyPlayers.add(p);
            }

            // Keep track of the globally closest player
            if (dist < minDistance) {
                minDistance = dist;
                closestPlayer = p;
            }
        }

        Player luckyPlayer = null;
        java.util.Random rand = new java.util.Random();

        if (!nearbyPlayers.isEmpty()) {
            // Pick random from those within 10 blocks
            luckyPlayer = nearbyPlayers.get(rand.nextInt(nearbyPlayers.size()));
        } else if (closestPlayer != null) {
            // No one within 10, pick the single closest
            luckyPlayer = closestPlayer;
        }

        if (luckyPlayer != null) {
            com.Chagui68.weaponsaddon.handlers.BossRewardHandler.giveRandomReward(luckyPlayer);

            // Broadcast the lucky player
            Bukkit.broadcastMessage(ChatColor.YELLOW + "★ " + ChatColor.GOLD + luckyPlayer.getName() +
                    ChatColor.WHITE + " has received a " + ChatColor.RED + "Boss Reward" + ChatColor.WHITE + "!");
        }
    }

}

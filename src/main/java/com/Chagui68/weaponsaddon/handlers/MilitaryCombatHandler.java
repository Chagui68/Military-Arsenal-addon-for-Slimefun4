package com.Chagui68.weaponsaddon.handlers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import com.Chagui68.weaponsaddon.WeaponsAddon;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static org.bukkit.Bukkit.getWorlds;

public class MilitaryCombatHandler implements Listener {

    private final Plugin plugin;

    public MilitaryCombatHandler(Plugin plugin) {
        this.plugin = plugin;
        startCombatTask();
    }

    // --- EQUIPMENT WEARABILITY HANDLER (FOR KING'S CROWN) ---

    @EventHandler
    public void onCrownRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getItem();
            if (item == null || item.getType() != Material.YELLOW_STAINED_GLASS)
                return;

            if (isKingCrown(item)) {
                Player p = e.getPlayer();
                ItemStack currentHelmet = p.getInventory().getHelmet();

                // Equip crown and return current helmet or clear hand
                p.getInventory().setHelmet(item.clone());
                p.getInventory().setItemInMainHand(currentHelmet);
                p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GOLD, 1.0f, 1.0f);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCrownInventoryClick(InventoryClickEvent e) {
        if (e.getSlotType() == InventoryType.SlotType.ARMOR && e.getRawSlot() == 5) { // Helmet slot
            ItemStack cursor = e.getCursor();
            if (cursor != null && cursor.getType() == Material.YELLOW_STAINED_GLASS) {
                if (isKingCrown(cursor)) {
                    // Allow placing glass in helmet slot
                    ItemStack current = e.getCurrentItem();
                    e.setCurrentItem(cursor.clone());
                    e.getWhoClicked().setItemOnCursor(current);
                    e.setCancelled(true);
                    if (e.getWhoClicked() instanceof Player) {
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(),
                                Sound.ITEM_ARMOR_EQUIP_GOLD, 1.0f, 1.0f);
                    }
                }
            }
        }
    }

    private boolean isKingCrown(ItemStack item) {
        if (item == null || !item.hasItemMeta())
            return false;
        NamespacedKey key = new NamespacedKey(WeaponsAddon.getInstance(), "is_king_crown");
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }

    // --- DAMAGE HANDLER ---
    @EventHandler
    public void onCombatDamage(EntityDamageByEntityEvent e) {
        // Daño de Arco para Elite Ranger
        if (e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            if (arrow.getShooter() instanceof Skeleton) {
                Skeleton shooter = (Skeleton) arrow.getShooter();
                if (shooter.getScoreboardTags().contains("EliteRanger")) {
                    // DAÑO DE RANGO Configurable
                    double damage = 24.0; // Normal

                    switch (shooter.getWorld().getDifficulty()) {
                        case EASY:
                            damage = 12.0;
                            break;
                        case NORMAL:
                            damage = 24.0;
                            break;
                        case HARD:
                            damage = 32.0;
                            break;
                        default:
                            break;
                    }

                    e.setDamage(damage);
                }
            }
        }
    }

    // Logic for The King: Spawn Warriors on hit (25s cooldown)
    @EventHandler
    public void onKingDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof ZombieVillager && e.getEntity().getScoreboardTags().contains("TheKing")) {
            ZombieVillager king = (ZombieVillager) e.getEntity();

            // Cooldown check
            if (king.hasMetadata("king_summon_cd")) {
                long cd = king.getMetadata("king_summon_cd").get(0).asLong();
                if (System.currentTimeMillis() < cd)
                    return;
            }

            // Spawn 2 Warriors
            Location loc = king.getLocation();
            Vector side = new Vector(-loc.getDirection().getZ(), 0, loc.getDirection().getX()).normalize();

            Location left = loc.clone().add(side.multiply(1.5));
            Location right = loc.clone().add(side.multiply(-1.5));

            Zombie w1 = (Zombie) king.getWorld().spawnEntity(left, EntityType.ZOMBIE);
            Zombie w2 = (Zombie) king.getWorld().spawnEntity(right, EntityType.ZOMBIE);

            MilitaryMobHandler.equipWarrior(w1);
            MilitaryMobHandler.equipWarrior(w2);

            if (e.getDamager() instanceof LivingEntity) {
                w1.setTarget((LivingEntity) e.getDamager());
                w2.setTarget((LivingEntity) e.getDamager());
            }

            king.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_HURT, 1.0f, 0.5f);
            king.getWorld().spawnParticle(Particle.CLOUD, loc, 15, 0.5, 0.5, 0.5, 0.05);

            // Set new cooldown (25 seconds)
            king.setMetadata("king_summon_cd", new FixedMetadataValue(plugin, System.currentTimeMillis() + 25000));
        }
    }

    // --- TARGET HANDLER ---
    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent e) {
        Entity entity = e.getEntity();

        // List of all our military mod tags
        boolean isMilitaryMob = entity.getScoreboardTags().contains("EliteRanger") ||
                entity.getScoreboardTags().contains("EliteKiller") ||
                entity.getScoreboardTags().contains("TheKing") ||
                entity.getScoreboardTags().contains("Pusher") ||
                entity.getScoreboardTags().contains("HeavyGunner") ||
                entity.getScoreboardTags().contains("Warrior") ||
                entity.getScoreboardTags().contains("BattleWitch");

        if (isMilitaryMob) {
            // Only allow targeting Players to prevent them from attacking Armor Stands or
            // Turrets
            if (e.getTarget() != null && !(e.getTarget() instanceof Player)) {
                e.setCancelled(true);
            }
        }
    }

    // --- AI TASK START ---
    private void startCombatTask() {
        // Tarea repetitiva cada 20 ticks (1 segundo) - Chequeo rápido para cambios de
        // arma
        new BukkitRunnable() {
            @Override
            public void run() {
                scanHybridMobs();
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    private void scanHybridMobs() {
        for (World world : getWorlds()) {
            for (Skeleton skeleton : world.getEntitiesByClass(Skeleton.class)) {
                // Verificar si es nuestro Elite Ranger
                if (skeleton.getScoreboardTags().contains("EliteRanger") && !skeleton.isDead()) {
                    handleHybridAI(skeleton);
                }
            }
        }
    }

    // --- AI LOGIC ---
    private void handleHybridAI(Skeleton ranger) {
        LivingEntity target = ranger.getTarget();
        if (target == null || target.isDead() || target.getWorld() != ranger.getWorld())
            return;

        double distance = ranger.getLocation().distance(target.getLocation());
        EntityEquipment equip = ranger.getEquipment();
        if (equip == null)
            return;

        ItemStack mainHand = equip.getItemInMainHand();
        ItemStack offHand = equip.getItemInOffHand();

        // Lógica Híbrida: Cambio de Arma según distancia
        if (distance <= 5.0) {
            // Cerca: Usar Espada (Melee AI)
            if (mainHand.getType() != Material.IRON_SWORD) {
                // 1. Buscar la Espada (Debería estar en la offhand o es nueva)
                ItemStack swordToEquip = offHand;

                if (swordToEquip == null || swordToEquip.getType() != Material.IRON_SWORD) {
                    swordToEquip = new ItemStack(Material.IRON_SWORD);
                    // Intentar recuperar encantamientos si es nueva (no debería pasar si spawneó
                    // bien)
                    MilitaryMobHandler.applyEnchantments(ranger.getWorld().getDifficulty(), new ItemStack(Material.BOW),
                            swordToEquip);
                }

                // 2. Equipar Espada en Principal
                equip.setItemInMainHand(swordToEquip);

                // 3. BORRAR la Secundadía (Para evitar que dispare flechas a quemarropa use el
                // arco en la offhand)
                // Sacrificamos el Arco actual para asegurar el comportamiento Melee
                equip.setItemInOffHand(null);

                ranger.getWorld().playSound(ranger.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1.0f, 2.0f);
            }
        } else {
            // Lejos: Usar Arco (Ranged AI)
            if (mainHand.getType() != Material.BOW) {
                // 1. Guardar la Espada actual en la Offhand (Para no perderla)
                if (mainHand.getType() == Material.IRON_SWORD) {
                    equip.setItemInOffHand(mainHand);
                }

                // 2. Generar Nuevo Arco (Recuperando encantamientos según dificultad)
                ItemStack newBow = new ItemStack(Material.BOW);
                // Pasamos una espada dummy porque solo queremos encantar el arco
                MilitaryMobHandler.applyEnchantments(ranger.getWorld().getDifficulty(), newBow,
                        new ItemStack(Material.IRON_SWORD));

                // 3. Equipar Arco en Principal
                equip.setItemInMainHand(newBow);

                ranger.getWorld().playSound(ranger.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.5f);
            }
        }
    }
}

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
import org.bukkit.inventory.EquipmentSlot;
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

    // --- PUSHER CONFIGURATION ---
    private static final double PUSHER_KNOCKBACK_MULTIPLIER = 5.0; // Fuerza horizontal
    private static final double PUSHER_KNOCKBACK_VERTICAL = 0.5; // Fuerza hacia arriba (Y)

    public MilitaryCombatHandler(Plugin plugin) {
        this.plugin = plugin;
        startCombatTask();
    }

    // --- EQUIPMENT WEARABILITY HANDLER (FOR KING'S CROWN) ---

    @EventHandler
    public void onCrownRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getItem();
            if (item == null || item.getType() != Material.YELLOW_STAINED_GLASS
                    || e.getHand() != EquipmentSlot.HAND)
                return;

            if (isKingCrown(item)) {
                Player p = e.getPlayer();
                ItemStack currentHelmet = p.getInventory().getHelmet();
                ItemStack crownToEquip = item.clone();
                crownToEquip.setAmount(1);

                // Equip crown and handle hand item swap
                p.getInventory().setHelmet(crownToEquip);

                // Handle hand item (decrement stack or swap)
                ItemStack handItem = item.clone();
                if (handItem.getAmount() > 1) {
                    handItem.setAmount(handItem.getAmount() - 1);
                    // If we have a current helmet, try to put it in inventory or drop it
                    if (currentHelmet != null && currentHelmet.getType() != Material.AIR) {
                        if (p.getInventory().firstEmpty() != -1) {
                            p.getInventory().addItem(currentHelmet);
                        } else {
                            p.getWorld().dropItemNaturally(p.getLocation(), currentHelmet);
                        }
                    }
                } else {
                    handItem = currentHelmet;
                }

                if (e.getHand() == EquipmentSlot.HAND) {
                    p.getInventory().setItemInMainHand(handItem);
                } else {
                    p.getInventory().setItemInOffHand(handItem);
                }

                p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GOLD, 1.0f, 1.0f);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCrownInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null)
            return;

        ItemStack item = null;
        boolean isShiftClick = e.getClick().isShiftClick();

        if (isShiftClick) {
            item = e.getCurrentItem();
        } else {
            item = e.getCursor();
        }

        if (item == null || item.getType() != Material.YELLOW_STAINED_GLASS || !isKingCrown(item)) {
            return;
        }

        Player p = (Player) e.getWhoClicked();

        // Handle Shift-Click
        if (isShiftClick) {
            if (p.getInventory().getHelmet() == null || p.getInventory().getHelmet().getType() == Material.AIR) {
                ItemStack crownToEquip = item.clone();
                crownToEquip.setAmount(1);

                p.getInventory().setHelmet(crownToEquip);

                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                    e.setCurrentItem(item);
                } else {
                    e.setCurrentItem(null);
                }

                p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GOLD, 1.0f, 1.0f);
                e.setCancelled(true);
            }
            return;
        }

        // Handle placing in helmet slot
        if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
            // Raw slot 5 is typically the helmet slot in most player inventory views
            // but we can also check the actual equipment slot if possible or just rely on
            // SlotType.ARMOR
            // and the fact that it's a crown (helmet).

            // In a standard player inventory, slot 39 (raw 5) is helmet.
            if (e.getRawSlot() == 5) {
                ItemStack currentHelmet = e.getCurrentItem();
                ItemStack crownToEquip = item.clone();
                crownToEquip.setAmount(1);

                e.setCurrentItem(crownToEquip);

                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                    p.setItemOnCursor(item); // Update the cursor with the remaining items
                    if (currentHelmet != null && currentHelmet.getType() != Material.AIR) {
                        if (p.getInventory().firstEmpty() != -1) {
                            p.getInventory().addItem(currentHelmet);
                        } else {
                            p.getWorld().dropItemNaturally(p.getLocation(), currentHelmet);
                        }
                    }
                } else {
                    p.setItemOnCursor(currentHelmet);
                }

                p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GOLD, 1.0f, 1.0f);
                e.setCancelled(true);
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
        // Zero damage and manual knockback for Pushers (they only push)
        Entity damager = e.getDamager();
        if (damager instanceof LivingEntity && damager.getScoreboardTags().contains("MA_Pusher")) {
            e.setDamage(0.0);

            // Manual knockback for 1.20.1 compatibility when damage is 0
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                Vector dir = player.getLocation().toVector().subtract(damager.getLocation().toVector()).normalize();
                player.setVelocity(dir.multiply(PUSHER_KNOCKBACK_MULTIPLIER).setY(PUSHER_KNOCKBACK_VERTICAL));
            }
        }

        // Daño de Arco para Elite Ranger
        if (e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            if (arrow.getShooter() instanceof Skeleton) {
                Skeleton shooter = (Skeleton) arrow.getShooter();
                if (shooter.getScoreboardTags().contains("MA_EliteRanger")) {
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
        if (e.getEntity() instanceof ZombieVillager && e.getEntity().getScoreboardTags().contains("MA_TheKing")) {
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
        boolean isMilitaryMob = entity.getScoreboardTags().contains("MA_EliteRanger") ||
                entity.getScoreboardTags().contains("MA_EliteKiller") ||
                entity.getScoreboardTags().contains("MA_TheKing") ||
                entity.getScoreboardTags().contains("MA_Pusher") ||
                entity.getScoreboardTags().contains("MA_HeavyGunner") ||
                entity.getScoreboardTags().contains("MA_Warrior") ||
                entity.getScoreboardTags().contains("MA_BattleWitch");

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
                if (skeleton.getScoreboardTags().contains("MA_EliteRanger") && !skeleton.isDead()) {
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

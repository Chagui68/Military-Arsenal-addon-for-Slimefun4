package com.Chagui68.weaponsaddon.handlers;

import com.Chagui68.weaponsaddon.items.MachineGun;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MilitaryMobHandler implements Listener {

    private final Random random = new Random();

    // Porcentaje de aparacion (0.25 = 25%)
    private static final double RANGER_CHANCE = 0.2; // 20%
    private static final double ELITE_CHANCE = 0.3; // 30%
    private static final double KING_CHANCE = 0.25; // 25%
    private static final double PUSHER_CHANCE = 0.8; // 80%
    private static final double WITCH_CHANCE = 0.25; // 25%

    public MilitaryMobHandler(Plugin plugin) {
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        // [SPAWN NATURAL]

        // 1. Filtro: Solo reemplazamos esqueletos y zombies que aparecen naturalmente
        // Permitir Spawn Natural, Generación de Mundo, HUEVOS e INVOCACIONES (Vanilla)

        if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL &&
                e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CHUNK_GEN &&
                e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG &&
                e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.REINFORCEMENTS)
            return;

        double roll = random.nextDouble(); // Lanza un dado del 0.0 al 1.0

        // Manejo de esqueletos
        if (e.getEntityType() == EntityType.SKELETON) {
            Skeleton skeleton = (Skeleton) e.getEntity();

            // Elite Ranger (20%)
            if (roll < RANGER_CHANCE) {
                equipEliteRanger(skeleton);
            }
        }

        // Manejo de ZOMBIES
        else if (e.getEntityType() == EntityType.ZOMBIE) {
            Zombie zombie = (Zombie) e.getEntity();

            // Elite Killer (30%)
            if (roll < ELITE_CHANCE) {
                equipEliteKiller(zombie);
            }
            //Pusher (80%)
            else if (roll < PUSHER_CHANCE) {
                equipPusher(zombie);
            }
        }
        //Manerjo Aldeanos zombies
        else if (e.getEntityType() == EntityType.ZOMBIE_VILLAGER) {
            ZombieVillager zombieVillager = (ZombieVillager) e.getEntity();

            // The King (25%)
            if (roll < KING_CHANCE) {
                equipKing(zombieVillager);
            }
        }

        // Manejo Brujas
        else if (e.getEntityType() == EntityType.WITCH) {
            Witch witch = (Witch) e.getEntity();

            // Battle Witch (25%)
            if (roll < WITCH_CHANCE) {
                equipBattleWitch(witch);
            }
        }
    }

    // Creacion de las entidades y definicion de su equipamiento

    public static void equipKing(ZombieVillager king) {
        king.setCustomName(ChatColor.DARK_GRAY + "♔ The King ♔");
        king.setCustomNameVisible(true);
        king.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(250.0); // 125 Corazones
        king.setHealth(250.0);
        king.setBaby(false);
        king.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(150.0);
        king.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.13); // Lentitud 3
        king.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0); // Inamovible
        king.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.5);
        king.addScoreboardTag("TheKing");

        EntityEquipment equip = king.getEquipment();
        if (equip != null) {
            ItemStack casco = new ItemStack(Material.YELLOW_STAINED_GLASS);
            ItemMeta metaCasco = casco.getItemMeta();
            if (metaCasco != null) {
                AttributeModifier helmet_protection = new AttributeModifier(
                        UUID.randomUUID(),
                        "protection", 5,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.HEAD);
                
                AttributeModifier helmet_toughness = new AttributeModifier(
                        UUID.randomUUID(),
                        "Toughness", 4,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.HEAD);

                metaCasco.addAttributeModifier(Attribute.GENERIC_ARMOR, helmet_protection);
                metaCasco.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, helmet_toughness);
                
                metaCasco.setDisplayName(ChatColor.of("#CFCD8A") + "♔ The King's Crown ♔");
                metaCasco.addEnchant(Enchantment.PROTECTION, 5, true);
                metaCasco.addEnchant(Enchantment.PROJECTILE_PROTECTION,5,true);
                metaCasco.addEnchant(Enchantment.BLAST_PROTECTION,5,true);
                metaCasco.addEnchant(Enchantment.FIRE_PROTECTION,5,true);
                
                casco.setItemMeta(metaCasco);
            }
            equip.setHelmet(casco);
            equip.setHelmetDropChance(0.01f); //1%

            ItemStack arma = new ItemStack(Material.GOLDEN_SWORD);
            ItemMeta metaArma = arma.getItemMeta();
            if (metaArma != null) {
                AttributeModifier sword_damage = new AttributeModifier(
                        UUID.randomUUID(),
                        "att", 15,
                        AttributeModifier.Operation.ADD_NUMBER, // Ataque = 15 de daño
                        EquipmentSlot.HAND);

                AttributeModifier sword_attack_speed = new AttributeModifier(
                        UUID.randomUUID(), "att_speed", -2.4,
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.HAND);
                // Daño dinamico en base atributos

                // Todos los valores o variables son exclusivos de esta entidad. En otras
                // palabras son locales

                // 1. Ocultar los atributos azules predeterminados de Minecraft para limpiar la
                // interfaz
                metaArma.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

                // 2. Definir valores base
                double Damage_Gold_SWORD = 4.0; // Daño base de una espada de oro
                double Extra_Damage_Attribute = 15.0; // Daño nuevo base de la espada

                // 3. Calcular el daño en base al encantamiento "Sharpness" (Filo)
                // Fórmula de Minecraft: 0.5 * nivel + 0.5
                int Sharpness_Level = 3; // Nivel del encantamiento
                double Bonus_Sharpness = (0.5 * Sharpness_Level) + 0.5;

                // 4. Calcular el daño TOTAL que el jugador verá y trendrá la espada
                double Total_Damage = Damage_Gold_SWORD + Extra_Damage_Attribute + Bonus_Sharpness;

                metaArma.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, sword_damage);
                metaArma.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, sword_attack_speed);
                metaArma.setDisplayName(ChatColor.of("#CFCD8A") + "♔ The King's Sword ♔");
                metaArma.addEnchant(Enchantment.SHARPNESS, Sharpness_Level, true);
                metaArma.addEnchant(Enchantment.LOOTING, 5, true);
                metaArma.addEnchant(Enchantment.FIRE_ASPECT, 4, true);
                metaArma.setUnbreakable(true);

                // Lore especial del arma
                // El lore.add(""); Sirve para dejar un espacio blanco
                List<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(ChatColor.RED + "❣ Weapon damage: " + ChatColor.WHITE + Total_Damage);
                lore.add(ChatColor.YELLOW + "⚡ Attack speed " + ChatColor.WHITE + 1.6);
                metaArma.setLore(lore);

                arma.setItemMeta(metaArma);
            }
            equip.setItemInMainHand(arma);
            equip.setItemInMainHandDropChance(0.1f); // 10%
            
            // Resto de la armadura (puedes personalizarlos luego igual que el casco)
            equip.setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
            equip.setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
            equip.setBoots(new ItemStack(Material.GOLDEN_BOOTS));
            
            equip.setChestplateDropChance(0.0f);
            equip.setLeggingsDropChance(0.0f);
            equip.setBootsDropChance(0.0f);
        }
    }

    public static void equipWarrior(Zombie warrior) {
        warrior.setCustomName(ChatColor.GRAY + "Royal Warrior");
        warrior.setCustomNameVisible(true);
        warrior.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0);
        warrior.setHealth(40.0);
        warrior.addScoreboardTag("Warrior");

        EntityEquipment equip = warrior.getEquipment();
        if (equip != null) {
            equip.setItemInMainHand(new ItemStack(Material.IRON_SWORD));
            equip.setHelmet(new ItemStack(Material.IRON_HELMET));
            equip.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            equip.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            equip.setBoots(new ItemStack(Material.IRON_BOOTS));

            equip.setItemInMainHandDropChance(0.0f);
            equip.setHelmetDropChance(0.0f);
            equip.setChestplateDropChance(0.0f);
            equip.setLeggingsDropChance(0.0f);
            equip.setBootsDropChance(0.0f);
        }
    }

    public static void equipPusher(Zombie pusher) {
        pusher.setCustomName(ChatColor.GOLD + "⇄ Pusher ⇄");
        pusher.setCustomNameVisible(true);
        pusher.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10.0);
        pusher.setHealth(10.0);
        pusher.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.40);
        pusher.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0.0);
        pusher.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(1.0);
        pusher.setBaby(true);
        pusher.addScoreboardTag("Pusher");

        EntityEquipment equip = pusher.getEquipment();
        if (equip != null) {

            // Arma en mano principal
            equip.setItemInMainHand(new ItemStack(Material.STICK));
            equip.setItemInMainHandDropChance(0.0f); // No dropea

            equip.setHelmet(new ItemStack(Material.PISTON));
            equip.setChestplate(getColorArmor(Material.LEATHER_CHESTPLATE, Color.YELLOW));
            equip.setLeggings(getColorArmor(Material.LEATHER_LEGGINGS, Color.YELLOW));
            equip.setBoots(getColorArmor(Material.LEATHER_BOOTS, Color.YELLOW));
            equip.setHelmetDropChance(0.0f);
            equip.setChestplateDropChance(0.0f);
            equip.setLeggingsDropChance(0.0f);
            equip.setBootsDropChance(0.0f);
        }

    }

    public static void equipEliteKiller(Zombie miniboss) {
        miniboss.setCustomName(ChatColor.WHITE + "☣ Elite Killer ☣ ");
        miniboss.setCustomNameVisible(true);
        miniboss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1); // Lentitud 4
        miniboss.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0); // No retroceso
        miniboss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(999999.0); // Instakill ( Posiblemente ) :V
                                                                                       // hace 999k de daño
        miniboss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000.0);
        miniboss.setHealth(1000.0); // Vida del enemigo 500 corazones
        miniboss.setBaby(false);

        miniboss.addScoreboardTag("EliteKiller");

        EntityEquipment equip = miniboss.getEquipment();
        if (equip != null) {

            // Arma en mano principal
            equip.setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
            equip.setItemInMainHandDropChance(0.0f); // No dropea

            equip.setHelmet(new ItemStack(Material.WHITE_STAINED_GLASS));
            equip.setChestplate(getColorArmor(Material.LEATHER_CHESTPLATE, Color.WHITE));
            equip.setLeggings(getColorArmor(Material.LEATHER_LEGGINGS, Color.WHITE));
            equip.setBoots(getColorArmor(Material.LEATHER_BOOTS, Color.WHITE));
            equip.setHelmetDropChance(0.0f);
            equip.setChestplateDropChance(0.0f);
            equip.setLeggingsDropChance(0.0f);
            equip.setBootsDropChance(0.0f);
        }

    }

    public static void equipHeavyGunner(Skeleton boss) {
        // 1. [NOMBRE DE LA ENTIDAD]: El nombre que verán los jugadores
        boss.setCustomName(ChatColor.DARK_RED + "☠ Heavy Gunner ☠");
        boss.setCustomNameVisible(true);
        // 2. [ATRIBUTOS]: Vida, Daño, Velocidad, etc.
        boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10000.0);
        boss.setHealth(10000.0);
        boss.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0); // No retroceso
        boss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3); // Esqueleto es más ágil
        boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(18.0); // 9 Corazones de daño melee

        // 3. [TAG]: Sirve para que BossAIHandler sepa qué IA usar
        boss.addScoreboardTag("HeavyGunner");

        // 4. [EQUIPO]: Armas y Armaduras
        EntityEquipment equip = boss.getEquipment();
        if (equip != null) {
            // Arma: Machine Gun
            equip.setItemInMainHand(MachineGun.MACHINE_GUN.clone());
            equip.setItemInMainHandDropChance(0.001f); // 0.1% probabilidad de drop

            // Secundaria: Cuchillo Táctico (Espada de Hierro)
            ItemStack tacticalKnife = new ItemStack(Material.IRON_SWORD);
            equip.setItemInOffHand(tacticalKnife);
            equip.setItemInOffHandDropChance(0.0f); // No dropear cuchillo

            // Armadura Pesada (Netherite)
            equip.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
            equip.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
            equip.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
            equip.setBoots(new ItemStack(Material.NETHERITE_BOOTS));
            equip.setHelmetDropChance(0.0f);
            equip.setChestplateDropChance(0.0f);
            equip.setLeggingsDropChance(0.0f);
            equip.setBootsDropChance(0.0f);

            // [EFECTOS DE POCIÓN]: Lo que añadimos anteriormente
            boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 1));
        }
    }

    public static void equipEliteRanger(Skeleton miniboss) {
        // [OTRA ENTIDAD]: Sigue el mismo patrón que la anterior
        miniboss.setCustomName(ChatColor.DARK_GREEN + "☠ Elite Ranger ☠");
        miniboss.setCustomNameVisible(true);

        // Escalar stats por dificultad
        double damage = 60.0;
        double health = 75.0;

        switch (miniboss.getWorld().getDifficulty()) {
            case EASY:
                damage = 40.0;
                health = 55.0;
                break;
            case NORMAL:
                damage = 60.0;
                health = 75.0;
                break;
            case HARD:
                damage = 80.0;
                health = 100.0;
                break;
            default: // PEACEFUL
                break;
        }

        miniboss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        miniboss.setHealth(health);
        miniboss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.35);
        miniboss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);

        // Tag para la IA Híbrida
        miniboss.addScoreboardTag("EliteRanger");

        EntityEquipment equip = miniboss.getEquipment();
        if (equip != null) {
            ItemStack bow = new ItemStack(Material.BOW);
            ItemStack sword = new ItemStack(Material.IRON_SWORD);

            // Encantamientos por probabilidad segun dificultad
            applyEnchantments(miniboss.getWorld().getDifficulty(), bow, sword);

            equip.setItemInMainHand(bow);
            equip.setItemInMainHandDropChance(0.0f);

            equip.setItemInOffHand(sword);
            equip.setItemInOffHandDropChance(0.0f);

            // Armadura de Camuflaje (Cuero Verde - Zombie Camo)
            equip.setHelmet(new ItemStack(Material.OAK_LEAVES));
            equip.setChestplate(getColorArmor(Material.LEATHER_CHESTPLATE, Color.GREEN));
            equip.setLeggings(getColorArmor(Material.LEATHER_LEGGINGS, Color.GREEN));
            equip.setBoots(getColorArmor(Material.LEATHER_BOOTS, Color.GREEN));
            equip.setHelmetDropChance(0.0f);
            equip.setChestplateDropChance(0.0f);
            equip.setLeggingsDropChance(0.0f);
            equip.setBootsDropChance(0.0f);
        }
    }

    public static void applyEnchantments(Difficulty difficulty, ItemStack bow, ItemStack sword) {
        if (difficulty == Difficulty.EASY)
            return;

        Random rnd = new Random();
        boolean isHard = difficulty == Difficulty.HARD;

        // Probabilidad de encantar: Normal 30%, Hard 70%
        if (rnd.nextDouble() > (isHard ? 0.3 : 0.7)) {
            try {
                // Sword Enchants
                Enchantment sharp = Enchantment.getByName("DAMAGE_ALL");
                Enchantment fire = Enchantment.getByName("FIRE_ASPECT");

                if (sharp != null)
                    sword.addEnchantment(sharp, isHard ? 3 : 1);
                if (isHard && rnd.nextBoolean() && fire != null)
                    sword.addEnchantment(fire, 1);

                // Bow Enchants
                Enchantment power = Enchantment.getByName("ARROW_DAMAGE");
                Enchantment punch = Enchantment.getByName("ARROW_KNOCKBACK");

                if (power != null)
                    bow.addEnchantment(power, isHard ? 3 : 1);
                if (isHard && rnd.nextBoolean() && punch != null)
                    bow.addEnchantment(punch, 1);
            } catch (Exception e) {
                // Ignorar errores de encantamiento para no romper el spawn
            }
        }
    }

    private static ItemStack getColorArmor(Material mat, Color color) {
        ItemStack item = new ItemStack(mat);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (meta != null) {
            meta.setColor(color);
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Equips a Battle Witch that throws Instant Damage III potions
     */
    public static void equipBattleWitch(Witch witch) {
        witch.setCustomName(ChatColor.DARK_PURPLE + "☠ Battle Witch ☠");
        witch.setCustomNameVisible(true);
        witch.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(80.0); // 40 hearts
        witch.setHealth(80.0);
        witch.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.28); // Slightly faster

        // Add tag for identification
        witch.addScoreboardTag("BattleWitch");

        // Give resistance to make her tankier
        witch.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
    }
}

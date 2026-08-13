package com.Chagui68.weaponsaddon.listeners;

import com.Chagui68.weaponsaddon.utils.VersionSafe;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;

public class EliteMobCombatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEliteStrike(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof LivingEntity) || !(e.getEntity() instanceof LivingEntity))
            return;
        LivingEntity elite = (LivingEntity) e.getDamager();
        LivingEntity victim = (LivingEntity) e.getEntity();
        if (elite.getScoreboardTags().contains("MA_FlameRaider")) {
            victim.setFireTicks(Math.max(victim.getFireTicks(), 80));
        } else if (elite.getScoreboardTags().contains("MA_VenomReaper")) {
            victim.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("POISON"), 100, 1));
        } else if (elite.getScoreboardTags().contains("MA_SiegeBreaker")) {
            victim.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("WEAKNESS"), 120, 1));
            victim.setVelocity(victim.getVelocity().add(victim.getLocation().toVector()
                    .subtract(elite.getLocation().toVector()).normalize().multiply(0.8).setY(0.3)));
        } else if (elite.getScoreboardTags().contains("MA_FrostWarden")) {
            victim.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("SLOWNESS"), 120, 2));
        } else if (elite.getScoreboardTags().contains("MA_DreadWeaver")) {
            victim.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("POISON"), 200, 1));
        } else if (elite.getScoreboardTags().contains("MA_ShockTrooper")) {
            victim.addPotionEffect(new PotionEffect(VersionSafe.getPotionEffectType("SLOWNESS"), 60, 0));
        } else if (elite.getScoreboardTags().contains("MA_ThunderTitan")) {
            victim.getWorld().strikeLightningEffect(victim.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEliteDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        if (entity.getScoreboardTags().contains("MA_MoltenColossus")) {
            Location loc = entity.getLocation();
            loc.getWorld().createExplosion(loc, 3.0f, false, false);
            loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.6f);
            e.getDrops().clear();
            e.setDroppedExp(0);
        }
    }
}

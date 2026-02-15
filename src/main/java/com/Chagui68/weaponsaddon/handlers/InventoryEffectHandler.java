package com.Chagui68.weaponsaddon.handlers;

import com.Chagui68.weaponsaddon.core.attributes.CustomEffectEmitter;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import com.Chagui68.weaponsaddon.WeaponsAddon;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class InventoryEffectHandler extends BukkitRunnable {

    @Override
    public void run() {
        boolean tungstenEnabled = WeaponsAddon.getInstance().getConfig()
                .getBoolean("inventory-effects.tungsten-weight.enabled", true);

        int slownessLevel = WeaponsAddon.getInstance().getConfig()
                .getInt("inventory-effects.tungsten-weight.slowness-level", 2);
        int interval = WeaponsAddon.getInstance().getConfig()
                .getInt("inventory-effects.tungsten-weight.check-interval", 60);
        int duration = interval + 40;

        for (Player player : getOnlinePlayers()) {
            // Process legacy Tungsten effects
            if (tungstenEnabled) {
                int finalLevel = getTungstenSlownessLevel(player, slownessLevel);
                if (finalLevel >= 0) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.SLOWNESS, duration, finalLevel, true, false, true));
                }
            }

            // Process NEW modular components with custom effects
            processCustomEffects(player);
        }
    }

    private void processCustomEffects(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null)
                continue;

            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            if (sfItem instanceof CustomEffectEmitter emitter) {
                emitter.applyEffect(player);
            }
        }
    }

    private int getTungstenSlownessLevel(Player player, int baseLevel) {
        int maxLevel = -1;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null)
                continue;

            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            if (sfItem != null) {
                String id = sfItem.getId();
                if (id.equals("MA_TUNGSTEN_INGOT")) {
                    maxLevel = Math.max(maxLevel, baseLevel);
                } else if (id.equals("MA_TUNGSTEN_BLADE")) {
                    maxLevel = Math.max(maxLevel, baseLevel + 1); // +1 higher than the ingot
                }
            }
        }
        return maxLevel;
    }
}

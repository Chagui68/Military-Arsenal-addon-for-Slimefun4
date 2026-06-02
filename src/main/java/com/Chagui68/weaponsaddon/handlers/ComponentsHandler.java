package com.Chagui68.weaponsaddon.handlers;

import com.github.drakescraft_labs.slimefun4.api.items.SlimefunItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ComponentsHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCompassUse(PlayerInteractEvent e) {
        ItemStack item = e.getItem();

        if (item == null || item.getType() != Material.COMPASS) return;

        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        if (sfItem != null && sfItem.getAddon() != null &&
                sfItem.getAddon().getName().equals("WeaponsAddon")) {
            e.setCancelled(true);
        }
    }
}

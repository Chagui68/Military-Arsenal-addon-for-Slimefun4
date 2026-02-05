package com.Chagui68.weaponsaddon.listeners;

import com.Chagui68.weaponsaddon.items.CustomRecipeItem;
import com.Chagui68.weaponsaddon.items.gui.RecipeViewerGUI;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SlimefunGuideListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSlimefunGuideClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();

        if (clicked == null || !clicked.hasItemMeta()) {
            return;
        }

        String title = e.getView().getTitle();
        boolean isSlimefunGuide = false;

        // Detectar si es la guía de Slimefun por el título
        if (title.contains("Slimefun") || title.contains("Guide") ||
                title.contains("guía") || title.contains("Guía")) {
            isSlimefunGuide = true;
        }

        // Verificación adicional por tamaño de inventario y presencia de items Slimefun
        if (e.getInventory().getSize() == 54) {
            SlimefunItem sfItemTest = SlimefunItem.getByItem(clicked);
            if (sfItemTest != null) {
                isSlimefunGuide = true;
            }
        }

        if (!isSlimefunGuide) {
            return;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(clicked);

        if (sfItem == null) {
            return;
        }

        // Verificar si es un item con receta personalizada
        if (sfItem instanceof CustomRecipeItem) {
            CustomRecipeItem customItem = (CustomRecipeItem) sfItem;

            e.setCancelled(true);
            p.closeInventory();

            // Abrir la GUI personalizada después de un tick
            Bukkit.getScheduler().runTaskLater(
                    Bukkit.getPluginManager().getPlugin("WeaponsAddon"),
                    () -> {
                        try {
                            if (customItem.getGridSize() == CustomRecipeItem.RecipeGridSize.GRID_4x4) {
                                RecipeViewerGUI.open4x4Recipe(p,
                                        ChatColor.stripColor(customItem.getResultItem().getItemMeta().getDisplayName()),
                                        customItem.getResultItem(),
                                        customItem.getFullRecipe());
                            } else {
                                RecipeViewerGUI.open6x6Recipe(p,
                                        ChatColor.stripColor(customItem.getResultItem().getItemMeta().getDisplayName()),
                                        customItem.getResultItem(),
                                        customItem.getFullRecipe());
                            }
                        } catch (Exception ex) {
                            p.sendMessage(ChatColor.RED + "Error al abrir la receta. Contacta a un administrador.");
                            ex.printStackTrace();
                        }
                    },
                    1L);
        }
    }
}

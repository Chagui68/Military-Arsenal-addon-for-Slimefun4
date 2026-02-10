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

    // Known Slimefun Guide titles (check multiple languages/versions)
    private static final String[] SLIMEFUN_GUIDE_IDENTIFIERS = {
            "Slimefun Guide",
            "Slimefun",
            "Item Group:",
            "Search Results"
    };

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

        // ONLY trigger in actual Slimefun Guide - strict check
        if (!isSlimefunGuide(title)) {
            return;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(clicked);

        if (sfItem == null) {
            return;
        }

        // Only handle CustomRecipeItem clicks
        if (sfItem instanceof CustomRecipeItem) {
            CustomRecipeItem customItem = (CustomRecipeItem) sfItem;

            e.setCancelled(true);
            p.closeInventory();

            // Open custom recipe viewer after a tick
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
                            p.sendMessage(ChatColor.RED + "Error opening recipe viewer.");
                            ex.printStackTrace();
                        }
                    },
                    1L);
        }
    }

    /**
     * Strictly check if the inventory is a Slimefun Guide.
     * Only matches actual Slimefun Guide titles, NOT crafting tables or other GUIs.
     */
    private boolean isSlimefunGuide(String title) {
        if (title == null)
            return false;

        // Exclude our own GUIs
        if (title.contains("Military") ||
                title.contains("Crafting Table") ||
                title.contains("Workshop") ||
                title.contains("Upgrade") ||
                title.contains("Recipe:") ||
                title.contains("Fabricator") ||
                title.contains("Terminal")) {
            return false;
        }

        // Check for Slimefun Guide identifiers
        for (String identifier : SLIMEFUN_GUIDE_IDENTIFIERS) {
            if (title.contains(identifier)) {
                return true;
            }
        }

        return false;
    }
}

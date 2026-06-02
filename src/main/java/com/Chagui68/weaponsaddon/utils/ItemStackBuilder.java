package com.Chagui68.weaponsaddon.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class ItemStackBuilder {
    private ItemStackBuilder() {
    }

    public static ItemStack create(Material material, String displayName, String... lore) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        if (lore.length > 0) {
            List<String> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(loreList);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack create(ItemStack item, int amount) {
        ItemStack stack = item.clone();
        stack.setAmount(amount);
        return stack;
    }
}

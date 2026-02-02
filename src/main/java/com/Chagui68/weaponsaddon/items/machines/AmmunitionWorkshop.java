package com.Chagui68.weaponsaddon.items.machines;

import com.Chagui68.weaponsaddon.items.MilitaryRecipeTypes;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class AmmunitionWorkshop extends SlimefunItem {

    public static final SlimefunItemStack AMMUNITION_WORKSHOP = new SlimefunItemStack(
            "AMMUNITION_WORKSHOP",
            Material.SMITHING_TABLE,
            "&6⚒ &eAmmunition Workshop",
            "",
            "&73×3 Basic Crafting Station",
            "&7For ammunition components",
            "",
            "&eRight-Click to open"
    );

    public AmmunitionWorkshop(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlock();
                BlockStorage.addBlockInfo(b, "id", "AMMUNITION_WORKSHOP");
            }
        });

        addItemHandler((BlockUseHandler) e -> {
            e.cancel();
            Player p = e.getPlayer();
            Block block = e.getClickedBlock().get();
            AmmunitionWorkshopHandler.openGuiStatic(p, block.getLocation());
        });
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] recipe = new ItemStack[]{
                new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.IRON_BLOCK),
                new ItemStack(Material.IRON_INGOT), SlimefunItems.STEEL_PLATE, new ItemStack(Material.IRON_INGOT),
                new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.ANVIL), new ItemStack(Material.IRON_BLOCK)
        };

        new AmmunitionWorkshop(category, AMMUNITION_WORKSHOP, RecipeType.ENHANCED_CRAFTING_TABLE, recipe).register(addon);
    }
}

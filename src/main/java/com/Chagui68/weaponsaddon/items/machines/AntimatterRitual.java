package com.Chagui68.weaponsaddon.items.machines;

import com.Chagui68.weaponsaddon.items.AntimatterRifle;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class AntimatterRitual extends SlimefunItem {

    public static final SlimefunItemStack ANTIMATTER_RITUAL_CORE = new SlimefunItemStack(
            "ANTIMATTER_RITUAL_CORE",
            Material.BEACON,
            "&4☢ &fAntimatter Ritual Core",
            "",
            "&7Central trigger for 9×9 ritual array",
            "&7Place catalysts on 16 pedestals",
            "&7Right-click to start ritual",
            "",
            "&6Required Structure:",
            "&7- 9×9 iron block border",
            "&7- 4 netherite blocks (cross pattern)",
            "&7- 16 Antimatter Pedestals (odd positions)",
            "&7- Crying obsidian filler",
            "",
            "&c⚠ Ritual destroys the altar after use",
            "&eClick core to begin annihilation process"
    );

    private static class RitualStructure {
        private static boolean validateStructure(Block core, Player p) {

            Material[][] expected = {
                    {Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK},
                    {Material.IRON_BLOCK, null, Material.CRYING_OBSIDIAN, null, Material.CRYING_OBSIDIAN, null, Material.CRYING_OBSIDIAN, null, Material.IRON_BLOCK},
                    {Material.IRON_BLOCK, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.IRON_BLOCK},
                    {Material.IRON_BLOCK, null, Material.CRYING_OBSIDIAN, null, Material.NETHERITE_BLOCK, null, Material.CRYING_OBSIDIAN, null, Material.IRON_BLOCK},
                    {Material.IRON_BLOCK, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.NETHERITE_BLOCK, Material.BEACON, Material.NETHERITE_BLOCK, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.IRON_BLOCK},
                    {Material.IRON_BLOCK, null, Material.CRYING_OBSIDIAN, null, Material.NETHERITE_BLOCK, null, Material.CRYING_OBSIDIAN, null, Material.IRON_BLOCK},
                    {Material.IRON_BLOCK, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.CRYING_OBSIDIAN, Material.IRON_BLOCK},
                    {Material.IRON_BLOCK, null, Material.CRYING_OBSIDIAN, null, Material.CRYING_OBSIDIAN, null, Material.CRYING_OBSIDIAN, null, Material.IRON_BLOCK},
                    {Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK}
            };

            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    // Saltear el centro donde está el beacon clickeado
                    if (row == 4 && col == 4) {
                        continue;
                    }

                    int offsetX = col - 4;
                    int offsetZ = row - 4;
                    Block checkBlock = core.getRelative(offsetX, 0, offsetZ);
                    Material expectedMat = expected[row][col];

                    if (expectedMat == null) {
                        SlimefunItem sfItem = BlockStorage.check(checkBlock);
                        if (sfItem == null || !sfItem.getId().equals("ANTIMATTER_PEDESTAL")) {
                            p.sendMessage(ChatColor.RED + "Missing pedestal at Row:" + row + " Col:" + col);
                            p.sendMessage(ChatColor.GRAY + "Position: X:" + checkBlock.getX() + " Y:" + checkBlock.getY() + " Z:" + checkBlock.getZ());
                            p.sendMessage(ChatColor.GRAY + "Found: " + (sfItem != null ? sfItem.getId() : checkBlock.getType()));
                            return false;
                        }
                    } else {
                        if (checkBlock.getType() != expectedMat) {
                            p.sendMessage(ChatColor.RED + "Wrong block at Row:" + row + " Col:" + col);
                            p.sendMessage(ChatColor.GRAY + "Position: X:" + checkBlock.getX() + " Y:" + checkBlock.getY() + " Z:" + checkBlock.getZ());
                            p.sendMessage(ChatColor.GRAY + "Expected: " + expectedMat + " | Found: " + checkBlock.getType());
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        private static void destroyRitual(Block core) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    int offsetX = col - 4;
                    int offsetZ = row - 4;
                    Block checkBlock = core.getRelative(offsetX, 0, offsetZ);

                    // Efecto de partículas antes de destruir
                    checkBlock.getWorld().spawnParticle(
                            Particle.SOUL_FIRE_FLAME,
                            checkBlock.getLocation().add(0.5, 0.5, 0.5),
                            10, 0.3, 0.3, 0.3, 0.05
                    );

                    // Eliminar de BlockStorage si es Slimefun item
                    BlockStorage.clearBlockInfo(checkBlock);

                    // Destruir el bloque
                    checkBlock.setType(Material.AIR);
                }
            }

            // Efecto final en el centro
            core.getWorld().spawnParticle(Particle.EXPLOSION, core.getLocation().add(0.5, 0.5, 0.5), 20, 2, 2, 2, 0.1);
            core.getWorld().playSound(core.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 3.0f, 0.8f);
        }
    }

    public AntimatterRitual(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlock();
                BlockStorage.addBlockInfo(b, "id", "ANTIMATTER_RITUAL_CORE");
            }
        });

        addItemHandler((BlockUseHandler) e -> {
            e.cancel();
            Player p = e.getPlayer();
            Block block = e.getClickedBlock().get();

            if (!RitualStructure.validateStructure(block, p)) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4☢ &cInvalid ritual structure!"));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Check the required pattern"));
                return;
            }

            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4☢ &fAntimatter Ritual &4ACTIVATED"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Annihilating matter... &c⚠"));
            p.getInventory().addItem(AntimatterRifle.ANTIMATTER_RIFLE.clone());
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a✓ &fAntimatter Rifle created successfully!"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Right-click entities for instant annihilation"));

            // Destruir el altar después de usarlo
            RitualStructure.destroyRitual(block);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c⚠ &7Ritual altar consumed by antimatter"));
        });
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] recipe = new ItemStack[]{
                new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.NETHERITE_BLOCK), new ItemStack(Material.IRON_BLOCK),
                new ItemStack(Material.NETHERITE_BLOCK), new ItemStack(Material.BEACON), new ItemStack(Material.NETHERITE_BLOCK),
                new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.NETHERITE_BLOCK), new ItemStack(Material.IRON_BLOCK)
        };

        new AntimatterRitual(category, ANTIMATTER_RITUAL_CORE, RecipeType.ENHANCED_CRAFTING_TABLE, recipe).register(addon);
    }
}

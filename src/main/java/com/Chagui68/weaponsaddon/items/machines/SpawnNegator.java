package com.Chagui68.weaponsaddon.items.machines;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.utils.ColorUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotHopperable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SpawnNegator extends SlimefunItem implements NotHopperable, EnergyNetComponent {

    private static final Set<Location> activeMachines = ConcurrentHashMap.newKeySet();
    private static final int ENERGY_CONSUMPTION = 50; // 1000 J / 10s = 50 J/tick

    public static final SlimefunItemStack SPAWN_NEGATOR = new SlimefunItemStack(
            "MA_SPAWN_NEGATOR",
            Material.BEACON,
            ColorUtils.translate("&b⚡ &3Spawn Negator &b⚡"),
            "",
            "&7Prevents custom military entities",
            "&7from spawning within its radius.",
            "",
            "&b⚡ &3Radius: &f100.0 blocks",
            "&b⚡ &3Consumption: &f1,000 J / 10s",
            "",
            "&eRight-Click to view status");

    public SpawnNegator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                Block b = e.getBlock();
                BlockStorage.addBlockInfo(b, "id", "MA_SPAWN_NEGATOR");
                activeMachines.add(b.getLocation());
            }
        });

        // Register the listeners
        WeaponsAddon.getInstance().getServer().getPluginManager().registerEvents(new SpawnNegatorListener(),
                WeaponsAddon.getInstance());

        // Visual effect and Energy consumption task
        new BukkitRunnable() {
            private double step = 0;

            @Override
            public void run() {
                activeMachines.removeIf(loc -> {
                    if (loc.getBlock().getType() != Material.BEACON)
                        return true;
                    String id = BlockStorage.getLocationInfo(loc, "id");
                    return id == null || !id.equals("MA_SPAWN_NEGATOR");
                });

                for (Location loc : activeMachines) {
                    int charge = getCharge(loc);
                    if (charge >= ENERGY_CONSUMPTION) {
                        removeCharge(loc, ENERGY_CONSUMPTION);

                        // Capsule particles
                        spawnCapsuleParticles(loc, step);
                    } else {
                        // Show "No Energy" particles occasionally
                        if (System.currentTimeMillis() % 2000 < 100) {
                            loc.getWorld().spawnParticle(Particle.SMOKE, loc.clone().add(0.5, 0.5, 0.5), 5, 0.2, 0.2,
                                    0.2, 0.01);
                        }
                    }
                }
                step += 0.2;
            }
        }.runTaskTimer(WeaponsAddon.getInstance(), 10L, 10L); // Run every 0.5s (10 ticks)
    }

    private void spawnCapsuleParticles(Location loc, double step) {
        Location center = loc.clone().add(0.5, 0.5, 0.5);
        double radius = 1.2;
        int points = 10;

        for (int i = 0; i < points; i++) {
            double angle = step + (i * ((Math.PI * 2) / points));
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;

            // Top ring
            loc.getWorld().spawnParticle(Particle.END_ROD, center.clone().add(x, 1.2, z), 1, 0, 0, 0, 0);
            // Bottom ring
            loc.getWorld().spawnParticle(Particle.END_ROD, center.clone().add(x, -0.2, z), 1, 0, 0, 0, 0);

            // Vertical lines (capsule walls)
            if (i % 2 == 0) {
                for (double y = -0.2; y <= 1.2; y += 0.4) {
                    loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, center.clone().add(x, y, z), 1, 0, 0, 0,
                            0.01);
                }
            }
        }
    }

    private static void openStatusGui(Player player, Location loc) {
        Inventory inv = Bukkit.createInventory(null, 9, ColorUtils.translate("&3Spawn Negator Status"));

        ItemStack background = new ItemStack(Material.CYAN_STAINED_GLASS_PANE);
        ItemMeta bgMeta = background.getItemMeta();
        if (bgMeta != null) {
            bgMeta.setDisplayName(" ");
            background.setItemMeta(bgMeta);
        }

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, background);
        }

        SlimefunItem item = BlockStorage.check(loc);
        if (item instanceof SpawnNegator) {
            SpawnNegator sn = (SpawnNegator) item;
            int charge = sn.getCharge(loc);
            String statusText = charge >= ENERGY_CONSUMPTION ? "&aACTIVE" : "&cINACTIVE (No Energy)";

            ItemStack info = new ItemStack(Material.BEACON);
            ItemMeta meta = info.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ColorUtils.translate("&b&lSpawn Negator Status"));
                meta.setLore(ColorUtils.translateList(List.of(
                        "",
                        "&7Status: " + statusText,
                        "&7Energy: &e" + charge + " / " + sn.getCapacity() + " J",
                        "&7Radius: &f100.0 blocks")));
                info.setItemMeta(meta);
            }
            inv.setItem(4, info);
        }

        player.openInventory(inv);
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] recipe = new ItemStack[] {
                SlimefunItems.STEEL_PLATE, SlimefunItems.STEEL_PLATE, SlimefunItems.STEEL_PLATE,
                SlimefunItems.ELECTRO_MAGNET, MilitaryComponents.VOID_CORE_MACHINE, SlimefunItems.ELECTRO_MAGNET,
                SlimefunItems.STEEL_PLATE, SlimefunItems.STEEL_PLATE, SlimefunItems.STEEL_PLATE
        };

        new SpawnNegator(category, SPAWN_NEGATOR, RecipeType.ENHANCED_CRAFTING_TABLE, recipe)
                .register(addon);
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return 10000;
    }

    public static class SpawnNegatorListener implements Listener {
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onSpawn(CreatureSpawnEvent e) {
            Entity entity = e.getEntity();

            boolean isMilitaryMob = entity.getScoreboardTags().stream().anyMatch(tag -> tag.contains("MA_"));

            if (isMilitaryMob) {
                double radiusSq = Math
                        .pow(WeaponsAddon.getInstance().getConfig().getDouble("spawn_negator.radius", 100.0), 2);
                Location spawnLoc = e.getLocation();

                for (Location machineLoc : activeMachines) {
                    if (machineLoc.getWorld().equals(spawnLoc.getWorld())) {
                        SlimefunItem item = BlockStorage.check(machineLoc);
                        if (item instanceof SpawnNegator) {
                            SpawnNegator sn = (SpawnNegator) item;
                            if (sn.getCharge(machineLoc) >= ENERGY_CONSUMPTION) {
                                if (machineLoc.distanceSquared(spawnLoc) <= radiusSq) {
                                    e.setCancelled(true);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInteract(PlayerInteractEvent e) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
                return;
            Block b = e.getClickedBlock();
            if (b != null && b.getType() == Material.BEACON) {
                if (BlockStorage.check(b, "MA_SPAWN_NEGATOR")) {
                    e.setCancelled(true);
                    openStatusGui(e.getPlayer(), b.getLocation());
                }
            }
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent e) {
            if (e.getView().getTitle().equals(ColorUtils.translate("&3Spawn Negator Status"))) {
                e.setCancelled(true);
            }
        }
    }
}

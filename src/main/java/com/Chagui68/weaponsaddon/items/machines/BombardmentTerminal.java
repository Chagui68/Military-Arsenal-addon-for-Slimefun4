package com.Chagui68.weaponsaddon.items.machines;

import com.Chagui68.weaponsaddon.items.CustomRecipeItem;
import com.Chagui68.weaponsaddon.items.MilitaryRecipeTypes;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.machines.energy.EnergyManager;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class BombardmentTerminal extends CustomRecipeItem implements EnergyNetComponent {

    private static final int ENERGY_CAPACITY = 4000000;
    private static final int ENERGY_PER_USE = 2000000;

    public static final SlimefunItemStack BOMBARDMENT_TERMINAL = new SlimefunItemStack(
            "BOMBARDMENT_TERMINAL",
            Material.CYAN_STAINED_GLASS,
            "&4💣 &cBombardment Terminal",
            "",
            "&7GPS-targeted airstrike system",
            "&7Drops TNT bombs at coordinates",
            "",
            "&6Energy: &e2,000,000 J per attack",
            "&6Fuel: &e10 TNT + 5 Nether Stars",
            "&6Attack: &e2 waves × 4 bombs",
            "&6Radius: &e10 blocks",
            "",
            "&eRight-Click to open",
            "",
            "&7Click in guide to view recipe");

    public BombardmentTerminal(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe,
            RecipeGridSize gridSize) {
        super(itemGroup, item, MilitaryRecipeTypes.getMilitaryMachineFabricator(), recipe, gridSize);
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return ENERGY_CAPACITY;
    }

    @Override
    public void preRegister() {
        addItemHandler((BlockUseHandler) e -> {
            e.cancel();
            Player p = e.getPlayer();
            Block block = e.getClickedBlock().get();
            Location blockLoc = block.getLocation();
            int charge = EnergyManager.getCharge(blockLoc);
            openTerminalGUI(p, blockLoc, charge);
        });

        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                me.mrCookieSlime.Slimefun.api.BlockStorage.addBlockInfo(e.getBlock(), "id", "BOMBARDMENT_TERMINAL");
                spawnSatelliteModel(e.getBlock().getLocation());
            }
        });

        addItemHandler(new io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                removeSatelliteModel(e.getBlock().getLocation());
            }

            @Override
            public void onExplode(Block b, List<ItemStack> drops) {
                removeSatelliteModel(b.getLocation());
            }
        });
    }

    private void spawnSatelliteModel(Location loc) {
        Location center = loc.clone().add(0.5, 0, 0.5);
        World world = loc.getWorld();
        String tag = "SF_SATELLITE_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();

        // 1. BASE ROBUSTA (Chasis de la Terminal)
        BlockDisplay base = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        base.setBlock(Material.NETHERITE_BLOCK.createBlockData());
        base.setTransformation(new Transformation(
                new Vector3f(-0.4f, 0.0f, -0.4f), 
                new Quaternionf(),
                new Vector3f(0.8f, 0.5f, 0.8f),    
                new Quaternionf()
        ));
        base.addScoreboardTag(tag);

        // 2. PANEL DE CONTROL (Teclado/Interfaz superior)
        BlockDisplay panel = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        panel.setBlock(Material.HEAVY_WEIGHTED_PRESSURE_PLATE.createBlockData());
        panel.setTransformation(new Transformation(
                new Vector3f(-0.35f, 0.5f, -0.35f),
                new Quaternionf(),
                new Vector3f(0.7f, 0.05f, 0.7f),
                new Quaternionf()
        ));
        panel.addScoreboardTag(tag);

        // 3. PANTALLA MILITAR (Monitor inclinado con brillo)
        BlockDisplay screen = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        screen.setBlock(Material.LIME_STAINED_GLASS_PANE.createBlockData());
        screen.setTransformation(new Transformation(
                new Vector3f(-0.3f, 0.55f, 0.1f),
                new Quaternionf().rotateX((float) Math.toRadians(-30)),
                new Vector3f(0.6f, 0.4f, 0.05f),
                new Quaternionf()
        ));
        screen.addScoreboardTag(tag);

        // 4. INDICADORES LED (Luces de estado)
        BlockDisplay led = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        led.setBlock(Material.GLOWSTONE.createBlockData());
        led.setTransformation(new Transformation(
                new Vector3f(0.2f, 0.51f, -0.3f),
                new Quaternionf(),
                new Vector3f(0.1f, 0.05f, 0.1f),
                new Quaternionf()
        ));
        led.addScoreboardTag(tag);

        // 5. ANTENA DE COMUNICACIÓN (Enlace Satelital)
        BlockDisplay antenna = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        antenna.setBlock(Material.LIGHTNING_ROD.createBlockData());
        antenna.setTransformation(new Transformation(
                new Vector3f(-0.3f, 0.5f, -0.3f),
                new Quaternionf(),
                new Vector3f(1.0f, 1.0f, 1.0f), // Escala normal para el pararrayos
                new Quaternionf()
        ));
        antenna.addScoreboardTag(tag);
        
        // 6. DETALLE DE ENERGIA (Núcleo lateral)
        BlockDisplay core = (BlockDisplay) world.spawnEntity(center, EntityType.BLOCK_DISPLAY);
        core.setBlock(Material.SEA_LANTERN.createBlockData());
        core.setTransformation(new Transformation(
                new Vector3f(0.35f, 0.1f, -0.2f),
                new Quaternionf(),
                new Vector3f(0.1f, 0.3f, 0.4f),
                new Quaternionf()
        ));
        core.addScoreboardTag(tag);
    }

    public static void removeSatelliteModel(Location loc) {
        String tag = "SF_SATELLITE_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
        // Usamos loc.clone() para no mutar la ubicación original
        Location searchLoc = loc.clone().add(0.5, 0.5, 0.5);
        for (Entity entity : loc.getWorld().getNearbyEntities(searchLoc, 1, 1, 1)) {
            if (entity.getScoreboardTags().contains(tag)) {
                entity.remove();
            }
        }
    }

    private void openTerminalGUI(Player p, Location blockLoc, int currentEnergy) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_RED + "Bombardment Terminal");

        for (int i = 0; i < 27; i++) {
            inv.setItem(i, new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        inv.setItem(10, new CustomItemStack(Material.RED_STAINED_GLASS_PANE,
                ChatColor.RED + "⬇ TNT SLOT ⬇"));
        inv.setItem(11, null);

        inv.setItem(15, new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE,
                ChatColor.YELLOW + "⬇ NETHER STAR SLOT ⬇"));
        inv.setItem(16, null);

        String energyStatus = currentEnergy >= ENERGY_PER_USE ? ChatColor.GREEN + "✓ Ready"
                : ChatColor.RED + "✗ Low energy";

        inv.setItem(22, new CustomItemStack(Material.LIME_STAINED_GLASS_PANE,
                ChatColor.GREEN + "▶ ACTIVATE ◀",
                ChatColor.GRAY + "Requires:",
                ChatColor.YELLOW + " • 10 TNT",
                ChatColor.YELLOW + " • 5 Nether Stars",
                ChatColor.AQUA + " • 2M J energy",
                "",
                energyStatus));

        inv.setItem(4, new CustomItemStack(Material.OBSERVER,
                ChatColor.DARK_RED + "⚡ TERMINAL ⚡",
                ChatColor.AQUA + "Energy: " + formatEnergy(currentEnergy) + "/" + formatEnergy(ENERGY_CAPACITY)));

        p.openInventory(inv);
        TerminalClickHandler.registerInventory(p, inv, blockLoc);
    }

    private String formatEnergy(int energy) {
        if (energy >= 1000000) {
            return String.format("%.1fM", energy / 1000000.0);
        } else if (energy >= 1000) {
            return String.format("%.1fK", energy / 1000.0);
        }
        return String.valueOf(energy);
    }

    public static int getEnergyRequired() {
        return ENERGY_PER_USE;
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] fullRecipe = new ItemStack[] {
                MilitaryComponents.REINFORCED_FRAME, MilitaryComponents.QUANTUM_PROCESSOR,
                MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.ENERGY_MATRIX,
                MilitaryComponents.QUANTUM_PROCESSOR, MilitaryComponents.REINFORCED_FRAME,
                MilitaryComponents.QUANTUM_PROCESSOR, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.QUANTUM_PROCESSOR,
                MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.EXPLOSIVE_CORE, MilitaryComponents.EXPLOSIVE_CORE,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.ENERGY_MATRIX,
                MilitaryComponents.ENERGY_MATRIX, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.EXPLOSIVE_CORE, MilitaryComponents.EXPLOSIVE_CORE,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.ENERGY_MATRIX,
                MilitaryComponents.QUANTUM_PROCESSOR, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.TARGETING_SYSTEM,
                MilitaryComponents.TARGETING_SYSTEM, MilitaryComponents.QUANTUM_PROCESSOR,
                MilitaryComponents.REINFORCED_FRAME, MilitaryComponents.QUANTUM_PROCESSOR,
                MilitaryComponents.HYDRAULIC_SYSTEM, MilitaryComponents.COOLANT_SYSTEM,
                MilitaryComponents.QUANTUM_PROCESSOR, MilitaryComponents.REINFORCED_FRAME
        };

        BombardmentTerminal terminal = new BombardmentTerminal(category, BOMBARDMENT_TERMINAL, fullRecipe,
                RecipeGridSize.GRID_6x6);
        terminal.register(addon);
        MachineFabricatorHandler.registerRecipe(terminal);
    }
}

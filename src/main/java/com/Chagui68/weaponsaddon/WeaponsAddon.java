package com.Chagui68.weaponsaddon;

import com.Chagui68.weaponsaddon.handlers.MachineGunHandler;
import com.Chagui68.weaponsaddon.items.machines.BombardmentTerminal;
import com.Chagui68.weaponsaddon.items.machines.BombardmentTerminalRecipeDisplay;
import com.Chagui68.weaponsaddon.items.MachineGun;
import com.Chagui68.weaponsaddon.items.MachineGunAmmo;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.machines.AmmunitionWorkshop;
import com.Chagui68.weaponsaddon.items.machines.AmmunitionWorkshopHandler;
import com.Chagui68.weaponsaddon.items.machines.MilitaryCraftingTable;
import com.Chagui68.weaponsaddon.items.machines.MilitaryCraftingHandler;
import com.Chagui68.weaponsaddon.items.machines.MilitaryMachineFabricator;
import com.Chagui68.weaponsaddon.items.machines.MachineFabricatorHandler;
import com.Chagui68.weaponsaddon.items.machines.TerminalClickHandler;
import com.Chagui68.weaponsaddon.items.gui.RecipeViewerGUI;
import com.Chagui68.weaponsaddon.listeners.SlimefunGuideListener;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class WeaponsAddon extends JavaPlugin implements SlimefunAddon {

    private static WeaponsAddon instance;

    @Override
    public void onEnable() {
        instance = this;

        Config config = new Config(this);


        NamespacedKey mainKey = new NamespacedKey(this, "military_arsenal");
        CustomItemStack mainItem = new CustomItemStack(
                Material.NETHERITE_SWORD,
                "&4⚔ &c&lMILITARY ARSENAL",
                "",
                "&7Advanced military equipment",
                "&7and tactical systems",
                "",
                "&e▶ Click to open categories",
                "&8⇨ Main Category"
        );
        NestedItemGroup mainGroup = new NestedItemGroup(mainKey, mainItem, 2);

        NamespacedKey componentsKey = new NamespacedKey(this, "military_components");
        CustomItemStack componentsItem = new CustomItemStack(
                Material.REDSTONE_BLOCK,
                "&6⚙ &eMilitary Components",
                "",
                "&7Basic materials for crafting",
                "&7military equipment",
                "",
                "&8⇨ Level 1 Components"
        );
        SubItemGroup componentsGroup = new SubItemGroup(componentsKey, mainGroup, componentsItem);

        NamespacedKey weaponsKey = new NamespacedKey(this, "military_weapons");
        CustomItemStack weaponsItem = new CustomItemStack(
                Material.DIAMOND_SWORD,
                "&c⚔ &4Military Weapons",
                "",
                "&7Advanced combat equipment",
                "&7and ammunition",
                "",
                "&8⇨ Tier 2 Weapons"
        );
        SubItemGroup weaponsGroup = new SubItemGroup(weaponsKey, mainGroup, weaponsItem);

        NamespacedKey machinesKey = new NamespacedKey(this, "military_machines");
        CustomItemStack machinesItem = new CustomItemStack(
                Material.OBSERVER,
                "&4💣 &cMilitary Machines",
                "",
                "&7Automated warfare systems",
                "&7and tactical devices",
                "",
                "&8⇨ Tier 2 Machines"
        );
        SubItemGroup machinesGroup = new SubItemGroup(machinesKey, mainGroup, machinesItem);

        mainGroup.register(this);


        getLogger().info("========================================");
        getLogger().info("Registering Event Handlers...");
        getServer().getPluginManager().registerEvents(new AmmunitionWorkshopHandler(), this);
        getServer().getPluginManager().registerEvents(new MilitaryCraftingHandler(), this);
        getServer().getPluginManager().registerEvents(new MachineFabricatorHandler(), this);
        getServer().getPluginManager().registerEvents(new MachineGunHandler(), this);
        getServer().getPluginManager().registerEvents(new RecipeViewerGUI(), this);
        getServer().getPluginManager().registerEvents(new SlimefunGuideListener(), this);
        TerminalClickHandler.setPlugin(this);
        getServer().getPluginManager().registerEvents(new TerminalClickHandler(), this);
        getLogger().info("✓ All event handlers registered");


        getLogger().info("========================================");
        getLogger().info("Registering Crafting Stations...");

        getLogger().info("→ Ammunition Workshop (3×3)");
        new AmmunitionWorkshop(
                weaponsGroup,
                AmmunitionWorkshop.AMMUNITION_WORKSHOP,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.CARTOGRAPHY_TABLE), new ItemStack(Material.IRON_BLOCK),
                        new ItemStack(Material.GUNPOWDER), new ItemStack(Material.ANVIL), new ItemStack(Material.GUNPOWDER),
                        new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.IRON_BLOCK)
                }
        ).register(this);

        getLogger().info("→ Military Crafting Table (4×4)");
        new MilitaryCraftingTable(
                machinesGroup,
                MilitaryCraftingTable.MILITARY_CRAFTING_TABLE,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.IRON_BLOCK),
                        new ItemStack(Material.REDSTONE), new ItemStack(Material.ANVIL), new ItemStack(Material.REDSTONE),
                        new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.IRON_BLOCK)
                }
        ).register(this);

        getLogger().info("→ Military Machine Fabricator (6×6)");
        new MilitaryMachineFabricator(
                machinesGroup,
                MilitaryMachineFabricator.MILITARY_MACHINE_FABRICATOR,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{
                        new ItemStack(Material.NETHERITE_BLOCK), new ItemStack(Material.RESPAWN_ANCHOR), new ItemStack(Material.NETHERITE_BLOCK),
                        new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.BEACON), new ItemStack(Material.REDSTONE_BLOCK),
                        new ItemStack(Material.NETHERITE_BLOCK), new ItemStack(Material.NETHER_STAR), new ItemStack(Material.NETHERITE_BLOCK)
                }
        ).register(this);

        getLogger().info("✓ All crafting stations registered");


        getLogger().info("========================================");
        getLogger().info("Registering Military Components...");
        MilitaryComponents.register(this, componentsGroup);
        getLogger().info("✓ All components registered");


        getLogger().info("========================================");
        getLogger().info("Registering Ammunition...");
        MachineGunAmmo.register(this, weaponsGroup);
        getLogger().info("✓ All ammunition registered");


        getLogger().info("========================================");
        getLogger().info("Registering Weapons...");
        MachineGun.register(this, weaponsGroup);
        getLogger().info("✓ All weapons registered");


        getLogger().info("========================================");
        getLogger().info("Registering Military Machines...");
        BombardmentTerminal.register(this, machinesGroup);
        BombardmentTerminalRecipeDisplay.register(this, machinesGroup);
        getLogger().info("✓ All machines registered");


        getLogger().info("========================================");
        getLogger().info("✓ WeaponsAddon enabled successfully!");
        getLogger().info("========================================");
        getLogger().info("Main Category: 1 | Subcategories: 3");
        getLogger().info("Crafting Stations: 3 (3×3 + 4×4 + 6×6)");
        getLogger().info("Recipe Viewer: ENABLED");
        getLogger().info("Inventory Persistence: ENABLED");
        getLogger().info("========================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("WeaponsAddon disabled!");
    }

    public static WeaponsAddon getInstance() {
        return instance;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/Chagui68/Military-Arsenal-addon-for-Slimefun4/issues";
    }
}
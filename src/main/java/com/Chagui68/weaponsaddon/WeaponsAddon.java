package com.Chagui68.weaponsaddon;

import com.Chagui68.weaponsaddon.handlers.ComponentsHandler;
import com.Chagui68.weaponsaddon.handlers.MachineGunHandler;
import com.Chagui68.weaponsaddon.handlers.UpgradeTableHandler;
import com.Chagui68.weaponsaddon.items.AntimatterRifle;
import com.Chagui68.weaponsaddon.items.BossSpawnEgg;
import com.Chagui68.weaponsaddon.items.MachineGun;
import com.Chagui68.weaponsaddon.items.MachineGunAmmo;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.gui.RecipeViewerGUI;
import com.Chagui68.weaponsaddon.items.machines.AmmunitionWorkshop;
import com.Chagui68.weaponsaddon.items.machines.AmmunitionWorkshopHandler;
import com.Chagui68.weaponsaddon.items.machines.AntimatterPedestal;
import com.Chagui68.weaponsaddon.items.machines.AntimatterRitual;
import com.Chagui68.weaponsaddon.items.machines.BombardmentTerminal;
import com.Chagui68.weaponsaddon.items.machines.MachineFabricatorHandler;
import com.Chagui68.weaponsaddon.items.machines.MilitaryCraftingHandler;
import com.Chagui68.weaponsaddon.items.machines.MilitaryCraftingTable;
import com.Chagui68.weaponsaddon.items.machines.MilitaryMachineFabricator;
import com.Chagui68.weaponsaddon.items.machines.TerminalClickHandler;
import com.Chagui68.weaponsaddon.items.machines.AttackTurret;
import com.Chagui68.weaponsaddon.items.machines.WeaponUpgradeTable;
import com.Chagui68.weaponsaddon.commands.WeaponsCommand;
import com.Chagui68.weaponsaddon.handlers.MilitaryCombatHandler;
import com.Chagui68.weaponsaddon.handlers.MilitaryMobHandler;
import com.Chagui68.weaponsaddon.items.vouchers.MilitaryVouchers;
import com.Chagui68.weaponsaddon.listeners.BossAIHandler;
import com.Chagui68.weaponsaddon.listeners.SlimefunGuideListener;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class WeaponsAddon extends JavaPlugin implements SlimefunAddon {

        private static WeaponsAddon instance;

        public WeaponsAddon() {
                instance = this;
        }

        @Override
        public void onEnable() {
                // Configuration
                saveDefaultConfig();

                // Create Item Groups
                NestedItemGroup mainGroup = new NestedItemGroup(
                                new NamespacedKey(this, "military_arsenal"),
                                new CustomItemStack(Material.NETHERITE_SWORD, "&4⚔ &cMilitary Arsenal"));

                SubItemGroup componentsGroup = new SubItemGroup(
                                new NamespacedKey(this, "military_components"),
                                mainGroup,
                                new CustomItemStack(Material.REDSTONE, "&6⚙ &eMilitary Components"));

                SubItemGroup weaponsGroup = new SubItemGroup(
                                new NamespacedKey(this, "military_weapons"),
                                mainGroup,
                                new CustomItemStack(Material.DIAMOND_SWORD, "&c⚔ &4Military Weapons"));

                SubItemGroup ammunitionGroup = new SubItemGroup(
                                new NamespacedKey(this, "military_ammunition"),
                                mainGroup,
                                new CustomItemStack(Material.FIREWORK_STAR, "&e🔸 &6Military Ammunition"));

                SubItemGroup workbenchesGroup = new SubItemGroup(
                                new NamespacedKey(this, "military_workbenches"),
                                mainGroup,
                                new CustomItemStack(Material.SMITHING_TABLE, "&6⚒ &eMilitary Workbenches"));

                SubItemGroup machinesGroup = new SubItemGroup(
                                new NamespacedKey(this, "military_machines"),
                                mainGroup,
                                new CustomItemStack(Material.BLAST_FURNACE, "&4⚔ &cMilitary Multiblocks"));

                SubItemGroup bossesGroup = new SubItemGroup(
                                new NamespacedKey(this, "military_bosses"),
                                mainGroup,
                                new CustomItemStack(Material.WITHER_SKELETON_SKULL, "&4☠ &cMilitary Bosses"));

                SubItemGroup vouchersGroup = new SubItemGroup(
                                new NamespacedKey(this, "military_vouchers"),
                                mainGroup,
                                new CustomItemStack(Material.PAPER, "&b✉ &3Military Vouchers"));

                SubItemGroup warMachinesGroup = new SubItemGroup(
                                new NamespacedKey(this, "war_machines"),
                                mainGroup,
                                new CustomItemStack(Material.OBSERVER, "&4💣 &cWar Machines"));

                SubItemGroup upgradesGroup = new SubItemGroup(
                                new NamespacedKey(this, "military_upgrades"),
                                mainGroup,
                                new CustomItemStack(Material.NETHER_STAR, "&b✨ &3Military Upgrades"));

                SubItemGroup defensesGroup = new SubItemGroup(
                                new NamespacedKey(this, "military_defenses"),
                                mainGroup,
                                new CustomItemStack(Material.SHIELD, "&1🛡 &9Military Defenses"));

                // Register all groups
                mainGroup.register(this);
                componentsGroup.register(this);
                weaponsGroup.register(this);
                ammunitionGroup.register(this);
                workbenchesGroup.register(this);
                machinesGroup.register(this);
                bossesGroup.register(this);
                vouchersGroup.register(this);
                warMachinesGroup.register(this);
                upgradesGroup.register(this);
                defensesGroup.register(this);

                // Register items with debug logging
                try {
                        getLogger().info("Registering MilitaryComponents...");
                        MilitaryComponents.register(this, componentsGroup, upgradesGroup);
                        getLogger().info("MilitaryComponents registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register MilitaryComponents: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering MilitaryVouchers...");
                        MilitaryVouchers.register(this, vouchersGroup);
                        getLogger().info("MilitaryVouchers registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register MilitaryVouchers: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering AmmunitionWorkshop...");
                        AmmunitionWorkshop.register(this, workbenchesGroup);
                        getLogger().info("AmmunitionWorkshop registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register AmmunitionWorkshop: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering MilitaryCraftingTable...");
                        MilitaryCraftingTable.register(this, workbenchesGroup);
                        getLogger().info("MilitaryCraftingTable registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register MilitaryCraftingTable: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering MilitaryMachineFabricator...");
                        MilitaryMachineFabricator.register(this, workbenchesGroup);
                        getLogger().info("MilitaryMachineFabricator registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register MilitaryMachineFabricator: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering WeaponUpgradeTable...");
                        WeaponUpgradeTable.register(this, workbenchesGroup);
                        getLogger().info("WeaponUpgradeTable registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register WeaponUpgradeTable: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering AttackTurret...");
                        AttackTurret.register(this, defensesGroup);
                        getLogger().info("AttackTurret registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register AttackTurret: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering BombardmentTerminal...");
                        BombardmentTerminal.register(this, warMachinesGroup);
                        getLogger().info("BombardmentTerminal registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register BombardmentTerminal: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering AntimatterPedestal...");
                        AntimatterPedestal.register(this, machinesGroup);
                        getLogger().info("AntimatterPedestal registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register AntimatterPedestal: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering AntimatterRitual...");
                        AntimatterRitual.register(this, machinesGroup);
                        getLogger().info("AntimatterRitual registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register AntimatterRitual: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering MachineGunAmmo...");
                        MachineGunAmmo.register(this, ammunitionGroup);
                        getLogger().info("MachineGunAmmo registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register MachineGunAmmo: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering MachineGun...");
                        MachineGun.register(this, weaponsGroup);
                        getLogger().info("MachineGun registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register MachineGun: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering AntimatterRifle...");
                        AntimatterRifle.register(this, weaponsGroup);
                        getLogger().info("AntimatterRifle registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register AntimatterRifle: " + e.getMessage());
                        e.printStackTrace();
                }

                try {
                        getLogger().info("Registering BossSpawnEgg...");
                        BossSpawnEgg.register(this, bossesGroup);
                        getLogger().info("BossSpawnEgg registered successfully!");
                } catch (Exception e) {
                        getLogger().severe("Failed to register BossSpawnEgg: " + e.getMessage());
                        e.printStackTrace();
                }

                // Register listeners
                getServer().getPluginManager().registerEvents(new SlimefunGuideListener(), this);
                getServer().getPluginManager().registerEvents(new RecipeViewerGUI(), this);
                getServer().getPluginManager().registerEvents(new ComponentsHandler(), this);
                getServer().getPluginManager().registerEvents(new MachineGunHandler(), this);
                getServer().getPluginManager().registerEvents(new TerminalClickHandler(), this);
                getServer().getPluginManager().registerEvents(new MilitaryCraftingHandler(), this);
                getServer().getPluginManager().registerEvents(new MachineFabricatorHandler(), this);
                getServer().getPluginManager().registerEvents(new AmmunitionWorkshopHandler(), this);
                getServer().getPluginManager().registerEvents(new UpgradeTableHandler(), this);
                getServer().getPluginManager().registerEvents(new BossAIHandler(this), this);
                getServer().getPluginManager().registerEvents(new MilitaryMobHandler(this), this);
                getServer().getPluginManager().registerEvents(new MilitaryCombatHandler(this), this);

                // Register commands

                WeaponsCommand weaponsCommand = new WeaponsCommand();
                getCommand("weapons").setExecutor(weaponsCommand);
                getCommand("weapons").setTabCompleter(weaponsCommand);

                getLogger().info("Military Arsenal addon enabled successfully!");
        }

        @Override
        public void onDisable() {
                AttackTurret.cleanupAllModels();
                getLogger().info("Military Arsenal addon disabled!");
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
                return null;
        }
}

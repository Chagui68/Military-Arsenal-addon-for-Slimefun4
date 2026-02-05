package com.Chagui68.weaponsaddon;

import com.Chagui68.weaponsaddon.handlers.ComponentsHandler;
import com.Chagui68.weaponsaddon.handlers.MachineGunHandler;
import com.Chagui68.weaponsaddon.items.AntimatterRifle;
import com.Chagui68.weaponsaddon.items.MachineGun;
import com.Chagui68.weaponsaddon.items.MachineGunAmmo;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.gui.RecipeViewerGUI;
import com.Chagui68.weaponsaddon.items.machines.*;
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

    @Override
    public void onEnable() {
        instance = this;

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

        SubItemGroup machinesGroup = new SubItemGroup(
                new NamespacedKey(this, "military_machines"),
                mainGroup,
                new CustomItemStack(Material.BLAST_FURNACE, "&4⚙ &cMilitary Machines"));

        mainGroup.register(this);
        componentsGroup.register(this);
        weaponsGroup.register(this);
        ammunitionGroup.register(this);
        machinesGroup.register(this);

        MilitaryComponents.register(this, componentsGroup);

        AmmunitionWorkshop.register(this, machinesGroup);
        MilitaryCraftingTable.register(this, machinesGroup);
        MilitaryMachineFabricator.register(this, machinesGroup);
        BombardmentTerminal.register(this, machinesGroup);
        AntimatterPedestal.register(this, machinesGroup);
        AntimatterRitual.register(this, machinesGroup);

        MachineGunAmmo.register(this, ammunitionGroup);

        MachineGun.register(this, weaponsGroup);
        AntimatterRifle.register(this, weaponsGroup);

        // Registrar listeners
        getServer().getPluginManager().registerEvents(new SlimefunGuideListener(), this);
        getServer().getPluginManager().registerEvents(new RecipeViewerGUI(), this);
        getServer().getPluginManager().registerEvents(new ComponentsHandler(), this);
        getServer().getPluginManager().registerEvents(new MachineGunHandler(), this);
        getServer().getPluginManager().registerEvents(new TerminalClickHandler(), this);
        getServer().getPluginManager().registerEvents(new MilitaryCraftingHandler(), this);
        getServer().getPluginManager().registerEvents(new MachineFabricatorHandler(), this);
        getServer().getPluginManager().registerEvents(new AmmunitionWorkshopHandler(), this);

        // Integración con Networks (si está disponible)
        // DESACTIVADO: Networks tiene un bug en esta versión que causa crashes
        // com.Chagui68.weaponsaddon.integrations.NetworksIntegration.register();

        getLogger().info("Military Arsenal addon enabled successfully!");
    }

    @Override
    public void onDisable() {
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

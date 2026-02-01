package com.Chagui68.weaponsaddon.items.machines;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

public class MilitaryMachines {
    public static void register(SlimefunAddon addon, ItemGroup category) {
        AmmunitionWorkshop.register(addon, category);
        MilitaryCraftingTable.register(addon, category);
        MilitaryMachineFabricator.register(addon, category);
        BombardmentTerminal.register(addon, category);
        BombardmentTerminalRecipeDisplay.register(addon, category);
        AntimatterPedestal.register(addon, category);
        AntimatterRitual.register(addon, category);
    }
}

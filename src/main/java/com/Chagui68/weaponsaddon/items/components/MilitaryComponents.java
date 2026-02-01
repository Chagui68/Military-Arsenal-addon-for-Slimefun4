package com.Chagui68.weaponsaddon.items.components;

import com.Chagui68.weaponsaddon.items.CustomRecipeItem;
import com.Chagui68.weaponsaddon.items.MilitaryRecipeTypes;
import com.Chagui68.weaponsaddon.items.machines.MilitaryCraftingHandler;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MilitaryComponents {

    public static final SlimefunItemStack BASIC_CIRCUIT = new SlimefunItemStack(
            "BASIC_CIRCUIT",
            Material.REDSTONE,
            "&7Basic Circuit",
            "",
            "&7Simple electronic circuit",
            "&7Foundation for all electronics",
            "",
            "&8⇨ Level 1"
    );

    public static final SlimefunItemStack MILITARY_CIRCUIT = new SlimefunItemStack(
            "MILITARY_CIRCUIT",
            Material.REDSTONE_BLOCK,
            "&cMilitary Circuit",
            "",
            "&7Advanced military-grade circuit",
            "&7Used in weapon systems",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 2"
    );

    public static final SlimefunItemStack REINFORCED_PLATING = new SlimefunItemStack(
            "REINFORCED_PLATING",
            Material.IRON_BLOCK,
            "&8Reinforced Plating",
            "",
            "&7Heavy-duty armor plating",
            "&7Provides structural integrity",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 2"
    );

    public static final SlimefunItemStack GUIDANCE_CHIP = new SlimefunItemStack(
            "GUIDANCE_CHIP",
            Material.PRISMARINE_SHARD,
            "&bGuidance Chip",
            "",
            "&7GPS guidance processor",
            "&7For precision targeting",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 3"
    );

    public static final SlimefunItemStack ADVANCED_PROCESSOR = new SlimefunItemStack(
            "ADVANCED_PROCESSOR",
            Material.QUARTZ,
            "&fAdvanced Processor",
            "",
            "&7High-speed computing core",
            "&7Enables complex calculations",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 3"
    );

    public static final SlimefunItemStack TARGETING_SYSTEM = new SlimefunItemStack(
            "TARGETING_SYSTEM",
            Material.COMPASS,
            "&6Targeting System",
            "",
            "&7Advanced aim assistance module",
            "&7Improves weapon accuracy",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 4"
    );

    public static final SlimefunItemStack RADAR_MODULE = new SlimefunItemStack(
            "RADAR_MODULE",
            Material.OBSERVER,
            "&aRadar Module",
            "",
            "&7Enemy detection system",
            "&7Scans surrounding area",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 4"
    );

    public static final SlimefunItemStack REINFORCED_FRAME = new SlimefunItemStack(
            "REINFORCED_FRAME",
            Material.NETHERITE_INGOT,
            "&8Reinforced Frame",
            "",
            "&7Military-grade structural frame",
            "&7Maximum durability",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 5"
    );

    public static final SlimefunItemStack POWER_CORE = new SlimefunItemStack(
            "POWER_CORE",
            Material.GLOWSTONE,
            "&ePower Core",
            "",
            "&7High-capacity energy cell",
            "&7Powers advanced systems",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 5"
    );

    public static final SlimefunItemStack WEAPON_BARREL = new SlimefunItemStack(
            "WEAPON_BARREL",
            Material.IRON_BARS,
            "&7Weapon Barrel",
            "",
            "&7Precision-machined barrel",
            "&7Improves projectile accuracy",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 6"
    );

    public static final SlimefunItemStack TRIGGER_MECHANISM = new SlimefunItemStack(
            "TRIGGER_MECHANISM",
            Material.TRIPWIRE_HOOK,
            "&6Trigger Mechanism",
            "",
            "&7Advanced firing system",
            "&7Rapid-fire capability",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 6"
    );

    public static final SlimefunItemStack QUANTUM_PROCESSOR = new SlimefunItemStack(
            "QUANTUM_PROCESSOR",
            Material.NETHER_STAR,
            "&dQuantum Processor",
            "",
            "&7Next-generation computing core",
            "&7Enables advanced weapon systems",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 7"
    );

    public static final SlimefunItemStack STABILIZER_UNIT = new SlimefunItemStack(
            "STABILIZER_UNIT",
            Material.END_ROD,
            "&5Stabilizer Unit",
            "",
            "&7Recoil compensation system",
            "&7Improves weapon stability",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 7"
    );

    public static final SlimefunItemStack EXPLOSIVE_CORE = new SlimefunItemStack(
            "EXPLOSIVE_CORE",
            Material.TNT,
            "&4Explosive Core",
            "",
            "&7Stabilized explosive payload",
            "&7Highly volatile",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 8"
    );

    public static final SlimefunItemStack ENERGY_MATRIX = new SlimefunItemStack(
            "ENERGY_MATRIX",
            Material.BEACON,
            "&bEnergy Matrix",
            "",
            "&7Advanced power distribution",
            "&7For high-energy systems",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 8"
    );

    public static final SlimefunItemStack HYDRAULIC_SYSTEM = new SlimefunItemStack(
            "HYDRAULIC_SYSTEM",
            Material.PISTON,
            "&9Hydraulic System",
            "",
            "&7Heavy-duty hydraulics",
            "&7For moving large components",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 9"
    );

    public static final SlimefunItemStack COOLANT_SYSTEM = new SlimefunItemStack(
            "COOLANT_SYSTEM",
            Material.CYAN_STAINED_GLASS,
            "&bCoolant System",
            "",
            "&7Temperature regulation",
            "&7Prevents overheating",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 9"
    );

    public static final SlimefunItemStack FUSION_REACTOR_CORE = new SlimefunItemStack(
            "FUSION_REACTOR_CORE",
            Material.RESPAWN_ANCHOR,
            "&4&lFusion Reactor Core",
            "",
            "&7Ultimate power source",
            "&7Generates massive energy",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 10"
    );

    public static final SlimefunItemStack ANTIMATTER_CELL = new SlimefunItemStack(
            "ANTIMATTER_CELL",
            Material.DRAGON_BREATH,
            "&5&lAntimatter Cell",
            "",
            "&7Contained antimatter",
            "&7Devastating explosive power",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 10"
    );

    public static final SlimefunItemStack ADVANCED_CIRCUIT = new SlimefunItemStack(
            "ADVANCED_CIRCUIT",
            Material.COMPARATOR,
            "&6Advanced Circuit",
            "",
            "&7High-performance circuit board",
            "&7For advanced military systems",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 4"
    );

    public static final SlimefunItemStack MILITARY_GRADE_CIRCUIT = new SlimefunItemStack(
            "MILITARY_GRADE_CIRCUIT",
            Material.REPEATER,
            "&c&lMilitary Grade Circuit",
            "",
            "&7Top-tier military electronics",
            "&7Maximum processing power",
            "",
            "&c⚠ Requires 4×4 Military Crafting Table",
            "",
            "&e⇨ SHIFT + RIGHT-CLICK in Guide",
            "&e  to view FULL 4×4 recipe",
            "",
            "&8⇨ Level 8"
    );


    public static void register(SlimefunAddon addon, ItemGroup category) {

        new SlimefunItem(category, BASIC_CIRCUIT, MilitaryRecipeTypes.AMMUNITION_WORKSHOP, new ItemStack[]{
                new ItemStack(Material.COPPER_INGOT), new ItemStack(Material.REDSTONE), new ItemStack(Material.COPPER_INGOT),
                new ItemStack(Material.REDSTONE), SlimefunItems.COPPER_WIRE, new ItemStack(Material.REDSTONE),
                new ItemStack(Material.COPPER_INGOT), new ItemStack(Material.REDSTONE), new ItemStack(Material.COPPER_INGOT)
        }).register(addon);

        ItemStack[] militaryCircuitRecipe = new ItemStack[]{
                new ItemStack(Material.REDSTONE_BLOCK), new ItemStack(Material.GOLD_BLOCK), new ItemStack(Material.GOLD_BLOCK), new ItemStack(Material.REDSTONE_BLOCK),
                new ItemStack(Material.GOLD_BLOCK), BASIC_CIRCUIT, BASIC_CIRCUIT, new ItemStack(Material.GOLD_BLOCK),
                new ItemStack(Material.GOLD_BLOCK), SlimefunItems.COPPER_WIRE, SlimefunItems.COPPER_WIRE, new ItemStack(Material.GOLD_BLOCK),
                new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_INGOT, new ItemStack(Material.REDSTONE_BLOCK)
        };
        CustomRecipeItem militaryCircuit = new CustomRecipeItem(
                category,
                MILITARY_CIRCUIT,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                militaryCircuitRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        militaryCircuit.register(addon);
        MilitaryCraftingHandler.registerRecipe(militaryCircuit);

        ItemStack[] reinforcedPlatingRecipe = new ItemStack[]{
                SlimefunItems.STEEL_PLATE, new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK), SlimefunItems.STEEL_PLATE,
                new ItemStack(Material.IRON_BLOCK), SlimefunItems.STEEL_PLATE, SlimefunItems.STEEL_PLATE, new ItemStack(Material.IRON_BLOCK),
                new ItemStack(Material.IRON_BLOCK), SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, new ItemStack(Material.IRON_BLOCK),
                SlimefunItems.STEEL_PLATE, new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK), SlimefunItems.STEEL_PLATE
        };
        CustomRecipeItem reinforcedPlating = new CustomRecipeItem(
                category,
                REINFORCED_PLATING,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                reinforcedPlatingRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        reinforcedPlating.register(addon);
        MilitaryCraftingHandler.registerRecipe(reinforcedPlating);

        ItemStack[] guidanceChipRecipe = new ItemStack[]{
                new ItemStack(Material.QUARTZ_BLOCK), MILITARY_CIRCUIT, MILITARY_CIRCUIT, new ItemStack(Material.QUARTZ_BLOCK),
                MILITARY_CIRCUIT, SlimefunItems.GPS_TRANSMITTER, SlimefunItems.GPS_TRANSMITTER, MILITARY_CIRCUIT,
                MILITARY_CIRCUIT, SlimefunItems.PROGRAMMABLE_ANDROID, SlimefunItems.PROGRAMMABLE_ANDROID, MILITARY_CIRCUIT,
                new ItemStack(Material.QUARTZ_BLOCK), MILITARY_CIRCUIT, MILITARY_CIRCUIT, new ItemStack(Material.QUARTZ_BLOCK)
        };
        CustomRecipeItem guidanceChip = new CustomRecipeItem(
                category,
                GUIDANCE_CHIP,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                guidanceChipRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        guidanceChip.register(addon);
        MilitaryCraftingHandler.registerRecipe(guidanceChip);

        ItemStack[] advancedProcessorRecipe = new ItemStack[]{
                new ItemStack(Material.QUARTZ), MILITARY_CIRCUIT, MILITARY_CIRCUIT, new ItemStack(Material.QUARTZ),
                MILITARY_CIRCUIT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.ADVANCED_CIRCUIT_BOARD, MILITARY_CIRCUIT,
                MILITARY_CIRCUIT, SlimefunItems.COOLING_UNIT, SlimefunItems.COOLING_UNIT, MILITARY_CIRCUIT,
                new ItemStack(Material.QUARTZ), MILITARY_CIRCUIT, MILITARY_CIRCUIT, new ItemStack(Material.QUARTZ)
        };
        CustomRecipeItem advancedProcessor = new CustomRecipeItem(
                category,
                ADVANCED_PROCESSOR,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                advancedProcessorRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        advancedProcessor.register(addon);
        MilitaryCraftingHandler.registerRecipe(advancedProcessor);

        ItemStack[] targetingSystemRecipe = new ItemStack[]{
                new ItemStack(Material.DIAMOND_BLOCK), GUIDANCE_CHIP, GUIDANCE_CHIP, new ItemStack(Material.DIAMOND_BLOCK),
                GUIDANCE_CHIP, SlimefunItems.GPS_CONTROL_PANEL, ADVANCED_PROCESSOR, GUIDANCE_CHIP,
                GUIDANCE_CHIP, new ItemStack(Material.OBSERVER), new ItemStack(Material.OBSERVER), GUIDANCE_CHIP,
                new ItemStack(Material.DIAMOND_BLOCK), MILITARY_CIRCUIT, MILITARY_CIRCUIT, new ItemStack(Material.DIAMOND_BLOCK)
        };
        CustomRecipeItem targetingSystem = new CustomRecipeItem(
                category,
                TARGETING_SYSTEM,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                targetingSystemRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        targetingSystem.register(addon);
        MilitaryCraftingHandler.registerRecipe(targetingSystem);

        ItemStack[] radarModuleRecipe = new ItemStack[]{
                new ItemStack(Material.OBSERVER), GUIDANCE_CHIP, GUIDANCE_CHIP, new ItemStack(Material.OBSERVER),
                GUIDANCE_CHIP, SlimefunItems.GPS_MARKER_TOOL, ADVANCED_PROCESSOR, GUIDANCE_CHIP,
                GUIDANCE_CHIP, ADVANCED_PROCESSOR, ADVANCED_PROCESSOR, GUIDANCE_CHIP,
                new ItemStack(Material.OBSERVER), MILITARY_CIRCUIT, MILITARY_CIRCUIT, new ItemStack(Material.OBSERVER)
        };
        CustomRecipeItem radarModule = new CustomRecipeItem(
                category,
                RADAR_MODULE,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                radarModuleRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        radarModule.register(addon);
        MilitaryCraftingHandler.registerRecipe(radarModule);

        ItemStack[] reinforcedFrameRecipe = new ItemStack[]{
                REINFORCED_PLATING, new ItemStack(Material.NETHERITE_INGOT), new ItemStack(Material.NETHERITE_INGOT), REINFORCED_PLATING,
                new ItemStack(Material.NETHERITE_INGOT), SlimefunItems.REINFORCED_PLATE, SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.NETHERITE_INGOT),
                new ItemStack(Material.NETHERITE_INGOT), SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, new ItemStack(Material.NETHERITE_INGOT),
                REINFORCED_PLATING, new ItemStack(Material.NETHERITE_INGOT), new ItemStack(Material.NETHERITE_INGOT), REINFORCED_PLATING
        };
        CustomRecipeItem reinforcedFrame = new CustomRecipeItem(
                category,
                REINFORCED_FRAME,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                reinforcedFrameRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        reinforcedFrame.register(addon);
        MilitaryCraftingHandler.registerRecipe(reinforcedFrame);

        ItemStack[] powerCoreRecipe = new ItemStack[]{
                new ItemStack(Material.GLOWSTONE), MILITARY_CIRCUIT, MILITARY_CIRCUIT, new ItemStack(Material.GLOWSTONE),
                MILITARY_CIRCUIT, SlimefunItems.POWER_CRYSTAL, SlimefunItems.ENERGIZED_CAPACITOR, MILITARY_CIRCUIT,
                MILITARY_CIRCUIT, SlimefunItems.ENERGIZED_CAPACITOR, SlimefunItems.POWER_CRYSTAL, MILITARY_CIRCUIT,
                new ItemStack(Material.GLOWSTONE), MILITARY_CIRCUIT, MILITARY_CIRCUIT, new ItemStack(Material.GLOWSTONE)
        };
        CustomRecipeItem powerCore = new CustomRecipeItem(
                category,
                POWER_CORE,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                powerCoreRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        powerCore.register(addon);
        MilitaryCraftingHandler.registerRecipe(powerCore);

        ItemStack[] weaponBarrelRecipe = new ItemStack[]{
                REINFORCED_FRAME, new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK), REINFORCED_FRAME,
                new ItemStack(Material.IRON_BLOCK), SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, new ItemStack(Material.IRON_BLOCK),
                new ItemStack(Material.IRON_BLOCK), REINFORCED_PLATING, REINFORCED_PLATING, new ItemStack(Material.IRON_BLOCK),
                REINFORCED_FRAME, new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_BLOCK), REINFORCED_FRAME
        };
        CustomRecipeItem weaponBarrel = new CustomRecipeItem(
                category,
                WEAPON_BARREL,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                weaponBarrelRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        weaponBarrel.register(addon);
        MilitaryCraftingHandler.registerRecipe(weaponBarrel);

        ItemStack[] triggerMechanismRecipe = new ItemStack[]{
                TARGETING_SYSTEM, MILITARY_CIRCUIT, MILITARY_CIRCUIT, TARGETING_SYSTEM,
                MILITARY_CIRCUIT, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.HEATING_COIL, MILITARY_CIRCUIT,
                MILITARY_CIRCUIT, ADVANCED_PROCESSOR, ADVANCED_PROCESSOR, MILITARY_CIRCUIT,
                new ItemStack(Material.TRIPWIRE_HOOK), MILITARY_CIRCUIT, MILITARY_CIRCUIT, new ItemStack(Material.TRIPWIRE_HOOK)
        };
        CustomRecipeItem triggerMechanism = new CustomRecipeItem(
                category,
                TRIGGER_MECHANISM,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                triggerMechanismRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        triggerMechanism.register(addon);
        MilitaryCraftingHandler.registerRecipe(triggerMechanism);

        ItemStack[] quantumProcessorRecipe = new ItemStack[]{
                new ItemStack(Material.NETHER_STAR), ADVANCED_PROCESSOR, ADVANCED_PROCESSOR, new ItemStack(Material.NETHER_STAR),
                ADVANCED_PROCESSOR, SlimefunItems.ANDROID_MEMORY_CORE, POWER_CORE, ADVANCED_PROCESSOR,
                ADVANCED_PROCESSOR, POWER_CORE, SlimefunItems.ANDROID_MEMORY_CORE, ADVANCED_PROCESSOR,
                new ItemStack(Material.NETHER_STAR), GUIDANCE_CHIP, GUIDANCE_CHIP, new ItemStack(Material.NETHER_STAR)
        };
        CustomRecipeItem quantumProcessor = new CustomRecipeItem(
                category,
                QUANTUM_PROCESSOR,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                quantumProcessorRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        quantumProcessor.register(addon);
        MilitaryCraftingHandler.registerRecipe(quantumProcessor);

        ItemStack[] stabilizerUnitRecipe = new ItemStack[]{
                REINFORCED_FRAME, POWER_CORE, POWER_CORE, REINFORCED_FRAME,
                POWER_CORE, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRIC_MOTOR, POWER_CORE,
                POWER_CORE, ADVANCED_PROCESSOR, ADVANCED_PROCESSOR, POWER_CORE,
                REINFORCED_FRAME, MILITARY_CIRCUIT, MILITARY_CIRCUIT, REINFORCED_FRAME
        };
        CustomRecipeItem stabilizerUnit = new CustomRecipeItem(
                category,
                STABILIZER_UNIT,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                stabilizerUnitRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        stabilizerUnit.register(addon);
        MilitaryCraftingHandler.registerRecipe(stabilizerUnit);

        ItemStack[] explosiveCoreRecipe = new ItemStack[]{
                new ItemStack(Material.TNT), QUANTUM_PROCESSOR, QUANTUM_PROCESSOR, new ItemStack(Material.TNT),
                QUANTUM_PROCESSOR, SlimefunItems.EXPLOSIVE_BOW, POWER_CORE, QUANTUM_PROCESSOR,
                QUANTUM_PROCESSOR, POWER_CORE, TARGETING_SYSTEM, QUANTUM_PROCESSOR,
                new ItemStack(Material.TNT), REINFORCED_FRAME, REINFORCED_FRAME, new ItemStack(Material.TNT)
        };
        CustomRecipeItem explosiveCore = new CustomRecipeItem(
                category,
                EXPLOSIVE_CORE,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                explosiveCoreRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        explosiveCore.register(addon);
        MilitaryCraftingHandler.registerRecipe(explosiveCore);

        ItemStack[] energyMatrixRecipe = new ItemStack[]{
                POWER_CORE, QUANTUM_PROCESSOR, QUANTUM_PROCESSOR, POWER_CORE,
                QUANTUM_PROCESSOR, SlimefunItems.ENERGIZED_CAPACITOR, SlimefunItems.ENERGIZED_CAPACITOR, QUANTUM_PROCESSOR,
                QUANTUM_PROCESSOR, SlimefunItems.POWER_CRYSTAL, SlimefunItems.POWER_CRYSTAL, QUANTUM_PROCESSOR,
                POWER_CORE, new ItemStack(Material.BEACON), new ItemStack(Material.BEACON), POWER_CORE
        };
        CustomRecipeItem energyMatrix = new CustomRecipeItem(
                category,
                ENERGY_MATRIX,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                energyMatrixRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        energyMatrix.register(addon);
        MilitaryCraftingHandler.registerRecipe(energyMatrix);

        ItemStack[] hydraulicSystemRecipe = new ItemStack[]{
                REINFORCED_FRAME, POWER_CORE, POWER_CORE, REINFORCED_FRAME,
                POWER_CORE, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRIC_MOTOR, POWER_CORE,
                POWER_CORE, new ItemStack(Material.PISTON), new ItemStack(Material.PISTON), POWER_CORE,
                REINFORCED_FRAME, QUANTUM_PROCESSOR, QUANTUM_PROCESSOR, REINFORCED_FRAME
        };
        CustomRecipeItem hydraulicSystem = new CustomRecipeItem(
                category,
                HYDRAULIC_SYSTEM,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                hydraulicSystemRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        hydraulicSystem.register(addon);
        MilitaryCraftingHandler.registerRecipe(hydraulicSystem);

        ItemStack[] coolantSystemRecipe = new ItemStack[]{
                new ItemStack(Material.PACKED_ICE), POWER_CORE, POWER_CORE, new ItemStack(Material.PACKED_ICE),
                POWER_CORE, SlimefunItems.COOLING_UNIT, SlimefunItems.COOLING_UNIT, POWER_CORE,
                POWER_CORE, ADVANCED_PROCESSOR, ADVANCED_PROCESSOR, POWER_CORE,
                new ItemStack(Material.PACKED_ICE), QUANTUM_PROCESSOR, QUANTUM_PROCESSOR, new ItemStack(Material.PACKED_ICE)
        };
        CustomRecipeItem coolantSystem = new CustomRecipeItem(
                category,
                COOLANT_SYSTEM,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                coolantSystemRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        coolantSystem.register(addon);
        MilitaryCraftingHandler.registerRecipe(coolantSystem);

        ItemStack[] fusionReactorCoreRecipe = new ItemStack[]{
                ENERGY_MATRIX, QUANTUM_PROCESSOR, QUANTUM_PROCESSOR, ENERGY_MATRIX,
                QUANTUM_PROCESSOR, SlimefunItems.NUCLEAR_REACTOR, SlimefunItems.NETHER_STAR_REACTOR, QUANTUM_PROCESSOR,
                QUANTUM_PROCESSOR, SlimefunItems.NETHER_STAR_REACTOR, SlimefunItems.NUCLEAR_REACTOR, QUANTUM_PROCESSOR,
                ENERGY_MATRIX, new ItemStack(Material.RESPAWN_ANCHOR), new ItemStack(Material.RESPAWN_ANCHOR), ENERGY_MATRIX
        };
        CustomRecipeItem fusionReactorCore = new CustomRecipeItem(
                category,
                FUSION_REACTOR_CORE,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                fusionReactorCoreRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        fusionReactorCore.register(addon);
        MilitaryCraftingHandler.registerRecipe(fusionReactorCore);

        ItemStack[] antimatterCellRecipe = new ItemStack[]{
                EXPLOSIVE_CORE, QUANTUM_PROCESSOR, QUANTUM_PROCESSOR, EXPLOSIVE_CORE,
                QUANTUM_PROCESSOR, SlimefunItems.CARBONADO, SlimefunItems.CARBONADO, QUANTUM_PROCESSOR,
                QUANTUM_PROCESSOR, new ItemStack(Material.DRAGON_BREATH), new ItemStack(Material.DRAGON_BREATH), QUANTUM_PROCESSOR,
                EXPLOSIVE_CORE, ENERGY_MATRIX, ENERGY_MATRIX, EXPLOSIVE_CORE
        };
        CustomRecipeItem antimatterCell = new CustomRecipeItem(
                category,
                ANTIMATTER_CELL,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                antimatterCellRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        antimatterCell.register(addon);
        MilitaryCraftingHandler.registerRecipe(antimatterCell);


        ItemStack[] advancedCircuitRecipe = new ItemStack[]{
                MILITARY_CIRCUIT, ADVANCED_PROCESSOR, ADVANCED_PROCESSOR, MILITARY_CIRCUIT,
                ADVANCED_PROCESSOR, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.BLISTERING_INGOT, ADVANCED_PROCESSOR,
                ADVANCED_PROCESSOR, SlimefunItems.BLISTERING_INGOT, SlimefunItems.ADVANCED_CIRCUIT_BOARD, ADVANCED_PROCESSOR,
                MILITARY_CIRCUIT, ADVANCED_PROCESSOR, ADVANCED_PROCESSOR, MILITARY_CIRCUIT
        };
        CustomRecipeItem advancedCircuit = new CustomRecipeItem(
                category,
                ADVANCED_CIRCUIT,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                advancedCircuitRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        advancedCircuit.register(addon);
        MilitaryCraftingHandler.registerRecipe(advancedCircuit);

        ItemStack[] militaryGradeCircuitRecipe = new ItemStack[]{
                QUANTUM_PROCESSOR, ADVANCED_CIRCUIT, ADVANCED_CIRCUIT, QUANTUM_PROCESSOR,
                ADVANCED_CIRCUIT, ENERGY_MATRIX, POWER_CORE, ADVANCED_CIRCUIT,
                ADVANCED_CIRCUIT, POWER_CORE, ENERGY_MATRIX, ADVANCED_CIRCUIT,
                QUANTUM_PROCESSOR, ADVANCED_CIRCUIT, ADVANCED_CIRCUIT, QUANTUM_PROCESSOR
        };
        CustomRecipeItem militaryGradeCircuit = new CustomRecipeItem(
                category,
                MILITARY_GRADE_CIRCUIT,
                MilitaryRecipeTypes.MILITARY_CRAFTING_TABLE,
                militaryGradeCircuitRecipe,
                CustomRecipeItem.RecipeGridSize.GRID_4x4
        );
        militaryGradeCircuit.register(addon);
        MilitaryCraftingHandler.registerRecipe(militaryGradeCircuit);

    }
}

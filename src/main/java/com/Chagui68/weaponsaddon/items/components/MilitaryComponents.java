package com.Chagui68.weaponsaddon.items.components;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import jdk.jfr.Category;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MilitaryComponents {

        // ========== TIER 1: BASIC COMPONENTS (Base para todo) ==========
        public static final SlimefunItemStack BASIC_CIRCUIT = new SlimefunItemStack(
                        "BASIC_CIRCUIT",
                        Material.REDSTONE_TORCH,
                        "&6⚡ &eBasic Circuit",
                        "",
                        "&7Foundation for all electronics",
                        "&7Used in basic machinery",
                        "",
                        "&8⇨ Tier 1 Component");

        public static final SlimefunItemStack STEEL_FRAME = new SlimefunItemStack(
                        "STEEL_FRAME",
                        Material.IRON_BARS,
                        "&7⬚ &fSteel Frame",
                        "",
                        "&7Basic structural component",
                        "&7Provides moderate strength",
                        "",
                        "&8⇨ Tier 1 Component");

        public static final SlimefunItemStack COPPER_COIL = new SlimefunItemStack(
                        "COPPER_COIL",
                        Material.LIGHTNING_ROD,
                        "&6◉ &eCopper Coil",
                        "",
                        "&7Electromagnetic component",
                        "&7For energy systems",
                        "",
                        "&8⇨ Tier 1 Component");

        public static final SlimefunItemStack MECHANICAL_PARTS = new SlimefunItemStack(
                        "MECHANICAL_PARTS",
                        Material.REPEATER,
                        "&7⚙ &fMechanical Parts",
                        "",
                        "&7Basic gears and springs",
                        "&7For simple mechanisms",
                        "",
                        "&8⇨ Tier 1 Component");

        // ========== TIER 2: INTERMEDIATE COMPONENTS ==========
        public static final SlimefunItemStack ADVANCED_CIRCUIT = new SlimefunItemStack(
                        "ADVANCED_CIRCUIT",
                        Material.COMPARATOR,
                        "&6⚡ &eAdvanced Circuit",
                        "",
                        "&7Enhanced electronic board",
                        "&7Handles complex operations",
                        "",
                        "&8⇨ Tier 2 Component");

        public static final SlimefunItemStack REINFORCED_PLATING = new SlimefunItemStack(
                        "REINFORCED_PLATING",
                        Material.NETHERITE_SCRAP,
                        "&8▩ &7Reinforced Plating",
                        "",
                        "&7Heavy-duty armor plates",
                        "&7Maximum protection",
                        "",
                        "&8⇨ Tier 2 Component");

        public static final SlimefunItemStack SERVO_MOTOR = new SlimefunItemStack(
                        "SERVO_MOTOR",
                        Material.PISTON,
                        "&b⚙ &3Servo Motor",
                        "",
                        "&7Precision motor unit",
                        "&7For controlled movement",
                        "",
                        "&8⇨ Tier 2 Component");

        public static final SlimefunItemStack POWER_CELL = new SlimefunItemStack(
                        "POWER_CELL",
                        Material.CRYING_OBSIDIAN,
                        "&e⚡ &6Power Cell",
                        "",
                        "&7Compact energy storage",
                        "&7Stores electrical power",
                        "",
                        "&8⇨ Tier 2 Component");

        // ========== TIER 3: SPECIALIZED WEAPON COMPONENTS ==========
        public static final SlimefunItemStack WEAPON_BARREL = new SlimefunItemStack(
                        "WEAPON_BARREL",
                        Material.CHAIN,
                        "&c▬ &4Weapon Barrel",
                        "",
                        "&7Precision rifled barrel",
                        "&7For projectile weapons",
                        "",
                        "&8⇨ Tier 3 Weapon Part");

        public static final SlimefunItemStack TRIGGER_MECHANISM = new SlimefunItemStack(
                        "TRIGGER_MECHANISM",
                        Material.TRIPWIRE_HOOK,
                        "&e⚲ &6Trigger Mechanism",
                        "",
                        "&7Hair-trigger firing system",
                        "&7Ultra-responsive activation",
                        "",
                        "&8⇨ Tier 3 Weapon Part");

        public static final SlimefunItemStack STABILIZER_UNIT = new SlimefunItemStack(
                        "STABILIZER_UNIT",
                        Material.DIAMOND,
                        "&b◎ &3Stabilizer Unit",
                        "",
                        "&7Gyroscopic stabilization",
                        "&7Reduces weapon recoil",
                        "",
                        "&8⇨ Tier 3 Weapon Part");

        public static final SlimefunItemStack TARGETING_SYSTEM = new SlimefunItemStack(
                        "TARGETING_SYSTEM",
                        Material.OBSERVER,
                        "&c◉ &4Targeting System",
                        "",
                        "&7Advanced tracking optics",
                        "&7Auto-aim technology",
                        "",
                        "&8⇨ Tier 3 Weapon Part");

        public static final SlimefunItemStack RADAR_MODULE = new SlimefunItemStack(
                        "RADAR_MODULE",
                        Material.SCULK_SENSOR,
                        "&a⦿ &2Radar Module",
                        "",
                        "&7Enemy detection system",
                        "&7Scans surroundings",
                        "",
                        "&8⇨ Tier 3 Weapon Part");

        // ========== TIER 3: ADVANCED MACHINERY COMPONENTS ==========
        public static final SlimefunItemStack REINFORCED_FRAME = new SlimefunItemStack(
                        "REINFORCED_FRAME",
                        Material.LODESTONE,
                        "&8▦ &7Reinforced Frame",
                        "",
                        "&7Ultra-strong chassis",
                        "&7For heavy machinery",
                        "",
                        "&8⇨ Tier 3 Machine Part");

        public static final SlimefunItemStack POWER_CORE = new SlimefunItemStack(
                        "POWER_CORE",
                        Material.BEACON,
                        "&e⬢ &6Power Core",
                        "",
                        "&7High-capacity reactor",
                        "&7Infinite power output",
                        "",
                        "&8⇨ Tier 3 Machine Part");

        public static final SlimefunItemStack MILITARY_CIRCUIT = new SlimefunItemStack(
                        "MILITARY_CIRCUIT",
                        Material.CALIBRATED_SCULK_SENSOR,
                        "&c⚡ &4Military Circuit",
                        "",
                        "&7Military-grade processor",
                        "&7Combat system control",
                        "",
                        "&8⇨ Tier 3 Machine Part");

        public static final SlimefunItemStack HYDRAULIC_SYSTEM = new SlimefunItemStack(
                        "HYDRAULIC_SYSTEM",
                        Material.DISPENSER,
                        "&9⚒ &1Hydraulic System",
                        "",
                        "&7High-pressure actuators",
                        "&7Extreme force output",
                        "",
                        "&8⇨ Tier 3 Machine Part");

        public static final SlimefunItemStack COOLANT_SYSTEM = new SlimefunItemStack(
                        "COOLANT_SYSTEM",
                        Material.CAULDRON,
                        "&b◈ &3Coolant System",
                        "",
                        "&7Liquid nitrogen cooling",
                        "&7Prevents overheating",
                        "",
                        "&8⇨ Tier 3 Machine Part");

        // ========== TIER 4: ULTIMATE COMPONENTS ==========
        public static final SlimefunItemStack QUANTUM_PROCESSOR = new SlimefunItemStack(
                        "QUANTUM_PROCESSOR",
                        Material.END_CRYSTAL,
                        "&d⬡ &5Quantum Processor",
                        "",
                        "&7Quantum computing core",
                        "&7Infinite calculations",
                        "",
                        "&8⇨ Tier 4 Ultimate");

        // ========== UPGRADE MODULES ==========
        public static final SlimefunItemStack DAMAGE_MODULE_I = new SlimefunItemStack(
                        "DAMAGE_MODULE_I",
                        Material.REDSTONE_LAMP,
                        "&c⚔ &4Damage Upgrade Module I",
                        "",
                        "&7Increases weapon attack damage",
                        "&7Bonus: &c+2 Damage",
                        "",
                        "&eUsed in Weapon Upgrade Table");

        public static final SlimefunItemStack SPEED_MODULE_I = new SlimefunItemStack(
                        "SPEED_MODULE_I",
                        Material.SUGAR,
                        "&b⚡ &3Speed Upgrade Module I",
                        "",
                        "&7Increases weapon attack speed",
                        "&7Bonus: &b+0.2 Speed",
                        "",
                        "&eUsed in Weapon Upgrade Table");

        public static final SlimefunItemStack ENERGY_MATRIX = new SlimefunItemStack(
                        "ENERGY_MATRIX",
                        Material.CONDUIT,
                        "&e⬢ &6Energy Matrix",
                        "",
                        "&7Dimensional energy storage",
                        "&7Stores unlimited power",
                        "",
                        "&8⇨ Tier 4 Ultimate");

        public static final SlimefunItemStack EXPLOSIVE_CORE = new SlimefunItemStack(
                        "EXPLOSIVE_CORE",
                        Material.TNT,
                        "&c☢ &4Explosive Core",
                        "",
                        "&7Concentrated TNT charge",
                        "&7Maximum destruction",
                        "",
                        "&8⇨ Tier 4 Ultimate");

        // ========== TIER 5: ANTIMATTER COMPONENTS (EXTREMADAMENTE COSTOSOS) ==========
        public static final SlimefunItemStack ANTIMATTER_PARTICLE = new SlimefunItemStack(
                        "ANTIMATTER_PARTICLE",
                        Material.AMETHYST_SHARD,
                        "&5◆ &dAntimatter Particle",
                        "",
                        "&7Unstable subatomic particles",
                        "&7Annihilates on contact with matter",
                        "",
                        "&c⚠ EXTREME DANGER",
                        "&8⇨ Tier 5 Antimatter");

        public static final SlimefunItemStack ANTIMATTER_CRYSTAL = new SlimefunItemStack(
                        "ANTIMATTER_CRYSTAL",
                        Material.DRAGON_BREATH,
                        "&5⬢ &dAntimatter Crystal",
                        "",
                        "&7Crystallized antimatter",
                        "&7Stabilized in quantum field",
                        "",
                        "&c⚠ Contains massive energy",
                        "&8⇨ Tier 5 Antimatter");

        public static final SlimefunItemStack CONTAINMENT_FIELD_GENERATOR = new SlimefunItemStack(
                        "CONTAINMENT_FIELD_GENERATOR",
                        Material.RESPAWN_ANCHOR,
                        "&b⬡ &3Containment Field Generator",
                        "",
                        "&7Quantum field projector",
                        "&7Prevents antimatter decay",
                        "",
                        "&6Power: &eInfinite",
                        "&8⇨ Tier 5 Antimatter");

        public static final SlimefunItemStack DIMENSIONAL_STABILIZER = new SlimefunItemStack(
                        "DIMENSIONAL_STABILIZER",
                        Material.LODESTONE,
                        "&d▦ &5Dimensional Stabilizer",
                        "",
                        "&7Anchors matter in spacetime",
                        "&7Prevents dimensional collapse",
                        "",
                        "&6Stability: &eMaximum",
                        "&8⇨ Tier 5 Antimatter");

        public static final SlimefunItemStack VOID_CORE_MACHINE = new SlimefunItemStack(
                        "VOID_CORE_MACHINE",
                        Material.BEACON,
                        "&1∞ &1Void Core Machine",
                        "",
                        "&7Extracts pure nothingness",
                        "&7Converts void into a powerful weapon",
                        "",
                        "&c⚠ EXTREME DANGER",
                        "",
                        "&8⇨ Tier 5 Antimatter"

        );

        public static final SlimefunItemStack VOID_ESSENCE = new SlimefunItemStack(
                        "VOID_ESSENCE",
                        Material.ECHO_SHARD,
                        "&0◉ &8Void Essence",
                        "",
                        "&7Pure nothingness extracted",
                        "&7From the void dimension",
                        "",
                        "&c⚠ Handle with extreme care",
                        "&8⇨ Tier 5 Antimatter");

        public static final SlimefunItemStack FIREARM_BARREL = new SlimefunItemStack(
          "FIREARM_BARREL",
                Material.FLINT_AND_STEEL,
                "&c🔥 Firearm Barrel",
                "&7Reinforced steel barrel",
                "&7Designed to withstand high pressures",
                ""
        );

        public static void register(SlimefunAddon addon, ItemGroup category, ItemGroup upgradesCategory) {

                new SlimefunItem(category, BASIC_CIRCUIT, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                new ItemStack(Material.REDSTONE), new ItemStack(Material.COPPER_INGOT),
                                new ItemStack(Material.REDSTONE),
                                new ItemStack(Material.COPPER_INGOT), new ItemStack(Material.GOLD_INGOT),
                                new ItemStack(Material.COPPER_INGOT),
                                new ItemStack(Material.REDSTONE), new ItemStack(Material.COPPER_INGOT),
                                new ItemStack(Material.REDSTONE)
                }).register(addon);

                new SlimefunItem(category, STEEL_FRAME, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                SlimefunItems.STEEL_INGOT, new ItemStack(Material.IRON_BLOCK),
                                SlimefunItems.STEEL_INGOT,
                                new ItemStack(Material.IRON_BLOCK), SlimefunItems.HARDENED_METAL_INGOT,
                                new ItemStack(Material.IRON_BLOCK),
                                SlimefunItems.STEEL_INGOT, new ItemStack(Material.IRON_BLOCK), SlimefunItems.STEEL_INGOT
                }).register(addon);

                new SlimefunItem(category, COPPER_COIL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                new ItemStack(Material.COPPER_INGOT), new ItemStack(Material.COPPER_INGOT),
                                new ItemStack(Material.COPPER_INGOT),
                                new ItemStack(Material.COPPER_INGOT), new ItemStack(Material.REDSTONE_BLOCK),
                                new ItemStack(Material.COPPER_INGOT),
                                new ItemStack(Material.COPPER_INGOT), new ItemStack(Material.COPPER_INGOT),
                                new ItemStack(Material.COPPER_INGOT)
                }).register(addon);

                new SlimefunItem(category, MECHANICAL_PARTS, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                new ItemStack(Material.IRON_NUGGET), SlimefunItems.STEEL_INGOT,
                                new ItemStack(Material.IRON_NUGGET),
                                SlimefunItems.STEEL_INGOT, new ItemStack(Material.CLOCK), SlimefunItems.STEEL_INGOT,
                                new ItemStack(Material.IRON_NUGGET), SlimefunItems.STEEL_INGOT,
                                new ItemStack(Material.IRON_NUGGET)
                }).register(addon);

                new SlimefunItem(category, ADVANCED_CIRCUIT, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                BASIC_CIRCUIT, SlimefunItems.ELECTRO_MAGNET, BASIC_CIRCUIT,
                                SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.CARBONADO,
                                SlimefunItems.SYNTHETIC_EMERALD,
                                BASIC_CIRCUIT, SlimefunItems.ELECTRIC_MOTOR, BASIC_CIRCUIT
                }).register(addon);

                new SlimefunItem(category, REINFORCED_PLATING, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HARDENED_METAL_INGOT,
                                SlimefunItems.REINFORCED_ALLOY_INGOT,
                                STEEL_FRAME, new ItemStack(Material.NETHERITE_INGOT), STEEL_FRAME,
                                SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HARDENED_METAL_INGOT,
                                SlimefunItems.DAMASCUS_STEEL_INGOT
                }).register(addon);

                new SlimefunItem(category, SERVO_MOTOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                COPPER_COIL, BASIC_CIRCUIT, COPPER_COIL,
                                MECHANICAL_PARTS, SlimefunItems.ELECTRIC_MOTOR, MECHANICAL_PARTS,
                                SlimefunItems.REDSTONE_ALLOY, MECHANICAL_PARTS, SlimefunItems.REDSTONE_ALLOY
                }).register(addon);

                new SlimefunItem(category, POWER_CELL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                SlimefunItems.BATTERY, COPPER_COIL, SlimefunItems.BATTERY,
                                ADVANCED_CIRCUIT, new ItemStack(Material.REDSTONE_BLOCK), ADVANCED_CIRCUIT,
                                SlimefunItems.SOLAR_PANEL, COPPER_COIL, SlimefunItems.SOLAR_PANEL
                }).register(addon);

                new SlimefunItem(category, WEAPON_BARREL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.HARDENED_METAL_INGOT,
                                SlimefunItems.DAMASCUS_STEEL_INGOT,
                                REINFORCED_PLATING, new ItemStack(Material.LIGHTNING_ROD), REINFORCED_PLATING,
                                SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.HARDENED_METAL_INGOT,
                                SlimefunItems.REINFORCED_ALLOY_INGOT
                }).register(addon);

                new SlimefunItem(category, TRIGGER_MECHANISM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                MECHANICAL_PARTS, ADVANCED_CIRCUIT, MECHANICAL_PARTS,
                                SERVO_MOTOR, new ItemStack(Material.TRIPWIRE_HOOK), SERVO_MOTOR,
                                SlimefunItems.GOLD_24K, MECHANICAL_PARTS, SlimefunItems.GOLD_24K
                }).register(addon);

                new SlimefunItem(category, STABILIZER_UNIT, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                SERVO_MOTOR, ADVANCED_CIRCUIT, SERVO_MOTOR,
                                POWER_CELL, new ItemStack(Material.DIAMOND), POWER_CELL,
                                STEEL_FRAME, SlimefunItems.ELECTRIC_MOTOR, STEEL_FRAME
                }).register(addon);

                new SlimefunItem(category, TARGETING_SYSTEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                ADVANCED_CIRCUIT, new ItemStack(Material.ENDER_EYE), ADVANCED_CIRCUIT,
                                new ItemStack(Material.SPYGLASS), POWER_CELL, new ItemStack(Material.SPYGLASS),
                                SERVO_MOTOR, new ItemStack(Material.OBSERVER), SERVO_MOTOR
                }).register(addon);

                new SlimefunItem(category, RADAR_MODULE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                COPPER_COIL, ADVANCED_CIRCUIT, COPPER_COIL,
                                POWER_CELL, new ItemStack(Material.SCULK_SENSOR), POWER_CELL,
                                SERVO_MOTOR, SlimefunItems.GPS_TRANSMITTER, SERVO_MOTOR
                }).register(addon);

                new SlimefunItem(category, REINFORCED_FRAME, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                REINFORCED_PLATING, STEEL_FRAME, REINFORCED_PLATING,
                                STEEL_FRAME, new ItemStack(Material.NETHERITE_BLOCK), STEEL_FRAME,
                                REINFORCED_PLATING, SlimefunItems.HARDENED_METAL_INGOT, REINFORCED_PLATING
                }).register(addon);

                new SlimefunItem(category, POWER_CORE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                POWER_CELL, ADVANCED_CIRCUIT, POWER_CELL,
                                COPPER_COIL, SlimefunItems.ENERGIZED_CAPACITOR, COPPER_COIL,
                                POWER_CELL, new ItemStack(Material.NETHER_STAR), POWER_CELL
                }).register(addon);

                new SlimefunItem(category, MILITARY_CIRCUIT, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                ADVANCED_CIRCUIT, SlimefunItems.CARBONADO, ADVANCED_CIRCUIT,
                                SlimefunItems.REINFORCED_ALLOY_INGOT, POWER_CORE, SlimefunItems.REINFORCED_ALLOY_INGOT,
                                ADVANCED_CIRCUIT, SlimefunItems.BLISTERING_INGOT_3, ADVANCED_CIRCUIT
                }).register(addon);

                new SlimefunItem(category, HYDRAULIC_SYSTEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                SERVO_MOTOR, new ItemStack(Material.PISTON), SERVO_MOTOR,
                                POWER_CELL, new ItemStack(Material.CAULDRON), POWER_CELL,
                                SERVO_MOTOR, new ItemStack(Material.STICKY_PISTON), SERVO_MOTOR
                }).register(addon);

                new SlimefunItem(category, COOLANT_SYSTEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                new ItemStack(Material.POWDER_SNOW_BUCKET), COPPER_COIL,
                                new ItemStack(Material.POWDER_SNOW_BUCKET),
                                SERVO_MOTOR, new ItemStack(Material.BLUE_ICE), SERVO_MOTOR,
                                new ItemStack(Material.PACKED_ICE), POWER_CELL, new ItemStack(Material.PACKED_ICE)
                }).register(addon);

                new SlimefunItem(category, QUANTUM_PROCESSOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                MILITARY_CIRCUIT, new ItemStack(Material.END_CRYSTAL), MILITARY_CIRCUIT,
                                new ItemStack(Material.ENDER_EYE), POWER_CORE, new ItemStack(Material.ENDER_EYE),
                                MILITARY_CIRCUIT, new ItemStack(Material.NETHER_STAR), MILITARY_CIRCUIT
                }).register(addon);

                new SlimefunItem(category, ENERGY_MATRIX, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                POWER_CORE, SlimefunItems.ENERGIZED_CAPACITOR, POWER_CORE,
                                new ItemStack(Material.CONDUIT), new ItemStack(Material.BEACON),
                                new ItemStack(Material.CONDUIT),
                                POWER_CORE, SlimefunItems.ENERGIZED_CAPACITOR, POWER_CORE
                }).register(addon);

                new SlimefunItem(category, EXPLOSIVE_CORE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                new ItemStack(Material.TNT), MILITARY_CIRCUIT, new ItemStack(Material.TNT),
                                new ItemStack(Material.GUNPOWDER), new ItemStack(Material.NETHER_STAR),
                                new ItemStack(Material.GUNPOWDER),
                                new ItemStack(Material.TNT), POWER_CORE, new ItemStack(Material.TNT)
                }).register(addon);

                new SlimefunItem(category, ANTIMATTER_PARTICLE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                QUANTUM_PROCESSOR, ENERGY_MATRIX, QUANTUM_PROCESSOR,
                                SlimefunItems.CARBONADO_EDGED_CAPACITOR, new ItemStack(Material.NETHER_STAR),
                                SlimefunItems.CARBONADO_EDGED_CAPACITOR,
                                EXPLOSIVE_CORE, ENERGY_MATRIX, EXPLOSIVE_CORE
                }).register(addon);

                new SlimefunItem(category, VOID_ESSENCE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                new ItemStack(Material.ECHO_SHARD), SlimefunItems.ENDER_LUMP_3,
                                new ItemStack(Material.ECHO_SHARD),
                                new ItemStack(Material.SCULK_CATALYST), new ItemStack(Material.END_CRYSTAL),
                                new ItemStack(Material.SCULK_CATALYST),
                                QUANTUM_PROCESSOR, SlimefunItems.WITHER_PROOF_OBSIDIAN, QUANTUM_PROCESSOR
                }).register(addon);

                new SlimefunItem(category, CONTAINMENT_FIELD_GENERATOR, RecipeType.ENHANCED_CRAFTING_TABLE,
                                new ItemStack[] {
                                                ENERGY_MATRIX, QUANTUM_PROCESSOR, ENERGY_MATRIX,
                                                POWER_CORE, new ItemStack(Material.RESPAWN_ANCHOR), POWER_CORE,
                                                ANTIMATTER_PARTICLE, VOID_ESSENCE, ANTIMATTER_PARTICLE
                                }).register(addon);

                new SlimefunItem(category, DIMENSIONAL_STABILIZER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                new ItemStack(Material.CRYING_OBSIDIAN), REINFORCED_FRAME,
                                new ItemStack(Material.CRYING_OBSIDIAN),
                                ANTIMATTER_PARTICLE, CONTAINMENT_FIELD_GENERATOR, ANTIMATTER_PARTICLE,
                                QUANTUM_PROCESSOR, VOID_ESSENCE, QUANTUM_PROCESSOR
                }).register(addon);

                new SlimefunItem(category, ANTIMATTER_CRYSTAL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                ANTIMATTER_PARTICLE, DIMENSIONAL_STABILIZER, ANTIMATTER_PARTICLE,
                                CONTAINMENT_FIELD_GENERATOR, new ItemStack(Material.DRAGON_BREATH),
                                CONTAINMENT_FIELD_GENERATOR,
                                ANTIMATTER_PARTICLE, VOID_ESSENCE, ANTIMATTER_PARTICLE
                }).register(addon);

                new SlimefunItem(category, VOID_CORE_MACHINE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                VOID_ESSENCE, ANTIMATTER_CRYSTAL, VOID_ESSENCE,
                                ANTIMATTER_CRYSTAL, new ItemStack(Material.BEACON), ANTIMATTER_CRYSTAL,
                                VOID_ESSENCE, ANTIMATTER_CRYSTAL, VOID_ESSENCE
                }).register(addon);

                // Upgrade Modules Registration
                new SlimefunItem(upgradesCategory, DAMAGE_MODULE_I, RecipeType.ENHANCED_CRAFTING_TABLE,
                                new ItemStack[] {
                                                new ItemStack(Material.REDSTONE), MilitaryComponents.ADVANCED_CIRCUIT,
                                                new ItemStack(Material.REDSTONE),
                                                new ItemStack(Material.IRON_INGOT), SlimefunItems.STEEL_PLATE,
                                                new ItemStack(Material.IRON_INGOT),
                                                new ItemStack(Material.REDSTONE), MilitaryComponents.ADVANCED_CIRCUIT,
                                                new ItemStack(Material.REDSTONE)
                                }).register(addon);

                new SlimefunItem(upgradesCategory, SPEED_MODULE_I, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                                new ItemStack(Material.SUGAR), MilitaryComponents.ADVANCED_CIRCUIT,
                                new ItemStack(Material.SUGAR),
                                new ItemStack(Material.IRON_INGOT), SlimefunItems.STEEL_PLATE,
                                new ItemStack(Material.IRON_INGOT),
                                new ItemStack(Material.SUGAR), MilitaryComponents.ADVANCED_CIRCUIT,
                                new ItemStack(Material.SUGAR)
                }).register(addon);
        }
}

package com.Chagui68.weaponsaddon.integrations;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

/**
 * Integración con el addon Networks de Slimefun usando reflexión.
 * Registra automáticamente los items del addon Military Arsenal en Networks,
 * pero filtra las recetas con grids mayores a 3x3 para evitar
 * incompatibilidades.
 * 
 * Esta implementación usa reflexión para no requerir Networks como dependencia
 * de compilación.
 */
public class NetworksIntegration {

    private static int registeredItems = 0;
    private static int filteredItems = 0;

    /**
     * Registra la integración con Networks si está disponible.
     * Este método debe llamarse después de que todos los items del addon
     * hayan sido registrados en Slimefun.
     */
    public static void register() {
        // Verificar si Networks está instalado
        if (!Bukkit.getPluginManager().isPluginEnabled("Networks")) {
            WeaponsAddon.getInstance().getLogger().info("Networks no está instalado, saltando integración.");
            return;
        }

        WeaponsAddon.getInstance().getLogger().info("Networks detectado, registrando items compatibles...");

        try {
            registerCompatibleItems();
            WeaponsAddon.getInstance().getLogger().info(
                    String.format(
                            "Integración con Networks completada: %d items registrados, %d items filtrados (recetas > 3x3)",
                            registeredItems, filteredItems));
        } catch (Exception e) {
            WeaponsAddon.getInstance().getLogger().warning(
                    "Error al integrar con Networks: " + e.getMessage());
        }
    }

    /**
     * Registra solo los items del addon que tienen recetas compatibles con
     * Networks.
     * Filtra items con recetas mayores a 3x3.
     * Usa reflexión para llamar a la API de Networks.
     */
    private static void registerCompatibleItems() throws Exception {
        registeredItems = 0;
        filteredItems = 0;

        // Obtener la clase ItemDictionary usando reflexión
        Class<?> itemDictionaryClass = Class.forName("io.github.sefiraat.networks.integrations.ItemDictionary");
        Method registerMethod = itemDictionaryClass.getMethod("registerItem", String.class, SlimefunItem.class);

        // Iterar sobre todos los items registrados en Slimefun
        for (SlimefunItem item : Slimefun.getRegistry().getAllSlimefunItems()) {
            // Solo procesar items de nuestro addon
            if (!isFromMilitaryArsenal(item)) {
                continue;
            }

            // Verificar si la receta es compatible
            if (isRecipeCompatible(item)) {
                try {
                    // Llamar a ItemDictionary.registerItem usando reflexión
                    registerMethod.invoke(null, item.getId(), item);
                    registeredItems++;
                } catch (Exception e) {
                    WeaponsAddon.getInstance().getLogger().warning(
                            "Error al registrar " + item.getId() + " en Networks: " + e.getMessage());
                }
            } else {
                filteredItems++;
                WeaponsAddon.getInstance().getLogger().fine(
                        "Item filtrado (receta > 3x3): " + item.getId());
            }
        }
    }

    /**
     * Verifica si un item pertenece al addon Military Arsenal.
     */
    private static boolean isFromMilitaryArsenal(SlimefunItem item) {
        if (item == null || item.getAddon() == null) {
            return false;
        }
        return item.getAddon() instanceof WeaponsAddon;
    }

    /**
     * Verifica si la receta de un item es compatible con Networks.
     * Una receta es compatible si:
     * - No es null o vacía
     * - Tiene 9 o menos items (3x3 o menor)
     * - No pertenece a tipos de recetas incompatibles
     */
    private static boolean isRecipeCompatible(SlimefunItem item) {
        ItemStack[] recipe = item.getRecipe();

        // Verificar que la receta no sea null o vacía
        if (recipe == null || recipe.length == 0) {
            return false;
        }

        // Filtrar recetas con más de 9 slots (mayores a 3x3)
        if (recipe.length > 9) {
            return false;
        }

        RecipeType recipeType = item.getRecipeType();

        // Filtrar tipos de recetas que usan grids grandes
        if (recipeType == RecipeType.ARMOR_FORGE) {
            // Armor Forge usa grid 3x3, pero puede tener comportamientos especiales
            return false;
        }

        if (recipeType == RecipeType.MAGIC_WORKBENCH) {
            // Magic Workbench puede usar grids mayores a 3x3
            return false;
        }

        // Tipos de recetas compatibles comunes:
        // - ENHANCED_CRAFTING_TABLE (3x3)
        // - GRIND_STONE
        // - SMELTERY
        // - ORE_CRUSHER
        // - Cualquier máquina custom que use recetas <= 3x3

        return true;
    }

    /**
     * Obtiene el número de items registrados en Networks.
     */
    public static int getRegisteredItemsCount() {
        return registeredItems;
    }

    /**
     * Obtiene el número de items filtrados (no registrados por tener recetas >
     * 3x3).
     */
    public static int getFilteredItemsCount() {
        return filteredItems;
    }
}

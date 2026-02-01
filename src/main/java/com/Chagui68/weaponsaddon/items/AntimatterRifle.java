package com.Chagui68.weaponsaddon.items;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.machines.AntimatterPedestal;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

public class AntimatterRifle extends SlimefunItem implements Listener {

    public static final SlimefunItemStack ANTIMATTER_RIFLE = new SlimefunItemStack(
            "ANTIMATTER_RIFLE",
            Material.DIAMOND_HOE,
            "&4☢ &fAntimatter Rifle",
            "",
            "&7Experimental antimatter weapon",
            "&7Annihilates matter on molecular level",
            "",
            "&6Damage: &c&lINSTANT DEATH",
            "&6Range: &e64 blocks",
            "&6Fire Mode: &eRaycast precision",
            "",
            "&c⚠ &4EXTREME &cDANGER: Matter annihilation",
            "&c⚠ Requires 9×9 Antimatter Ritual Array",
            "",
            "&eRight-Click to annihilate target",
            "&cRequires Antimatter Cells",
            "",
            "&a✓ Unbreakable"
    );

    public AntimatterRifle(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (!isItem(item)) return;
        if (!e.getAction().toString().contains("RIGHT")) return;

        e.setCancelled(true);

        RayTraceResult result = p.getWorld().rayTraceEntities(
                p.getEyeLocation(),
                p.getEyeLocation().getDirection(),
                64.0,
                entity -> entity instanceof LivingEntity && entity != p
        );

        if (result != null && result.getHitEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) result.getHitEntity();
            target.damage(Double.MAX_VALUE);
            target.setHealth(0);
            target.getWorld().createExplosion(target.getLocation(), 0F, false, false);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4☢ &cMatter annihilated!"));
        } else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c✘ No target in range"));
        }
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        ItemStack[] recipe = new ItemStack[]{
                new ItemStack(Material.NETHERITE_INGOT), new ItemStack(Material.NETHER_STAR), new ItemStack(Material.NETHERITE_INGOT),
                MilitaryComponents.MILITARY_GRADE_CIRCUIT, new ItemStack(Material.BEACON), MilitaryComponents.MILITARY_GRADE_CIRCUIT,
                new ItemStack(Material.NETHERITE_INGOT), new ItemStack(Material.NETHER_STAR), new ItemStack(Material.NETHERITE_INGOT)
        };

        AntimatterRifle rifle = new AntimatterRifle(category, ANTIMATTER_RIFLE, RecipeType.ANCIENT_ALTAR, recipe);
        rifle.register(addon);

        WeaponsAddon.getInstance().getServer().getPluginManager().registerEvents(rifle, addon.getJavaPlugin());
    }
}

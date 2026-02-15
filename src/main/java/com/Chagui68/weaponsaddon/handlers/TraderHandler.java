package com.Chagui68.weaponsaddon.handlers;

import com.Chagui68.weaponsaddon.items.vouchers.MilitaryVouchers;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TraderHandler implements Listener {

    private final Random random = new Random();

    // Configuración de probabilidades
    private static final double ECHO_SHARD_CHANCE = 0.5; // 50%
    private static final double SADDLE_CHANCE = 0.1; // 10%

    @EventHandler
    public void onTraderSpawn(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.WANDERING_TRADER) {
            WanderingTrader trader = (WanderingTrader) event.getEntity();

            if (random.nextDouble() < ECHO_SHARD_CHANCE) {
                addEchoShardTrade(trader);
            }

            if (random.nextDouble() < SADDLE_CHANCE) {
                addSaddleTrade(trader);
            }
        }
    }

    private void addEchoShardTrade(WanderingTrader trader) {
        List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());

        ItemStack price = MilitaryVouchers.VOUCHER_KEY.clone();
        price.setAmount(1);
        ItemStack result = new ItemStack(Material.ECHO_SHARD, 64);

        MerchantRecipe recipe = new MerchantRecipe(result, 3);
        recipe.addIngredient(price);
        recipes.add(recipe);

        trader.setRecipes(recipes);
    }

    private void addSaddleTrade(WanderingTrader trader) {
        List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());

        ItemStack price = MilitaryVouchers.VOUCHER_KEY.clone();
        price.setAmount(1);
        ItemStack result = new ItemStack(Material.SADDLE);

        MerchantRecipe recipe = new MerchantRecipe(result, 10);
        recipe.addIngredient(price);
        recipes.add(recipe);

        trader.setRecipes(recipes);
    }
}

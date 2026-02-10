package com.Chagui68.weaponsaddon.handlers;

import com.Chagui68.weaponsaddon.items.components.MilitaryComponents;
import com.Chagui68.weaponsaddon.items.MachineGun;
import com.Chagui68.weaponsaddon.items.MachineGunAmmo;
import com.Chagui68.weaponsaddon.items.AntimatterRifle;
import com.Chagui68.weaponsaddon.items.vouchers.MilitaryVouchers;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BossRewardHandler {

        private static final Random random = new Random();

        /**
         * Gives a random reward to a player.
         * 
         * @param player The player to reward.
         */
        public static void giveRandomReward(Player player) {
                List<ItemStack> rewards = getRewards();
                int index = random.nextInt(rewards.size());
                ItemStack reward = rewards.get(index);

                // Give item (handle full inventory)
                if (player.getInventory().firstEmpty() == -1) {
                        player.getWorld().dropItemNaturally(player.getLocation(), reward);
                        player.sendMessage(ChatColor.YELLOW
                                        + "Your inventory was full, the reward was dropped at your feet!");
                } else {
                        player.getInventory().addItem(reward);
                }

                // Notify player
                player.sendMessage("");
                player.sendMessage(ChatColor.GOLD + "╔═══════════════════════════════╗");
                player.sendMessage(
                                ChatColor.GOLD + "║" + ChatColor.YELLOW + "      BOSS REWARD RECEIVED     "
                                                + ChatColor.GOLD + "║");
                player.sendMessage(ChatColor.GOLD + "╠═══════════════════════════════╣");
                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.WHITE + "You received: ");

                String itemName = reward.hasItemMeta() && reward.getItemMeta().hasDisplayName()
                                ? reward.getItemMeta().getDisplayName()
                                : reward.getType().toString();

                player.sendMessage(ChatColor.GOLD + "║ " + ChatColor.AQUA + "▶ " + itemName);
                player.sendMessage(ChatColor.GOLD + "╚═══════════════════════════════╝");
                player.sendMessage("");

                // Effects
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                player.spawnParticle(org.bukkit.Particle.FLASH, player.getLocation(), 10);
                player.spawnParticle(org.bukkit.Particle.END_ROD, player.getLocation().add(0, 1, 0), 15, 0.5, 0.5, 0.5,
                                0.05);
        }

        /**
         * Returns the list of 32 rewards.
         * Edit this list to change rewards.
         */
        private static List<ItemStack> getRewards() {
                List<ItemStack> rewards = new ArrayList<>();

                // 16-20: Vouchers (Special Requisitions)
                rewards.add(MilitaryVouchers.VOUCHER_WEAPON_UPGRADE.clone());
                rewards.add(MilitaryVouchers.VOUCHER_TANK_PART.clone());
                rewards.add(MilitaryVouchers.VOUCHER_BATTERY.clone());
                rewards.add(MilitaryVouchers.VOUCHER_DOCUMENT.clone());
                rewards.add(MilitaryVouchers.VOUCHER_KEY.clone());

                // --- 2. ARMAMENT & AMMO (5 items) ---
                rewards.add(MachineGun.MACHINE_GUN.clone());
                rewards.add(AntimatterRifle.ANTIMATTER_RIFLE.clone());
                rewards.add(new CustomItemStack(MachineGunAmmo.MACHINE_GUN_AMMO, 64));
                rewards.add(new CustomItemStack(MachineGunAmmo.MACHINE_GUN_AMMO, 32));
                rewards.add(MilitaryComponents.WEAPON_BARREL.clone());

                // 1-5: High-tier components (originally moved here)
                rewards.add(MilitaryComponents.QUANTUM_PROCESSOR.clone());
                rewards.add(MilitaryComponents.ENERGY_MATRIX.clone());
                rewards.add(MilitaryComponents.EXPLOSIVE_CORE.clone());
                rewards.add(MilitaryComponents.POWER_CORE.clone());
                rewards.add(MilitaryComponents.MILITARY_CIRCUIT.clone());

                // --- 4. SLIMEFUN RARE MATERIALS (5 items) ---
                rewards.add(SlimefunItems.CARBONADO.clone());
                rewards.add(SlimefunItems.REINFORCED_ALLOY_INGOT.clone());
                rewards.add(SlimefunItems.SYNTHETIC_DIAMOND.clone());
                rewards.add(SlimefunItems.BLISTERING_INGOT_3.clone());
                rewards.add(new ItemStack(Material.NETHERITE_INGOT, 2));

                // 21-25: Support components
                rewards.add(MilitaryComponents.STABILIZER_UNIT.clone());
                rewards.add(MilitaryComponents.TARGETING_SYSTEM.clone());
                rewards.add(MilitaryComponents.RADAR_MODULE.clone());
                rewards.add(MilitaryComponents.HYDRAULIC_SYSTEM.clone());
                rewards.add(MilitaryComponents.COOLANT_SYSTEM.clone());

                // 26-30: Utility items
                rewards.add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 3));
                rewards.add(new ItemStack(Material.DIAMOND_BLOCK, 4));
                rewards.add(new ItemStack(Material.EXPERIENCE_BOTTLE, 64));
                rewards.add(new ItemStack(Material.TOTEM_OF_UNDYING, 1));
                rewards.add(new ItemStack(Material.NETHERITE_BLOCK, 1));

                // 31-32: Special Commendations (Vouchers)
                rewards.add(MilitaryVouchers.VOUCHER_COMMENDATION.clone());
                rewards.add(MilitaryVouchers.VOUCHER_EMBLEM.clone());

                return rewards;
        }

}

package com.Chagui68.weaponsaddon.items.machines;

import com.Chagui68.weaponsaddon.items.AntimatterRifle;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AntimatterRitual extends MultiBlockMachine {

    public static final SlimefunItemStack ANTIMATTER_RITUAL_CORE = new SlimefunItemStack(
            "ANTIMATTER_RITUAL_CORE",
            Material.BEACON,
            "&4☢ &fAntimatter Ritual Core",
            "",
            "&7Central trigger for 9×9 ritual array",
            "&7Place catalysts on 16 pedestals",
            "&7Right-click to start ritual",
            "",
            "&6Required Structure:",
            "&7- 9×9 iron block border",
            "&7- 4 netherite blocks (cross pattern)",
            "&7- 16 Antimatter Pedestals (odd positions)",
            "&7- Crying obsidian filler",
            "",
            "&eClick core to begin annihilation process"
    );

    public AntimatterRitual(ItemGroup itemGroup) {
        super(itemGroup, ANTIMATTER_RITUAL_CORE, build9x9Pattern(), BlockFace.SELF);
    }

    private static ItemStack[] build9x9Pattern() {
        ItemStack I = new ItemStack(Material.IRON_BLOCK);
        ItemStack N = new ItemStack(Material.NETHERITE_BLOCK);
        ItemStack B = new ItemStack(Material.BEACON);
        ItemStack P = AntimatterPedestal.ANTIMATTER_PEDESTAL.clone();
        ItemStack O = new ItemStack(Material.CRYING_OBSIDIAN);

        // Flatten 9x9 grid row-major (81 slots)
        ItemStack[] pattern = new ItemStack[81];

        for (int i=0; i<9; i++) pattern[i] = I;

        pattern[9]=I; pattern[10]=P; pattern[11]=O; pattern[12]=P;
        pattern[13]=O; pattern[14]=P; pattern[15]=O; pattern[16]=P; pattern[17]=I;

        pattern[18]=I; for(int i=19;i<26;i++) pattern[i]=O; pattern[26]=I;

        pattern[27]=I; pattern[28]=P; pattern[29]=O; pattern[30]=P;
        pattern[31]=N; pattern[32]=P; pattern[33]=O; pattern[34]=P; pattern[35]=I;

        pattern[36]=I; pattern[37]=O; pattern[38]=O; pattern[39]=N;
        pattern[40]=B; pattern[41]=N; pattern[42]=O; pattern[43]=O; pattern[44]=I;

        pattern[45]=I; pattern[46]=P; pattern[47]=O; pattern[48]=P;
        pattern[49]=N; pattern[50]=P; pattern[51]=O; pattern[52]=P; pattern[53]=I;

        pattern[54]=I; for(int i=55;i<62;i++) pattern[i]=O; pattern[62]=I;

        pattern[63]=I; pattern[64]=P; pattern[65]=O; pattern[66]=P;
        pattern[67]=O; pattern[68]=P; pattern[69]=O; pattern[70]=P; pattern[71]=I;

        for (int i=72; i<81; i++) pattern[i] = I;

        return pattern;
    }

    @Override
    public void onInteract(Player p, Block b) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&4☢ &fAntimatter Ritual &4ACTIVATED"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7Annihilating matter... &c⚠"));

        p.getInventory().addItem(AntimatterRifle.ANTIMATTER_RIFLE.clone());

        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&a✓ &fAntimatter Rifle created successfully!"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7Right-click entities for instant annihilation"));
    }

    public static void register(SlimefunAddon addon, ItemGroup category) {
        new AntimatterRitual(category).register(addon);
    }
}

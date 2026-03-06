package com.Chagui68.weaponsaddon.listeners;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class CinematicJoinListener implements Listener {

    private static final NamespacedKey GM_KEY = new NamespacedKey(WeaponsAddon.getInstance(), "original_gamemode");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.getPersistentDataContainer().has(GM_KEY, PersistentDataType.STRING)) {
            String gmName = player.getPersistentDataContainer().get(GM_KEY, PersistentDataType.STRING);
            if (gmName != null) {
                try {
                    GameMode gm = GameMode.valueOf(gmName);
                    player.setGameMode(gm);
                    player.sendMessage("§d[Military Arsenal] §fYour gamemode has been restored after the cinematic.");
                } catch (IllegalArgumentException ex) {
                    player.setGameMode(GameMode.SURVIVAL);
                }
                player.getPersistentDataContainer().remove(GM_KEY);
            }
        }
    }
}

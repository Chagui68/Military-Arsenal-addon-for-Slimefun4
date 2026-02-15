package com.Chagui68.weaponsaddon.core.attributes;

import org.bukkit.entity.Player;


public interface CustomEffectEmitter {

  /*
  Esta interfaz de java sirve de puente para poder colocar efectos a items
  especificos por separado como lo hace la radioactividad del slimefun normal
   */
    void applyEffect(Player player);
}

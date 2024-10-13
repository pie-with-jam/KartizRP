package ru.AlertKaput.nationsForge.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

public class SnowballExtinguishFire implements Listener {
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        // Проверяем, что это снежок
        if (event.getEntity() instanceof Snowball) {
            // Получаем местоположение удара
            Vector hitLocation = event.getEntity().getLocation().toVector();

            // Проверяем блоки вокруг места удара
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        Block block = event.getEntity().getWorld().getBlockAt(
                                event.getEntity().getLocation().add(x, y, z)
                        );

                        if (block.getType() == Material.FIRE) {
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
}

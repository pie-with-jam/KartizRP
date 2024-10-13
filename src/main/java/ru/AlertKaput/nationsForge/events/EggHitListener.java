package ru.AlertKaput.nationsForge.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Egg;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.AlertKaput.nationsForge.NationsForge;
import ru.AlertKaput.nationsForge.menus.Country;
import ru.AlertKaput.nationsForge.menus.IdeologyMenu;

import java.util.*;

public class EggHitListener implements Listener {
    // Хранилище для отслеживания выбранных блоков каждым игроком


    @EventHandler
    public void onEggHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Egg) {
            Block hitBlock = event.getHitBlock();
            BlockFace hitFace = event.getHitBlockFace();

            if (hitBlock != null && hitFace != null) {
                // Определяем блок, который нужно зажечь, исходя из того, в какую грань попало яйцо
                Block fireBlock = null;

                switch (hitFace) {
                    case UP:
                        fireBlock = hitBlock.getRelative(BlockFace.UP);
                        break;
                    case DOWN:
                        fireBlock = hitBlock.getRelative(BlockFace.DOWN);
                        break;
                    case NORTH:
                        fireBlock = hitBlock.getRelative(BlockFace.NORTH);
                        break;
                    case SOUTH:
                        fireBlock = hitBlock.getRelative(BlockFace.SOUTH);
                        break;
                    case EAST:
                        fireBlock = hitBlock.getRelative(BlockFace.EAST);
                        break;
                    case WEST:
                        fireBlock = hitBlock.getRelative(BlockFace.WEST);
                        break;
                }

                // Проверяем, можно ли установить огонь на этом блоке
                if (fireBlock != null && fireBlock.getType() == Material.AIR) {
                    fireBlock.setType(Material.FIRE); // Устанавливаем огонь
                }
            }
        }
    }

    // Отключаем спавн курицы при броске яйца
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG) {
            event.setCancelled(true); // Отменяем спавн курицы
        }
    }
}


package ru.AlertKaput.nationsForge.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.AlertKaput.nationsForge.utils.StateRegistration;

public class MovementListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (StateRegistration.getRegistration(event.getPlayer()) != null) {
            event.setCancelled(true);  // Блокируем движение игрока, если он в процессе регистрации
        }
    }
}

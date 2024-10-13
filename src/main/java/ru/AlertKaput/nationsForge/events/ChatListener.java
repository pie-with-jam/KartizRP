package ru.AlertKaput.nationsForge.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.AlertKaput.nationsForge.utils.StateRegistration;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        StateRegistration registration = StateRegistration.getRegistration(event.getPlayer());
        if (registration != null) {
            event.setCancelled(true);  // Останавливаем обычные сообщения
            registration.handleChatInput(event.getMessage());  // Обрабатываем ввод
        }
    }
}
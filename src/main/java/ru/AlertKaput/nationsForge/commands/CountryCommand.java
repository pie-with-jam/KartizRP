package ru.AlertKaput.nationsForge.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.AlertKaput.nationsForge.menus.Country;

public class CountryCommand implements CommandExecutor {
    // Метод вызывается при выполнении команды
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Проверяем, что команду выполнил игрок
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Открываем меню для игрока
            Country menu = new Country();
            menu.openMenu(player);

            return true; // Команда выполнена успешно
        } else {
            sender.sendMessage("Эту команду может использовать только игрок.");
            return false;
        }
    }
}
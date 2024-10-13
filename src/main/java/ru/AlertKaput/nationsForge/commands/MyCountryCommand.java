package ru.AlertKaput.nationsForge.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.AlertKaput.nationsForge.menus.Country;
import ru.AlertKaput.nationsForge.menus.MyCountry;

public class MyCountryCommand implements CommandExecutor {

    // Метод вызывается при выполнении команды
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        try {
            // Проверяем, что команду выполнил игрок
            if (sender instanceof Player) {
                Player player = (Player) sender;

                // Открываем меню для игрока
                MyCountry menu = new MyCountry();
                menu.openMenu(player);

                return true; // Команда выполнена успешно
            } else {
                sender.sendMessage("Эту команду может использовать только игрок.");
                return false;
            }
        } catch (Exception e) {
            sender.sendMessage("Произошла ошибка: " + e.getMessage());
            return false;
        }
    }
}
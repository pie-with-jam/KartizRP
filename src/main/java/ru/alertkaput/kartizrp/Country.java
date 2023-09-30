package ru.alertkaput.kartizrp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class Country implements CommandExecutor {
    private final JavaPlugin plugin;

    public Country(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("country") && sender instanceof Player) {
            // Проверяем, достаточно ли аргументов
            if (args.length < 2) {
                sender.sendMessage("Используйте: /country create <Название>");
                return true;
            }

            String subCommand = args[0].toLowerCase();  // Получаем первый аргумент и преобразуем в нижний регистр
            Player player = (Player) sender;

            if (subCommand.equals("create")) {
                // Проверяем, есть ли у игрока уже страна
                if (player.hasMetadata("Country")) {
                    player.sendMessage("У вас уже есть страна!");
                    return true;
                }

                // Объединяем аргументы в одну строку
                String countryName = String.join(args[1]);

                // Проверяем длину названия страны
                if (countryName.length() > 24) {
                    player.sendMessage("Название страны не должно превышать 24 символа!");
                    return true;
                }

                // Заменяем "_" на пробелы
                countryName = countryName.replace("_", " ");

                // Устанавливаем переменную Country и Owner
                player.setMetadata("Country", new FixedMetadataValue(plugin, countryName));
                player.setMetadata("Owner", new FixedMetadataValue(plugin, true));

                // Поздравляем игрока
                player.sendMessage("Поздравляем! Вы создали страну: " + countryName);

                return true;
            }
        }
        return false;
    }
}
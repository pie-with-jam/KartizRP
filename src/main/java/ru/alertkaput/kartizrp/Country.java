package ru.alertkaput.kartizrp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class Country implements CommandExecutor {
    private final JavaPlugin plugin;
    private final FileConfiguration config;

    public Country(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("country") && sender instanceof Player) {
            // Проверяем, достаточно ли аргументов
            if (args.length < 2) {
                sender.sendMessage(config.getString("messages.usage"));
                return true;
            }

            String subCommand = args[0].toLowerCase();
            Player player = (Player) sender;

            if (subCommand.equals("create")) {
                // Проверяем, есть ли у игрока уже страна
                if (player.hasMetadata("Country")) {
                    player.sendMessage(config.getString("messages.country_exists"));
                    return true;
                }

                // Объединяем аргументы в одну строку
                String countryName = String.join(args[1]);

                // Проверяем длину названия страны
                if (countryName.length() > 24) {
                    player.sendMessage(config.getString("messages.country_name_length"));
                    return true;
                }

                // Заменяем "_" на пробелы
                countryName = countryName.replace("_", " ");

                // Устанавливаем переменную Country и Owner
                player.setMetadata("Country", new FixedMetadataValue(plugin, countryName));
                player.setMetadata("Owner", new FixedMetadataValue(plugin, true));

                // Поздравляем игрока
                player.sendMessage(config.getString("messages.country_created").replace("%country%", countryName));

                return true;
            }
        }
        return false;
    }
}
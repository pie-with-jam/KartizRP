package ru.alertkaput.kartizrp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginConfig {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    // Конструктор класса, принимающий плагин и инициализирующий конфигурацию
    public PluginConfig(JavaPlugin plugin) {
        this.plugin = plugin;

        // Сохранение конфигурации по умолчанию, если она не существует
        plugin.saveDefaultConfig();

        // Получение конфигурации из плагина
        this.config = plugin.getConfig();
    }

    // Метод для получения объекта конфигурации
    public FileConfiguration getConfig() {
        return config;
    }
}
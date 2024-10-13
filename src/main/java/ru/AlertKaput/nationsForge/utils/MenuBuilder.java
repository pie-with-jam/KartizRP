package ru.AlertKaput.nationsForge.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MenuBuilder {

    private static final Map<Inventory, String> inventoryRegistry = new HashMap<>();
    private final String menuId;
    private final Inventory inventory;
    private final Random random = new Random();
    private final Plugin plugin;

    private final Sound[] openingSounds = {
            Sound.ENTITY_PLAYER_LEVELUP,
            Sound.BLOCK_NOTE_BLOCK_BELL,
            Sound.ENTITY_EXPERIENCE_ORB_PICKUP
    };

    // Конструктор для создания меню с ID
    public MenuBuilder(String menuId, String title, int rows, Plugin plugin) {
        this.menuId = menuId;
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
        this.plugin = plugin;

        // Регистрируем меню по его инвентарю
        inventoryRegistry.put(this.inventory, menuId);
    }

    // Метод для добавления предмета в слот с обязательными параметрами
    public MenuBuilder addItem(int slot, Material material, String name) {
        return addItem(slot, material, name, null);
    }

    // Метод для добавления предмета в слот с обязательными и необязательными параметрами
    public MenuBuilder addItem(int slot, Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (name != null) {
            meta.setDisplayName(name);
        }
        if (lore != null) {
            meta.setLore(lore);
        }

        item.setItemMeta(meta);
        inventory.setItem(slot, item);

        return this;
    }

    // Метод для открытия меню для игрока с воспроизведением случайного звука
    public void open(Player player) {
        player.openInventory(inventory);

        // Выбираем случайный звук из массива openingSounds
        Sound randomSound = openingSounds[random.nextInt(openingSounds.length)];
        player.playSound(player.getLocation(), randomSound, 1.0f, 1.0f);
    }

    // Получение ID меню
    public String getMenuId() {
        return menuId;
    }

    // Получение ID меню по инвентарю
    public static String getMenuId(Inventory inventory) {
        return inventoryRegistry.get(inventory);
    }
}
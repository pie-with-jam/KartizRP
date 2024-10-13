package ru.AlertKaput.nationsForge.events;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ru.AlertKaput.nationsForge.NationsForge;
import ru.AlertKaput.nationsForge.utils.DataUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PlayerJoin implements Listener {
    private final File personsFile = NationsForge.getInstance().getDataFolder().toPath().resolve("persons.json").toFile();
    private JsonObject personsData;

    // Карта для временного хранения данных игрока перед их окончательным сохранением
    private final Map<String, String> tempGenders = new HashMap<>();
    private final Map<String, String> tempAgeGroups = new HashMap<>();
    private final Map<String, Boolean> isRegistering = new HashMap<>();

    public PlayerJoin() {
        personsData = DataUtils.loadDatabase(personsFile);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        // Проверяем, есть ли игрок в базе данных
        if (!personsData.has(playerName)) {
            // Блокируем движение игрока
            isRegistering.put(playerName, true);

            // Запланировать сообщение через 2-3 секунды
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(ChatColor.GREEN + "Добро пожаловать, " + playerName + "! Давайте создадим ваш персонаж.");
                    askGender(player);
                }
            }.runTaskLater(NationsForge.getInstance(), 60); // 60 тиков = 3 секунды

            // Напоминание каждые 5 секунд
            new BukkitRunnable() {
                private int timeElapsed = 0;

                @Override
                public void run() {
                    // Если игрок завершил регистрацию, отменяем задачу
                    if (!isRegistering.get(playerName)) {
                        cancel();
                        return;
                    }

                    // Если прошло 30 секунд, кикаем игрока
                    if (timeElapsed >= 30) {
                        player.kickPlayer(ChatColor.RED + "Вы не завершили регистрацию персонажа вовремя.");
                        cancel();
                        return;
                    }

                    player.sendMessage(ChatColor.RED + "Пожалуйста, завершите регистрацию персонажа.");
                    timeElapsed += 5;
                }
            }.runTaskTimer(NationsForge.getInstance(), 100, 100); // 100 тиков = 5 секунд
        }
    }

    // Запретить движение игрока, пока идет регистрация
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (isRegistering.getOrDefault(playerName, false)) {
            event.setCancelled(true);
        }
    }

    private void askGender(Player player) {
        player.sendMessage(ChatColor.YELLOW + "Выберите пол, кликнув на один из вариантов:");

        // Создаем кликабельные сообщения для выбора пола
        TextComponent maleOption = new TextComponent(ChatColor.GOLD + "[Мужской]");
        maleOption.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setgender male"));

        TextComponent femaleOption = new TextComponent(ChatColor.GOLD + "[Женский]");
        femaleOption.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setgender female"));

        // Отправляем игроку сообщения
        player.spigot().sendMessage(maleOption);
        player.spigot().sendMessage(femaleOption);
    }

    // Метод вызывается при выборе пола, например через команду /setgender
    public void setGender(Player player, String gender) {
        String playerName = player.getName();

        // Проверяем, выбрал ли игрок уже пол
        if (tempGenders.containsKey(playerName)) {
            player.sendMessage(ChatColor.RED + "Вы уже выбрали пол.");
            return;
        }

        // Проверяем корректность выбора пола
        if (gender.equals("male") || gender.equals("female")) {
            tempGenders.put(playerName, gender);
            askAge(player, gender);
        } else {
            player.sendMessage(ChatColor.RED + "Неверный выбор пола. Попробуйте снова.");
            askGender(player);
        }
    }

    private void askAge(Player player, String gender) {
        player.sendMessage(ChatColor.YELLOW + "Выберите возрастную группу:");

        TextComponent youngOption;
        TextComponent adultOption;
        TextComponent oldOption;

        if (gender.equals("male")) {
            youngOption = new TextComponent(ChatColor.GOLD + "[Юноша]");
            adultOption = new TextComponent(ChatColor.GOLD + "[Мужчина]");
            oldOption = new TextComponent(ChatColor.GOLD + "[Пожилой]");
        } else {
            youngOption = new TextComponent(ChatColor.GOLD + "[Девушка]");
            adultOption = new TextComponent(ChatColor.GOLD + "[Женщина]");
            oldOption = new TextComponent(ChatColor.GOLD + "[Пожилая]");
        }

        // Привязываем команды к кликам на возрастные группы
        youngOption.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setage young"));
        adultOption.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setage adult"));
        oldOption.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setage old"));

        // Отправляем игроку кликабельные сообщения
        player.spigot().sendMessage(youngOption);
        player.spigot().sendMessage(adultOption);
        player.spigot().sendMessage(oldOption);
    }

    // Метод вызывается при выборе возраста, например через команду /setage
    public void setAge(Player player, String ageGroup) {
        String playerName = player.getName();

        // Проверяем, выбрал ли игрок уже возрастную группу
        if (tempAgeGroups.containsKey(playerName)) {
            player.sendMessage(ChatColor.RED + "Вы уже выбрали возраст.");
            return;
        }

        // Проверяем корректность выбора возраста
        if (ageGroup.equals("young") || ageGroup.equals("adult") || ageGroup.equals("old")) {
            tempAgeGroups.put(playerName, ageGroup);
            player.sendMessage(ChatColor.YELLOW + "Введите вашу национальность (одно слово, не менее 2 символов, с заглавной буквы, не более 16 символов):");
        } else {
            player.sendMessage(ChatColor.RED + "Неверный выбор возраста. Попробуйте снова.");
            askAge(player, tempGenders.get(playerName));
        }
    }

    // Обработчик ввода чата для получения национальности
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        // Проверяем, вводит ли игрок национальность
        if (tempAgeGroups.containsKey(playerName)) {
            String nationality = event.getMessage().trim();

            // Валидация национальности
            if (isValidNationality(nationality)) {
                // Сохраняем данные игрока
                savePlayerData(playerName, tempGenders.get(playerName), tempAgeGroups.get(playerName), nationality);
                player.sendMessage(ChatColor.GREEN + "Ваш персонаж успешно создан!");

                // Очищаем временные данные и разблокируем движение
                tempGenders.remove(playerName);
                tempAgeGroups.remove(playerName);
                isRegistering.put(playerName, false);
            } else {
                player.sendMessage(ChatColor.RED + "Неверный формат национальности. Попробуйте снова.");
            }

            // Отменяем сообщение, чтобы оно не отображалось в общем чате
            event.setCancelled(true);
        }
    }

    // Валидация национальности
    private boolean isValidNationality(String nationality) {
        return Pattern.matches("[А-Я][а-я]{1,15}", nationality) && nationality.length() >= 2;
    }

    // Метод для сохранения данных игрока
    private void savePlayerData(String playerName, String gender, String ageGroup, String nationality) {
        JsonObject playerData = new JsonObject();
        playerData.addProperty("gender", gender);
        playerData.addProperty("ageGroup", ageGroup);
        playerData.addProperty("nationality", nationality);

        personsData.add(playerName, playerData);
        DataUtils.saveDatabase(personsFile, personsData);
    }

    // Проверяем, установлен ли пол в базе данных
    public boolean isGenderSetInDatabase(String playerName) {
        if (personsData.has(playerName)) {
            JsonObject playerData = personsData.getAsJsonObject(playerName);
            return playerData.has("gender"); // Проверяем наличие данных о поле
        }
        return false;
    }

    // Проверяем, установлен ли возраст в базе данных
    public boolean isAgeSetInDatabase(String playerName) {
        if (personsData.has(playerName)) {
            JsonObject playerData = personsData.getAsJsonObject(playerName);
            return playerData.has("ageGroup"); // Проверяем наличие данных о возрасте
        }
        return false;
    }
}
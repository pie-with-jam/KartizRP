package ru.AlertKaput.nationsForge.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.gson.JsonObject;
import ru.AlertKaput.nationsForge.NationsForge;
import ru.AlertKaput.nationsForge.menus.Country;
import ru.AlertKaput.nationsForge.menus.IdeologyMenu;
import ru.AlertKaput.nationsForge.utils.DataUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class StateRegistration {
    private static final Map<Player, StateRegistration> registrations = new HashMap<>();
    private final Player player;
    private String stateName;
    private String stateMotto;
    private String stateIdeology;
    private String leaderTitle;
    private int step = 0;

    public StateRegistration(Player player) {
        this.player = player;

        // Проверка, является ли игрок членом государства
        File databaseFile = NationsForge.getDatabaseFile();
        JsonObject database = DataUtils.loadDatabase(databaseFile);

        boolean hasState = false;

        // Проходим по каждой стране в базе данных
        for (String key : database.keySet()) {
            JsonObject stateData = database.getAsJsonObject(key);

            // Проверяем, есть ли у игрока запись о стране в любом из полей
            if (stateData.has("ruler") && stateData.get("ruler").getAsString().equals(player.getName())) {
                hasState = true;
                break;
            }
        }

        if (hasState) {
            player.sendMessage(ChatColor.RED + "Вы не можете создать страну, так как вы уже являетесь членом государства.");
        } else {
            registrations.put(player, this);
            startProcess();
        }
    }

    public void setStateIdeology(String ideology) {
        this.stateIdeology = ideology;
        player.sendMessage(ChatColor.AQUA + "Введите титул лидера " + ChatColor.YELLOW + "(до 64 символов, с заглавной буквы):");
        step++;
    }

    private void startProcess() {
        player.sendMessage(ChatColor.AQUA + "Введите название государства " + ChatColor.YELLOW + "(до 64 символов, каждое слово с заглавной буквы):");
    }


    public void handleChatInput(String input) {
        input = formatInput(input);

        if (input.length() < 2 || input.length() > 64) {
            player.sendMessage(ChatColor.RED + "Введенная строка должна содержать от 2 до 64 символов. Попробуйте снова:");
            return;
        }

        switch (step) {
            case 0:
                this.stateName = input;
                player.sendMessage(ChatColor.AQUA + "Введите национальный девиз " + ChatColor.YELLOW + "(до 64 символов, с заглавной буквы):");
                step++;
                break;
            case 1:
                this.stateMotto = input;
                // Открываем меню выбора идеологии
                IdeologyMenu menu = new IdeologyMenu();
                Bukkit.getScheduler().runTask(NationsForge.getPlugin(NationsForge.class), () -> menu.openMenu(player));
                player.sendMessage(ChatColor.AQUA + "Выберите идеологию в открывшемся меню.");
                step++;
                break;
            case 2:
                // Этот шаг обрабатывается в меню идеологий
                player.sendMessage(ChatColor.RED + "Пожалуйста, выберите идеологию из меню.");
                break;
            case 3:
                this.leaderTitle = input;
                saveState();
                player.sendMessage(ChatColor.GREEN + "Государство успешно зарегистрировано!");
                // Разблокируем движение игрока
                player.setWalkSpeed(0.2f);
                player.setFlySpeed(0.1f);
                registrations.remove(player);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Произошла ошибка. Попробуйте снова.");
                break;
        }
    }

    private void saveState() {
        File databaseFile = new File(JavaPlugin.getProvidingPlugin(getClass()).getDataFolder(), "database.json");
        JsonObject database = DataUtils.loadDatabase(databaseFile);

        JsonObject stateData = new JsonObject();
        stateData.addProperty("name", stateName);
        stateData.addProperty("motto", stateMotto);
        stateData.addProperty("ideology", stateIdeology);
        stateData.addProperty("leaderTitle", leaderTitle);
        stateData.addProperty("ruler", player.getName());

        // Добавляем новые поля
        stateData.addProperty("exitFee", 100);  // Пошлина за выезд
        stateData.addProperty("dailyTax", 20);  // Ежедневный налог
        stateData.addProperty("minSalary", 200);  // Минимальная зарплата

        stateData.addProperty("join", "open");

        // Добавляем пустой список участников
        stateData.add("members", new JsonObject());  // Создаем пустой объект для участников

        // Форматируем текущую дату в формат ДД.ММ.ГГГГ
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String creationDate = dateFormat.format(new Date());
        stateData.addProperty("creationDate", creationDate);  // Дата создания страны

        database.add(stateName, stateData);
        DataUtils.saveDatabase(databaseFile, database);
    }

    public static StateRegistration getRegistration(Player player) {
        return registrations.get(player);
    }

    private String formatInput(String input) {
        if (input.length() < 2) {
            return "";
        }

        String[] words = input.split("[ -]");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formatted.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return formatted.toString().trim();
    }
}
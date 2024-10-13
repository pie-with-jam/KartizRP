package ru.AlertKaput.nationsForge.menus;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.AlertKaput.nationsForge.NationsForge;
import ru.AlertKaput.nationsForge.commands.CountryCommand;
import ru.AlertKaput.nationsForge.utils.DataUtils;
import ru.AlertKaput.nationsForge.utils.MenuBuilder;
import ru.AlertKaput.nationsForge.utils.ColorUtils;

import javax.print.DocFlavor;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class CountryList {

    public CountryList() {
    }

    public static final Map<Player, Integer> playerPages = new HashMap<>();

    @SuppressWarnings("deprecation")
    public static void openMenu(Player player, int page) {

        // Объявление переменных цветов
        String beforeColor = ColorUtils.hexToRgb("7e7e7e");
        String afterColor = ColorUtils.hexToRgb("1e93d6");
        String whiteColor = ColorUtils.hexToRgb("ffffff");
        String valueColor = ColorUtils.hexToRgb("7dfcd1");
        String itemColor = ColorUtils.hexToRgb("fcfc54");

        String TodayColor = ColorUtils.hexToRgb("54fc54");
        String YesterdayColor = ColorUtils.hexToRgb("fcfc54");
        String Yesterday1Color = ColorUtils.hexToRgb("fc5454");
        String Yesterday2Color = ColorUtils.hexToRgb("a80000");
        String notificationColor = ColorUtils.hexToRgb("a9a9a9");
        String invokeColor = ColorUtils.hexToRgb("fca800");

        // Загружаем базу данных
        File databaseFile = NationsForge.getDatabaseFile();
        JsonObject database = DataUtils.loadDatabase(databaseFile);

        // Количество стран на странице
        int itemsPerPage = 36;
        int startIndex = page * itemsPerPage;
        int endIndex = startIndex + itemsPerPage;

        List<String> countryNames = new ArrayList<>(database.keySet());
        int totalPages = (int) Math.ceil(countryNames.size() / (double) itemsPerPage);

        MenuBuilder menu = new MenuBuilder("2", "lorem ipsum", 6, NationsForge.getInstance());

        // Серые стеклянные панели
        int[] grayPaneSlots = {9,10,11,12,13,14,15,16,17};
        for (int slot : grayPaneSlots) {
            menu.addItem(slot, Material.GRAY_STAINED_GLASS_PANE, " ");
        }

        menu.addItem(4, Material.NETHER_STAR, YesterdayColor + "Nether Star");

        // Перебор всех стран в базе данных
        // Добавление стран на текущей странице
        int index = 18;
        for (int i = startIndex; i < endIndex && i < countryNames.size(); i++) {
            String key = countryNames.get(i);
            JsonObject stateData = database.getAsJsonObject(key);

            // Получение правителя страны и проверка онлайн-статуса
            String ruler = stateData.get("ruler").getAsString();
            Player rulerPlayer = Bukkit.getPlayerExact(ruler);
            if (rulerPlayer == null || !rulerPlayer.isOnline()) {
                continue;  // Пропускаем страну, если правитель оффлайн
            }


            // Получение данных о стране
            String stateName = stateData.get("name").getAsString();
            String motto = stateData.get("motto").getAsString();
            String ideology = stateData.get("ideology").getAsString();
            int exitFee = stateData.get("exitFee").getAsInt();
            int dailyTax = stateData.get("dailyTax").getAsInt();
            int minSalary = stateData.get("minSalary").getAsInt();
            String creationDate = stateData.get("creationDate").getAsString();

            // Создание описания для страны
            List<String> countryDescription = new ArrayList<>();
            countryDescription.add(beforeColor + "| Национальный девиз: ");
            countryDescription.add(whiteColor + motto);
            countryDescription.add("");
            countryDescription.add(beforeColor + "| Правитель " + afterColor + ruler);
            countryDescription.add(beforeColor + "| Идеология " + valueColor + ideology);
            countryDescription.add("");
            countryDescription.add(whiteColor + "Основные законы:");
            countryDescription.add(beforeColor + "| Пошлина за выезд " + afterColor + exitFee + valueColor + "₪");
            countryDescription.add(beforeColor + "| Ежедневный налог " + afterColor + dailyTax + valueColor + "₪");
            countryDescription.add("");
            countryDescription.add(whiteColor + "Доп. информация:");
            countryDescription.add(beforeColor + "| Минимальная ЗП " + afterColor + minSalary + valueColor + "₪");
            countryDescription.add(beforeColor + "| Страна с " + afterColor + creationDate);

            // Добавление страны в меню
            menu.addItem(index, Material.GRAY_TERRACOTTA, whiteColor + stateName, countryDescription);
            index++;  // Переход к следующему слоту
        }

        // Кнопка "Вперед"
        if (page < totalPages - 1) {
            menu.addItem(53, Material.ARROW, ChatColor.GREEN + "Вперед", Collections.singletonList(ChatColor.YELLOW + "Перейти на следующую страницу"));
        }

        // Кнопка "Назад"
        if (page > 0) {
            menu.addItem(45, Material.ARROW, ChatColor.GREEN + "Назад", Collections.singletonList(ChatColor.YELLOW + "Вернуться на предыдущую страницу"));
        }


        // Открываем меню для игрока
        menu.open(player);

        playerPages.put(player, page);
    }
}
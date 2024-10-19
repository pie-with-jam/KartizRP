package ru.AlertKaput.nationsForge.menus;

import com.google.gson.JsonObject;
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
import java.util.Arrays;
import java.util.List;

public class MyCountry {

    @SuppressWarnings("deprecation")
    public void openMenu(Player player) {

        // Объявление переменных цветов
        String beforeColor = ColorUtils.hexToRgb("7e7e7e");
        String afterColor = ColorUtils.hexToRgb("1e93d6");
        String whiteColor = ColorUtils.hexToRgb("ffffff");
        String valueColor = ColorUtils.hexToRgb("7dfcd1");
        String itemColor = ColorUtils.hexToRgb("fcfc54");
        String turquoise = ColorUtils.hexToRgb("5e9c9e");
        String green = ColorUtils.hexToRgb("6a8c23");
        String darkRed = ColorUtils.hexToRgb("a80000");

        String TodayColor = ColorUtils.hexToRgb("54fc54");
        String YesterdayColor = ColorUtils.hexToRgb("fcfc54");
        String Yesterday1Color = ColorUtils.hexToRgb("fc5454");
        String Yesterday2Color = ColorUtils.hexToRgb("a80000");
        String notificationColor = ColorUtils.hexToRgb("a9a9a9");
        String invokeColor = ColorUtils.hexToRgb("fca800");

        MenuBuilder menu = new MenuBuilder("3", "lorem ipsum", 6, NationsForge.getInstance());

        // Серые стеклянные панели
        int[] grayPaneSlots = {1,9,10,11,12,14,15,16,17,18,22,27,31,36,40,45,49,50,51,52,53};
        for (int slot : grayPaneSlots) {
            menu.addItem(slot, Material.GRAY_STAINED_GLASS_PANE, " ");
        }

        menu.addItem(0, Material.WHEAT, whiteColor + "Уплата ежедневного налога",
                Arrays.asList(
                        beforeColor + "Нажмите " + afterColor + "ЛКМ" + beforeColor + " чтобы уплатить",
                        afterColor + "ПКМ" + beforeColor + ", поставить автоуплату при заходе"
                ));

        menu.addItem(2, Material.BOOK, whiteColor + "Основные законы гос-ва",
                Arrays.asList(
                        beforeColor + "| Пошлина за выезд: " + afterColor + "100" + valueColor + "₪",
                        beforeColor + "| Ежедневный налог: " + afterColor + "20" + valueColor + "₪",
                        beforeColor + "| Минимальная ЗП: " + afterColor + "200" + valueColor + "₪"
                ));

        menu.addItem(3, Material.RAW_GOLD, itemColor + "Казна государства",
                Arrays.asList(
                        beforeColor + "Денег в казне: " + afterColor + "3000" + valueColor + "₪",
                        beforeColor + "| Нажмите " + afterColor + "ЛКМ" + beforeColor + " чтобы пополнить",
                        beforeColor + "| Нажмите " + afterColor + "ПКМ" + beforeColor + " чтобы снять",
                        beforeColor + "| " + afterColor + "СКМ, " + beforeColor + "открыть меню выдачи зарплат"
                ));

        menu.addItem(4, Material.LEGACY_EMPTY_MAP, whiteColor + "Налоговые уклонисты",
                Arrays.asList(
                        beforeColor + "| " + afterColor + "bobik40 " + beforeColor + "Не уплочены (" + TodayColor + "Сегодня" + beforeColor + ")",
                        beforeColor + "| " + afterColor + "bobik41 " + beforeColor + "Не уплочены (" + YesterdayColor + "2 дня" + beforeColor + ")",
                        beforeColor + "| " + afterColor + "bobik42 " + beforeColor + "Не уплочены (" + Yesterday1Color + "3 дня" + beforeColor + ")",
                        beforeColor + "| " + afterColor + "bobik43 " + beforeColor + "Не уплочены (" + Yesterday2Color + "4+ дня" + beforeColor + ")"
                ));

        menu.addItem(7, Material.IRON_HORSE_ARMOR, notificationColor + "Уведомления",
                Arrays.asList(
                        beforeColor + "| Принятие/отклонение дип. предложений",
                        beforeColor + "| И другие события с точными датами"
                ));

        menu.addItem(5, Material.MINECART, whiteColor + "Установить точку приезда",
                Arrays.asList(
                        beforeColor + "| Установите точку, где будут появляться",
                        beforeColor + "новые жители. (Установить там где вы стоите)"
                ));

        menu.addItem(6, Material.END_CRYSTAL, whiteColor + "Смена идеологии",
                Arrays.asList(
                        beforeColor + "| Смена нынешней идеологии или девиза (кд раз в сутки)",
                        afterColor + "ЛКМ - Изменить нынешнюю идеологию гос-ва",
                        afterColor + "ПКМ - Изменить национальный девиз"
                ));

        JsonObject database = DataUtils.loadDatabase(NationsForge.getDatabaseFile());
        for (String key : database.keySet()) {
            JsonObject stateData = database.getAsJsonObject(key);
            String playerName = player.getName();

            // Проверяем, является ли игрок правителем страны
            if (stateData.has("ruler") && stateData.get("ruler").getAsString().equals(playerName)) {
                // Проверяем текущее состояние join
                String currentJoinStatus = stateData.has("join") ? stateData.get("join").getAsString() : "open";

                if (currentJoinStatus.equals("close")) {
                    // Меняем значение join на открытое
                    menu.addItem(13, Material.RED_STAINED_GLASS_PANE, darkRed + "Въезд в страну закрыт",
                            Arrays.asList(
                                    beforeColor + "| В вашу страну нельзя въехать",
                                    afterColor + "ЛКМ - Сделать страну открытой для въезда",
                                    afterColor + "СКМ - Сделать въезд по заявкам"
                            ));
                } else if (currentJoinStatus.equals("open")) {
                    menu.addItem(13, Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "Открыта для въезда",
                            Arrays.asList(
                                    beforeColor + "| В вашей стране может поселиться кто желает",
                                    afterColor + "ПКМ - Сделать страну закрытой для въезда",
                                    afterColor + "СКМ - Сделать въезд по заявкам"
                            ));
                } else if (currentJoinStatus.equals("invites")) {
                    menu.addItem(13, Material.BLUE_STAINED_GLASS_PANE, ChatColor.BLUE + "Для въезда нужно разрешение",
                            Arrays.asList(
                                    beforeColor + "| В вашу страну можно въехать оставив заявку",
                                    afterColor + "ЛКМ - Сделать страну открытой для въезда",
                                    afterColor + "ПКМ - Сделать страну закрытой для въезда"
                            ));
                }
            }
        }

        menu.addItem(46, Material.RED_STAINED_GLASS_PANE, Yesterday1Color + "Покинуть страну",
                Arrays.asList(
                        beforeColor + "| Будьте внимательны!",
                        beforeColor + "| В своих указах правители могут",
                        beforeColor + "| Менять политику в отношении эмигрантов"
                ));

        menu.addItem(47, Material.ORANGE_STAINED_GLASS_PANE, invokeColor + "Уволиться с работы",
                List.of(
                        beforeColor + "| Ваш статус заменится на \"Безработный\""
                ));

        menu.addItem(48, Material.YELLOW_STAINED_GLASS_PANE, YesterdayColor + "Протестовать!",
                Arrays.asList(
                        beforeColor + "| Недовольны положением себя и своих граждан?",
                        beforeColor + "| Хотите большую зарплату, низкие налоги?",
                        beforeColor + "| Но не в состоянии или не желаете уехать из страны?",
                        beforeColor + "| Ваше священное право - протестовать!"
                ));

        // Обновляем головы в нужные слоты
        updateHeadSlots(menu, beforeColor, afterColor, whiteColor);

        menu.addItem(8, Material.CARTOGRAPHY_TABLE, whiteColor + "Внешняя политика",
                List.of(
                        beforeColor + "Меню взаимодействия с другими странами"
                ));

        // Открываем меню для игрока
        menu.open(player);
    }

    private void updateHeadSlots(MenuBuilder menu, String beforeColor, String afterColor, String whiteColor) {
        // Голова президента
        menu.addItem(20, Material.PLAYER_HEAD, whiteColor + "Президент",
                Arrays.asList(
                        beforeColor + "| Статус: " + afterColor + "Президент",
                        afterColor + "ЛКМ - Сменить название своего титула"
                ));

        // Головы министров
        String[] ministerNames = {"bobik32", "bobik33", "bobik34", "bobik35", "bobik36", "bobik37"};
        String ministerStatuses = "Министр";
        int[] ministerSlots = {28, 29, 30, 37, 38, 39};

        for (int i = 0; i < ministerNames.length; i++) {
            menu.addItem(ministerSlots[i], Material.PLAYER_HEAD, whiteColor + ministerNames[i],
                    Arrays.asList(
                            beforeColor + "| Статус: " + afterColor + ministerStatuses,
                            beforeColor + "| Нажмите " + afterColor + "ЛКМ " + beforeColor + "чтобы изменить статус",
                            beforeColor + "| " + afterColor + "ПКМ" + beforeColor + ", - разжаловать из правительства",
                            beforeColor + "| " + afterColor + "ЛКМ" + beforeColor + ", - передать полномочия лидера государства"
                    ));
        }

        // Головы разнорабочих
        String[] workerNames = {"bobik40", "bobik41", "bobik42", "bobik43", "bobik44", "bobik45", "bobik40", "bobik41", "bobik42", "bobik43", "bobik44", "bobik45"};
        String workerStatus = "разнорабочий";
        int[] workerSlots = {23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44};

        for (int i = 0; i < workerNames.length; i++) {
            menu.addItem(workerSlots[i], Material.PLAYER_HEAD, whiteColor + workerNames[i],
                    Arrays.asList(
                            beforeColor + "| Cтатус: " + afterColor + workerStatus,
                            beforeColor + "| Нажмите " + afterColor + "ЛКМ " + beforeColor + "чтобы изменить статус",
                            beforeColor + "| " + afterColor + "ПКМ " + beforeColor + "чтобы выдворить*",
                            beforeColor + "| " + afterColor + "СКМ" + beforeColor + ", чтобы сделать сленом правительства",
                            beforeColor + "| * - Налог за выезд не взимается"
                    ));
        }
    }
}
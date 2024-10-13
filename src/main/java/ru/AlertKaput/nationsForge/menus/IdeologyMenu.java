package ru.AlertKaput.nationsForge.menus;

import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.AlertKaput.nationsForge.NationsForge;
import ru.AlertKaput.nationsForge.utils.ColorUtils;
import ru.AlertKaput.nationsForge.utils.DataUtils;
import ru.AlertKaput.nationsForge.utils.MenuBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class IdeologyMenu {

    @SuppressWarnings("deprecation")
    public void openMenu(Player player) {
        // Получаем имя игрока
        String playerName = player.getName();

        String beforeColor = ColorUtils.hexToRgb("7e7e7e");
        String afterColor = ColorUtils.hexToRgb("7e7e00");

        // Создаём меню с названием "Персональная информация" и 4 строками (4*9 слотов)
        MenuBuilder menu = new MenuBuilder("4", "Lorem ipsum", 1, NationsForge.getInstance());

        // Серые стеклянные панели
        int[] grayPaneSlots = {0, 8};
        for (int slot : grayPaneSlots) {
            menu.addItem(slot, Material.GRAY_STAINED_GLASS_PANE, " "); // Панели без названия
        }

        // Информация о игроке (пол, возраст, национальность)
        menu.addItem(1, Material.BLACK_CONCRETE, ChatColor.DARK_RED + "Коммунизм",
                List.of(
                        beforeColor + "| Социальное равенство с отсутствием частной собственности"
                )
        );

        menu.addItem(2, Material.RED_CONCRETE, ChatColor.RED + "Социализм",
                List.of(
                        beforeColor + "| Социальное равенство, де-юре частная собственность"
                )
        );

        menu.addItem(3, Material.CYAN_CONCRETE, ChatColor.DARK_AQUA + "Социал-Либерализм",
                List.of(
                        beforeColor + "| Права и свобода граждан; с вмешательством гос-ва в экономику"
                )
        );

        menu.addItem(4, Material.BLUE_CONCRETE, ChatColor.DARK_BLUE + "Либерализм",
                List.of(
                        beforeColor + "| Права и свобода граждан - превыше всего"
                )
        );

        menu.addItem(5, Material.LIGHT_BLUE_CONCRETE, ChatColor.AQUA + "Консерватизм",
                List.of(
                        beforeColor + "| Традиционные ценности и стабильность"
                )
        );

        menu.addItem(6, Material.YELLOW_CONCRETE, ChatColor.YELLOW + "Национализм",
                List.of(
                        beforeColor + "| Титульная нация - приоритет в любом вопросе"
                )
        );

        menu.addItem(7, Material.BLACK_CONCRETE, afterColor + "Фашизм",
                List.of(
                        beforeColor + "| Радикальная защита национальных идей"
                )
        );

        // Открываем меню для игрока
        menu.open(player);
    }
}
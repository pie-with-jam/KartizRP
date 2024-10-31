package ru.AlertKaput.nationsForge.events;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.AlertKaput.nationsForge.NationsForge;
import ru.AlertKaput.nationsForge.menus.CountryList;
import ru.AlertKaput.nationsForge.menus.MyCountry;
import ru.AlertKaput.nationsForge.utils.DataUtils;
import ru.AlertKaput.nationsForge.utils.MenuBuilder;
import ru.AlertKaput.nationsForge.utils.StateRegistration;

import static ru.AlertKaput.nationsForge.menus.CountryList.openMenu;
import static ru.AlertKaput.nationsForge.menus.CountryList.playerPages;

public class GuiEvents implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        String menuId = MenuBuilder.getMenuId(event.getClickedInventory());
        if (menuId == null) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;

        // Логика меню с ID 1
        if (menuId.equals("1")) {
            event.setCancelled(true);
            if (clickedItem.getType() == Material.GRAY_STAINED_GLASS_PANE) {
                player.closeInventory();
                player.performCommand("menu");
            }
            if (clickedItem.getType() == Material.TOTEM_OF_UNDYING) {
                player.closeInventory();
                MyCountry menu = new MyCountry();
                menu.openMenu(player);
            }
            if (clickedItem.getType() == Material.MAP && clickedItem.getData().getData() == 0) {
                // Открываем меню "Country"
                CountryList menu = new CountryList();
                openMenu(player, 0);
            }
            if (clickedItem.getType() == Material.NETHER_STAR) {
                // Проверка, является ли игрок правителем или членом другой страны
                JsonObject database = DataUtils.loadDatabase(NationsForge.getDatabaseFile());

                for (String key : database.keySet()) {
                    JsonObject stateData = database.getAsJsonObject(key);

                    // Проверка, является ли игрок правителем
                    if (stateData.has("ruler") && stateData.get("ruler").getAsString().equals(player.getName())) {
                        player.sendMessage(ChatColor.RED + "Вы уже являетесь правителем страны " + stateData.get("name").getAsString() + ", и не можете создать новую.");
                        player.closeInventory();
                        return;  // Прерываем дальнейшее выполнение
                    }

                    // Проверка, является ли игрок участником страны
                    if (stateData.has("members") && stateData.get("members").isJsonArray()) {
                        JsonArray members = stateData.getAsJsonArray("members");
                        for (int i = 0; i < members.size(); i++) {
                            if (members.get(i).getAsString().equals(player.getName())) {
                                player.sendMessage(ChatColor.RED + "Вы уже являетесь участником страны " + stateData.get("name").getAsString() + ", и не можете создать новую.");
                                player.closeInventory();
                                return;  // Прерываем дальнейшее выполнение
                            }
                        }
                    }
                }

                // Если проверки пройдены, открываем регистрацию новой страны
                player.closeInventory();
                new StateRegistration(player);
            }
        }

        // Логика меню с ID 4
        if (menuId.equals("4")) {
            event.setCancelled(true);
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                String displayName = clickedItem.getItemMeta().getDisplayName();

                // Проверяем, не является ли предмет серой стеклянной панелью
                if (!displayName.equals(" ")) {
                    String plainName = ChatColor.stripColor(displayName);
                    StateRegistration registration = StateRegistration.getRegistration(player);
                    if (registration != null) {
                        registration.setStateIdeology(plainName);
                        player.closeInventory();
                    }
                }
            }
        }

        // Логика меню с ID 2 (список стран)
        if (menuId.equals("2")) {
            event.setCancelled(true);
            if (event.isLeftClick()) {
                int slot = event.getSlot();
                JsonObject database = DataUtils.loadDatabase(NationsForge.getDatabaseFile());
                int currentPage = playerPages.getOrDefault(player, 0);

                // Переход на следующую страницу
                if (clickedItem.getType() == Material.ARROW && slot == 53) {
                    openMenu(player, currentPage + 1);
                    return;
                }

                // Переход на предыдущую страницу
                if (clickedItem.getType() == Material.ARROW && slot == 45) {
                    openMenu(player, currentPage - 1);
                    return;
                }

                // Проверка, является ли игрок правителем или членом другой страны
                for (String key : database.keySet()) {
                    JsonObject stateData = database.getAsJsonObject(key);
                    if (stateData.get("ruler").getAsString().equals(player.getName())) {
                        player.sendMessage(ChatColor.RED + "Вы уже правитель страны " + stateData.get("name").getAsString());
                        return;
                    }

                    // Проверяем, является ли игрок членом страны
                    if (stateData.get("members").isJsonArray()) {
                        JsonArray members = stateData.getAsJsonArray("members");
                        for (int i = 0; i < members.size(); i++) {
                            if (members.get(i).getAsString().equals(player.getName())) {
                                player.sendMessage(ChatColor.RED + "Вы уже член страны " + stateData.get("name").getAsString());
                                return;
                            }
                        }
                    }
                }

                // Логика присоединения к стране
                String countryName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                JsonObject stateData = database.getAsJsonObject(countryName);

                // Проверяем, что members является массивом
                if (!stateData.get("members").isJsonArray()) {
                    stateData.add("members", new JsonArray());
                }

                JsonArray members = stateData.getAsJsonArray("members");
                members.add(player.getName());

                // Сохраняем обновленную базу данных
                DataUtils.saveDatabase(NationsForge.getDatabaseFile(), database);

                player.sendMessage(ChatColor.GREEN + "Вы присоединились к стране " + ChatColor.AQUA + countryName + ChatColor.GREEN + "!");
            }
        }

        if (menuId.equals("3")) {
            event.setCancelled(true);
            int slot = event.getSlot();
            JsonObject database = DataUtils.loadDatabase(NationsForge.getDatabaseFile());
            String playerName = player.getName();

            // Если нажата панелька для изменения статуса (slot == 13)
            if (slot == 13) {
                for (String key : database.keySet()) {
                    JsonObject stateData = database.getAsJsonObject(key);

                    // Проверяем, является ли игрок правителем страны
                    if (stateData.has("ruler") && stateData.get("ruler").getAsString().equals(playerName)) {
                        String currentJoinStatus = stateData.has("join") ? stateData.get("join").getAsString() : "open";

                        if (event.isLeftClick()) {
                            if (currentJoinStatus.equals("close")) {
                                stateData.addProperty("join", "open");
                                player.sendMessage(ChatColor.GREEN + "Статус вступления в страну " + ChatColor.AQUA + stateData.get("name").getAsString() + ChatColor.GREEN + " изменён на открытый!");
                            } else if (currentJoinStatus.equals("invites")) {
                                stateData.addProperty("join", "open");
                                player.sendMessage(ChatColor.GREEN + "Статус вступления в страну " + ChatColor.AQUA + stateData.get("name").getAsString() + ChatColor.GREEN + " изменён на открытый!");
                            }
                        } else if (event.isRightClick()) {
                            if (currentJoinStatus.equals("open")) {
                                stateData.addProperty("join", "close");
                                player.sendMessage(ChatColor.GREEN + "Статус вступления в страну " + ChatColor.AQUA + stateData.get("name").getAsString() + ChatColor.RED + " изменён на закрытый!");
                            } else if (currentJoinStatus.equals("invites")) {
                                stateData.addProperty("join", "close");
                                player.sendMessage(ChatColor.GREEN + "Статус вступления в страну " + ChatColor.AQUA + stateData.get("name").getAsString() + ChatColor.RED + " изменён на закрытый!");
                            }
                        } else if (event.getClick().isMouseClick()) {
                            stateData.addProperty("join", "invites");
                            player.sendMessage(ChatColor.GREEN + "Статус вступления в страну " + ChatColor.AQUA + stateData.get("name").getAsString() + ChatColor.YELLOW + " изменён на по заявкам!");
                        }

                        DataUtils.saveDatabase(NationsForge.getDatabaseFile(), database);

                        player.closeInventory();
                        MyCountry menu = new MyCountry();
                        menu.openMenu(player);
                        return;
                    }
                }
            }
        }
    }
}
package ru.AlertKaput.nationsForge;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.AlertKaput.nationsForge.utils.DataUtils;

import java.io.File;

public class StatePlaceholder extends PlaceholderExpansion {

    private final NationsForge plugin;

    public StatePlaceholder(NationsForge plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "state";
    }

    @Override
    public String getAuthor() {
        return "YourName";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("name")) {
            File databaseFile = plugin.getDatabaseFile();
            JsonObject database = DataUtils.loadDatabase(databaseFile);

            if (database == null) {
                plugin.getLogger().warning("Database is null or failed to load!");
                return "No State";  // Если база данных не загружена
            }

            for (String key : database.keySet()) {
                JsonObject stateData = database.getAsJsonObject(key);

                // Проверяем правителя
                if (stateData.has("ruler")) {
                    String ruler = stateData.get("ruler").getAsString();
                    //plugin.getLogger().info("Checking ruler: " + ruler);

                    if (ruler.equalsIgnoreCase(player.getName())) {
                        //plugin.getLogger().info(player.getName() + " is the ruler of " + stateData.get("name").getAsString());
                        return stateData.get("name").getAsString();
                    }
                }

                // Проверяем членов страны
                if (stateData.has("members") && stateData.get("members").isJsonArray()) {
                    JsonArray members = stateData.getAsJsonArray("members");

                    for (int i = 0; i < members.size(); i++) {
                        String memberName = members.get(i).getAsString();
                        //plugin.getLogger().info("Checking member: " + memberName);

                        if (memberName.equalsIgnoreCase(player.getName())) {
                            //plugin.getLogger().info(player.getName() + " is a member of " + stateData.get("name").getAsString());
                            return stateData.get("name").getAsString();  // Возвращаем название страны
                        }
                    }
                }
            }
            return "No State";  // Если игрок не является ни правителем, ни членом страны
        }
        return null;  // Если идентификатор не соответствует "name"
    }
}
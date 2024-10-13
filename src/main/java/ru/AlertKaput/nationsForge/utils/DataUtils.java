package ru.AlertKaput.nationsForge.utils;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataUtils {

    private static final Gson gson = new Gson();

    public static JsonObject loadDatabase(File file) {
        JsonObject jsonObject = new JsonObject();
        try (FileReader reader = new FileReader(file)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement.isJsonObject()) {
                jsonObject = jsonElement.getAsJsonObject();
            } else {
                // Логируем и создаем новый объект, если данные не являются JSON объектом
                System.err.println("Expected JSON object, but got: " + jsonElement);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка исключения, если файл не может быть прочитан
        }
        return jsonObject;
    }

    public static void saveDatabase(File file, JsonObject json) {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initializeDatabase(File file) {
        JsonObject json = loadDatabase(file);
        if (json.size() == 0) {
            json = new JsonObject();
            saveDatabase(file, json);
        }
    }
}

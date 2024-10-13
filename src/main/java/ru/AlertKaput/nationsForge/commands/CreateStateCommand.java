package ru.AlertKaput.nationsForge.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.google.gson.JsonObject;
import ru.AlertKaput.nationsForge.NationsForge;
import ru.AlertKaput.nationsForge.utils.DataUtils;

import java.io.File;

public class CreateStateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Команда доступна только игрокам.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage("Используйте: /created <название>");
            return true;
        }

        String stateName = args[0];
        File databaseFile = NationsForge.getDatabaseFile();
        JsonObject database = DataUtils.loadDatabase(databaseFile);

        if (database.has(stateName)) {
            player.sendMessage("Государство с таким названием уже существует.");
            return true;
        }

        JsonObject stateData = new JsonObject();
        stateData.addProperty("name", stateName);
        stateData.addProperty("ruler", player.getName());
        database.add(stateName, stateData);

        DataUtils.saveDatabase(databaseFile, database);
        player.sendMessage("Государство " + stateName + " создано и вы назначены правителем!");
        return true;
    }
}

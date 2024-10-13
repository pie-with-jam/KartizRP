package ru.AlertKaput.nationsForge.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.AlertKaput.nationsForge.events.PlayerJoin;

public class SetGenderAgeCommand implements CommandExecutor {
    private final PlayerJoin joinListener;

    public SetGenderAgeCommand(PlayerJoin joinListener) {
        this.joinListener = joinListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String playerName = player.getName();

            // Проверяем, если команда setgender
            if (label.equalsIgnoreCase("setgender")) {
                if (joinListener.isGenderSetInDatabase(playerName)) { // Проверка по базе данных
                    player.sendMessage(ChatColor.RED + "Вы уже выбрали пол.");
                    return true;
                }

                if (args.length > 0) {
                    String gender = args[0];
                    joinListener.setGender(player, gender);
                    return true;
                }
            }

            // Проверяем, если команда setage
            if (label.equalsIgnoreCase("setage")) {
                if (joinListener.isAgeSetInDatabase(playerName)) { // Проверка по базе данных
                    player.sendMessage(ChatColor.RED + "Вы уже выбрали возраст.");
                    return true;
                }

                if (args.length > 0) {
                    String ageGroup = args[0];
                    joinListener.setAge(player, ageGroup);
                    return true;
                }
            }
        }
        return false;
    }
}
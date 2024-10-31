package ru.AlertKaput.nationsForge;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.AlertKaput.nationsForge.commands.*;
import ru.AlertKaput.nationsForge.events.*;
import ru.AlertKaput.nationsForge.menus.CountryList;
import ru.AlertKaput.nationsForge.utils.DataUtils;
import ru.AlertKaput.nationsForge.utils.MenuBuilder;

import java.io.File;
import java.io.IOException;

public final class NationsForge extends JavaPlugin {
    private static File databaseFile;
    public static NationsForge instance;
    private File personsFile;
    private JsonObject personsData;

    public static Plugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.databaseFile = new File(getDataFolder(), "database.json");

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File databaseFile = new File(dataFolder, "database.json");
        if (!databaseFile.exists()) {
            try {
                databaseFile.createNewFile();
                getLogger().info("database.json file created successfully.");
            } catch (IOException e) {
                getLogger().severe("Failed to create database.json file!");
                e.printStackTrace();
            }
        } else {
            getLogger().info("database.json file already exists.");
        }

        personsFile = new File(dataFolder, "persons.json");
        if (!personsFile.exists()) {
            try {
                personsFile.createNewFile();
                getLogger().info("persons.json file created successfully.");
            } catch (IOException e) {
                getLogger().severe("Failed to create persons.json file!");
                e.printStackTrace();
            }
        }

        personsData = DataUtils.loadDatabase(personsFile);
        DataUtils.initializeDatabase(databaseFile);

        PlayerJoin playerJoin = new PlayerJoin();

        Bukkit.getPluginManager().registerEvents(playerJoin, this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new MovementListener(), this);
        Bukkit.getPluginManager().registerEvents(new EggHitListener(), this);
        Bukkit.getPluginManager().registerEvents(new GuiEvents(), this);
        Bukkit.getPluginManager().registerEvents(new SnowballExtinguishFire(), this);


        this.getCommand("join").setExecutor(new JoinCommand());
        this.getCommand("setgender").setExecutor(new SetGenderAgeCommand(playerJoin));
        this.getCommand("setage").setExecutor(new SetGenderAgeCommand(playerJoin));
        this.getCommand("country").setExecutor(new CountryCommand());
        this.getCommand("c").setExecutor(new CountryCommand());


        this.getCommand("mycountry").setExecutor(new MyCountryCommand());
        this.getCommand("myc").setExecutor(new MyCountryCommand());

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new StatePlaceholder(this).register();
        }

        getLogger().info("BuilderGUI enabled!");
    }


    @Override
    public void onDisable() {
        getLogger().info("BuilderGUI disabled!");
    }

    public static File getDatabaseFile() {
        return databaseFile;
    }

}
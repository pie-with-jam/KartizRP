package ru.alertkaput.kartizrp;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class KartizRP extends JavaPlugin {

    Logger log = getLogger();

    @Override
    public void onEnable() {
        log.info("Your plugin has been enabled!");
        getCommand("country").setExecutor(new Country(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package ru.alertkaput.kartizrp;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class KartizRP extends JavaPlugin {

    private PluginConfig pluginConfig;
    Logger log = getLogger();

    @Override
    public void onEnable() {
        pluginConfig = new PluginConfig(this);
        log.info("Your plugin has been enabled!");
        getCommand("country").setExecutor(new Country(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

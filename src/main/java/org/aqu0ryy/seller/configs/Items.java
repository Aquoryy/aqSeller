package org.aqu0ryy.seller.configs;

import org.aqu0ryy.seller.Loader;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Items {

    private final Loader plugin;
    private File file;
    private YamlConfiguration config;

    public Items(Loader plugin) {
        this.plugin = plugin;
        setup();
    }

    public YamlConfiguration get() {
        return config;
    }

    private void setup() {
        file = new File(plugin.getDataFolder(), "items.yml");

        if (!file.exists()) {
            plugin.saveResource("items.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);;
        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception error) {
            plugin.getLogger().severe(String.valueOf(error));
        }
    }

    public void reload() {
        try {
            config.load(file);
        } catch (Exception error) {
            plugin.getLogger().severe(String.valueOf(error));
        }
    }
}

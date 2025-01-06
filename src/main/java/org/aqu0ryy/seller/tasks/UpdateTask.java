package org.aqu0ryy.seller.tasks;

import org.aqu0ryy.seller.Loader;
import org.aqu0ryy.seller.configs.Config;
import org.aqu0ryy.seller.sql.Database;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateTask extends BukkitRunnable {

    private final Loader plugin;
    private final Database database;
    private final Config config;

    public UpdateTask(Loader plugin, Database database, Config config) {
        this.plugin = plugin;
        this.database = database;
        this.config = config;
    }

    @Override
    public void run() {
        database.setBalance(config.get().getInt("seller-settings.balance"));
        long interval = config.get().getLong("seller-settings.update-balance") * 20L;

        plugin.setNextUpdateTime(System.currentTimeMillis() + interval * 50L);
    }
}
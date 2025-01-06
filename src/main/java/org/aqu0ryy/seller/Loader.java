package org.aqu0ryy.seller;

import org.aqu0ryy.seller.commands.CommandSeller;
import org.aqu0ryy.seller.configs.Config;
import org.aqu0ryy.seller.configs.Items;
import org.aqu0ryy.seller.hooks.VaultHook;
import org.aqu0ryy.seller.placeholders.SellerExpansion;
import org.aqu0ryy.seller.sql.Database;
import org.aqu0ryy.seller.tasks.UpdateTask;
import org.aqu0ryy.seller.utils.ChatUtil;
import org.aqu0ryy.seller.utils.CooldownUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Loader extends JavaPlugin {

    private Config config;
    private Items items;
    private Database database;

    private long nextUpdateTime;

    @Override
    public void onEnable() {
        ChatUtil chatUtil = new ChatUtil();
        VaultHook vaultHook = new VaultHook(this);
        CooldownUtil cooldownUtil = new CooldownUtil();

        if (vaultHook.setupEconomy() && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            config = new Config(this);
            items = new Items(this);
            database = new Database(this, config);

            long interval = config.get().getLong("seller-settings.update-balance") * 20L;
            nextUpdateTime = System.currentTimeMillis() + interval * 50L;

            database.setup();
            new CommandSeller(this, config, items, database, chatUtil, cooldownUtil, vaultHook);
            new UpdateTask(this, database, config).runTaskTimer(this, interval, interval);
            new SellerExpansion(this, config, database).register();
        } else {
            chatUtil.sendMessage(Bukkit.getConsoleSender(), "<red>[x] Установите Vault и PlaceholderAPI.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        database.close();
    }

    public void reload() {
        config.reload();
        items.reload();
    }

    public long getNextUpdateTime() {
        return nextUpdateTime;
    }

    public void setNextUpdateTime(long nextUpdateTime) {
        this.nextUpdateTime = nextUpdateTime;
    }
}
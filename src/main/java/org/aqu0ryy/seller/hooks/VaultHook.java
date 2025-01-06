package org.aqu0ryy.seller.hooks;

import net.milkbowl.vault.economy.Economy;
import org.aqu0ryy.seller.Loader;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private static Economy economy = null;
    private final Loader plugin;

    public VaultHook(Loader plugin) {
        this.plugin = plugin;
    }

    public boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return true;
    }

    public void giveMoney(Player player, int money) {
        economy.depositPlayer(player, money);
    }

    public int getMoney(Player player) {
        return (int) economy.getBalance(player);
    }
}

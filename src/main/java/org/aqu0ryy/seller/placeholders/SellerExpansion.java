package org.aqu0ryy.seller.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.aqu0ryy.seller.Loader;
import org.aqu0ryy.seller.configs.Config;
import org.aqu0ryy.seller.sql.Database;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class SellerExpansion extends PlaceholderExpansion {

    private final Loader plugin;
    private final Config config;
    private final Database database;

    public SellerExpansion(Loader plugin, Config config, Database database) {
        this.plugin = plugin;
        this.config = config;
        this.database = database;
    }

    @Override public @NotNull String getIdentifier() {
        return "aqseller";
    }

    @Override public @NotNull String getAuthor() {
        return "aqu0ryy";
    }

    @Override public @NotNull String getVersion() {
        return "0.0.1";
    }

    @Override public String onRequest(@NotNull OfflinePlayer player, @NotNull String identifier) {
        if (!identifier.isEmpty()) {
            if (identifier.equalsIgnoreCase("timeleft")) {
                long currentTime = System.currentTimeMillis();
                long nextUpdateTime = plugin.getNextUpdateTime();
                long timeUntilUpdate = nextUpdateTime - currentTime;

                if (timeUntilUpdate <= 0) {
                    return "Сейчас";
                }

                long hours = TimeUnit.MILLISECONDS.toHours(timeUntilUpdate);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(timeUntilUpdate) - TimeUnit.HOURS.toMinutes(hours);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(timeUntilUpdate) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeUntilUpdate));

                return String.format("%d ч. %d мин. %d сек.", hours, minutes, seconds);
            }

            if (identifier.equalsIgnoreCase("balance")) {
                return String.valueOf(database.getBalance());
            }
        }

        return  null;
    }
}

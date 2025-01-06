package org.aqu0ryy.seller.sql;

import org.aqu0ryy.seller.Loader;
import org.aqu0ryy.seller.configs.Config;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Database {

    private final Loader plugin;
    private final Config config;

    private Connection connection;

    public Database(Loader plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void setup() {
        File folder = plugin.getDataFolder();

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File db = new File(folder, config.get().getString("database"));

        try {
            if (!db.exists()) {
                db.createNewFile();
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath());
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS seller (id INTEGER PRIMARY KEY, balance INTEGER)");

            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM seller");

            if (resultSet.next() && resultSet.getInt(1) == 0) {
                setBalance(config.get().getInt("seller-settings.balance"));
            }
        } catch (IOException | SQLException error) {
            error.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public int getBalance() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT balance FROM seller LIMIT 1");

            if (resultSet.next()) {
                return resultSet.getInt("balance");
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return 0;
    }

    public void setBalance(int balance) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO seller (id, balance) VALUES (1, ?) ON CONFLICT(id) DO UPDATE SET balance = excluded.balance");
            preparedStatement.setInt(1, balance);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void deductBalance(int amount) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE seller SET balance = balance - ? WHERE id = 1");
            preparedStatement.setInt(1, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
}

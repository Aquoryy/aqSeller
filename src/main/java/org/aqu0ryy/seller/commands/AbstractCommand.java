package org.aqu0ryy.seller.commands;

import org.aqu0ryy.seller.Loader;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    public AbstractCommand(Loader plugin, String command) {
        PluginCommand pluginCommand = plugin.getCommand(command);

        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    public abstract void execute(CommandSender sender, String label, String[] args);

    public List<String> complete(CommandSender sender, String[] args) {
        return null;
    }

    private List<String> filter(List<String> list, String[] args) {
        if (list != null) {
            String last = args[args.length - 1];
            List<String> result = new ArrayList<>();

            for (String arg : list) {
                if (arg.toLowerCase().startsWith(last.toLowerCase())) {
                    result.add(arg);
                }
            }

            return result;
        }

        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        execute(sender, label, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return filter(complete(sender, args), args);
    }
}

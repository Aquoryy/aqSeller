package org.aqu0ryy.seller.commands;

import com.google.common.collect.Lists;
import org.aqu0ryy.seller.Loader;
import org.aqu0ryy.seller.configs.Config;
import org.aqu0ryy.seller.configs.Items;
import org.aqu0ryy.seller.hooks.VaultHook;
import org.aqu0ryy.seller.sql.Database;
import org.aqu0ryy.seller.utils.ChatUtil;
import org.aqu0ryy.seller.utils.CooldownUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class CommandSeller extends AbstractCommand {

    private final Loader plugin;
    private final Config config;
    private final Items items;
    private final Database database;
    private final ChatUtil chatUtil;
    private final CooldownUtil cooldownUtil;
    private final VaultHook vaultHook;

    public CommandSeller(Loader plugin, Config config, Items items, Database database, ChatUtil chatUtil,
                         CooldownUtil cooldownUtil,
                         VaultHook vaultHook) {
        super(plugin, "aqseller");
        this.plugin = plugin;
        this.config = config;
        this.items = items;
        this.database = database;
        this.chatUtil = chatUtil;
        this.cooldownUtil = cooldownUtil;
        this.vaultHook = vaultHook;
    }

    @Override public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            String help = sender.hasPermission("aqseller.admin") ? "plugin-messages.admin-help" : "plugin-messages.user-help";

            for (String message : config.get().getStringList(help)) {
                chatUtil.sendMessage(sender, message.replace("{label}", label));
            }
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("aqseller.admin")) {
                    plugin.reload();
                    chatUtil.sendMessage(sender, config.get().getString("command-messages.reload.success"));
                } else {
                    chatUtil.sendMessage(sender, config.get().getString("plugin-messages.no-perm"));
                }
            } else if (args[0].equalsIgnoreCase("balance")) {
                if (sender.hasPermission("aqseller.admin")) {
                    chatUtil.sendMessage(sender, config.get().getString("command-messages.balance.success")
                            .replace("{money}", String.valueOf(database.getBalance())));
                } else {
                    chatUtil.sendMessage(sender, config.get().getString("plugin-messages.no-perm"));
                }
            } else {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    ItemStack item = player.getInventory().getItemInMainHand();
                    int money;

                    try {
                        money = Integer.parseInt(args[0]);
                    } catch (NumberFormatException error) {
                        chatUtil.sendMessage(sender, config.get().getString("command-messages.args.no-integer"));
                        return;
                    }

                    if (!item.getType().isAir()) {
                        if (items.get().contains("item-list." + item.getType().name().toLowerCase())) {
                            if (money <= items.get().getInt("item-list." + item.getType().name().toLowerCase())) {
                                if (money <= database.getBalance()) {
                                    if (money > 0) {
                                        if (config.get().getBoolean("cooldown-settings.enabled")) {
                                            String time = cooldownUtil.getCooldownValue(player.getUniqueId(), config.get().getInt("cooldown-settings.cooldown"));

                                            if (!cooldownUtil.isCooldown(player.getUniqueId(), config.get().getInt("cooldown-settings.cooldown"))) {
                                                cooldownUtil.setCooldown(player.getUniqueId());
                                                Random random = new Random();

                                                if (random.nextInt(100) >= config.get().getInt("seller-settings.chance")) {
                                                    sellItem(player, money, item);
                                                } else {
                                                    chatUtil.sendMessage(player, config.get().getString("command-messages.args.deny"));
                                                }
                                            } else {
                                                chatUtil.sendMessage(player, config.get().getString("command-messages.args.cooldown").replace("{time}", time));
                                            }
                                        } else {
                                            sellItem(player, money, item);
                                        }
                                    } else {
                                        chatUtil.sendMessage(player, config.get().getString("command-messages.args.no-zero"));
                                    }
                                } else {
                                    chatUtil.sendMessage(player, config.get().getString("command-messages.args.no-money"));
                                }
                            } else {
                                chatUtil.sendMessage(player, config.get().getString("command-messages.args.no-sell"));
                            }
                        } else {
                            chatUtil.sendMessage(player, config.get().getString("command-messages.args.unknown-item"));
                        }
                    } else {
                        chatUtil.sendMessage(player, config.get().getString("command-messages.args.air-item"));
                    }
                } else {
                    chatUtil.sendMessage(sender, config.get().getString("plugin-messages.no-console"));
                }
            }
        }
    }

    @Override public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("aqseller.admin")) {
                return Lists.newArrayList("reload", "balance", "10", "100", "500", "1000");
            } else {
                return Lists.newArrayList("10", "100", "500", "1000");
            }
        }

        return Lists.newArrayList();
    }

    private void sellItem(Player player, int money, ItemStack item) {
        int amount = item.getAmount() - 1;

        if (amount > 0) {
            item.setAmount(amount);
        } else {
            player.getInventory().removeItem(item);
        }

        vaultHook.giveMoney(player, money);
        database.deductBalance(money);

        chatUtil.sendMessage(player, config.get().getString("command-messages.args.success").replace("{money}", String.valueOf(money)));
    }
}

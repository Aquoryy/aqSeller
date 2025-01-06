package org.aqu0ryy.seller.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

public class ChatUtil {

    public String color(String message) {
        MiniMessage miniMessage = MiniMessage.get();

        return LegacyComponentSerializer.legacySection().serialize(miniMessage.deserialize(message));
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }
}

package org.aqu0ryy.seller.utils;

import java.util.HashMap;
import java.util.UUID;

public class CooldownUtil {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    public void setCooldown(UUID uuid) {
        cooldown.put(uuid, System.currentTimeMillis());
    }

    private Long getCooldown(UUID uuid) {
        return (System.currentTimeMillis() - cooldown.get(uuid)) / 1000;
    }

    public boolean isCooldown(UUID uuid, int time) {
        if(cooldown.containsKey(uuid)) {
            return getCooldown(uuid) <= time;
        }

        return false;
    }

    public String getCooldownValue(UUID uuid, int time) {
        if(!cooldown.containsKey(uuid)) {
            return "Нет информации";
        }

        long cd = time - getCooldown(uuid);
        String formatedTime = cd + " сек.";

        if(cd >= 60) {
            formatedTime = (cd / 60) + " мин. ";
        }

        if(cd >= 60 * 60) {
            formatedTime = ((cd / 60) / 60) + " ч. ";
        }

        if(cd >= 60 * 60 * 24) {
            formatedTime = ((cd / 60) / 60) / 24 + " д. ";
        }

        return formatedTime;
    }
}

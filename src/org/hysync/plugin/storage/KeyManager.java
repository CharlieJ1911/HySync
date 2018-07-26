package org.hysync.plugin.storage;

import org.hysync.plugin.HySync;
import org.hysync.plugin.message.Lang;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class KeyManager {
    private HySync hysync;
    private static Set<Key> keyStorage;

    public KeyManager(HySync hysync){
        this.hysync = hysync;
        keyStorage = new HashSet<>();
    }

    public static Set<Key> getKeys(){
        return keyStorage;
    }
    public static Key getRandomKey() {
        return keyStorage.stream().skip(ThreadLocalRandom.current().nextInt(keyStorage.size())).findFirst().orElse(null);
    }

    public static boolean isAlreadyAdded(UUID key) {
        return keyStorage.contains(key);
    }

    public static boolean isValidKey(String key){
        return key.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
    }
}

package org.hysync.plugin.storage;

import org.hysync.plugin.HySync;

import java.util.HashSet;
import java.util.Set;
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
    public static <key> key getRandomKey(Set<key> set) {
        return set.stream().skip(ThreadLocalRandom.current().nextInt(set.size())).findFirst().orElse(null);
    }

    public static boolean isValidKey(String key){
        return key.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
    }
}

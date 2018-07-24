package org.hysync.plugin.storage;

import org.hysync.plugin.HySync;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class KeyManager {
    private HySync hysync;
    private Set<Key> keyStorage;

    public KeyManager(HySync hysync){
        this.hysync = hysync;
        this.keyStorage = new HashSet<>();
    }

    public Set<Key> getKeys(){
        return keyStorage;
    }
    public <key> key getRandomKey(Set<key> set) {
        return set.stream().skip(ThreadLocalRandom.current().nextInt(set.size())).findFirst().orElse(null);
    }
}

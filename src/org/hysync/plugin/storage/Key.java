package org.hysync.plugin.storage;

import java.util.UUID;

public class Key {
    private UUID keyUuid;
    private UUID playerUuid;

    public Key(UUID keyUuid, UUID playerUuid){
        this.keyUuid = keyUuid;
        this.playerUuid = playerUuid;
    }

    public UUID getKeyUuid() {
        return keyUuid;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }
}

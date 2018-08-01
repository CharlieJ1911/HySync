package org.hysync.plugin.storage;

import java.util.UUID;

public class HyKey {
    private UUID keyUuid;
    private UUID playerUuid;

    public HyKey(UUID keyUuid, UUID playerUuid){
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

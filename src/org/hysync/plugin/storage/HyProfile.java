package org.hysync.plugin.storage;

import com.google.gson.JsonObject;

import java.util.UUID;

public class HyProfile {
    private UUID uuid;
    private String rank;
    private JsonObject playerData;
    private Long lastUpdated;
    public HyProfile(UUID uuid, JsonObject playerData){
        this.uuid = uuid;
        this.playerData = playerData;
        this.lastUpdated = System.currentTimeMillis();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getRank() {
        return rank;
    }

    public JsonObject getPlayerData() {
        return playerData;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setPlayerData(JsonObject playerData) {
        this.playerData = playerData;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}

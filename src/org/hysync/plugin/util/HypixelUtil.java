package org.hysync.plugin.util;

import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.request.RequestBuilder;
import net.hypixel.api.request.RequestParam;
import net.hypixel.api.request.RequestType;
import net.hypixel.api.util.Callback;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.hysync.plugin.HySync;
import org.hysync.plugin.message.Lang;
import org.hysync.plugin.storage.Key;
import org.hysync.plugin.storage.KeyManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HypixelUtil {
    private HySync hySync;
    private Map<UUID, String> playerRanks;
    private Map<UUID, ChatColor> plusColour;
    public HypixelUtil(HySync hySync){
        this.hySync = hySync;
        this.playerRanks = new HashMap<>();
        this.plusColour = new HashMap<>();
    }

    public void setRank(UUID uuid){
        Key apiKey = KeyManager.getRandomKey();

        HypixelAPI.getInstance().setApiKey(apiKey.getKeyUuid());
        Request request = RequestBuilder.newBuilder(RequestType.PLAYER).addParam(RequestParam.PLAYER_BY_UUID, uuid).createRequest();

        HypixelAPI.getInstance().getAsync(request, (Callback<PlayerReply>) (failCause, result) -> {
            if(failCause != null){
                hySync.getLogger().warning("============================================");
                failCause.printStackTrace();
                hySync.getLogger().warning("============================================");
                return;
            }

            if(!result.isSuccess()) {
                hySync.getLogger().info("The API Key '"+apiKey.getKeyUuid()+"' is invalid.");
                return;
            }

            JsonObject player = result.getPlayer();
            String rank;

            if(player.get("rank") != null && !player.get("rank").getAsString().equalsIgnoreCase("normal")){
                // Staff
                rank = player.get("rank").getAsString();

            } else if(player.get("monthlyPackageRank") != null){
                // Monthly Paid Ranks
                rank = player.get("monthlyPackageRank").getAsString();
                if(rank.equalsIgnoreCase("SUPERSTAR")){
                    // MVP++
                    rank = "MVP_PLUS_PLUS";

                    plusColour.put(uuid, ChatColor.valueOf(result.getPlayer().get("rankPlusColor").getAsString()));
                }
            } else if(player.get("newPackageRank") != null){
                // Donator (Below MVP++)
                rank = player.get("newPackageRank").getAsString();
            } else if(player.get("packageRank") != null){
                // Unsure
                rank = player.get("packageRank").getAsString();
            } else {
                // Normal Player
                rank = "NONE";
            }
            if(rank.equalsIgnoreCase("MVP_PLUS")) plusColour.put(uuid, ChatColor.valueOf(result.getPlayer().get("rankPlusColor").getAsString()));
            playerRanks.put(uuid, rank);

            if(Bukkit.getPlayer(uuid) != null){
                Lang.RANK_SET_TO.send(Bukkit.getPlayer(uuid), rank);
            }
            HypixelAPI.getInstance().finish();
        });
    }

    public String getRank(UUID uuid){
        return playerRanks.get(uuid);
    }

    public ChatColor getPlusColor(UUID uuid) {
        return plusColour.get(uuid);
    }
}

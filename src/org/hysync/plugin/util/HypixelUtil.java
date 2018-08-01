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
import org.hysync.plugin.storage.HyKey;
import org.hysync.plugin.storage.HyProfile;
import org.hysync.plugin.storage.KeyManager;
import org.hysync.plugin.storage.ProfileManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
        HyKey apiKey = KeyManager.getRandomKey();

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
                hySync.getLogger().warning("The API HyKey '"+apiKey.getKeyUuid()+"' is invalid.");
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
                // Post-EULA Donator Rank (Below MVP++)
                rank = player.get("newPackageRank").getAsString();
            } else if(player.get("packageRank") != null){
                // Pre-EULA Donator Rank
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

            if(ProfileManager.getProfiles().get(uuid) != null){
                // Profile Exists
                HyProfile profile = ProfileManager.getProfiles().get(uuid);

                if(System.currentTimeMillis() - profile.getLastUpdated() >= TimeUnit.SECONDS.toMillis(30)){
                    profile.setRank(rank);
                    profile.setPlayerData(result.getPlayer());
                    profile.setLastUpdated(System.currentTimeMillis());
                    hySync.getLogger().info("Updating Profile for " + Bukkit.getPlayer(uuid).getName() + ".");
                }
            } else {
                // Create Profile
                HyProfile profile = new HyProfile(uuid, result.getPlayer());
                profile.setRank(rank);
                ProfileManager.getProfiles().put(uuid, profile);
                hySync.getLogger().info("Creating Profile for " + Bukkit.getPlayer(uuid).getName() + ".");
            }
        });
    }

    public String getRank(UUID uuid){
        return playerRanks.get(uuid);
    }

    public ChatColor getPlusColor(UUID uuid) {
        return plusColour.get(uuid);
    }

    public String getPrefix(String rank, JsonObject playerData){
        String rankPrefix = null;
        ChatColor plusColor = ChatColor.RED;

        switch(rank) {
            case "ADMIN":
                rankPrefix = ChatColor.RED + "[ADMIN]";
                break;
            case "MODERATOR":
                rankPrefix = ChatColor.DARK_GREEN + "[MOD]";
                break;
            case "HELPER":
                rankPrefix = ChatColor.BLUE + "[HELPER]";
                break;
            case "YOUTUBER":
                rankPrefix = ChatColor.RED + "[" + ChatColor.RESET + "YOUTUBER" + ChatColor.RED + "]";
                break;
            case "MVP_PLUS_PLUS":
                plusColor = ChatColor.valueOf(playerData.get("rankPlusColor").getAsString());
                ChatColor rankColor = ChatColor.valueOf(playerData.get("monthlyRankColor").getAsString()) == null ?
                        ChatColor.valueOf(playerData.get("SUPERSTAR_COLOR").getAsString()) : ChatColor.valueOf(playerData.get("monthlyRankColor").getAsString());
                rankPrefix = rankColor + "[MVP" + plusColor + "++" + rankColor + "]";
                break;
            case "MVP_PLUS":
                plusColor = ChatColor.valueOf(playerData.get("rankPlusColor").getAsString());
                rankPrefix = ChatColor.AQUA + "[MVP" + plusColor + "+" + ChatColor.AQUA + "]";
                break;
            case "MVP":
                rankPrefix = ChatColor.AQUA + "[MVP]";
                break;
            case "VIP_PLUS":
                rankPrefix = ChatColor.GREEN + "[VIP" + ChatColor.GOLD + "+" + ChatColor.GREEN + "]";
                break;
            case "VIP":
                rankPrefix = ChatColor.GREEN + "[VIP]";
                break;
            case "DEFAULT":
                rankPrefix = ChatColor.GRAY.toString();
                break;
        }

        return rankPrefix;
    }
}

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

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HypixelUtil {
    private HySync hySync;
    public HypixelUtil(HySync hySync){
        this.hySync = hySync;
    }

    public void setRank(UUID uuid){
        if(ProfileManager.getProfiles().get(uuid) != null){
            // Profile Exists
            HyProfile profile = ProfileManager.getProfiles().get(uuid);

            if(System.currentTimeMillis() - profile.getLastUpdated() >= TimeUnit.SECONDS.toMillis(30)){
                updateRank(uuid);
            } else {
                hySync.getLogger().info(Bukkit.getPlayer(uuid).getName() + " was updated only shortly.");
            }
        } else {
            updateRank(uuid);
        }
    }

    private void updateRank(UUID uuid){
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
                hySync.getLogger().warning("The API Key '" + apiKey.getKeyUuid() + "' is invalid.");
                return;
            }

            if(result.isThrottle()){
                hySync.getLogger().warning("The API Key " + apiKey.getKeyUuid() + " is being throttled.");
                return;
            }

            JsonObject player = result.getPlayer();
            String rank = null;

            String staffRank = player.has("rank") ? player.get("rank").getAsString() : null;
            String monthlyRank = player.has("monthlyPackageRank") ? player.get("monthlyPackageRank").getAsString() : null;
            String postEulaRank = player.has("newPackageRank") ? player.get("newPackageRank").getAsString() : null;
            String preEulaRank = player.has("packageRank") ? player.get("packageRank").getAsString() : null;

            if(staffRank != null){
                if(staffRank.equalsIgnoreCase("normal")) rank = "NONE";
                else rank = staffRank;
            } else if(monthlyRank != null && !monthlyRank.equalsIgnoreCase("NONE")){
                if(monthlyRank.equalsIgnoreCase("SUPERSTAR")) rank = "MVP_PLUS_PLUS";
            } else if(postEulaRank != null){
                rank = postEulaRank;
            } else if(preEulaRank != null){
                rank = preEulaRank;
            }

            HyProfile profile;
            if(ProfileManager.getProfiles().get(uuid) != null)
                profile = ProfileManager.getProfiles().get(uuid);
            else
                profile = new HyProfile(uuid, result.getPlayer());

            profile.setRank(rank);

            if(profile.getRank() == null){
                // Rank is Disabled
                Lang.RANK_DISABLED.send(Bukkit.getPlayer(uuid), rank);
                return;
            }
            ProfileManager.getProfiles().put(uuid, profile);
            hySync.getLogger().info("Updating Profile for " + Bukkit.getPlayer(uuid).getName() + ".");

            if(Bukkit.getPlayer(uuid) != null){
                Lang.RANK_SET_TO.send(Bukkit.getPlayer(uuid), profile.getRank().getAlias());
            }

            HypixelAPI.getInstance().finish();
        });
    }

    public String getPrefix(HyProfile profile){
        String prefix = profile.getRank().getPrefix();
        if(profile.getRank().getId().contains("MVP_PLUS")) {
            String rankPlusColor = profile.getPlayerData().get("rankPlusColor").getAsString();
            String rankColor = profile.getPlayerData().get("monthlyRankColor").getAsString();
            prefix = prefix.replace("{rankColor}", ChatColor.valueOf(rankColor).toString());
            prefix = prefix.replace("{plusColor}", ChatColor.valueOf(rankPlusColor).toString());
        }
        return prefix;
    }
}

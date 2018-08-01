package org.hysync.plugin.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.hysync.plugin.HySync;
import org.hysync.plugin.storage.HyProfile;
import org.hysync.plugin.storage.KeyManager;
import org.hysync.plugin.storage.ProfileManager;

public class ProfileSetEvent implements Listener {
    private HySync hySync;
    ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    Scoreboard scoreboard;

    public ProfileSetEvent(HySync hySync){
        this.hySync = hySync;
        this.scoreboard = scoreboardManager.getNewScoreboard();

        hySync.getServer().getPluginManager().registerEvents(this, hySync);
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent e){

        if(KeyManager.getKeys().size() < 1) return;

        Player player = e.getPlayer();
        hySync.getHypixelUtil().setRank(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(hySync, () -> {
            HyProfile profile = ProfileManager.getProfiles().get(player.getUniqueId());
            hySync.getLogger().info(player.getName() + "'s rank is " + profile.getRank());
            hySync.getLogger().info(player.getName() + "'s rank colour is " + profile.getPlayerData().get("rankPlusColor").getAsString());

            player.setDisplayName(hySync.getHypixelUtil().getPrefix(profile.getRank(), profile.getPlayerData()) + " " + player.getName());
        }, 40);

        if(scoreboard.getTeam(e.getPlayer().getName()) == null) {
            scoreboard.registerNewTeam(e.getPlayer().getName());
            // TODO: Rank prefix + plus color if correct rank
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        scoreboard.getTeam(event.getPlayer().getName()).unregister();
    }
}

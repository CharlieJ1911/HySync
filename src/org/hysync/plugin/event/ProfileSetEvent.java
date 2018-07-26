package org.hysync.plugin.event;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.hysync.plugin.HySync;
import org.hysync.plugin.storage.KeyManager;

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
        hySync.getHypixelUtil().setRank(e.getPlayer().getUniqueId());

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

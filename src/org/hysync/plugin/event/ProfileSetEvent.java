package org.hysync.plugin.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.hysync.plugin.HySync;
import org.hysync.plugin.storage.HyProfile;
import org.hysync.plugin.storage.KeyManager;
import org.hysync.plugin.storage.ProfileManager;

public class ProfileSetEvent implements Listener {
    private HySync hySync;
    private Scoreboard scoreboard;

    public ProfileSetEvent(HySync hySync){
        this.hySync = hySync;
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        hySync.getServer().getPluginManager().registerEvents(this, hySync);
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent e){
        if(KeyManager.getKeys().size() < 1) return;

        Player player = e.getPlayer();
        hySync.getHypixelUtil().setRank(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(hySync, () -> {
            HyProfile profile = ProfileManager.getProfiles().get(player.getUniqueId());

            String prefix = hySync.getHypixelUtil().getPrefix(profile) + " ";
            player.setDisplayName(prefix + player.getName() + ChatColor.RESET);

            if(scoreboard.getTeam(player.getName()) == null) {
                scoreboard.registerNewTeam(player.getName());
                scoreboard.getTeam(player.getName()).setPrefix(prefix);
            }
            scoreboard.getTeam(player.getName()).addPlayer(player);
            player.setScoreboard(scoreboard);
        }, 40);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(scoreboard.getTeam(event.getPlayer().getName()) != null) scoreboard.getTeam(event.getPlayer().getName()).unregister();
    }
}

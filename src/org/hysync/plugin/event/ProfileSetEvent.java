package org.hysync.plugin.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.hysync.plugin.HySync;
import org.hysync.plugin.storage.KeyManager;

public class ProfileSetEvent implements Listener {
    private HySync hySync;
    public ProfileSetEvent(HySync hySync){
        this.hySync = hySync;
        hySync.getServer().getPluginManager().registerEvents(this, hySync);
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent e){
        if(KeyManager.getKeys().size() < 1) return;
        hySync.getHypixelUtil().setRank(e.getPlayer().getUniqueId());
    }

}

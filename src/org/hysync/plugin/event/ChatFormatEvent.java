package org.hysync.plugin.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.hysync.plugin.HySync;
import org.hysync.plugin.storage.HyProfile;
import org.hysync.plugin.storage.ProfileManager;

public class ChatFormatEvent implements Listener {
    private HySync hySync;

    public ChatFormatEvent(HySync hySync) {
        this.hySync = hySync;
        hySync.getServer().getPluginManager().registerEvents(this, hySync);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        HyProfile profile = ProfileManager.getProfiles().get(player.getUniqueId());

        e.setFormat(hySync.getHypixelUtil().getPrefix(profile) + " " + player.getName() + ChatColor.RESET + ": " + e.getMessage());
    }
}
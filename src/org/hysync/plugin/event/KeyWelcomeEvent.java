package org.hysync.plugin.event;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.hysync.plugin.HySync;
import org.hysync.plugin.conversation.APIKeyPrompt;
import org.hysync.plugin.message.Lang;
import org.hysync.plugin.storage.KeyManager;

import java.util.UUID;

public class KeyWelcomeEvent implements Listener {
    private HySync hySync;
    private ConversationFactory factory;
    private UUID currentlyRegistering;
    public KeyWelcomeEvent(HySync hySync){
        this.hySync = hySync;
        hySync.getServer().getPluginManager().registerEvents(this, hySync);
        factory = new ConversationFactory(hySync).withFirstPrompt(new APIKeyPrompt())
                .withEscapeSequence(Lang.EXIT_CODE.asString()).withLocalEcho(false)
        .addConversationAbandonedListener(new ConvAbandonedListener(hySync));
        hySync.activeSetupUser = null;
    }

    @EventHandler
    public void onOperatorJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6[APPLE] " + player.getName()));
        player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&6[APPLE] " + player.getName()));

        if(KeyManager.getKeys().size() > 0) return;
        if(!player.isOp()) return;


        if(currentlyRegistering != null) return;

            new BukkitRunnable(){
                @Override
                public void run() {
                    hySync.activeSetupUser = player.getUniqueId();
                    factory.buildConversation(player).begin();
                }
            }.runTaskLaterAsynchronously(hySync, 10);
    }

    @EventHandler
    public void onOperatorLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        if(KeyManager.getKeys().size() > 0) return;

        if(hySync.activeSetupUser == player.getUniqueId()){
            hySync.getLogger().info(player.getName() + " left during setup.");
        }
    }
}

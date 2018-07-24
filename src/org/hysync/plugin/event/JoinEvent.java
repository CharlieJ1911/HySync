package org.hysync.plugin.event;

import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.hysync.plugin.HySync;
import org.hysync.plugin.conversation.APIKeyPrompt;

public class JoinEvent implements Listener {
    private HySync plugin;
    private ConversationFactory factory;
    public JoinEvent(HySync plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        factory = new ConversationFactory(plugin).withFirstPrompt(new APIKeyPrompt())
                .withEscapeSequence("cancel").withLocalEcho(false);
    }

    @EventHandler
    public void onOperatorJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if(player.isOp()){
            new BukkitRunnable(){

                @Override
                public void run() {
                    factory.buildConversation(player).begin();
                }
            }.runTaskLaterAsynchronously(plugin, 10);
        }
    }
}

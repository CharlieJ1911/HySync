package org.hysync.plugin.event;

import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.entity.Player;
import org.hysync.plugin.HySync;
import org.hysync.plugin.message.Lang;

public class ConvAbandonedListener implements ConversationAbandonedListener {
    private HySync hySync;
    public ConvAbandonedListener(HySync hysync){
        this.hySync = hysync;
    }
    @Override
    public void conversationAbandoned(ConversationAbandonedEvent conversation) {
        Player player = (Player) conversation.getContext().getForWhom();
        Lang.WELCOME_EXIT.send(player);
        hySync.activeSetupUser = null;
    }
}
package org.hysync.plugin.conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.hysync.plugin.message.Lang;
import org.hysync.plugin.storage.HyKey;
import org.hysync.plugin.storage.KeyManager;

import java.util.UUID;

public class APIKeyPrompt extends ValidatingPrompt {

    @Override
    public String getPromptText(ConversationContext context) {
        Player player = (Player) context.getForWhom();
        return Lang.WELCOME.asString(player.getName(), Lang.EXIT_CODE.asString());
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String key) {
        if(!KeyManager.isValidKey(key)) {
            Player player = (Player) context.getForWhom();
            Lang.ADD_INVALID_KEY.sendRaw(player);
            return false;
        }
        return true;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String key) {
        Player player = (Player) context.getForWhom();
        Lang.ADD_VALID_KEY.sendRaw(player, key);
        KeyManager.getKeys().add(new HyKey(UUID.fromString(key), player.getUniqueId()));
        context.setSessionData("APIKey", key);
        return END_OF_CONVERSATION;
    }
}

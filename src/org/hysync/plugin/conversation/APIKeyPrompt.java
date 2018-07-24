package org.hysync.plugin.conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.hysync.plugin.storage.Message;
import org.hysync.plugin.util.StringUtil;

public class APIKeyPrompt extends ValidatingPrompt {

    @Override
    public String getPromptText(ConversationContext context) {
        String[] msg = Message.WELCOME_ADD_KEY.getMsg();
        if(msg.length == 1){
            return StringUtil.translate(msg[0]);
        } else {
            for (int i = 0; i < msg.length-1; i++) {
                context.getForWhom().sendRawMessage(StringUtil.translate(msg[i]));
            }
            return StringUtil.translate(msg[msg.length-1]);
        }
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String key) {
        if(key.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")){
            // Genuine Key
            return true;
        } else {
            // Not a Key
            context.getForWhom().sendRawMessage(" ");
            context.getForWhom().sendRawMessage(StringUtil.translate("&cYou've specified an invalid key. If you cannot remember, use &f/api new &con Hypixel to obtain a new one."));
            context.getForWhom().sendRawMessage(" ");
            return false;
        }
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String key) {
        context.getForWhom().sendRawMessage(" ");
        context.getForWhom().sendRawMessage(StringUtil.translate("&eKey set to &6"+key));
        context.getForWhom().sendRawMessage(" ");
        return null;
    }
}

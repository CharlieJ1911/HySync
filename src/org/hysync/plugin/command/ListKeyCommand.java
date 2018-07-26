package org.hysync.plugin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hysync.plugin.HySync;
import org.hysync.plugin.message.Lang;
import org.hysync.plugin.storage.Key;
import org.hysync.plugin.storage.KeyManager;
import org.hysync.plugin.util.StringUtil;

@CommandAlias("hysync")
public class ListKeyCommand extends BaseCommand {
    public ListKeyCommand(HySync hysync){
        hysync.getCommandManager().registerCommand(this, true);
    }

    @Subcommand("list")
    public void onKeyList(Player player){
        if(!StringUtil.hasPerm(player, "hysync.key.list")) return;
        Lang.HEADER.send(player);
        Lang.LISTING_KEYS.send(player);
        KeyManager.getKeys().forEach(key -> player.spigot().sendMessage(getKeyFormat(key).create()));
        Lang.FOOTER.send(player);
    }

    private ComponentBuilder getKeyFormat(Key key){
        return new ComponentBuilder(Lang.KEY_ID.asString(key.getKeyUuid()))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        TextComponent.fromLegacyText(Lang.KEY_ID_HOVER.asString(Bukkit.getOfflinePlayer(key.getPlayerUuid()).getName()))));
    }
}

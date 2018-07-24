package org.hysync.plugin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hysync.plugin.HySync;
import org.hysync.plugin.storage.Key;
import org.hysync.plugin.util.StringUtil;

@CommandAlias("hysync")
public class ListKeyCommand extends BaseCommand {
    private HySync hySync;
    public ListKeyCommand(HySync hysync){
        this.hySync = hysync;
        hysync.getCommandManager().registerCommand(this, true);
    }

    @Subcommand("list")
    public void onKeyList(Player sender){
        if(sender.hasPermission("hysync.key.list")){
            sender.sendMessage(StringUtil.translate("&9&m----------------------------------------------------"));
            sender.sendMessage(StringUtil.translate("&6Listing API Keys"));
            hySync.getKeyManager().getKeys().forEach(key -> sender.spigot().sendMessage(getKeyFormat(key).create()));
            sender.sendMessage(StringUtil.translate("&9&m----------------------------------------------------"));
        } else {
            // TODO: Add Permission Message
        }
    }

    private ComponentBuilder getKeyFormat(Key key){
        return new ComponentBuilder(key.getKeyUuid().toString())
                .color(ChatColor.GREEN)
                .bold(false)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Created by ")
                        .color(ChatColor.YELLOW)
                        .append("" + Bukkit.getOfflinePlayer(key.getPlayerUuid()).getName())
                        .color(ChatColor.GOLD)
                        .create()));
    }
}

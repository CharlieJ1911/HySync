package org.hysync.plugin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;
import org.hysync.plugin.HySync;
import org.hysync.plugin.storage.Key;
import org.hysync.plugin.util.StringUtil;

import java.util.UUID;

@CommandAlias("hysync")
public class AddKeyCommand extends BaseCommand {
    private HySync hySync;
    public AddKeyCommand(HySync hysync){
        this.hySync = hysync;
        hysync.getCommandManager().registerCommand(this, true);
    }

    @Subcommand("add")
    public void onKeyAdd(Player sender, String key){
        if(sender.hasPermission("hysync.key.add")){
            hySync.getKeyManager().getKeys().add(new Key(UUID.fromString(key), sender.getUniqueId()));
            sender.sendMessage(StringUtil.translate("&eSuccessfully added &6"+key));
        } else {
            // TODO: Add Permission Message
        }
    }
}

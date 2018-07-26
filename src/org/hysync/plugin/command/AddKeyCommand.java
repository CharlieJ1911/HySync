package org.hysync.plugin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;
import org.hysync.plugin.HySync;
import org.hysync.plugin.message.Lang;
import org.hysync.plugin.storage.Key;
import org.hysync.plugin.storage.KeyManager;
import org.hysync.plugin.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@CommandAlias("hysync")
public class AddKeyCommand extends BaseCommand {
    private HySync hySync;
    public AddKeyCommand(@NotNull HySync hysync){
        this.hySync = hysync;
        hysync.getCommandManager().registerCommand(this, true);
    }

    @Subcommand("add")
    public void onKeyAdd(Player player, String key){
        if(!StringUtil.hasPerm(player, "hysync.key.add")) return;

        if(!KeyManager.isValidKey(key)){
            Lang.ADD_INVALID_KEY.send(player);
            return;
        }

        Lang.ADD_VALID_KEY.send(player, key);
        KeyManager.getKeys().add(new Key(UUID.fromString(key), player.getUniqueId()));
    }
}

package org.hysync.plugin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import org.bukkit.entity.Player;
import org.hysync.plugin.HySync;
import org.hysync.plugin.message.Lang;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends BaseCommand {
    private HySync hySync;
    public MainCommand(@NotNull HySync hysync){
        this.hySync = hysync;
        hysync.getCommandManager().registerCommand(this, true);
    }

    @CommandAlias("hysync")
    public void onKeyAdd(Player player){
        Lang.MAIN_COMMAND.send(player, Lang.HEADER.asString(), hySync.getDescription().getVersion(), Lang.FOOTER.asString());
    }
}

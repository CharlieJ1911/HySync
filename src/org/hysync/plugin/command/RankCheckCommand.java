package org.hysync.plugin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;
import org.hysync.plugin.HySync;
import org.hysync.plugin.message.Lang;
import org.hysync.plugin.util.StringUtil;

@CommandAlias("hysync")
public class RankCheckCommand extends BaseCommand {
    private HySync hySync;
    public RankCheckCommand(HySync hysync){
        this.hySync = hysync;
        hysync.getCommandManager().registerCommand(this, true);
    }

    @Subcommand("check")
    public void onKeyAdd(Player player, String target){
        if(!StringUtil.hasPerm(player, "hysync.check")) return;

        if(player.getName().equalsIgnoreCase(target)){
            Lang.RANK_CHECK_SELF.send(player, hySync.getHypixelUtil().getRank(player.getUniqueId()));
        } else {
            Lang.RANK_CHECK_OTHER.send(player, hySync.getHypixelUtil().getRank(player.getUniqueId()));
        }
    }
}

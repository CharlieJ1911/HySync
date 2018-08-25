package org.hysync.plugin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hysync.plugin.HySync;
import org.hysync.plugin.message.Lang;
import org.hysync.plugin.storage.HyProfile;
import org.hysync.plugin.storage.ProfileManager;
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
            HyProfile profile = ProfileManager.getProfiles().get(player.getUniqueId());
            if(profile == null) {
                Lang.REQUIRE_RELOG.send(player);
                return;
            }

            Lang.RANK_CHECK_SELF.send(player, Lang.HEADER.asString(), profile.getRank().getAlias(), Lang.FOOTER.asString());
        } else {
            HyProfile profile = ProfileManager.getProfiles().get(Bukkit.getOfflinePlayer(target).getUniqueId());
            Lang.RANK_CHECK_OTHER.send(player, Lang.HEADER.asString(), profile.getPlayerData().get("username"),
                    profile.getRank().getAlias(), Lang.FOOTER.asString());
        }
    }
}

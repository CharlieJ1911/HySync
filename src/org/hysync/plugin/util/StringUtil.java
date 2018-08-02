package org.hysync.plugin.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.hysync.plugin.message.Lang;

public class StringUtil {

    public static String translate(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static boolean hasPerm(Player player, String permission){
        if(!player.hasPermission(permission)){
            Lang.NO_PERMISSION.send(player, permission);
            return false;
        }
        return true;
    }
}

package org.hysync.plugin.util;

import org.bukkit.ChatColor;

public class StringUtil {
    public static String translate(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}

package org.hysync.plugin.message;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public enum Lang {

    WELCOME("&aHey there {0}! This is your first time running HySync. In order to work properly, we require a valid Hypixel API key.",
            "&ePlease enter your Hypixel API key in chat, or type '&c{1}&e' to cancel."),
    WELCOME_EXIT("&cSetup cancelled, note the plugin will not work until you do so."),
    ADD_VALID_KEY("&7", "&eKey set to &a{0}", "&7"),
    ADD_INVALID_KEY("&7", "&cYou've specified an invalid key. If you cannot remember, use &f/api new &con Hypixel to obtain a new one.", "&7"),
    NO_PERMISSION("&cYou do not have {0} to use this command!"),
    LISTING_KEYS("&6Listing API Keys"),
    HEADER("&9&m----------------------------------------------------"),
    FOOTER("&9&m----------------------------------------------------"),
    EXIT_CODE("cancel"),
    KEY_ID("&a{0}"),
    KEY_ID_HOVER("&eCreated by &6{0}"),

    ;

    private String message;

    private static ConfigWrapper configWrapper;
    private static FileConfiguration c;

    Lang(final String... def) {
        this.message = Arrays.stream(def).collect(Collectors.joining("\n"));
    }

    private String getMessage() {
        return this.message;
    }

    public String getPath() {
        return this.name();
    }

    /**
     * Replaces variables in message with given arguments
     *
     * @param s String to format
     * @param objects Arguments to replace
     * @return Colored & formatted string
     */
    private String format(String s, Object... objects) {
        for (int i = 0; i < objects.length; i++) {
            s = s.replace("{" + i + "}", String.valueOf(objects[i]));
        }

        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Turn the lang value into a string object with args replaced
     *
     * @param objects Arguments to replace
     * @return String value of message
     */
    public String asString(Object... objects) {
        Optional<String> opt = Optional.empty();

        if (c.contains(this.name())) {
            if (c.isList(this.name())) {
                opt = Optional.ofNullable(c.getStringList(this.name()).stream().collect(Collectors.joining("\n")));
            } else if (c.isString(this.name())) {
                opt = Optional.ofNullable(c.getString(this.name()));
            }
        }

        return format(opt.orElse(this.message), objects);
    }

    /**
     * Sends a lang value to a player after parsing placeholders and arguments
     *
     * @param player Player to send the message to
     * @param args Arguments to replace
     */
    public void send(Player player, Object... args) {
        String message = asString(args);

        Arrays.stream(message.split("\n")).forEach(player::sendMessage);
    }

    /**
     * Sends a raw lang value to a player after parsing placeholders and arguments
     *
     * @param player Player to send the message to
     * @param args Arguments to replace
     */
    public void sendRaw(Player player, Object... args) {
        String message = asString(args);

        Arrays.stream(message.split("\n")).forEach(player::sendRawMessage);
    }

    /**
     * Similar to {@link #send(Player, Object...)} Only replaces PAPI placeholders if sender is a
     * player
     */
    public void send(CommandSender sender, Object... args) {
        if (sender instanceof Player) {
            send((Player) sender, args);
        } else {
            Arrays.stream(asString(args).split("\n")).forEach(sender::sendMessage);
        }
    }

    public static ConfigWrapper getConfigWrapper() {
        return configWrapper;
    }

    public static boolean init(ConfigWrapper wrapper) {

        wrapper.loadConfig();

        if (wrapper.getConfig() == null) {
            return false;
        }

        configWrapper = wrapper;
        c = wrapper.getConfig();

        for (final Lang value : Lang.values()) {
            if (value.getMessage().split("\n").length == 1) {
                c.addDefault(value.getPath(), value.getMessage());
                continue;
            }

            c.addDefault(value.getPath(), value.getMessage().split("\n"));
        }

        c.options().copyDefaults(true);
        wrapper.saveConfig();
        return true;
    }

    public static void reload() {
        configWrapper.loadConfig();
        c = configWrapper.getConfig();
    }
}

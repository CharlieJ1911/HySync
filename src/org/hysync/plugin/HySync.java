package org.hysync.plugin;

import co.aikar.commands.BukkitCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.hysync.plugin.command.AddKeyCommand;
import org.hysync.plugin.command.ListKeyCommand;
import org.hysync.plugin.event.JoinEvent;
import org.hysync.plugin.storage.KeyManager;

public class HySync extends JavaPlugin {
    private KeyManager keyManager;
    private BukkitCommandManager commandManager;

    public KeyManager getKeyManager() {
        return keyManager;
    }

    public BukkitCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void onEnable(){
        new JoinEvent(this);

        keyManager = new KeyManager(this);
        commandManager = new BukkitCommandManager(this);
        new AddKeyCommand(this);
        new ListKeyCommand(this);
    }

    @Override
    public void onDisable(){

    }
}

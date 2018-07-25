package org.hysync.plugin.message;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigWrapper {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;
    private final String folderName;
    private final String fileName;

    public ConfigWrapper(final JavaPlugin instance, final String folderName, final String fileName) {
        this.plugin = instance;
        this.folderName = folderName;
        this.fileName = fileName;
    }

    public ConfigWrapper(final JavaPlugin instance, final String fileName) {
        this.plugin = instance;
        this.fileName = fileName;
        this.folderName = null;
    }

    public void setConfig(FileConfiguration c) {
        this.config = c;
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }


    public FileConfiguration loadConfig() {
        if (configFile == null) {
            if (folderName != null && !folderName.isEmpty()) {
                configFile = new File(plugin.getDataFolder() + File.separator + folderName, fileName);
            } else {
                configFile = new File(plugin.getDataFolder(), fileName);
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        return config;
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (final IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }
}

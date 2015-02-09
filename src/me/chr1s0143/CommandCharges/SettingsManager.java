package me.chr1s0143.CommandCharges;

/**
 * Created by chris on 05/02/2015.
 */
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class SettingsManager {

    private SettingsManager() { }

    static SettingsManager instance = new SettingsManager();

    public static SettingsManager getInstance() {
        return instance;
    }

    Plugin plugin;

    FileConfiguration configFile;
    File configfile;

    public void setupConfigFile(Plugin plugin) {

        configfile = new File(plugin.getDataFolder(), "Config.yml");

        if (!configfile.exists()) {
            try {
                configfile.createNewFile();
            }
            catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create Config.yml!");
            }
        }

        configFile = YamlConfiguration.loadConfiguration(configfile);
    }

    public FileConfiguration getConfigFile() {
        return configFile;
    }

    public void saveDefaultConfigFile() {
        if (configfile == null) {
            configfile = new File(plugin.getDataFolder(), "Config.yml");
        }
        if (!configfile.exists()) {
            plugin.saveResource("Config.yml", false);
        }
    }

    public void saveConfigFile() {
        try {
            configFile.save(configfile);
        }
        catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save Config.yml!");
        }
    }

    public void reloadConfigFile() {
        configFile = YamlConfiguration.loadConfiguration(configfile);
    }
}

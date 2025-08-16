package dev.azuremyst.azuremenus.config;

import dev.azuremyst.azuremenus.AzureMenusPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * Manages plugin configuration files
 */
public class ConfigManager {
    
    private final AzureMenusPlugin plugin;
    private FileConfiguration config;
    private File configFile;
    
    // Configuration values
    private String defaultLanguage;
    private boolean debugMode;
    private int menuUpdateInterval;
    private boolean soundsEnabled;
    private double soundVolume;
    private float soundPitch;
    
    public ConfigManager(AzureMenusPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
    }
    
    /**
     * Load configuration from file
     */
    public void loadConfig() {
        // Create plugin folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        // Create config.yml if it doesn't exist
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        
        config = YamlConfiguration.loadConfiguration(configFile);
        
        // Load defaults from resource
        InputStream defaultConfigStream = plugin.getResource("config.yml");
        if (defaultConfigStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream));
            config.setDefaults(defaultConfig);
        }
        
        // Cache frequently used values
        this.defaultLanguage = config.getString("language", "ru");
        this.debugMode = config.getBoolean("debug", false);
        this.menuUpdateInterval = config.getInt("menu-update-interval", 20);
        this.soundsEnabled = config.getBoolean("sounds.enabled", true);
        this.soundVolume = config.getDouble("sounds.volume", 1.0);
        this.soundPitch = (float) config.getDouble("sounds.pitch", 1.0);
        
        plugin.getLogger().info("Configuration loaded successfully!");
    }
    
    /**
     * Save configuration to file
     */
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config.yml!", e);
        }
    }
    
    /**
     * Reload configuration
     */
    public void reloadConfig() {
        loadConfig();
    }
    
    /**
     * Get configuration value
     */
    public <T> T get(String path, T defaultValue) {
        Object value = config.get(path, defaultValue);
        try {
            @SuppressWarnings("unchecked")
            T result = (T) value;
            return result;
        } catch (ClassCastException e) {
            plugin.getLogger().warning("Invalid configuration value at path: " + path);
            return defaultValue;
        }
    }
    
    /**
     * Set configuration value
     */
    public void set(String path, Object value) {
        config.set(path, value);
    }
    
    // Getters for cached values
    public String getDefaultLanguage() {
        return defaultLanguage;
    }
    
    public boolean isDebugMode() {
        return debugMode;
    }
    
    public int getMenuUpdateInterval() {
        return menuUpdateInterval;
    }
    
    public boolean isSoundsEnabled() {
        return soundsEnabled;
    }
    
    public double getSoundVolume() {
        return soundVolume;
    }
    
    public float getSoundPitch() {
        return soundPitch;
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
}

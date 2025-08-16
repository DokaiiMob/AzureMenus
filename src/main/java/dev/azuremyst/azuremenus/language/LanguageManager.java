package dev.azuremyst.azuremenus.language;

import dev.azuremyst.azuremenus.AzureMenusPlugin;
import dev.azuremyst.azuremenus.utils.ColorUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Manages plugin localization and messages
 */
public class LanguageManager {
    
    private final AzureMenusPlugin plugin;
    private final Map<String, FileConfiguration> languages;
    private String defaultLanguage;
    private FileConfiguration currentLanguage;
    
    public LanguageManager(AzureMenusPlugin plugin) {
        this.plugin = plugin;
        this.languages = new HashMap<>();
    }
    
    /**
     * Load all language files
     */
    public void loadLanguages() {
        this.defaultLanguage = plugin.getConfigManager().getDefaultLanguage();
        
        // Create lang folder if it doesn't exist
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }
        
        // Load default language files
        loadLanguage("ru");
        loadLanguage("en");
        
        // Set current language
        this.currentLanguage = languages.get(defaultLanguage);
        if (currentLanguage == null) {
            plugin.getLogger().warning("Default language '" + defaultLanguage + "' not found! Using 'ru'.");
            this.currentLanguage = languages.get("ru");
        }
        
        plugin.getLogger().info("Loaded " + languages.size() + " language(s). Using: " + defaultLanguage);
    }
    
    /**
     * Load specific language file
     */
    private void loadLanguage(String lang) {
        File langFile = new File(plugin.getDataFolder(), "lang/" + lang + ".yml");
        
        // Save default language file if it doesn't exist
        if (!langFile.exists()) {
            plugin.saveResource("lang/" + lang + ".yml", false);
        }
        
        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
        
        // Load defaults from resource
        InputStream defaultLangStream = plugin.getResource("lang/" + lang + ".yml");
        if (defaultLangStream != null) {
            YamlConfiguration defaultLangConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultLangStream));
            langConfig.setDefaults(defaultLangConfig);
        }
        
        languages.put(lang, langConfig);
    }
    
    /**
     * Get message by key
     */
    public String getMessage(String key) {
        return getMessage(key, new HashMap<>());
    }
    
    /**
     * Get message by key with placeholders
     */
    public String getMessage(String key, Map<String, Object> placeholders) {
        String message = currentLanguage.getString(key);
        
        if (message == null) {
            plugin.getLogger().warning("Missing translation key: " + key);
            return "<red>Missing translation: " + key + "</red>";
        }
        
        // Replace placeholders
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        
        // Convert legacy color codes if needed
        if (message.contains("&")) {
            message = ColorUtil.convertLegacyToMiniMessage(message);
        }
        
        return message;
    }
    
    /**
     * Get message as Component
     */
    public Component getMessageComponent(String key) {
        return getMessageComponent(key, new HashMap<>());
    }
    
    /**
     * Get message as Component with placeholders
     */
    public Component getMessageComponent(String key, Map<String, Object> placeholders) {
        String message = getMessage(key, placeholders);
        return ColorUtil.parseComponent(message);
    }
    
    /**
     * Get message list
     */
    public List<String> getMessageList(String key) {
        List<String> messages = currentLanguage.getStringList(key);
        
        if (messages.isEmpty()) {
            plugin.getLogger().warning("Missing translation key: " + key);
            messages.add("<red>Missing translation: " + key + "</red>");
        }
        
        // Convert legacy color codes
        messages.replaceAll(message -> {
            if (message.contains("&")) {
                return ColorUtil.convertLegacyToMiniMessage(message);
            }
            return message;
        });
        
        return messages;
    }
    
    /**
     * Get message list as Components
     */
    public List<Component> getMessageListComponents(String key) {
        return getMessageList(key).stream()
            .map(ColorUtil::parseComponent)
            .toList();
    }
    
    /**
     * Send message to player
     */
    public void sendMessage(Player player, String key) {
        sendMessage(player, key, new HashMap<>());
    }
    
    /**
     * Send message to player with placeholders
     */
    public void sendMessage(Player player, String key, Map<String, Object> placeholders) {
        Component message = getMessageComponent(key, placeholders);
        player.sendMessage(message);
    }
    
    /**
     * Send prefixed message to player
     */
    public void sendPrefixedMessage(Player player, String key) {
        sendPrefixedMessage(player, key, new HashMap<>());
    }
    
    /**
     * Send prefixed message to player with placeholders
     */
    public void sendPrefixedMessage(Player player, String key, Map<String, Object> placeholders) {
        Component prefix = getMessageComponent("prefix");
        Component message = getMessageComponent(key, placeholders);
        player.sendMessage(prefix.append(Component.text(" ")).append(message));
    }
    
    /**
     * Check if language exists
     */
    public boolean hasLanguage(String lang) {
        return languages.containsKey(lang);
    }
    
    /**
     * Get available languages
     */
    public String[] getAvailableLanguages() {
        return languages.keySet().toArray(new String[0]);
    }
    
    /**
     * Set default language
     */
    public void setDefaultLanguage(String lang) {
        if (hasLanguage(lang)) {
            this.defaultLanguage = lang;
            this.currentLanguage = languages.get(lang);
            plugin.getConfigManager().set("language", lang);
            plugin.getConfigManager().saveConfig();
        } else {
            plugin.getLogger().warning("Language '" + lang + "' not found!");
        }
    }
    
    /**
     * Get current language
     */
    public String getCurrentLanguage() {
        return defaultLanguage;
    }
    
    /**
     * Create formatted placeholder map
     */
    public static Map<String, Object> createPlaceholders(String... keyValuePairs) {
        Map<String, Object> placeholders = new HashMap<>();
        
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("Key-value pairs must be even");
        }
        
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            placeholders.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }
        
        return placeholders;
    }
}

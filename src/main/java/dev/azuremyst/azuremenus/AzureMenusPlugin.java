package dev.azuremyst.azuremenus;

import dev.azuremyst.azuremenus.config.ConfigManager;
import dev.azuremyst.azuremenus.commands.AzureMenusCommand;
import dev.azuremyst.azuremenus.language.LanguageManager;
import dev.azuremyst.azuremenus.menu.MenuManager;
import dev.azuremyst.azuremenus.placeholders.PlaceholderService;
import dev.azuremyst.azuremenus.actions.ActionRegistry;
import dev.azuremyst.azuremenus.animation.AnimationEngine;
import dev.azuremyst.azuremenus.economy.EconomyService;
import dev.azuremyst.azuremenus.listeners.MenuListener;
import dev.azuremyst.azuremenus.utils.ColorUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * AzureMenus - Advanced menu system for AzureMyst Minecraft server
 * 
 * @author AzureMyst Team
 * @version 1.0.0
 */
public final class AzureMenusPlugin extends JavaPlugin {
    
    private static AzureMenusPlugin instance;
    
    // Core managers
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private MenuManager menuManager;
    private PlaceholderService placeholderService;
    private ActionRegistry actionRegistry;
    private AnimationEngine animationEngine;
    private EconomyService economyService;
    
    @Override
    public void onLoad() {
        instance = this;
        getLogger().info("Loading AzureMenus v" + getDescription().getVersion() + "...");
    }
    
    @Override
    public void onEnable() {
        try {
            // Initialize core components in order
            this.configManager = new ConfigManager(this);
            this.languageManager = new LanguageManager(this);
            this.placeholderService = new PlaceholderService(this);
            this.economyService = new EconomyService(this);
            this.actionRegistry = new ActionRegistry(this);
            this.animationEngine = new AnimationEngine(this);
            this.menuManager = new MenuManager(this);
            
            // Register commands
            getCommand("azuremenus").setExecutor(new AzureMenusCommand(this));
            
            // Register event listeners
            getServer().getPluginManager().registerEvents(new MenuListener(this), this);
            
            // Load configurations
            configManager.loadConfig();
            languageManager.loadLanguages();
            menuManager.loadMenus();
            
            getLogger().info(ColorUtil.stripColor(languageManager.getMessage("plugin.enabled")));
            
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable AzureMenus!", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    @Override
    public void onDisable() {
        try {
            // Clean shutdown
            if (animationEngine != null) {
                animationEngine.shutdown();
            }
            
            if (menuManager != null) {
                menuManager.closeAllMenus();
            }
            
            if (placeholderService != null) {
                placeholderService.unregister();
            }
            
            getLogger().info(ColorUtil.stripColor(languageManager != null ? 
                languageManager.getMessage("plugin.disabled") : 
                "<gradient:#8A2BE2:#00BFFF>AzureMenus отключён</gradient>"));
                
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Error during shutdown", e);
        } finally {
            instance = null;
        }
    }
    
    /**
     * Reload all plugin components
     */
    public void reload() {
        try {
            getLogger().info("Reloading AzureMenus...");
            
            // Close all open menus
            menuManager.closeAllMenus();
            
            // Reload configurations
            configManager.loadConfig();
            languageManager.loadLanguages();
            menuManager.loadMenus();
            
            getLogger().info(ColorUtil.stripColor(languageManager.getMessage("plugin.reloaded")));
            
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to reload AzureMenus!", e);
        }
    }
    
    // Getters
    public static AzureMenusPlugin getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public LanguageManager getLanguageManager() {
        return languageManager;
    }
    
    public MenuManager getMenuManager() {
        return menuManager;
    }
    
    public PlaceholderService getPlaceholderService() {
        return placeholderService;
    }
    
    public ActionRegistry getActionRegistry() {
        return actionRegistry;
    }
    
    public AnimationEngine getAnimationEngine() {
        return animationEngine;
    }
    
    public EconomyService getEconomyService() {
        return economyService;
    }
}

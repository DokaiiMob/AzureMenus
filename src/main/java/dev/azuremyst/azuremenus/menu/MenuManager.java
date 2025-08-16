package dev.azuremyst.azuremenus.menu;

import dev.azuremyst.azuremenus.AzureMenusPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages menu loading, caching and operations
 */
public class MenuManager {
    
    private final AzureMenusPlugin plugin;
    private final Map<String, Menu> loadedMenus;
    private final Map<String, BukkitTask> updateTasks;
    private final MenuParser parser;
    
    public MenuManager(AzureMenusPlugin plugin) {
        this.plugin = plugin;
        this.loadedMenus = new ConcurrentHashMap<>();
        this.updateTasks = new ConcurrentHashMap<>();
        this.parser = new MenuParser(plugin);
    }
    
    /**
     * Load all menus from the menus directory
     */
    public void loadMenus() {
        // Clear existing menus and tasks
        stopAllUpdateTasks();
        loadedMenus.clear();
        
        File menusDir = new File(plugin.getDataFolder(), "menus");
        if (!menusDir.exists()) {
            menusDir.mkdirs();
            plugin.getLogger().info("Created menus directory");
        }
        
        File[] menuFiles = menusDir.listFiles((dir, name) -> name.endsWith(".yml") || name.endsWith(".yaml"));
        if (menuFiles == null || menuFiles.length == 0) {
            plugin.getLogger().info("No menu files found in menus directory");
            return;
        }
        
        int loadedCount = 0;
        for (File menuFile : menuFiles) {
            String menuName = menuFile.getName().replaceAll("\\.(yml|yaml)$", "");
            
            try {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(menuFile);
                Menu menu = parser.parseMenu(menuName, config);
                
                if (menu != null) {
                    loadedMenus.put(menuName.toLowerCase(), menu);
                    
                    // Start update task if needed
                    if (menu.getUpdateInterval() > 0) {
                        startUpdateTask(menu);
                    }
                    
                    loadedCount++;
                    plugin.getLogger().info("Loaded menu: " + menuName);
                } else {
                    plugin.getLogger().warning("Failed to load menu: " + menuName);
                }
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error loading menu file " + menuFile.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        plugin.getLogger().info("Loaded " + loadedCount + " menu(s)");
    }
    
    /**
     * Get a menu by name
     */
    public Menu getMenu(String name) {
        return loadedMenus.get(name.toLowerCase());
    }
    
    /**
     * Get all loaded menu names
     */
    public Set<String> getMenuNames() {
        return new HashSet<>(loadedMenus.keySet());
    }
    
    /**
     * Open a menu for a player
     */
    public boolean openMenu(Player player, String menuName) {
        Menu menu = getMenu(menuName);
        if (menu == null) {
            return false;
        }
        
        menu.open(player);
        return true;
    }
    
    /**
     * Check if a menu exists
     */
    public boolean hasMenu(String name) {
        return loadedMenus.containsKey(name.toLowerCase());
    }
    
    /**
     * Close all open menus
     */
    public void closeAllMenus() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof Menu) {
                player.closeInventory();
            }
        }
    }
    
    /**
     * Start update task for a menu
     */
    private void startUpdateTask(Menu menu) {
        String menuName = menu.getName();
        
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    menu.update();
                } catch (Exception e) {
                    plugin.getLogger().warning("Error updating menu " + menuName + ": " + e.getMessage());
                }
            }
        }.runTaskTimer(plugin, menu.getUpdateInterval(), menu.getUpdateInterval());
        
        updateTasks.put(menuName, task);
    }
    
    /**
     * Stop all update tasks
     */
    private void stopAllUpdateTasks() {
        for (BukkitTask task : updateTasks.values()) {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
        updateTasks.clear();
    }
    
    /**
     * Reload menus
     */
    public void reload() {
        closeAllMenus();
        loadMenus();
    }
    
    /**
     * Shutdown manager
     */
    public void shutdown() {
        closeAllMenus();
        stopAllUpdateTasks();
        loadedMenus.clear();
    }
}

package dev.azuremyst.azuremenus.listeners;

import dev.azuremyst.azuremenus.AzureMenusPlugin;
import dev.azuremyst.azuremenus.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Handles menu-related inventory events
 */
public class MenuListener implements Listener {
    
    private final AzureMenusPlugin plugin;
    
    public MenuListener(AzureMenusPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Handle inventory clicks in menus
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        
        if (!(holder instanceof Menu menu)) {
            return; // Not a menu
        }
        
        // Cancel the event to prevent item taking/placing
        event.setCancelled(true);
        
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        
        int slot = event.getRawSlot();
        
        // Only handle clicks in the menu inventory, not player inventory
        if (slot >= 0 && slot < event.getInventory().getSize()) {
            try {
                menu.handleClick(player, slot, event.getClick());
            } catch (Exception e) {
                plugin.getLogger().warning("Error handling menu click for player " + 
                    player.getName() + " in menu " + menu.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Handle inventory drag events in menus
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        
        if (holder instanceof Menu) {
            // Cancel dragging in menus
            event.setCancelled(true);
        }
    }
    
    /**
     * Handle inventory close events
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        
        if (holder instanceof Menu menu && event.getPlayer() instanceof Player player) {
            // Optional: Handle menu close logic here
            if (plugin.getConfigManager().isDebugMode()) {
                plugin.getLogger().info("Player " + player.getName() + " closed menu " + menu.getName());
            }
        }
    }
}

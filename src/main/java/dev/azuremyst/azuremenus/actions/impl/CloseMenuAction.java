package dev.azuremyst.azuremenus.actions.impl;

import dev.azuremyst.azuremenus.actions.Action;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Set;

/**
 * Action that closes the menu
 */
public class CloseMenuAction implements Action {
    
    private final Set<ClickType> allowedClickTypes;
    private final String permission;
    
    public CloseMenuAction(Set<ClickType> allowedClickTypes, String permission) {
        this.allowedClickTypes = allowedClickTypes;
        this.permission = permission;
    }
    
    @Override
    public boolean canExecute(Player player, ClickType clickType) {
        // Check click type
        if (allowedClickTypes != null && !allowedClickTypes.isEmpty()) {
            if (!allowedClickTypes.contains(clickType)) {
                return false;
            }
        }
        
        // Check permission
        if (permission != null && !permission.isEmpty()) {
            if (!player.hasPermission(permission)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void execute(Player player, ClickType clickType) {
        player.closeInventory();
    }
    
    @Override
    public String getType() {
        return "close";
    }
    
    // Getters
    public Set<ClickType> getAllowedClickTypes() {
        return allowedClickTypes;
    }
    
    public String getPermission() {
        return permission;
    }
}

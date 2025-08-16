package dev.azuremyst.azuremenus.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * Interface for menu actions
 */
public interface Action {
    
    /**
     * Check if this action can be executed by the player
     */
    boolean canExecute(Player player, ClickType clickType);
    
    /**
     * Execute the action
     */
    void execute(Player player, ClickType clickType);
    
    /**
     * Get the action type identifier
     */
    String getType();
    
    /**
     * Check if this action should be executed asynchronously
     */
    default boolean isAsync() {
        return false;
    }
}

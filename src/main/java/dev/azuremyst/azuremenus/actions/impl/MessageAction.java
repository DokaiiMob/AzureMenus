package dev.azuremyst.azuremenus.actions.impl;

import dev.azuremyst.azuremenus.AzureMenusPlugin;
import dev.azuremyst.azuremenus.actions.Action;
import dev.azuremyst.azuremenus.utils.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Set;

/**
 * Action that sends a message to the player
 */
public class MessageAction implements Action {
    
    private final String message;
    private final Set<ClickType> allowedClickTypes;
    private final String permission;
    
    public MessageAction(String message, Set<ClickType> allowedClickTypes, String permission) {
        this.message = message != null ? message : "";
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
        if (message.isEmpty()) return;
        
        String processedMessage = replacePlaceholders(player, message);
        Component messageComponent = ColorUtil.parseComponent(processedMessage);
        
        player.sendMessage(messageComponent);
    }
    
    /**
     * Replace placeholders in the message
     */
    private String replacePlaceholders(Player player, String text) {
        if (text == null || text.isEmpty()) return text;
        
        // Replace internal placeholders
        text = text.replace("%player_name%", player.getName());
        text = text.replace("%player_displayname%", player.getDisplayName());
        text = text.replace("%server_online%", String.valueOf(Bukkit.getOnlinePlayers().size()));
        text = text.replace("%server_max%", String.valueOf(Bukkit.getMaxPlayers()));
        
        // Use PlaceholderAPI if available
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            text = PlaceholderAPI.setPlaceholders(player, text);
        }
        
        return text;
    }
    
    @Override
    public String getType() {
        return "message";
    }
    
    // Getters
    public String getMessage() {
        return message;
    }
    
    public Set<ClickType> getAllowedClickTypes() {
        return allowedClickTypes;
    }
    
    public String getPermission() {
        return permission;
    }
}

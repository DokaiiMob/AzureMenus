package dev.azuremyst.azuremenus.actions.impl;

import dev.azuremyst.azuremenus.AzureMenusPlugin;
import dev.azuremyst.azuremenus.actions.Action;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;

/**
 * Action that executes commands
 */
public class CommandAction implements Action {
    
    private final List<String> commands;
    private final boolean asConsole;
    private final Set<ClickType> allowedClickTypes;
    private final String permission;
    private final boolean async;
    
    public CommandAction(List<String> commands, boolean asConsole, 
                        Set<ClickType> allowedClickTypes, String permission, boolean async) {
        this.commands = commands != null ? commands : List.of();
        this.asConsole = asConsole;
        this.allowedClickTypes = allowedClickTypes;
        this.permission = permission;
        this.async = async;
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
        if (commands.isEmpty()) return;
        
        // Check for blocked commands
        List<String> blockedCommands = AzureMenusPlugin.getInstance()
            .getConfigManager().get("security.blocked-commands", List.of());
        
        for (String command : commands) {
            String processedCommand = replacePlaceholders(player, command);
            
            // Remove leading slash if present
            if (processedCommand.startsWith("/")) {
                processedCommand = processedCommand.substring(1);
            }
            
            // Check if command is blocked
            String commandName = processedCommand.split(" ")[0].toLowerCase();
            if (blockedCommands.contains(commandName)) {
                AzureMenusPlugin.getInstance().getLanguageManager()
                    .sendPrefixedMessage(player, "actions.command.blocked");
                continue;
            }
            
            if (async) {
                executeAsync(player, processedCommand);
            } else {
                executeSync(player, processedCommand);
            }
        }
    }
    
    /**
     * Execute command synchronously
     */
    private void executeSync(Player player, String command) {
        try {
            if (asConsole) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            } else {
                player.performCommand(command);
            }
        } catch (Exception e) {
            AzureMenusPlugin.getInstance().getLogger().warning(
                "Failed to execute command '" + command + "': " + e.getMessage());
            AzureMenusPlugin.getInstance().getLanguageManager()
                .sendPrefixedMessage(player, "actions.command.error");
        }
    }
    
    /**
     * Execute command asynchronously
     */
    private void executeAsync(Player player, String command) {
        new BukkitRunnable() {
            @Override
            public void run() {
                executeSync(player, command);
            }
        }.runTaskAsynchronously(AzureMenusPlugin.getInstance());
    }
    
    /**
     * Replace placeholders in the command
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
        return asConsole ? "command-console" : "command-player";
    }
    
    @Override
    public boolean isAsync() {
        return async;
    }
    
    // Getters
    public List<String> getCommands() {
        return commands;
    }
    
    public boolean isAsConsole() {
        return asConsole;
    }
    
    public Set<ClickType> getAllowedClickTypes() {
        return allowedClickTypes;
    }
    
    public String getPermission() {
        return permission;
    }
}

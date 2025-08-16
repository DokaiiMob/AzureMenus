package dev.azuremyst.azuremenus.actions;

import dev.azuremyst.azuremenus.AzureMenusPlugin;
import dev.azuremyst.azuremenus.actions.impl.*;
import org.bukkit.event.inventory.ClickType;

import java.util.*;
import java.util.function.Function;

/**
 * Registry for menu actions
 */
public class ActionRegistry {
    
    private final AzureMenusPlugin plugin;
    private final Map<String, ActionFactory> actionFactories;
    
    public ActionRegistry(AzureMenusPlugin plugin) {
        this.plugin = plugin;
        this.actionFactories = new HashMap<>();
        
        registerDefaultActions();
    }
    
    /**
     * Register default action types
     */
    private void registerDefaultActions() {
        // Message action
        register("message", config -> {
            String message = (String) config.get("message");
            Set<ClickType> clickTypes = parseClickTypes(config.get("click-types"));
            String permission = (String) config.get("permission");
            return new MessageAction(message, clickTypes, permission);
        });
        
        // Command actions
        register("command", config -> {
            List<String> commands = parseStringList(config.get("commands"));
            Set<ClickType> clickTypes = parseClickTypes(config.get("click-types"));
            String permission = (String) config.get("permission");
            boolean async = Boolean.parseBoolean(String.valueOf(config.get("async")));
            return new CommandAction(commands, false, clickTypes, permission, async);
        });
        
        register("command-console", config -> {
            List<String> commands = parseStringList(config.get("commands"));
            Set<ClickType> clickTypes = parseClickTypes(config.get("click-types"));
            String permission = (String) config.get("permission");
            boolean async = Boolean.parseBoolean(String.valueOf(config.get("async")));
            return new CommandAction(commands, true, clickTypes, permission, async);
        });
        
        // Close menu action
        register("close", config -> {
            Set<ClickType> clickTypes = parseClickTypes(config.get("click-types"));
            String permission = (String) config.get("permission");
            return new CloseMenuAction(clickTypes, permission);
        });
    }
    
    /**
     * Register an action factory
     */
    public void register(String type, ActionFactory factory) {
        actionFactories.put(type.toLowerCase(), factory);
    }
    
    /**
     * Create action from configuration
     */
    public Action createAction(String type, Map<String, Object> config) {
        ActionFactory factory = actionFactories.get(type.toLowerCase());
        if (factory == null) {
            plugin.getLogger().warning("Unknown action type: " + type);
            return null;
        }
        
        try {
            return factory.create(config);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to create action of type " + type + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Parse click types from configuration
     */
    private Set<ClickType> parseClickTypes(Object clickTypes) {
        if (clickTypes == null) return null;
        
        Set<ClickType> result = new HashSet<>();
        
        if (clickTypes instanceof List<?> list) {
            for (Object item : list) {
                try {
                    result.add(ClickType.valueOf(item.toString().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid click type: " + item);
                }
            }
        } else if (clickTypes instanceof String str) {
            try {
                result.add(ClickType.valueOf(str.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid click type: " + str);
            }
        }
        
        return result.isEmpty() ? null : result;
    }
    
    /**
     * Parse string list from configuration
     */
    @SuppressWarnings("unchecked")
    private List<String> parseStringList(Object value) {
        if (value == null) return List.of();
        
        if (value instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        } else if (value instanceof String str) {
            return List.of(str);
        }
        
        return List.of();
    }
    
    /**
     * Get all registered action types
     */
    public Set<String> getRegisteredTypes() {
        return Collections.unmodifiableSet(actionFactories.keySet());
    }
    
    /**
     * Functional interface for action factories
     */
    @FunctionalInterface
    public interface ActionFactory {
        Action create(Map<String, Object> config) throws Exception;
    }
}

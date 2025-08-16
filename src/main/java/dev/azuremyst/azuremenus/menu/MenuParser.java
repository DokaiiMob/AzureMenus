package dev.azuremyst.azuremenus.menu;

import dev.azuremyst.azuremenus.AzureMenusPlugin;
import dev.azuremyst.azuremenus.actions.Action;
import dev.azuremyst.azuremenus.menu.items.MenuItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;

import java.util.*;

/**
 * Parser for loading menus from YAML configuration
 */
public class MenuParser {
    
    private final AzureMenusPlugin plugin;
    
    public MenuParser(AzureMenusPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Parse menu from YAML configuration
     */
    public Menu parseMenu(String name, YamlConfiguration config) {
        try {
            // Basic menu properties
            String title = config.getString("title", name);
            int size = config.getInt("size", 27);
            int updateInterval = config.getInt("update-interval", -1);
            String sound = config.getString("sound", "");
            
            // Permissions
            Set<String> permissions = new HashSet<>();
            List<String> permList = config.getStringList("permissions");
            if (!permList.isEmpty()) {
                permissions.addAll(permList);
            } else if (config.contains("permission")) {
                permissions.add(config.getString("permission"));
            }
            
            // Fill settings
            boolean fillEmpty = config.getBoolean("fill.enabled", false);
            MenuItem fillItem = null;
            if (fillEmpty && config.contains("fill")) {
                fillItem = parseFillItem(config.getConfigurationSection("fill"));
            }
            
            // Parse items
            Map<Integer, MenuItem> items = new HashMap<>();
            ConfigurationSection itemsSection = config.getConfigurationSection("items");
            if (itemsSection != null) {
                for (String itemKey : itemsSection.getKeys(false)) {
                    ConfigurationSection itemSection = itemsSection.getConfigurationSection(itemKey);
                    if (itemSection != null) {
                        MenuItem item = parseMenuItem(itemKey, itemSection);
                        if (item != null) {
                            // Parse slot(s)
                            Object slotValue = itemSection.get("slot");
                            List<Integer> slots = parseSlots(slotValue);
                            
                            for (Integer slot : slots) {
                                if (slot >= 0 && slot < size) {
                                    items.put(slot, item);
                                }
                            }
                        }
                    }
                }
            }
            
            // Additional properties
            Map<String, Object> properties = new HashMap<>();
            for (String key : config.getKeys(false)) {
                if (!isReservedKey(key)) {
                    properties.put(key, config.get(key));
                }
            }
            
            return new Menu(name, title, size, items, permissions, 
                          updateInterval, sound, fillEmpty, fillItem, properties);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to parse menu '" + name + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Parse fill item
     */
    private MenuItem parseFillItem(ConfigurationSection section) {
        String material = section.getString("material", "BLACK_STAINED_GLASS_PANE");
        String name = section.getString("name", " ");
        
        return new MenuItem(
            "fill", 
            parseMaterial(material),
            1, 
            (short) 0,
            name,
            List.of(),
            List.of(),
            Set.of(),
            null,
            false,
            Set.of(ItemFlag.HIDE_ATTRIBUTES),
            null,
            null,
            new HashMap<>()
        );
    }
    
    /**
     * Parse menu item from configuration section
     */
    private MenuItem parseMenuItem(String id, ConfigurationSection section) {
        try {
            // Material
            String materialStr = section.getString("material", "STONE");
            Material material = parseMaterial(materialStr);
            
            // Basic properties
            int amount = section.getInt("amount", 1);
            short durability = (short) section.getInt("durability", 0);
            String name = section.getString("name", "");
            List<String> lore = section.getStringList("lore");
            
            // Permissions
            Set<String> permissions = new HashSet<>();
            List<String> permList = section.getStringList("permissions");
            if (!permList.isEmpty()) {
                permissions.addAll(permList);
            } else if (section.contains("permission")) {
                permissions.add(section.getString("permission"));
            }
            
            // Condition
            String condition = section.getString("condition", null);
            
            // Visual properties
            boolean enchanted = section.getBoolean("enchanted", false);
            Set<ItemFlag> itemFlags = parseItemFlags(section.getStringList("item-flags"));
            
            // Skull properties
            String skullTexture = section.getString("skull-texture", null);
            String skullOwner = section.getString("skull-owner", null);
            
            // Parse actions
            List<Action> actions = parseActions(section);
            
            // Additional properties
            Map<String, Object> properties = new HashMap<>();
            for (String key : section.getKeys(false)) {
                if (!isReservedItemKey(key)) {
                    properties.put(key, section.get(key));
                }
            }
            
            return new MenuItem(id, material, amount, durability, name, lore, actions,
                              permissions, condition, enchanted, itemFlags, 
                              skullTexture, skullOwner, properties);
            
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to parse menu item '" + id + "': " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Parse actions from configuration section
     */
    private List<Action> parseActions(ConfigurationSection section) {
        List<Action> actions = new ArrayList<>();
        
        // Check for actions list
        if (section.contains("actions")) {
            List<Map<?, ?>> actionsList = section.getMapList("actions");
            for (Map<?, ?> actionMap : actionsList) {
                @SuppressWarnings("unchecked")
                Map<String, Object> actionConfig = (Map<String, Object>) actionMap;
                String type = (String) actionConfig.get("type");
                if (type != null) {
                    Action action = plugin.getActionRegistry().createAction(type, actionConfig);
                    if (action != null) {
                        actions.add(action);
                    }
                }
            }
        }
        
        // Legacy support - direct action properties
        if (section.contains("command")) {
            Map<String, Object> actionConfig = new HashMap<>();
            actionConfig.put("commands", List.of(section.getString("command")));
            Action action = plugin.getActionRegistry().createAction("command", actionConfig);
            if (action != null) {
                actions.add(action);
            }
        }
        
        if (section.contains("message")) {
            Map<String, Object> actionConfig = new HashMap<>();
            actionConfig.put("message", section.getString("message"));
            Action action = plugin.getActionRegistry().createAction("message", actionConfig);
            if (action != null) {
                actions.add(action);
            }
        }
        
        return actions;
    }
    
    /**
     * Parse slot specification
     */
    private List<Integer> parseSlots(Object slotValue) {
        List<Integer> slots = new ArrayList<>();
        
        if (slotValue instanceof Integer) {
            slots.add((Integer) slotValue);
        } else if (slotValue instanceof String str) {
            // Handle range notation (e.g., "10-15")
            if (str.contains("-")) {
                String[] parts = str.split("-");
                if (parts.length == 2) {
                    try {
                        int start = Integer.parseInt(parts[0].trim());
                        int end = Integer.parseInt(parts[1].trim());
                        for (int i = start; i <= end; i++) {
                            slots.add(i);
                        }
                    } catch (NumberFormatException e) {
                        plugin.getLogger().warning("Invalid slot range: " + str);
                    }
                }
            } else {
                // Single slot as string
                try {
                    slots.add(Integer.parseInt(str));
                } catch (NumberFormatException e) {
                    plugin.getLogger().warning("Invalid slot number: " + str);
                }
            }
        } else if (slotValue instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof Integer) {
                    slots.add((Integer) item);
                } else {
                    try {
                        slots.add(Integer.parseInt(item.toString()));
                    } catch (NumberFormatException e) {
                        plugin.getLogger().warning("Invalid slot number in list: " + item);
                    }
                }
            }
        }
        
        return slots;
    }
    
    /**
     * Parse material from string
     */
    private Material parseMaterial(String materialStr) {
        try {
            return Material.valueOf(materialStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid material: " + materialStr + ", using STONE");
            return Material.STONE;
        }
    }
    
    /**
     * Parse item flags from string list
     */
    private Set<ItemFlag> parseItemFlags(List<String> flagStrings) {
        Set<ItemFlag> flags = new HashSet<>();
        for (String flagStr : flagStrings) {
            try {
                flags.add(ItemFlag.valueOf(flagStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid item flag: " + flagStr);
            }
        }
        return flags;
    }
    
    /**
     * Check if key is reserved for menu configuration
     */
    private boolean isReservedKey(String key) {
        return Set.of("title", "size", "update-interval", "sound", "permissions", 
                     "permission", "fill", "items").contains(key);
    }
    
    /**
     * Check if key is reserved for item configuration
     */
    private boolean isReservedItemKey(String key) {
        return Set.of("slot", "material", "amount", "durability", "name", "lore", 
                     "permissions", "permission", "condition", "enchanted", "item-flags",
                     "skull-texture", "skull-owner", "actions", "command", "message").contains(key);
    }
}

package dev.azuremyst.azuremenus.menu;

import dev.azuremyst.azuremenus.AzureMenusPlugin;
import dev.azuremyst.azuremenus.menu.items.MenuItem;
import dev.azuremyst.azuremenus.utils.ColorUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a menu that can be displayed to players
 */
public class Menu implements InventoryHolder {
    
    private final String name;
    private final String title;
    private final int size;
    private final Map<Integer, MenuItem> items;
    private final Set<String> permissions;
    private final int updateInterval;
    private final String sound;
    private final boolean fillEmpty;
    private final MenuItem fillItem;
    private final Map<String, Object> properties;
    
    private Inventory inventory;
    
    public Menu(String name, String title, int size, Map<Integer, MenuItem> items,
                Set<String> permissions, int updateInterval, String sound,
                boolean fillEmpty, MenuItem fillItem, Map<String, Object> properties) {
        this.name = name;
        this.title = title;
        this.size = validateSize(size);
        this.items = items != null ? items : new HashMap<>();
        this.permissions = permissions != null ? permissions : Set.of();
        this.updateInterval = updateInterval;
        this.sound = sound;
        this.fillEmpty = fillEmpty;
        this.fillItem = fillItem;
        this.properties = properties != null ? properties : new HashMap<>();
        
        createInventory();
    }
    
    /**
     * Validate and fix menu size
     */
    private int validateSize(int size) {
        if (size < 9) return 9;
        if (size > 54) return 54;
        // Round to nearest multiple of 9
        return (int) (Math.ceil(size / 9.0) * 9);
    }
    
    /**
     * Create the Bukkit inventory
     */
    private void createInventory() {
        Component titleComponent = ColorUtil.parseComponent(title);
        this.inventory = Bukkit.createInventory(this, size, titleComponent);
    }
    
    /**
     * Check if player has permission to view this menu
     */
    public boolean hasPermission(Player player) {
        if (permissions.isEmpty()) return true;
        
        for (String permission : permissions) {
            if (player.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Populate inventory with items
     */
    public void populate(Player player) {
        inventory.clear();
        
        // Fill empty slots if configured
        if (fillEmpty && fillItem != null) {
            for (int i = 0; i < size; i++) {
                inventory.setItem(i, fillItem.buildItemStack(player));
            }
        }
        
        // Place menu items
        for (Map.Entry<Integer, MenuItem> entry : items.entrySet()) {
            int slot = entry.getKey();
            MenuItem item = entry.getValue();
            
            if (slot >= 0 && slot < size && item.canView(player)) {
                inventory.setItem(slot, item.buildItemStack(player));
            }
        }
    }
    
    /**
     * Handle click on menu item
     */
    public void handleClick(Player player, int slot, org.bukkit.event.inventory.ClickType clickType) {
        MenuItem item = items.get(slot);
        if (item != null) {
            item.handleClick(player, clickType);
        }
    }
    
    /**
     * Open menu for player
     */
    public void open(Player player) {
        if (!hasPermission(player)) {
            AzureMenusPlugin.getInstance().getLanguageManager()
                .sendPrefixedMessage(player, "menus.errors.permission-denied");
            return;
        }
        
        populate(player);
        player.openInventory(inventory);
        
        // Play sound if configured
        if (sound != null && !sound.isEmpty()) {
            try {
                org.bukkit.Sound bukkitSound = org.bukkit.Sound.valueOf(sound.toUpperCase());
                player.playSound(player.getLocation(), bukkitSound, 1.0f, 1.0f);
            } catch (IllegalArgumentException e) {
                AzureMenusPlugin.getInstance().getLogger().warning(
                    "Invalid sound '" + sound + "' in menu '" + name + "'");
            }
        }
    }
    
    /**
     * Update menu for all viewers
     */
    public void update() {
        for (org.bukkit.entity.HumanEntity viewer : inventory.getViewers()) {
            if (viewer instanceof Player player) {
                populate(player);
            }
        }
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getTitle() {
        return title;
    }
    
    public int getSize() {
        return size;
    }
    
    public Map<Integer, MenuItem> getItems() {
        return items;
    }
    
    public Set<String> getPermissions() {
        return permissions;
    }
    
    public int getUpdateInterval() {
        return updateInterval;
    }
    
    public String getSound() {
        return sound;
    }
    
    public boolean isFillEmpty() {
        return fillEmpty;
    }
    
    public MenuItem getFillItem() {
        return fillItem;
    }
    
    public Map<String, Object> getProperties() {
        return properties;
    }
    
    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }
}

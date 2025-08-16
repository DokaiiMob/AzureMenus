package dev.azuremyst.azuremenus.menu.items;

import dev.azuremyst.azuremenus.AzureMenusPlugin;
import dev.azuremyst.azuremenus.actions.Action;
import dev.azuremyst.azuremenus.utils.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/**
 * Represents an item in a menu
 */
public class MenuItem {
    
    private final String id;
    private final Material material;
    private final int amount;
    private final short durability;
    private final String name;
    private final List<String> lore;
    private final List<Action> actions;
    private final Set<String> permissions;
    private final String condition;
    private final boolean enchanted;
    private final Set<ItemFlag> itemFlags;
    private final String skullTexture;
    private final String skullOwner;
    private final Map<String, Object> properties;
    
    public MenuItem(String id, Material material, int amount, short durability,
                   String name, List<String> lore, List<Action> actions,
                   Set<String> permissions, String condition, boolean enchanted,
                   Set<ItemFlag> itemFlags, String skullTexture, String skullOwner,
                   Map<String, Object> properties) {
        this.id = id;
        this.material = material != null ? material : Material.STONE;
        this.amount = Math.max(1, Math.min(64, amount));
        this.durability = durability;
        this.name = name != null ? name : "";
        this.lore = lore != null ? lore : new ArrayList<>();
        this.actions = actions != null ? actions : new ArrayList<>();
        this.permissions = permissions != null ? permissions : new HashSet<>();
        this.condition = condition;
        this.enchanted = enchanted;
        this.itemFlags = itemFlags != null ? itemFlags : new HashSet<>();
        this.skullTexture = skullTexture;
        this.skullOwner = skullOwner;
        this.properties = properties != null ? properties : new HashMap<>();
    }
    
    /**
     * Check if player can view this item
     */
    public boolean canView(Player player) {
        // Check permissions
        if (!permissions.isEmpty()) {
            boolean hasPermission = false;
            for (String permission : permissions) {
                if (player.hasPermission(permission)) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission) return false;
        }
        
        // Check condition
        if (condition != null && !condition.isEmpty()) {
            return evaluateCondition(player, condition);
        }
        
        return true;
    }
    
    /**
     * Evaluate a condition string
     */
    private boolean evaluateCondition(Player player, String condition) {
        // Replace placeholders
        String processedCondition = replacePlaceholders(player, condition);
        
        // Simple condition evaluation (can be expanded later)
        // For now, just check if it's not "false" or "0"
        return !processedCondition.equalsIgnoreCase("false") && 
               !processedCondition.equals("0") &&
               !processedCondition.isEmpty();
    }
    
    /**
     * Build ItemStack for this menu item
     */
    public ItemStack buildItemStack(Player player) {
        ItemStack item = new ItemStack(material, amount);
        
        // Set durability if specified
        if (durability > 0) {
            item.setDurability(durability);
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            // Set display name
            if (!name.isEmpty()) {
                String processedName = replacePlaceholders(player, name);
                Component nameComponent = ColorUtil.parseComponent(processedName);
                meta.displayName(nameComponent);
            }
            
            // Set lore
            if (!lore.isEmpty()) {
                List<Component> loreComponents = new ArrayList<>();
                for (String loreLine : lore) {
                    String processedLine = replacePlaceholders(player, loreLine);
                    loreComponents.add(ColorUtil.parseComponent(processedLine));
                }
                meta.lore(loreComponents);
            }
            
            // Add enchantment effect
            if (enchanted) {
                item.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.UNBREAKING, 1);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            
            // Add item flags
            if (!itemFlags.isEmpty()) {
                meta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
            }
            
            // Handle skull meta
            if (meta instanceof SkullMeta skullMeta) {
                if (skullOwner != null && !skullOwner.isEmpty()) {
                    String processedOwner = replacePlaceholders(player, skullOwner);
                    skullMeta.setOwner(processedOwner);
                }
                
                if (skullTexture != null && !skullTexture.isEmpty()) {
                    // Set skull texture (base64)
                    setSkullTexture(skullMeta, skullTexture);
                }
            }
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Set skull texture using base64
     */
    private void setSkullTexture(SkullMeta meta, String texture) {
        try {
            // This is a simplified version - full implementation would use reflection
            // or a library like NBT-API for setting custom skull textures
            AzureMenusPlugin.getInstance().getLogger().info(
                "Skull texture setting not fully implemented: " + texture);
        } catch (Exception e) {
            AzureMenusPlugin.getInstance().getLogger().warning(
                "Failed to set skull texture: " + e.getMessage());
        }
    }
    
    /**
     * Replace placeholders in a string
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
    
    /**
     * Handle click on this item
     */
    public void handleClick(Player player, ClickType clickType) {
        if (!canView(player)) return;
        
        // Execute actions
        for (Action action : actions) {
            if (action.canExecute(player, clickType)) {
                action.execute(player, clickType);
            }
        }
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public short getDurability() {
        return durability;
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getLore() {
        return lore;
    }
    
    public List<Action> getActions() {
        return actions;
    }
    
    public Set<String> getPermissions() {
        return permissions;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public boolean isEnchanted() {
        return enchanted;
    }
    
    public Set<ItemFlag> getItemFlags() {
        return itemFlags;
    }
    
    public String getSkullTexture() {
        return skullTexture;
    }
    
    public String getSkullOwner() {
        return skullOwner;
    }
    
    public Map<String, Object> getProperties() {
        return properties;
    }
}

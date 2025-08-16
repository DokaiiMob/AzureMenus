package dev.azuremyst.azuremenus.placeholders;

import dev.azuremyst.azuremenus.AzureMenusPlugin;

/**
 * Manages placeholder integrations
 */
public class PlaceholderService {
    
    private final AzureMenusPlugin plugin;
    
    public PlaceholderService(AzureMenusPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void unregister() {
        // TODO: Implement placeholder unregistration
    }
}

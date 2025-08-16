package dev.azuremyst.azuremenus.commands;

import dev.azuremyst.azuremenus.AzureMenusPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Main command executor for AzureMenus
 */
public class AzureMenusCommand implements CommandExecutor {
    
    private final AzureMenusPlugin plugin;
    
    public AzureMenusCommand(AzureMenusPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // TODO: Implement command logic
        return true;
    }
}

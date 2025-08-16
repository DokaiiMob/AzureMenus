package dev.azuremyst.azuremenus.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.awt.Color;
import java.util.regex.Pattern;

/**
 * Utility class for handling colors, gradients and text formatting for AzureMyst server
 */
public class ColorUtil {
    
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();
    
    // AzureMyst signature colors
    public static final String AZURE_PURPLE = "#8A2BE2"; // BlueViolet
    public static final String AZURE_CYAN = "#00BFFF";   // DeepSkyBlue
    
    // Common gradient patterns
    public static final String AZURE_GRADIENT = "<gradient:" + AZURE_PURPLE + ":" + AZURE_CYAN + ">";
    public static final String AZURE_GRADIENT_REVERSE = "<gradient:" + AZURE_CYAN + ":" + AZURE_PURPLE + ">";
    
    // Regex patterns for color detection
    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:[^>]+>");
    
    /**
     * Parse MiniMessage format to Component
     */
    public static Component parseComponent(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }
        return miniMessage.deserialize(text);
    }
    
    /**
     * Parse and strip colors from text for console output
     */
    public static String stripColor(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        Component component = parseComponent(text);
        return legacySerializer.serialize(component).replaceAll("§[0-9a-fk-or]", "");
    }
    
    /**
     * Apply AzureMyst gradient to text
     */
    public static String applyAzureGradient(String text) {
        return AZURE_GRADIENT + text + "</gradient>";
    }
    
    /**
     * Apply reversed AzureMyst gradient to text
     */
    public static String applyAzureGradientReverse(String text) {
        return AZURE_GRADIENT_REVERSE + text + "</gradient>";
    }
    
    /**
     * Create a custom gradient between two hex colors
     */
    public static String createGradient(String startHex, String endHex, String text) {
        return "<gradient:" + startHex + ":" + endHex + ">" + text + "</gradient>";
    }
    
    /**
     * Check if text contains MiniMessage formatting
     */
    public static boolean containsFormatting(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        return text.contains("<") && text.contains(">") || 
               HEX_PATTERN.matcher(text).find() ||
               GRADIENT_PATTERN.matcher(text).find();
    }
    
    /**
     * Convert legacy color codes (&) to MiniMessage format
     */
    public static String convertLegacyToMiniMessage(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // Basic color code conversion
        return text
            .replace("&0", "<black>")
            .replace("&1", "<dark_blue>")
            .replace("&2", "<dark_green>")
            .replace("&3", "<dark_aqua>")
            .replace("&4", "<dark_red>")
            .replace("&5", "<dark_purple>")
            .replace("&6", "<gold>")
            .replace("&7", "<gray>")
            .replace("&8", "<dark_gray>")
            .replace("&9", "<blue>")
            .replace("&a", "<green>")
            .replace("&b", "<aqua>")
            .replace("&c", "<red>")
            .replace("&d", "<light_purple>")
            .replace("&e", "<yellow>")
            .replace("&f", "<white>")
            .replace("&k", "<obf>")
            .replace("&l", "<b>")
            .replace("&m", "<st>")
            .replace("&n", "<u>")
            .replace("&o", "<i>")
            .replace("&r", "<reset>");
    }
    
    /**
     * Interpolate between two colors
     */
    public static String interpolateColor(String startHex, String endHex, float ratio) {
        Color startColor = Color.decode(startHex);
        Color endColor = Color.decode(endHex);
        
        int red = (int) (startColor.getRed() + ratio * (endColor.getRed() - startColor.getRed()));
        int green = (int) (startColor.getGreen() + ratio * (endColor.getGreen() - startColor.getGreen()));
        int blue = (int) (startColor.getBlue() + ratio * (endColor.getBlue() - startColor.getBlue()));
        
        return String.format("#%02X%02X%02X", red, green, blue);
    }
    
    /**
     * Create a progress bar with AzureMyst gradient
     */
    public static String createProgressBar(double percentage, int length) {
        int filled = (int) (percentage * length);
        StringBuilder bar = new StringBuilder();
        
        bar.append(AZURE_GRADIENT);
        
        // Filled portion
        for (int i = 0; i < filled; i++) {
            bar.append("█");
        }
        
        bar.append("</gradient><gray>");
        
        // Empty portion
        for (int i = filled; i < length; i++) {
            bar.append("░");
        }
        
        bar.append("</gray>");
        
        return bar.toString();
    }
    
    /**
     * Format a prefix with AzureMyst styling
     */
    public static String formatPrefix(String prefix) {
        return applyAzureGradient("【" + prefix + "】");
    }
    
    /**
     * Validate hex color format
     */
    public static boolean isValidHex(String hex) {
        return hex != null && HEX_PATTERN.matcher(hex).matches();
    }
}

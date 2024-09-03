package net.smoothplugins.smoothbase.paper.gui.menu.manager;

import net.smoothplugins.smoothbase.paper.gui.menu.listener.InventoryClickListener;
import net.smoothplugins.smoothbase.paper.gui.menu.listener.InventoryCloseListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Manages the registration of menu event listeners.
 */
public class MenuManager {

    private final Plugin plugin;

    /**
     * Creates a new MenuManager.
     *
     * @param plugin The plugin instance.
     */
    public MenuManager(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers the menu event listeners.
     */
    public void registerListeners() {
        InventoryClickListener inventoryClickListener = new InventoryClickListener();
        InventoryCloseListener inventoryCloseListener = new InventoryCloseListener();
        Bukkit.getPluginManager().registerEvents(inventoryClickListener, plugin);
        Bukkit.getPluginManager().registerEvents(inventoryCloseListener, plugin);
    }
}

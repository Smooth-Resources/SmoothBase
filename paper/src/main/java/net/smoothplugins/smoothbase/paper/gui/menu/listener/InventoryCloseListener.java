package net.smoothplugins.smoothbase.paper.gui.menu.listener;

import net.smoothplugins.smoothbase.paper.gui.menu.manager.OpenMenuManager;
import net.smoothplugins.smoothbase.paper.gui.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Listener for inventory close events.
 */
public class InventoryCloseListener implements Listener {

    /**
     * Handles the inventory close event.
     *
     * @param event The inventory close event.
     */
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        Menu menu = OpenMenuManager.getMenu(player);
        if (menu == null) return;

        OpenMenuManager.removePlayer(player);
        menu.onClose(event);
    }
}

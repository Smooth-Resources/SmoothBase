package com.smoothresources.smoothbase.paper.gui.menu.listener;

import com.smoothresources.smoothbase.paper.gui.menu.Menu;
import com.smoothresources.smoothbase.paper.gui.menu.manager.OpenMenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Listener for inventory click events.
 */
public class InventoryClickListener implements Listener {

    /**
     * Handles the inventory click event.
     *
     * @param event The inventory click event.
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Menu menu = OpenMenuManager.getMenu(player);
        if (menu == null) return;

        menu.onClick(event);
    }
}

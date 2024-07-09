package net.smoothplugins.smoothbase.paper.menu.listener;

import net.smoothplugins.smoothbase.paper.menu.Menu;
import net.smoothplugins.smoothbase.paper.menu.button.Button;
import net.smoothplugins.smoothbase.paper.menu.button.ClickableButton;
import net.smoothplugins.smoothbase.paper.menu.event.PlayerClickButtonEvent;
import net.smoothplugins.smoothbase.paper.menu.manager.OpenMenuManager;
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

        event.setCancelled(true);

        int clickedSlot = event.getSlot();
        Button button = menu.getButtonBySlot(clickedSlot);

        if (!(button instanceof ClickableButton clickableButton)) return;
        clickableButton.onClick(new PlayerClickButtonEvent(menu, clickableButton, player, event));
    }
}

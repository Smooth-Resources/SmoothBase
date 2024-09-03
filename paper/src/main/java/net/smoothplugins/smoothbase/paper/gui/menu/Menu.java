package net.smoothplugins.smoothbase.paper.gui.menu;

import net.smoothplugins.smoothbase.paper.gui.GUI;
import net.smoothplugins.smoothbase.paper.gui.menu.button.ClickableButton;
import net.smoothplugins.smoothbase.paper.gui.menu.manager.OpenMenuManager;
import net.smoothplugins.smoothbase.paper.gui.menu.button.Button;
import net.smoothplugins.smoothbase.paper.gui.menu.event.PlayerClickButtonEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a menu in the game.
 */
public abstract class Menu extends GUI {

    private Inventory inventory;
    private final List<Button> items;
    private ItemStack backgroundItem;

    /**
     * Creates a new Menu for a player.
     *
     * @param player The player.
     */
    public Menu(@NotNull Player player) {
        super(player);
        this.items = new ArrayList<>();
    }

    /**
     * Creates the inventory for the menu.
     */
    public abstract void createInventory();

    /**
     * Sets the items in the menu.
     */
    public abstract void setItems();

    /**
     * Handles the menu close event.
     *
     * @param event The inventory close event.
     */
    public void onClose(InventoryCloseEvent event) {
        // Most of the time not needed. Override if needed.
    }

    /**
     * Handles the menu click event.
     *
     * @param event The inventory click event.
     */
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        int clickedSlot = event.getSlot();
        Button button = getButtonBySlot(clickedSlot);

        if (!(button instanceof ClickableButton clickableButton)) return;
        clickableButton.onClick(new PlayerClickButtonEvent(this, clickableButton, getPlayer(), event));
    }

    /**
     * Opens the menu for the player.
     */
    @Override
    public void open() {
        createInventory();
        if (inventory == null) {
            throw new IllegalStateException("Inventory cannot be null, please create it in createInventory method.");
        }

        setItems();

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, backgroundItem);
            }
        }

        if (getPlayer().openInventory(inventory) != null) {
            OpenMenuManager.addPlayer(getPlayer(), this);
        }
    }

    /**
     * Sets a button in the menu.
     *
     * @param button The button to set.
     */
    public void setItem(@NotNull Button button) {
        Button previousButton = getButtonBySlot(button.getSlots().get(0));
        if (previousButton != null) {
            removeItem(previousButton);
        }

        items.add(button);
        button.getSlots().forEach(slot -> inventory.setItem(slot, button.getItemStack()));
    }

    /**
     * Removes a button from the menu.
     *
     * @param button The button to remove.
     */
    public void removeItem(@NotNull Button button) {
        items.remove(button);
        button.getSlots().forEach(slot -> inventory.setItem(slot, null));
    }

    /**
     * Gets the button in a specific slot.
     *
     * @param slot The slot.
     * @return The button, or null if no button is in the slot.
     */
    @Nullable
    public Button getButtonBySlot(int slot) {
        return items.stream()
                .filter(button -> button.getSlots().contains(slot))
                .findFirst()
                .orElse(null);
    }

    /**
     * Sets the background item for the menu.
     *
     * @param backgroundItem The background item.
     */
    public void setBackgroundItem(@Nullable ItemStack backgroundItem) {
        this.backgroundItem = backgroundItem;
    }

    /**
     * Gets the background item of the menu.
     *
     * @return The background item, or null if not set.
     */
    @Nullable
    public ItemStack getBackgroundItem() {
        return backgroundItem;
    }

    /**
     * Gets the inventory of the menu.
     *
     * @return The inventory.
     */
    @Nullable
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Sets the inventory for the menu.
     *
     * @param inventory The inventory to set.
     */
    public void setInventory(@NotNull Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Gets the items in the menu.
     *
     * @return The list of items.
     */
    @NotNull
    public List<Button> getItems() {
        return items;
    }
}

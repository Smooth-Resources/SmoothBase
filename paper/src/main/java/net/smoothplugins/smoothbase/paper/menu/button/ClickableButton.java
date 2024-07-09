package net.smoothplugins.smoothbase.paper.menu.button;

import net.smoothplugins.smoothbase.paper.menu.event.PlayerClickButtonEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a clickable button in a menu.
 */
public abstract class ClickableButton extends Button {

    /**
     * Creates a ClickableButton with a single slot.
     *
     * @param itemStack The item stack representing the button.
     * @param slot      The slot where the button is placed.
     */
    public ClickableButton(@NotNull ItemStack itemStack, int slot) {
        super(itemStack, slot);
    }

    /**
     * Handles the click event on the button.
     *
     * @param event The click event.
     */
    public abstract void onClick(@NotNull PlayerClickButtonEvent event);
}

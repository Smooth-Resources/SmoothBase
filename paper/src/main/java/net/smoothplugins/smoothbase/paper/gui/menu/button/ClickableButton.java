package net.smoothplugins.smoothbase.paper.gui.menu.button;

import net.smoothplugins.smoothbase.paper.gui.menu.event.PlayerClickButtonEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a clickable button in a menu.
 */
public class ClickableButton extends Button {

    private final Consumer<PlayerClickButtonEvent> onClickAction;

    /**
     * Creates a ClickableButton with a single slot.
     *
     * @param itemStack The item stack representing the button.
     * @param slot      The slot where the button is placed.
     * @param onClick   The action to execute on click.
     */
    public ClickableButton(@NotNull ItemStack itemStack, int slot, @NotNull Consumer<PlayerClickButtonEvent> onClick) {
        super(itemStack, slot);
        this.onClickAction = onClick;
    }

    /**
     * Creates a ClickableButton with multiple slots.
     *
     * @param itemStack The item stack representing the button.
     * @param slots     The slots where the button is placed.
     * @param onClick   The action to execute on click.
     */
    public ClickableButton(@NotNull ItemStack itemStack, List<Integer> slots, @NotNull Consumer<PlayerClickButtonEvent> onClick) {
        super(itemStack, slots);
        this.onClickAction = onClick;
    }

    /**
     * Handles the click event on the button.
     *
     * @param event The click event.
     */
    public void onClick(@NotNull PlayerClickButtonEvent event) {
        onClickAction.accept(event);
    }
}
package com.smoothresources.smoothbase.paper.gui.menu.button;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a button in a menu with a corresponding item and slots.
 */
public class Button {

    private final ItemStack itemStack;
    private final List<Integer> slots;

    /**
     * Creates a Button with a single slot.
     *
     * @param itemStack The item stack representing the button.
     * @param slot      The slot where the button is placed.
     */
    public Button(@NotNull ItemStack itemStack, int slot) {
        this.itemStack = itemStack;
        this.slots = new ArrayList<>();
        this.slots.add(slot);
    }

    /**
     * Creates a Button with multiple slots.
     *
     * @param itemStack The item stack representing the button.
     * @param slots     The slots where the button is placed.
     */
    public Button(@NotNull ItemStack itemStack, @NotNull List<Integer> slots) {
        this.itemStack = itemStack;
        this.slots = slots;
    }

    /**
     * Adds a slot to the button.
     *
     * @param slot The slot to add.
     */
    public void addSlot(int slot) {
        slots.add(slot);
    }

    /**
     * Removes a slot from the button.
     *
     * @param slot The slot to remove.
     */
    public void removeSlot(int slot) {
        slots.remove(slot);
    }

    /**
     * Sets a single slot for the button, clearing any existing slots.
     *
     * @param slot The slot to set.
     */
    public void setSlot(int slot) {
        slots.clear();
        slots.add(slot);
    }

    /**
     * Gets the item stack of the button.
     *
     * @return The item stack.
     */
    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Gets the slots of the button.
     *
     * @return The slots.
     */
    @NotNull
    public List<Integer> getSlots() {
        return slots;
    }
}

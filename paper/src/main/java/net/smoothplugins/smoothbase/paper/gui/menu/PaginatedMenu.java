package net.smoothplugins.smoothbase.paper.gui.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a paginated menu in the game.
 */
public abstract class PaginatedMenu extends Menu {

    private int currentPage;

    /**
     * Creates a new PaginatedMenu for a player.
     *
     * @param player The player.
     */
    public PaginatedMenu(@NotNull Player player) {
        super(player);
    }

    /**
     * Gets the current page of the menu.
     *
     * @return The current page.
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the current page of the menu.
     *
     * @param currentPage The current page to set.
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}

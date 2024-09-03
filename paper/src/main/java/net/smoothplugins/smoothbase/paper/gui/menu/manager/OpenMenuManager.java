package net.smoothplugins.smoothbase.paper.gui.menu.manager;

import net.smoothplugins.smoothbase.paper.gui.menu.Menu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Manages open menus for players.
 */
public class OpenMenuManager {

    private static final HashMap<Player, Menu> playersWithOpenMenu = new HashMap<>();

    /**
     * Adds a player with an open menu.
     *
     * @param player The player.
     * @param menu   The menu.
     */
    public synchronized static void addPlayer(@NotNull Player player, @NotNull Menu menu) {
        playersWithOpenMenu.put(player, menu);
    }

    /**
     * Removes a player with an open menu.
     *
     * @param player The player.
     */
    public synchronized static void removePlayer(@NotNull Player player) {
        playersWithOpenMenu.remove(player);
    }

    /**
     * Gets the menu for a player.
     *
     * @param player The player.
     * @return The menu, or null if no menu is open.
     */
    @Nullable
    public synchronized static Menu getMenu(@NotNull Player player) {
        return playersWithOpenMenu.get(player);
    }

    /**
     * Checks if a player has an open menu.
     *
     * @param player The player.
     * @return True if the player has an open menu, false otherwise.
     */
    public synchronized static boolean hasMenu(@NotNull Player player) {
        return playersWithOpenMenu.containsKey(player);
    }
}

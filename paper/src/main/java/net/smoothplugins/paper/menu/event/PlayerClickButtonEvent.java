package net.smoothplugins.paper.menu.event;

import net.smoothplugins.paper.menu.Menu;
import net.smoothplugins.paper.menu.button.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerClickButtonEvent {

    private final Menu menu;
    private final Button button;
    private final Player player;
    private final InventoryClickEvent originalBukkitEvent;

    public PlayerClickButtonEvent(@NotNull Menu menu, @NotNull Button button, @NotNull Player player, @NotNull InventoryClickEvent originalBukkitEvent) {
        this.menu = menu;
        this.button = button;
        this.player = player;
        this.originalBukkitEvent = originalBukkitEvent;
    }

    @NotNull
    public Menu getMenu() {
        return menu;
    }

    @NotNull
    public Button getButton() {
        return button;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public InventoryClickEvent getOriginalBukkitEvent() {
        return originalBukkitEvent;
    }
}

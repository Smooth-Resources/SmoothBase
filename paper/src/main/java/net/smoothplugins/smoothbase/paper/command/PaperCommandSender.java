package net.smoothplugins.smoothbase.paper.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a command sender in a Paper environment.
 */
public class PaperCommandSender implements net.smoothplugins.smoothbase.common.command.CommandSender {

    private final CommandSender sender;

    /**
     * Creates a new PaperCommandSender.
     *
     * @param sender The CommandSender instance.
     */
    public PaperCommandSender(@NotNull CommandSender sender) {
        this.sender = sender;
    }

    /**
     * Gets the Player instance of the sender.
     *
     * @return The Player instance.
     * @throws IllegalStateException If the sender is not a player.
     */
    public Player getPlayer() {
        if (!isPlayer()) {
            throw new IllegalStateException("Sender is not a player.");
        }

        return (Player) sender;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(@NotNull Component component) {
        sender.sendMessage(component);
    }
}

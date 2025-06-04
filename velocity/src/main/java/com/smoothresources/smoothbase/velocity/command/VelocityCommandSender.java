package com.smoothresources.smoothbase.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import com.smoothresources.smoothbase.common.command.CommandSender;
import com.smoothresources.smoothbase.common.component.ComponentUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a command sender in a Velocity environment.
 */
public class VelocityCommandSender implements CommandSender {

    private final CommandSource source;

    /**
     * Creates a new VelocityCommandSender.
     *
     * @param source The CommandSource instance.
     */
    public VelocityCommandSender(@NotNull CommandSource source) {
        this.source = source;
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

        return (Player) source;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return source.hasPermission(permission);
    }

    @Override
    public boolean isPlayer() {
        return source instanceof Player;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        source.sendMessage(ComponentUtils.toComponent(message));
    }

    @Override
    public void sendMessage(@NotNull Component component) {
        source.sendMessage(component);
    }
}

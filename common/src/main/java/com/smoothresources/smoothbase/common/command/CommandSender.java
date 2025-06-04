package com.smoothresources.smoothbase.common.command;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a command sender, which can be a player or the console.
 */
public interface CommandSender {

    /**
     * Checks if the sender has the specified permission.
     *
     * @param permission The permission to check.
     * @return true if the sender has the permission, false otherwise.
     */
    boolean hasPermission(@NotNull String permission);

    /**
     * Checks if the sender is a player.
     *
     * @return true if the sender is a player, false otherwise.
     */
    boolean isPlayer();

    /**
     * Sends a plain text message to the sender.
     *
     * @param message The message to send.
     */
    void sendMessage(@NotNull String message);

    /**
     * Sends a text component to the sender.
     *
     * @param component The text component to send.
     */
    void sendMessage(@NotNull Component component);
}

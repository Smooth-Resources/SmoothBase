package net.smoothplugins.smoothbase.common.command;

import org.jetbrains.annotations.NotNull;

/**
 * Manages the registration and handling of commands.
 */
public interface CommandManager {

    /**
     * Registers a command with the command manager.
     *
     * @param command The command to register.
     */
    void registerCommand(@NotNull Command command);
}

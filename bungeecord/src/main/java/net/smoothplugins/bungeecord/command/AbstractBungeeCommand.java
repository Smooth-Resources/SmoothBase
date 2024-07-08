package net.smoothplugins.bungeecord.command;

import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an abstract command that can be executed in a BungeeCord environment.
 */
public abstract class AbstractBungeeCommand extends Command implements TabExecutor {

    /**
     * Creates a new AbstractBungeeCommand.
     *
     * @param name       The name of the command.
     * @param permission The permission required to execute the command.
     * @param aliases    The aliases of the command.
     */
    public AbstractBungeeCommand(@NotNull String name, @Nullable String permission, @Nullable String... aliases) {
        super(name, permission, aliases);
    }
}

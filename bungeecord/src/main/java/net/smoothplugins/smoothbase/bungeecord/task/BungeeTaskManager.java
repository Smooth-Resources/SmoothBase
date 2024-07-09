package net.smoothplugins.smoothbase.bungeecord.task;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.smoothplugins.smoothbase.common.task.TaskManager;
import org.jetbrains.annotations.NotNull;

/**
 * Task manager for executing tasks in a BungeeCord server environment.
 */
public class BungeeTaskManager extends TaskManager {

    private final ProxyServer server;
    private final Plugin plugin;

    /**
     * Creates a new BungeeTaskManager.
     *
     * @param server The ProxyServer instance.
     * @param plugin The plugin instance.
     */
    public BungeeTaskManager(@NotNull ProxyServer server, @NotNull Plugin plugin) {
        this.server = server;
        this.plugin = plugin;
    }

    @Override
    public void runTaskSync(@NotNull Runnable task) {
        // BungeeCord does not support synchronous tasks (everything is async)
        server.getScheduler().runAsync(plugin, task);
    }
}

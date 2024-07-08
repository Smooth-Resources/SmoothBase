package net.smoothplugins.velocity.task;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.smoothplugins.common.task.TaskManager;
import org.jetbrains.annotations.NotNull;

/**
 * Task manager for executing tasks in a Velocity server environment.
 */
public class VelocityTaskManager extends TaskManager {

    private final ProxyServer server;
    private final Plugin plugin;

    /**
     * Creates a new VelocityTaskManager.
     *
     * @param server The ProxyServer instance.
     * @param plugin The plugin instance.
     */
    public VelocityTaskManager(@NotNull ProxyServer server, @NotNull Plugin plugin) {
        this.server = server;
        this.plugin = plugin;
    }

    @Override
    public void runTaskSync(@NotNull Runnable task) {
        // Velocity does not support synchronous tasks (everything is async)
        server.getScheduler()
                .buildTask(plugin, task)
                .schedule();
    }
}

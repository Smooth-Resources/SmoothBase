package com.smoothresources.smoothbase.velocity.task;

import com.velocitypowered.api.proxy.ProxyServer;
import com.smoothresources.smoothbase.common.task.TaskManager;
import org.jetbrains.annotations.NotNull;

/**
 * Task manager for executing tasks in a Velocity server environment.
 */
public class VelocityTaskManager extends TaskManager {

    private final ProxyServer server;
    private final Object plugin;


    /**
     * Creates a new VelocityTaskManager.
     *
     * @param server The ProxyServer instance.
     * @param plugin The plugin instance.
     */
    public VelocityTaskManager(@NotNull ProxyServer server, @NotNull Object plugin) {
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

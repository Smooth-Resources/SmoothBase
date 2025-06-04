package com.smoothresources.smoothbase.paper.task;

import com.smoothresources.smoothbase.common.task.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Task manager for executing tasks in a Paper server environment.
 */
public class PaperTaskManager extends TaskManager {

    private final Plugin plugin;

    /**
     * Creates a new PaperTaskManager.
     *
     * @param plugin The plugin instance.
     */
    public PaperTaskManager(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runTaskSync(@NotNull Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }
}

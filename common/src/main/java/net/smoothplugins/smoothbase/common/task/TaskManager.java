package net.smoothplugins.smoothbase.common.task;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract class for managing tasks.
 */
public abstract class TaskManager {

    /**
     * Runs a task synchronously.
     *
     * @param task The task to run.
     */
    public abstract void runTaskSync(@NotNull Runnable task);

    /**
     * Runs a task asynchronously.
     *
     * @param task The task to run.
     * @return The thread running the task.
     */
    @NotNull
    public Thread runTaskAsync(@NotNull Runnable task) {
        Thread thread = new Thread(task);
        thread.start();
        return thread;
    }

    /**
     * Runs a task synchronously after a delay.
     *
     * @param task  The task to run.
     * @param delay The delay in milliseconds before running the task.
     * @return The thread running the task.
     */
    @NotNull
    public Thread runTaskLaterSync(@NotNull Runnable task, long delay) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            runTaskSync(task);
        });
        thread.start();
        return thread;
    }

    /**
     * Runs a task asynchronously after a delay.
     *
     * @param task  The task to run.
     * @param delay The delay in milliseconds before running the task.
     * @return The thread running the task.
     */
    @NotNull
    public Thread runTaskLaterAsync(@NotNull Runnable task, long delay) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            task.run();
        });
        thread.start();
        return thread;
    }

    /**
     * Runs a task synchronously at a fixed rate.
     *
     * @param task         The task to run.
     * @param initialDelay The initial delay in milliseconds before running the task.
     * @param delay        The delay in milliseconds between successive executions.
     * @return The thread running the task.
     */
    @NotNull
    public Thread runTaskTimerSync(@NotNull Runnable task, long initialDelay, long delay) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(initialDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                runTaskSync(task);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        return thread;
    }

    /**
     * Runs a task synchronously a specified number of times at a fixed rate.
     *
     * @param task         The task to run.
     * @param initialDelay The initial delay in milliseconds before running the task.
     * @param delay        The delay in milliseconds between successive executions.
     * @param times        The number of times to execute the task.
     * @return The thread running the task.
     */
    @NotNull
    public Thread runTaskTimerSync(@NotNull Runnable task, long initialDelay, long delay, int times) {
        AtomicInteger timesExecuted = new AtomicInteger(0);
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(initialDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                runTaskSync(task);
                if (timesExecuted.incrementAndGet() > times) {
                    Thread.currentThread().interrupt();
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        return thread;
    }

    /**
     * Runs a task asynchronously at a fixed rate.
     *
     * @param task         The task to run.
     * @param initialDelay The initial delay in milliseconds before running the task.
     * @param delay        The delay in milliseconds between successive executions.
     * @return The thread running the task.
     */
    @NotNull
    public Thread runTaskTimerAsync(@NotNull Runnable task, long initialDelay, long delay) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(initialDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                task.run();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        return thread;
    }

    /**
     * Runs a task asynchronously a specified number of times at a fixed rate.
     *
     * @param task         The task to run.
     * @param initialDelay The initial delay in milliseconds before running the task.
     * @param delay        The delay in milliseconds between successive executions.
     * @param times        The number of times to execute the task.
     * @return The thread running the task.
     */
    @NotNull
    public Thread runTaskTimerAsync(@NotNull Runnable task, long initialDelay, long delay, int times) {
        AtomicInteger timesExecuted = new AtomicInteger(0);
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(initialDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                task.run();
                if (timesExecuted.incrementAndGet() > times) {
                    Thread.currentThread().interrupt();
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        return thread;
    }
}

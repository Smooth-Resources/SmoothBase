package com.smoothresources.smoothbase.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import com.smoothresources.smoothbase.common.command.Command;
import com.smoothresources.smoothbase.common.command.CommandManager;
import com.smoothresources.smoothbase.common.command.CommandUtils;
import com.smoothresources.smoothbase.common.command.TargetableCommand;
import com.smoothresources.smoothbase.common.task.TaskManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Manages the registration and execution of commands in a Velocity environment.
 */
public class VelocityCommandManager implements CommandManager {

    private final ProxyServer server;
    private final TaskManager taskManager;
    private final CommandUtils commandUtils;

    /**
     * Creates a new VelocityCommandManager.
     *
     * @param server      The ProxyServer instance.
     * @param taskManager The TaskManager instance.
     * @param commandUtils The CommandUtils instance.
     */
    public VelocityCommandManager(@NotNull ProxyServer server, @NotNull TaskManager taskManager, @NotNull CommandUtils commandUtils) {
        this.server = server;
        this.taskManager = taskManager;
        this.commandUtils = commandUtils;
    }

    /**
     * Gets the ProxyServer instance.
     *
     * @return The ProxyServer instance.
     */
    public ProxyServer getServer() {
        return server;
    }

    /**
     * Gets the TaskManager instance.
     *
     * @return The TaskManager instance.
     */
    public TaskManager getTaskManager() {
        return taskManager;
    }

    /**
     * Gets the CommandUtils instance.
     *
     * @return The CommandUtils instance.
     */
    public CommandUtils getCommandUtils() {
        return commandUtils;
    }

    @Override
    public void registerCommand(@NotNull Command command) {
        server.getCommandManager().register(command.getName(), new SimpleCommand() {
            @Override
            public void execute(Invocation invocation) {
                CommandSource source = invocation.source();
                String[] args = invocation.arguments();

                Command applicableCommand = commandUtils.getApplicableCommand(command, args);
                VelocityCommandSender velocityCommandSender = new VelocityCommandSender(source);

                if (!(applicableCommand instanceof TargetableCommand targetableCommand)) {
                    if (!commandUtils.performsChecks(false, applicableCommand, velocityCommandSender, args)) return;
                    commandUtils.executeCommand(taskManager, applicableCommand, velocityCommandSender, args);
                    return;
                }

                if (!targetableCommand.isTargetable(velocityCommandSender, args)) {
                    if (!commandUtils.performsChecks(false, applicableCommand, velocityCommandSender, args)) return;
                    commandUtils.executeCommand(taskManager, applicableCommand, velocityCommandSender, args);
                    return;
                }

                if (!targetableCommand.hasOriginalSenderPermission(velocityCommandSender, args)) {
                    velocityCommandSender.sendMessage(targetableCommand.getPermissionErrorMessage());
                    return;
                }

                String targetUsername = targetableCommand.getTargetUsername(velocityCommandSender, args);

                if (server.getPlayer(targetUsername).isEmpty()) {
                    velocityCommandSender.sendMessage(targetableCommand.getTargetNotFoundErrorMessage(targetUsername));
                    return;
                }

                VelocityCommandSender targetVelocityCommandSender = new VelocityCommandSender(server.getPlayer(targetUsername).get());
                String[] targetArgs = targetableCommand.getTargetArgs(velocityCommandSender, args);

                targetableCommand.setExecutedAsTarget(true);
                commandUtils.executeCommand(taskManager, targetableCommand, targetVelocityCommandSender, targetArgs);
                velocityCommandSender.sendMessage(targetableCommand.getExecutedToTargetMessage(targetUsername));
            }

            @Override
            public List<String> suggest(Invocation invocation) {
                CommandSource source = invocation.source();
                String[] args = invocation.arguments();

                Command applicableCommand = commandUtils.getApplicableCommand(command, args);
                VelocityCommandSender velocityCommandSender = new VelocityCommandSender(source);

                return applicableCommand.onTabComplete(velocityCommandSender, args);
            }
        }, command.getAliases().toArray(new String[0]));
    }
}

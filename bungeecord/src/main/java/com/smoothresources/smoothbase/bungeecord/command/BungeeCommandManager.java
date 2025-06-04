package com.smoothresources.smoothbase.bungeecord.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import com.smoothresources.smoothbase.common.command.Command;
import com.smoothresources.smoothbase.common.command.CommandManager;
import com.smoothresources.smoothbase.common.command.CommandUtils;
import com.smoothresources.smoothbase.common.command.TargetableCommand;
import com.smoothresources.smoothbase.common.task.TaskManager;
import org.jetbrains.annotations.NotNull;

/**
 * Manages the registration and execution of commands in a BungeeCord environment.
 */
public class BungeeCommandManager implements CommandManager {

    private final ProxyServer server;
    private final Plugin plugin;
    private final TaskManager taskManager;
    private final CommandUtils commandUtils;

    /**
     * Creates a new BungeeCommandManager.
     *
     * @param plugin The Plugin instance.
     * @param taskManager The TaskManager instance.
     * @param commandUtils The CommandUtils instance.
     */
    public BungeeCommandManager(@NotNull Plugin plugin, @NotNull TaskManager taskManager, @NotNull CommandUtils commandUtils) {
        this.server = plugin.getProxy();
        this.plugin = plugin;
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
     * Gets the Plugin instance.
     *
     * @return The Plugin instance.
     */
    public Plugin getPlugin() {
        return plugin;
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
        server.getPluginManager().registerCommand(plugin, new AbstractBungeeCommand(command.getName(),
                command.getPermission(), command.getAliases().toArray(new String[0])) {
            @Override
            public void execute(CommandSender commandSender, String[] args) {
                Command applicableCommand = commandUtils.getApplicableCommand(command, args);
                BungeeCommandSender bungeeCommandSender = new BungeeCommandSender(commandSender, plugin);

                if (!(applicableCommand instanceof TargetableCommand targetableCommand)) {
                    if (!commandUtils.performsChecks(false, applicableCommand, bungeeCommandSender, args)) return;
                    commandUtils.executeCommand(taskManager, applicableCommand, bungeeCommandSender, args);
                    return;
                }

                if (!targetableCommand.isTargetable(bungeeCommandSender, args)) {
                    if (!commandUtils.performsChecks(false, applicableCommand, bungeeCommandSender, args)) return;
                    commandUtils.executeCommand(taskManager, applicableCommand, bungeeCommandSender, args);
                    return;
                }

                if (!targetableCommand.hasOriginalSenderPermission(bungeeCommandSender, args)) {
                    bungeeCommandSender.sendMessage(targetableCommand.getPermissionErrorMessage());
                    return;
                }

                String targetUsername = targetableCommand.getTargetUsername(bungeeCommandSender, args);

                if (server.getPlayer(targetUsername) == null) {
                    bungeeCommandSender.sendMessage(targetableCommand.getTargetNotFoundErrorMessage(targetUsername));
                    return;
                }

                BungeeCommandSender targetBungeeCommandSender = new BungeeCommandSender(server.getPlayer(targetUsername), plugin);
                String[] targetArgs = targetableCommand.getTargetArgs(bungeeCommandSender, args);

                targetableCommand.setExecutedAsTarget(true);
                commandUtils.executeCommand(taskManager, targetableCommand, targetBungeeCommandSender, targetArgs);
                bungeeCommandSender.sendMessage(targetableCommand.getExecutedToTargetMessage(targetUsername));
            }

            @Override
            public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
                Command applicableCommand = commandUtils.getApplicableCommand(command, args);
                BungeeCommandSender bungeeCommandSender = new BungeeCommandSender(sender, plugin);

                return applicableCommand.onTabComplete(bungeeCommandSender, args);
            }
        });
    }
}

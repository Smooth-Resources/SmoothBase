package net.smoothplugins.smoothbase.bungeecord.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.smoothplugins.smoothbase.bungeecord.task.BungeeTaskManager;
import net.smoothplugins.smoothbase.common.command.Command;
import net.smoothplugins.smoothbase.common.command.CommandManager;
import net.smoothplugins.smoothbase.common.command.CommandUtils;
import net.smoothplugins.smoothbase.common.command.TargetableCommand;
import net.smoothplugins.smoothbase.common.task.TaskManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Manages the registration and execution of commands in a BungeeCord environment.
 */
public class BungeeCommandManager implements CommandManager {

    private final ProxyServer server;
    private final Plugin plugin;
    private final TaskManager taskManager;

    /**
     * Creates a new BungeeCommandManager.
     *
     * @param server The ProxyServer instance.
     * @param plugin The Plugin instance.
     */
    public BungeeCommandManager(@NotNull ProxyServer server, @NotNull Plugin plugin) {
        this.server = server;
        this.plugin = plugin;
        this.taskManager = new BungeeTaskManager(server, plugin);
    }

    @Override
    public void registerCommand(@NotNull Command command) {
        server.getPluginManager().registerCommand(plugin, new AbstractBungeeCommand(command.getName(),
                command.getPermission(), command.getAliases().toArray(new String[0])) {
            @Override
            public void execute(CommandSender commandSender, String[] args) {
                Command applicableCommand = CommandUtils.getApplicableCommand(command, args);
                BungeeCommandSender bungeeCommandSender = new BungeeCommandSender(commandSender, plugin);

                if (!(applicableCommand instanceof TargetableCommand targetableCommand)) {
                    if (!CommandUtils.performsChecks(false, applicableCommand, bungeeCommandSender, args)) return;
                    CommandUtils.executeCommand(taskManager, applicableCommand, bungeeCommandSender, args);
                    return;
                }

                if (!targetableCommand.isTargetable(bungeeCommandSender, args)) {
                    if (!CommandUtils.performsChecks(false, applicableCommand, bungeeCommandSender, args)) return;
                    CommandUtils.executeCommand(taskManager, applicableCommand, bungeeCommandSender, args);
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
                CommandUtils.executeCommand(taskManager, targetableCommand, targetBungeeCommandSender, targetArgs);
                bungeeCommandSender.sendMessage(targetableCommand.getExecutedToTargetMessage(targetUsername));
            }

            @Override
            public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
                Command applicableCommand = CommandUtils.getApplicableCommand(command, args);
                BungeeCommandSender bungeeCommandSender = new BungeeCommandSender(sender, plugin);

                if (!CommandUtils.performsChecks(true, applicableCommand, bungeeCommandSender, args)) return new ArrayList<>();

                return applicableCommand.onTabComplete(bungeeCommandSender, args);
            }
        });
    }
}

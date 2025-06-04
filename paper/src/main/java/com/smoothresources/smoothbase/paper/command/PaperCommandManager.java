package com.smoothresources.smoothbase.paper.command;

import com.smoothresources.smoothbase.common.command.Command;
import com.smoothresources.smoothbase.common.command.CommandManager;
import com.smoothresources.smoothbase.common.command.CommandUtils;
import com.smoothresources.smoothbase.common.command.TargetableCommand;
import com.smoothresources.smoothbase.common.task.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Manages the registration and execution of commands in a Paper environment.
 */
public class PaperCommandManager implements CommandManager {

    private final Plugin plugin;
    private final TaskManager taskManager;
    private final CommandUtils commandUtils;

    /**
     * Creates a new PaperCommandManager.
     *
     * @param plugin The Plugin instance.
     * @param taskManager The TaskManager instance.
     * @param commandUtils The CommandUtils instance.
     */
    public PaperCommandManager(@NotNull Plugin plugin, @NotNull TaskManager taskManager, @NotNull CommandUtils commandUtils) {
        this.plugin = plugin;
        this.taskManager = taskManager;
        this.commandUtils = commandUtils;
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
        command.registerSubcommands();

        org.bukkit.command.Command bukkitCommand = new org.bukkit.command.Command(command.getName()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
                Command applicableCommand = commandUtils.getApplicableCommand(command, args);
                PaperCommandSender paperCommandSender = new PaperCommandSender(sender);

                if (!(applicableCommand instanceof TargetableCommand targetableCommand)) {
                    if (!commandUtils.performsChecks(false, applicableCommand, paperCommandSender, args)) return true;
                    commandUtils.executeCommand(taskManager, applicableCommand, paperCommandSender, args);
                    return true;
                }

                if (!targetableCommand.isTargetable(paperCommandSender, args)) {
                    if (!commandUtils.performsChecks(false, applicableCommand, paperCommandSender, args)) return true;
                    commandUtils.executeCommand(taskManager, applicableCommand, paperCommandSender, args);
                    return true;
                }

                if (!targetableCommand.hasOriginalSenderPermission(paperCommandSender, args)) {
                    paperCommandSender.sendMessage(targetableCommand.getPermissionErrorMessage());
                    return true;
                }

                String targetUsername = targetableCommand.getTargetUsername(paperCommandSender, args);
                Player target = Bukkit.getPlayer(targetUsername);

                if (target == null) {
                    paperCommandSender.sendMessage(targetableCommand.getTargetNotFoundErrorMessage(targetUsername));
                    return true;
                }

                PaperCommandSender targetPaperCommandSender = new PaperCommandSender(target);
                String[] targetArgs = targetableCommand.getTargetArgs(paperCommandSender, args);

                targetableCommand.setExecutedAsTarget(true);
                commandUtils.executeCommand(taskManager, targetableCommand, targetPaperCommandSender, targetArgs);
                paperCommandSender.sendMessage(targetableCommand.getExecutedToTargetMessage(targetUsername));
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                Command applicableCommand = commandUtils.getApplicableCommand(command, args);
                PaperCommandSender paperCommandSender = new PaperCommandSender(sender);

                return applicableCommand.onTabComplete(paperCommandSender, args);
            }
        };

        if (!command.getAliases().isEmpty()) {
            bukkitCommand.setAliases(command.getAliases());
        }

        Bukkit.getCommandMap().register(plugin.getName(), bukkitCommand);
    }

}

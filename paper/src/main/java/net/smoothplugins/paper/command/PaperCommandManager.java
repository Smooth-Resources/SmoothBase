package net.smoothplugins.paper.command;

import net.smoothplugins.common.command.Command;
import net.smoothplugins.common.command.CommandManager;
import net.smoothplugins.common.command.CommandUtils;
import net.smoothplugins.common.command.TargetableCommand;
import net.smoothplugins.common.task.TaskManager;
import net.smoothplugins.paper.task.PaperTaskManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the registration and execution of commands in a Paper environment.
 */
public class PaperCommandManager implements CommandManager {

    private final Plugin plugin;
    private final TaskManager taskManager;

    /**
     * Creates a new PaperCommandManager.
     *
     * @param plugin The Plugin instance.
     */
    public PaperCommandManager(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.taskManager = new PaperTaskManager(plugin);
    }

    @Override
    public void registerCommand(@NotNull Command command) {
        command.registerSubcommands();

        org.bukkit.command.Command bukkitCommand = new org.bukkit.command.Command(command.getName()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
                Command applicableCommand = CommandUtils.getApplicableCommand(command, args);
                PaperCommandSender paperCommandSender = new PaperCommandSender(sender);

                if (!(applicableCommand instanceof TargetableCommand targetableCommand)) {
                    if (!CommandUtils.performsChecks(false, applicableCommand, paperCommandSender, args)) return true;
                    CommandUtils.executeCommand(taskManager, applicableCommand, paperCommandSender, args);
                    return true;
                }

                if (!targetableCommand.isTargetable(paperCommandSender, args)) {
                    if (!CommandUtils.performsChecks(false, applicableCommand, paperCommandSender, args)) return true;
                    CommandUtils.executeCommand(taskManager, applicableCommand, paperCommandSender, args);
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
                CommandUtils.executeCommand(taskManager, targetableCommand, targetPaperCommandSender, targetArgs);
                paperCommandSender.sendMessage(targetableCommand.getExecutedToTargetMessage(targetUsername));
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                Command applicableCommand = CommandUtils.getApplicableCommand(command, args);
                PaperCommandSender paperCommandSender = new PaperCommandSender(sender);

                if (!CommandUtils.performsChecks(true, applicableCommand, paperCommandSender, args)) return new ArrayList<>();

                return applicableCommand.onTabComplete(paperCommandSender, args);
            }
        };

        if (!command.getAliases().isEmpty()) {
            bukkitCommand.setAliases(command.getAliases());
        }

        Bukkit.getCommandMap().register(plugin.getName(), bukkitCommand);
    }
}

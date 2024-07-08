package net.smoothplugins.common.command;

import net.smoothplugins.common.task.TaskManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling command-related operations.
 */
public class CommandUtils {

    /**
     * Gets the applicable command based on the main command and the provided arguments.
     *
     * @param mainCommand The main command.
     * @param args        The arguments provided by the sender.
     * @return The applicable command.
     */
    @NotNull
    public static Command getApplicableCommand(@NotNull Command mainCommand, @NotNull String[] args) {
        if (args.length == 0) {
            return mainCommand;
        }

        // Try to get the required subcommand (if exists)
        for (Command subcommand : mainCommand.getSubcommands()) {
            List<String> aliases = new ArrayList<>(subcommand.getAliases());
            aliases.add(subcommand.getName());

            for (String alias : aliases) {
                if (alias.equalsIgnoreCase(args[0])) {
                    return getApplicableCommand(subcommand, removeFirstArg(args));
                }
            }
        }

        return mainCommand;
    }

    /**
     * Performs various checks before executing a command.
     *
     * @param silent   Whether to suppress error messages.
     * @param command  The command to check.
     * @param sender   The sender of the command.
     * @param args     The arguments provided by the sender.
     * @return true if all checks pass, false otherwise.
     */
    public static boolean performsChecks(boolean silent, @NotNull Command command, @NotNull CommandSender sender, @NotNull String[] args) {
        if (command.mustBePlayer() && !sender.isPlayer()) {
            if (!silent) {
                sender.sendMessage(command.getNotPlayerErrorMessage());
            }

            return false;
        }

        if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            if (!silent) {
                sender.sendMessage(command.getPermissionErrorMessage());
            }

            return false;
        }

        if (command.checkArgs() && command.getArgsLength() != args.length) {
            if (!silent) {
                sender.sendMessage(command.getUsageErrorMessage());
            }

            return false;
        }

        return true;
    }

    /**
     * Executes a command, either synchronously or asynchronously.
     *
     * @param taskManager The task manager to handle async execution.
     * @param command     The command to execute.
     * @param sender      The sender of the command.
     * @param args        The arguments provided by the sender.
     */
    public static void executeCommand(TaskManager taskManager, @NotNull Command command, @NotNull CommandSender sender, @NotNull String[] args) {
        if (command.executeAsync()) {
            taskManager.runTaskAsync(() -> command.onCommand(sender, args));
        } else {
            command.onCommand(sender, args);
        }
    }

    /**
     * Removes the first argument from an array of arguments.
     *
     * @param args The original array of arguments.
     * @return A new array of arguments with the first argument removed.
     */
    @NotNull
    private static String[] removeFirstArg(@NotNull String[] args) {
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
        return newArgs;
    }
}

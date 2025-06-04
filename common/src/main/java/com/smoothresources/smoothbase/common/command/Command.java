package com.smoothresources.smoothbase.common.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstract command that can be executed.
 */
public abstract class Command {

    private final List<Command> subcommands = new ArrayList<>();

    /**
     * Gets the name of the command.
     *
     * @return The name of the command.
     */
    @NotNull
    public abstract String getName();

    /**
     * Gets the list of aliases for the command.
     *
     * @return The list of aliases.
     */
    @NotNull
    public abstract List<String> getAliases();

    /**
     * Gets the permission required to execute the command.
     *
     * @return The permission string, or null if no permission is required.
     */
    @Nullable
    public abstract String getPermission();

    /**
     * Checks if the command arguments are valid.
     *
     * @return true if the arguments are valid, false otherwise.
     */
    public abstract boolean checkArgs();

    /**
     * Gets the required length of the arguments.
     *
     * @return The length of the arguments.
     */
    public abstract int getArgsLength();

    /**
     * Gets the usage string for the command.
     *
     * @return The usage string.
     */
    @NotNull
    public abstract String getUsage();

    /**
     * Checks if the command must be executed by a player.
     *
     * @return true if the command must be executed by a player, false otherwise.
     */
    public abstract boolean mustBePlayer();

    /**
     * Checks if the command should be executed asynchronously.
     *
     * @return true if the command should be executed asynchronously, false otherwise.
     */
    public abstract boolean executeAsync();

    /**
     * Registers the subcommands for this command.
     */
    public abstract void registerSubcommands();

    /**
     * Gets the list of subcommands.
     *
     * @return The list of subcommands.
     */
    @NotNull
    public List<Command> getSubcommands() {
        return subcommands;
    }

    /**
     * Executes the command with the specified sender and arguments.
     *
     * @param sender The sender of the command.
     * @param args   The arguments of the command.
     */
    public abstract void onCommand(@NotNull CommandSender sender, @NotNull String[] args);

    /**
     * Provides tab completion options for the command.
     *
     * @param sender The sender of the command.
     * @param args   The arguments of the command.
     * @return The list of tab completion options.
     */
    public abstract List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args);

    /**
     * Gets the error message to display when the command must be executed by a player.
     *
     * @return The error message.
     */
    @NotNull
    public Component getNotPlayerErrorMessage() {
        return Component.text("You must be a player to do that.").color(NamedTextColor.RED);
    }

    /**
     * Gets the error message to display when the sender lacks the required permission.
     *
     * @return The error message.
     */
    @NotNull
    public Component getPermissionErrorMessage() {
        return Component.text("You don't have permission.").color(NamedTextColor.RED);
    }

    /**
     * Gets the error message to display when the command is used incorrectly.
     *
     * @return The error message.
     */
    @NotNull
    public Component getUsageErrorMessage() {
        return Component.text("Wrong usage, use: " + getUsage()).color(NamedTextColor.RED);
    }
}

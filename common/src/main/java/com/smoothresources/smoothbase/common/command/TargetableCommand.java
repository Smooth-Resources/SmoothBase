package com.smoothresources.smoothbase.common.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * Represents an abstract command that can target other players.
 */
public abstract class TargetableCommand extends Command {

    private boolean executedAsTarget = false;

    /**
     * Gets the message to display when a command is executed on a target.
     *
     * @param targetUsername The username of the target.
     * @return The message component.
     */
    public Component getExecutedToTargetMessage(String targetUsername) {
        return Component.text("Command executed to " + targetUsername + ".").color(NamedTextColor.GREEN);
    }

    /**
     * Gets the error message to display when the target is not found.
     *
     * @param targetUsername The username of the target.
     * @return The error message component.
     */
    public Component getTargetNotFoundErrorMessage(String targetUsername) {
        return Component.text(targetUsername + " has not been found.").color(NamedTextColor.RED);
    }

    /**
     * Checks if the command can target another player.
     * The implementations should check if the passed arguments are valid to target another player.
     *
     * @param sender The sender of the command.
     * @param args   The arguments provided by the sender.
     * @return true if the command can target another player, false otherwise.
     */
    public abstract boolean isTargetable(CommandSender sender, String[] args);

    /**
     * Checks if the original sender has permission to execute the command on the target.
     *
     * @param sender The sender of the command.
     * @param args   The arguments provided by the sender.
     * @return true if the sender has permission, false otherwise.
     */
    public abstract boolean hasOriginalSenderPermission(CommandSender sender, String[] args);

    /**
     * Gets the username of the target player.
     *
     * @param sender The sender of the command.
     * @param args   The arguments provided by the sender.
     * @return The target player's username.
     */
    public abstract String getTargetUsername(CommandSender sender, String[] args);

    /**
     * Gets the arguments to pass to the target player.
     *
     * @param sender The sender of the command.
     * @param args   The arguments provided by the sender.
     * @return The arguments for the target player.
     */
    public abstract String[] getTargetArgs(CommandSender sender, String[] args);

    /**
     * Checks if the command is executed as the target player.
     * This is useful to allow certain conditions to be executed only when the command is executed as
     * the target player (for example, to avoid a cooldown).
     *
     * @return true if the command is executed as the target player, false otherwise.
     */
    public boolean isExecutedAsTarget() {
        return executedAsTarget;
    }

    /**
     * Sets whether the command is executed as the target player.
     *
     * @param executedAsTarget true if the command is executed as the target player, false otherwise.
     */
    public void setExecutedAsTarget(boolean executedAsTarget) {
        this.executedAsTarget = executedAsTarget;
    }
}

package net.smoothplugins.smoothbase.command;

import net.kyori.adventure.text.Component;
import net.smoothplugins.smoothbase.utility.ComponentTranslator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Command {

    private final Set<Command> subcommands = new HashSet<>();

    public abstract String getName();

    public abstract List<String> getAliases();

    public abstract String getPermission();

    public abstract int getArgsLength();

    public abstract String getUsage();

    public abstract boolean mustBePlayer();

    public abstract void registerSubcommands();

    public abstract void execute(CommandSender sender, String[] args);

    public abstract List<String> tabComplete(CommandSender sender, String[] args);

    public Component getPlayerComponent() {
        return ComponentTranslator.toComponent("&c✘ You must be a player to do that.");
    }

    public Component getPermissionComponent() {
        return ComponentTranslator.toComponent("&c✘ You don't have permission.");
    }

    public Component getUsageComponent() {
        return ComponentTranslator.toComponent("&c✘ Use: " + getUsage());
    }

    public void performChecksAndExecute(CommandSender sender, String[] args) {
        Command applicableCommand = getApplicableCommandOrSubcommand(args);
        if (applicableCommand != this) {
            applicableCommand.performChecksAndExecute(sender, removeFirstArg(args));
            return;
        }

        if (mustBePlayer() && !(sender instanceof Player)) {
            sender.sendMessage(getPlayerComponent());
            return;
        }

        if (getPermission() != null && !sender.hasPermission(getPermission())) {
            sender.sendMessage(getPermissionComponent());
            return;
        }

        if (getArgsLength() != -1 && args.length != getArgsLength()) {
            sender.sendMessage(getUsageComponent());
            return;
        }

        execute(sender, args);
    }

    public List<String> performChecksAndTabComplete(CommandSender sender, String[] args) {
        Command applicableCommand = getApplicableCommandOrSubcommand(args);
        if (applicableCommand != this) {
            return applicableCommand.performChecksAndTabComplete(sender, removeFirstArg(args));
        }

        return tabComplete(sender, args);
    }

    public void registerSubcommand(Command command) {
        subcommands.add(command);
    }

    public void unregisterSubcommand(Command command) {
        subcommands.remove(command);
    }

    public Set<Command> getSubcommands() {
        return subcommands;
    }

    public void registerCommand(Plugin plugin) {
        registerSubcommands();

        org.bukkit.command.Command bukkitCommand = new org.bukkit.command.Command(getName()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
                performChecksAndExecute(sender, args);
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                return performChecksAndTabComplete(sender, args);
            }
        };

        List<String> aliases = getAliases();
        if (aliases != null && !aliases.isEmpty()) {
            bukkitCommand.setAliases(getAliases());
        }

        Bukkit.getCommandMap().register(plugin.getName(), bukkitCommand);
    }

    private Command getApplicableCommandOrSubcommand(String[] args) {
        if (args.length == 0) {
            return this;
        }

        for (Command subcommand : subcommands) {
            if (subcommand.getName().equalsIgnoreCase(args[0])) {
                return subcommand;
            }

            if (subcommand.getAliases() == null) continue;

            for (String alias : subcommand.getAliases()) {
                if (alias.equalsIgnoreCase(args[0])) {
                    return subcommand;
                }
            }
        }

        return this;
    }

    private String[] removeFirstArg(String[] args) {
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
        return newArgs;
    }
}

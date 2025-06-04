package com.smoothresources.smoothbase.bungeecord.command;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import com.smoothresources.smoothbase.common.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a command sender in a BungeeCord environment.
 */
public class BungeeCommandSender implements CommandSender {

    private final net.md_5.bungee.api.CommandSender sender;
    private final Plugin plugin;

    /**
     * Creates a new BungeeCommandSender.
     *
     * @param sender The CommandSender instance.
     * @param plugin The Plugin instance.
     */
    public BungeeCommandSender(@NotNull net.md_5.bungee.api.CommandSender sender, @NotNull Plugin plugin) {
        this.sender = sender;
        this.plugin = plugin;
    }

    /**
     * Gets the Player instance of the sender.
     *
     * @return The Player instance.
     * @throws IllegalStateException If the sender is not a player.
     */
    public ProxiedPlayer getPlayer() {
        if (!isPlayer()) {
            throw new IllegalStateException("Sender is not a player.");
        }

        return (ProxiedPlayer) sender;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public boolean isPlayer() {
        return sender instanceof ProxiedPlayer;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(@NotNull Component component) {
        try (BungeeAudiences bungeeAudiences = BungeeAudiences.create(plugin)) {
            Audience audience = bungeeAudiences.sender(sender);
            audience.sendMessage(component);
        }
    }
}

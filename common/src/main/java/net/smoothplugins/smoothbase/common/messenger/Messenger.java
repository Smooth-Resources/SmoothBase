package net.smoothplugins.smoothbase.common.messenger;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for handling messaging operations.
 */
public interface Messenger {

    /**
     * Connects the messenger to the messaging service.
     */
    void connect();

    /**
     * Disconnects the messenger from the messaging service.
     */
    void disconnect();

    /**
     * Sends a message.
     *
     * @param message The message to send.
     */
    void send(@NotNull Message message);

    /**
     * Sends a conversation.
     *
     * @param conversation The conversation to send.
     */
    void send(@NotNull Conversation conversation);

    /**
     * Handles an incoming message.
     *
     * @param message The incoming message.
     */
    void onMessage(@NotNull Message message);

    /**
     * Handles an incoming conversation.
     *
     * @param conversation The incoming conversation.
     */
    void onMessage(@NotNull Conversation conversation);
}

package com.smoothresources.smoothbase.common.messenger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents a conversation with a unique identifier and a callback.
 */
public class Conversation extends Message {

    private final UUID conversationUUID;
    private final ConversationCallback callback;
    private final Type type;

    public Conversation(Class<?> clazz, String json, UUID conversationUUID, ConversationCallback callback, Type type) {
        super(clazz, json);
        this.conversationUUID = conversationUUID;
        this.callback = callback;
        this.type = type;
    }

    /**
     * Creates a new conversation of type REQUEST.
     *
     * @param clazz     The class type of the message.
     * @param json      The JSON content of the message.
     * @param callback  The callback for the conversation.
     * @return The created conversation.
     */
    @NotNull
    public static Conversation ofRequest(@NotNull Class<?> clazz, @NotNull String json, @NotNull ConversationCallback callback) {
        return new Conversation(clazz, json, UUID.randomUUID(), callback, Type.REQUEST);
    }

    /**
     * Creates a new conversation of type RESPONSE.
     *
     * @param clazz            The class type of the message.
     * @param json             The JSON content of the message.
     * @param conversationUUID The unique identifier of the conversation.
     * @return The created conversation.
     */
    @NotNull
    public static Conversation ofResponse(@NotNull Class<?> clazz, @NotNull String json, @NotNull UUID conversationUUID) {
        return new Conversation(clazz, json, conversationUUID, null, Type.RESPONSE);
    }

    /**
     * Gets the unique identifier of the conversation.
     *
     * @return The unique identifier.
     */
    @NotNull
    public UUID getConversationUUID() {
        return conversationUUID;
    }

    /**
     * Gets the callback for the conversation.
     *
     * @return The callback, or null if the conversation is a response.
     */
    @Nullable
    public ConversationCallback getCallback() {
        return callback;
    }

    /**
     * Gets the type of the conversation.
     *
     * @return The type of the conversation.
     */
    @NotNull
    public Type getType() {
        return type;
    }

    /**
     * Enum representing the type of the conversation.
     */
    public enum Type {
        REQUEST,
        RESPONSE
    }
}

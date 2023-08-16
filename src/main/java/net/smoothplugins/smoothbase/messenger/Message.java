package net.smoothplugins.smoothbase.messenger;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Message {
    private final MessageType type;
    private final UUID identifier;
    private final Object message;

    public Message(MessageType type, UUID identifier, Object message) {
        this.type = type;
        this.identifier = identifier;
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    @Nullable
    public UUID getIdentifier() {
        return identifier;
    }

    public Object getMessage() {
        return message;
    }

    public enum MessageType {
        NORMAL,
        REQUEST,
        RESPONSE
    }
}

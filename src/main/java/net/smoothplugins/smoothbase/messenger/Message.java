package net.smoothplugins.smoothbase.messenger;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Message {

    private final MessageType type;
    private final UUID identifier;
    private final String JSON;

    public Message(MessageType type, UUID identifier, String JSON) {
        this.type = type;
        this.identifier = identifier;
        this.JSON = JSON;
    }

    public MessageType getType() {
        return type;
    }

    @Nullable
    public UUID getIdentifier() {
        return identifier;
    }

    public String getJSON() {
        return JSON;
    }

    public enum MessageType {
        NORMAL,
        REQUEST,
        RESPONSE
    }
}

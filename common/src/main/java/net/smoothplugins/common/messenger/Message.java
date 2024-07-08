package net.smoothplugins.common.messenger;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a message with a class type and JSON content.
 */
public class Message {

    private final Class<?> clazz;
    private final String json;

    /**
     * Creates a new Message.
     *
     * @param clazz The class type of the message.
     * @param json  The JSON content of the message.
     */
    public Message(@NotNull Class<?> clazz, @NotNull String json) {
        this.clazz = clazz;
        this.json = json;
    }

    /**
     * Gets the class type of the message.
     *
     * @return The class type.
     */
    @NotNull
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * Gets the JSON content of the message.
     *
     * @return The JSON content.
     */
    @NotNull
    public String getJson() {
        return json;
    }
}

package net.smoothplugins.common.placeholder;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * Utility class for replacing placeholders in strings and lists of strings.
 */
public class PlaceholderReplacer {

    /**
     * Replaces placeholders in a string with their corresponding values.
     *
     * @param message      The message containing placeholders.
     * @param placeholders The map of placeholders and their values.
     * @return The message with placeholders replaced.
     */
    @NotNull
    public static String replace(@NotNull String message, @NotNull HashMap<String, String> placeholders) {
        String messageWithPlaceholders = message;
        for (HashMap.Entry<String, String> entry : placeholders.entrySet()) {
            if (!message.contains(entry.getKey())) continue;

            if (entry.getValue() == null) entry.setValue("");

            messageWithPlaceholders = messageWithPlaceholders.replace(entry.getKey(), entry.getValue());
        }

        return messageWithPlaceholders;
    }

    /**
     * Replaces placeholders in a list of strings with their corresponding values.
     *
     * @param messages     The list of messages containing placeholders.
     * @param placeholders The map of placeholders and their values.
     * @return The list of messages with placeholders replaced.
     */
    @NotNull
    public static List<String> replace(@NotNull List<String> messages, @NotNull HashMap<String, String> placeholders) {
        messages.replaceAll(line -> replace(line, placeholders));
        return messages;
    }
}

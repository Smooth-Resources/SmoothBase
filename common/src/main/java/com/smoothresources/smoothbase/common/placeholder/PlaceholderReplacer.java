package com.smoothresources.smoothbase.common.placeholder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for replacing placeholders in strings and lists of strings.
 */
public class PlaceholderReplacer {

    /**
     * Replaces placeholders in a string with their corresponding values.
     * If a placeholder maps to a list of strings, those strings will be concatenated
     * into a single string separated by a delimiter.
     *
     * @param message      The message containing placeholders.
     * @param placeholders The map of placeholders and their values.
     * @return The message with placeholders replaced.
     */
    @NotNull
    public static String replace(@NotNull String message, @NotNull HashMap<String, List<String>> placeholders) {
        String messageWithPlaceholders = message;

        for (HashMap.Entry<String, List<String>> entry : placeholders.entrySet()) {
            String placeholder = entry.getKey();

            if (!message.contains(placeholder)) {
                continue;
            }

            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                messageWithPlaceholders = messageWithPlaceholders.replace(placeholder, "");
                continue;
            }

            String values = String.join(", ", entry.getValue());
            messageWithPlaceholders = messageWithPlaceholders.replace(placeholder, values);
        }

        return messageWithPlaceholders;
    }


    /**
     * Replaces placeholders in a list of strings with their corresponding values.
     * If a placeholder maps to a list of strings, those strings will be expanded into
     * the resulting list of messages.
     *
     * @param messages     The list of messages containing placeholders.
     * @param placeholders The map of placeholders and their values.
     * @return The list of messages with placeholders replaced.
     */
    @NotNull
    public static List<String> replace(@NotNull List<String> messages, @NotNull HashMap<String, List<String>> placeholders) {
        List<String> result = new ArrayList<>();

        for (String line : messages) {

            boolean add = true;
            for (Map.Entry<String, List<String>> entry : placeholders.entrySet()) {
                String placeholder = entry.getKey();
                List<String> values = entry.getValue();

                if (!line.contains(placeholder)) continue;

                if (values.size() == 1) {
                    line = line.replace(placeholder, values.get(0));
                    continue;
                }

                add = false;
                for (String value : values) {
                    result.add(line.replace(placeholder, value));
                }
            }

            if (add) {
                result.add(line);
            }
        }

        return result;
    }
}

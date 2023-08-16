package net.smoothplugins.smoothbase.utility;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class PlaceholderReplacer {

    @Nullable
    public static String replace(String message, Map<String, String> placeholders){
        if (message == null) return null;
        if (placeholders == null) return message;

        String messageWithPlaceholders = message;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            if (!message.contains(entry.getKey())) continue;

            messageWithPlaceholders = messageWithPlaceholders.replace(entry.getKey(), entry.getValue());
        }

        return messageWithPlaceholders;
    }

    @Nullable
    public static List<String> replace(List<String> message, Map<String, String> placeholders){
        if (message == null) return null;
        if (placeholders == null) return message;

        message.replaceAll(line -> replace(line, placeholders));

        return message;
    }

    @Nullable
    public static String replace(String message, String placeholder, String value){
        if (message == null) return null;
        if (placeholder == null || value == null) return message;

        if (message.contains(placeholder)) {
            message = message.replace(placeholder, value);
        }

        return message;
    }
}

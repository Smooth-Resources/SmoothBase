package net.smoothplugins.smoothbase.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;

public class ComponentTranslator {

    public static Component toComponent(String text) {
        if (text == null) return null;

        if (hasMiniMessageFormat(text)) {
            MiniMessage miniMessage = MiniMessage.miniMessage();
            return miniMessage.deserialize(text);
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }

    public static List<Component> toComponent(String... texts) {
        if (texts == null) return null;

        List<Component> components = new ArrayList<>();
        for (String text : texts) {
            components.add(toComponent(text));
        }

        return components;
    }

    public static List<Component> toComponent(List<String> list) {
        if (list == null) return null;

        List<Component> components = new ArrayList<>();
        for (String line : list) {
            components.add(toComponent(line));
        }

        return components;
    }

    public static String toString(Component component) {
        if (component == null) return null;

        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

    public static boolean hasMiniMessageFormat(String text) {
        return text.contains("<") && text.contains(">");
    }
}

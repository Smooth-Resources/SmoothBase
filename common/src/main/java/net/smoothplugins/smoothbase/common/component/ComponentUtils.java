package net.smoothplugins.smoothbase.common.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for working with Adventure components.
 */
public class ComponentUtils {

    /**
     * Converts a string to a Component. If the string contains MiniMessage format, it uses MiniMessage to deserialize it;
     * otherwise, it uses the legacy ampersand serializer.
     * This allows using any of both formats, MiniMessage and legacy.
     *
     * @param text The text to convert.
     * @return The converted Component.
     */
    @NotNull
    public static Component toComponent(@NotNull String text) {
        if (hasMiniMessageFormat(text)) {
            MiniMessage miniMessage = MiniMessage.miniMessage();
            return miniMessage.deserialize(text);
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }

    /**
     * Converts an array of strings to a list of Components.
     *
     * @param texts The array of texts to convert.
     * @return The list of converted Components.
     */
    @NotNull
    public static List<Component> toComponent(@NotNull String... texts) {
        List<Component> components = new ArrayList<>();
        for (String text : texts) {
            components.add(toComponent(text));
        }

        return components;
    }

    /**
     * Converts a list of strings to a list of Components.
     *
     * @param list The list of texts to convert.
     * @return The list of converted Components.
     */
    @NotNull
    public static List<Component> toComponent(@NotNull List<String> list) {
        List<Component> components = new ArrayList<>();
        for (String line : list) {
            components.add(toComponent(line));
        }

        return components;
    }

    /**
     * Converts a Component to a string using the legacy ampersand serializer.
     *
     * @param component The component to convert.
     * @return The converted string.
     */
    @NotNull
    public static String toString(@NotNull Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

    /**
     * Checks if a string contains MiniMessage format.
     *
     * @param text The text to check.
     * @return true if the text contains MiniMessage format, false otherwise.
     */
    public static boolean hasMiniMessageFormat(@NotNull String text) {
        return text.contains("<") && text.contains(">") && !text.contains("&");
    }

    /**
     * Removes specified text decorations from a Component.
     *
     * @param component   The component to modify.
     * @param decorations The decorations to remove.
     * @return The modified component without the specified decorations.
     */
    @NotNull
    public static Component removeDecorations(@NotNull Component component, @NotNull TextDecoration... decorations) {
        for (TextDecoration decoration : decorations) {
            component = component.decoration(decoration, false);
        }

        return component;
    }

    /**
     * Removes specified text decorations from a list of Components.
     *
     * @param components  The list of components to modify.
     * @param decorations The decorations to remove.
     * @return The list of modified components without the specified decorations.
     */
    @NotNull
    public static List<Component> removeDecorations(@NotNull List<Component> components, @NotNull TextDecoration... decorations) {
        List<Component> newComponents = new ArrayList<>();
        for (Component component : components) {
            newComponents.add(removeDecorations(component, decorations));
        }

        return newComponents;
    }
}

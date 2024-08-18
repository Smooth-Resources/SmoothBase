package net.smoothplugins.smoothbase.common.placeholder;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * Builder class for creating and managing placeholders.
 */
public class PlaceholderBuilder {

    private final HashMap<String, List<String>> placeholders;

    /**
     * Creates a new PlaceholderBuilder.
     */
    public PlaceholderBuilder() {
        this.placeholders = new HashMap<>();
    }

    /**
     * Adds a placeholder to the builder.
     *
     * @param key   The key of the placeholder.
     * @param value The value of the placeholder.
     * @return The current instance of PlaceholderBuilder.
     */
    @NotNull
    public PlaceholderBuilder add(@NotNull String key, @NotNull String value) {
        placeholders.put(key, List.of(value));
        return this;
    }

    /**
     * Adds a placeholder to the builder.
     *
     * @param key    The key of the placeholder.
     * @param values The values of the placeholder.
     * @return The current instance of PlaceholderBuilder.
     */
    @NotNull
    public PlaceholderBuilder add(@NotNull String key, @NotNull List<String> values) {
        placeholders.put(key, values);
        return this;
    }

    /**
     * Removes a placeholder from the builder.
     *
     * @param key The key of the placeholder to remove.
     * @return The current instance of PlaceholderBuilder.
     */
    @NotNull
    public PlaceholderBuilder remove(@NotNull String key) {
        placeholders.remove(key);
        return this;
    }

    /**
     * Clears all placeholders from the builder.
     *
     * @return The current instance of PlaceholderBuilder.
     */
    @NotNull
    public PlaceholderBuilder clear() {
        placeholders.clear();
        return this;
    }

    /**
     * Builds and returns the map of placeholders.
     *
     * @return The map of placeholders.
     */
    @NotNull
    public HashMap<String, List<String>> build() {
        return placeholders;
    }
}

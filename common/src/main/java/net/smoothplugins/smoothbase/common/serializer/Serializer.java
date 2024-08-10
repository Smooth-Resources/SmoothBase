package net.smoothplugins.smoothbase.common.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.smoothplugins.smoothbase.common.serializer.adapter.ClassAdapter;
import net.smoothplugins.smoothbase.common.serializer.adapter.ComponentAdapter;
import net.smoothplugins.smoothbase.common.serializer.adapter.LocalDateAdapter;
import net.smoothplugins.smoothbase.common.serializer.adapter.LocalDateTimeAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Utility class for serializing and deserializing objects using Gson.
 */
public class Serializer {

    private final Gson gson;

    /**
     * Creates a new Serializer with the provided Gson instance.
     *
     * @param gson The Gson instance to use for serialization and deserialization.
     */
    public Serializer(@NotNull Gson gson) {
        this.gson = gson;
    }

    /**
     * Serializes an object to a JSON string.
     *
     * @param object The object to serialize.
     * @return The JSON string representation of the object.
     */
    @NotNull
    public String serialize(@Nullable Object object) {
        return gson.toJson(object);
    }

    /**
     * Deserializes a JSON string to an object of the specified class.
     *
     * @param json      The JSON string to deserialize.
     * @param classOfT  The class of the object to deserialize.
     * @param <T>       The type of the object to deserialize.
     * @return The deserialized object, or null if the JSON string is null.
     */
    @Nullable
    public <T> T deserialize(@Nullable String json, @NotNull Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * Builder class for creating Serializer instances with custom configurations.
     */
    public static class Builder  {

        private final GsonBuilder gsonBuilder;

        /**
         * Creates a new Builder instance.
         */
        public Builder() {
            gsonBuilder = new GsonBuilder();
        }

        /**
         * Registers default type adapters for common types.
         *
         * @return The current Builder instance.
         */
        @NotNull
        public Builder registerDefaultAdapters(List<String> classWhitelist) {
            registerTypeAdapter(Class.class, new ClassAdapter(classWhitelist));
            registerTypeAdapter(Component.class, new ComponentAdapter());
            registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
            registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
            return this;
        }

        /**
         * Registers a type adapter for a specific class type.
         *
         * @param type    The adapter type.
         * @param adapter The adapter to register.
         * @return The current Builder instance.
         */
        @NotNull
        public Builder registerTypeAdapter(@NotNull Type type, @NotNull Object adapter) {
            gsonBuilder.registerTypeAdapter(type, adapter);
            return this;
        }

        /**
         * Registers a type hierarchy adapter for a specific class type.
         *
         * @param type    The class type.
         * @param adapter The adapter to register.
         * @return The current Builder instance.
         */
        @NotNull
        public Builder registerTypeHierarchyAdapter(@NotNull Class<?> type, @NotNull Object adapter) {
            gsonBuilder.registerTypeHierarchyAdapter(type, adapter);
            return this;
        }

        /**
         * Builds and returns a Serializer instance with the configured Gson instance.
         *
         * @return The created Serializer instance.
         */
        @NotNull
        public Serializer build() {
            return new Serializer(gsonBuilder.create());
        }
    }
}

package com.smoothresources.smoothbase.paper.serializer;

import com.google.gson.Gson;
import com.smoothresources.smoothbase.common.serializer.Serializer;
import com.smoothresources.smoothbase.paper.serializer.adapter.ItemStackAdapter;
import com.smoothresources.smoothbase.paper.serializer.adapter.LocationAdapter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Specialized Serializer for handling Paper-specific objects such as ItemStack and Location.
 */
public class PaperSerializer extends Serializer {

    /**
     * Creates a new PaperSerializer with the provided Gson instance.
     *
     * @param gson The Gson instance to use for serialization and deserialization.
     */
    public PaperSerializer(@NotNull Gson gson) {
        super(gson);
    }

    /**
     * Builder class for creating PaperSerializer instances with custom configurations.
     */
    public static class Builder extends Serializer.Builder {

        /**
         * Registers default type adapters for common types, including Paper-specific types.
         *
         * @return The current Builder instance.
         */
        @NotNull
        @Override
        public Builder registerDefaultAdapters(List<String> classWhitelist) {
            super.registerDefaultAdapters(classWhitelist);
            registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter());
            registerTypeAdapter(Location.class, new LocationAdapter());
            return this;
        }
    }
}

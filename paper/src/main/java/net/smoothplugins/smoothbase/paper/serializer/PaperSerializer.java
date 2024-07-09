package net.smoothplugins.smoothbase.paper.serializer;

import com.google.gson.Gson;
import net.smoothplugins.smoothbase.common.serializer.Serializer;
import net.smoothplugins.smoothbase.paper.serializer.adapter.ItemStackAdapter;
import net.smoothplugins.smoothbase.paper.serializer.adapter.LocationAdapter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
        public Builder registerDefaultAdapters() {
            super.registerDefaultAdapters();
            registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter());
            registerTypeAdapter(Location.class, new LocationAdapter());
            return this;
        }
    }
}

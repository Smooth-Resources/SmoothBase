package net.smoothplugins.smoothbase.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.smoothplugins.smoothbase.serializer.adapter.*;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Serializer {

    private final Gson gson;

    public Serializer(@Nullable HashMap<Type, Object> additionalAdapters) {
        if (additionalAdapters == null) additionalAdapters = new HashMap<>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Component.class, new ComponentAdapter());
        gsonBuilder.registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Location.class, new LocationAdapter());
        gsonBuilder.registerTypeAdapter(LongAdapter.class, new LongAdapter());
        gsonBuilder.registerTypeAdapter(Sound.class, new SoundAdapter());
        gsonBuilder.registerTypeAdapter(Title.class, new TitleAdapter());

        for (Type type : additionalAdapters.keySet()) {
            gsonBuilder.registerTypeAdapter(type, additionalAdapters.get(type));
        }

        this.gson = gsonBuilder.create();
    }

    public String serialize(Object object) {
        return gson.toJson(object);
    }

    public <T> T deserialize(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}

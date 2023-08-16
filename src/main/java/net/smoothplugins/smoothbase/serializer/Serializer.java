package net.smoothplugins.smoothbase.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.smoothplugins.smoothbase.serializer.adapter.LocalDateAdapter;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;

public class Serializer {

    private final Gson gson;

    public Serializer(@Nullable HashMap<Type, Object> additionalAdapters) {
        if (additionalAdapters == null) additionalAdapters = new HashMap<>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());

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

package net.smoothplugins.smoothbase.common.serializer.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class ClassAdapter implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

    private final List<String> whitelist;

    public ClassAdapter(List<String> whitelist) {
        this.whitelist = whitelist;
    }

    @Override
    public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }

    @Override
    public Class<?> deserialize(JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context)
            throws JsonParseException {
        try {
            String className = json.getAsString();
            if (!whitelist.contains(className)) {
                throw new JsonParseException("Class not in whitelist");
            }

            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}


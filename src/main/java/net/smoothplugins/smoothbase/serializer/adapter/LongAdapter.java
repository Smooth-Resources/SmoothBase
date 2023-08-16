package net.smoothplugins.smoothbase.serializer.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;

public class LongAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Long.parseLong(json.getAsString());
    }

    @Override
    public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}

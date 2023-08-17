package net.smoothplugins.smoothbase.serializer.adapter;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.lang.reflect.Type;

public class ComponentAdapter implements JsonSerializer<Component>, JsonDeserializer<Component> {

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        return miniMessage.deserialize(json.getAsString());
    }

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        return new JsonPrimitive(miniMessage.serialize(src));
    }
}

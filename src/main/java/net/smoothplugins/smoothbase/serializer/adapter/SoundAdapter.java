package net.smoothplugins.smoothbase.serializer.adapter;

import com.google.gson.*;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

import java.lang.reflect.Type;

public class SoundAdapter implements JsonDeserializer<Sound> {

    @Override
    public Sound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject soundJsonObject = json.getAsJsonObject();
        JsonObject nameJsonObject = soundJsonObject.get("name").getAsJsonObject();

        Key name = Key.key(nameJsonObject.get("namespace").getAsString(), nameJsonObject.get("key").getAsString());
        Sound.Source source = Sound.Source.valueOf(soundJsonObject.get("source").getAsString());
        float volume = soundJsonObject.get("volume").getAsFloat();
        float pitch = soundJsonObject.get("pitch").getAsFloat();

        return Sound.sound(name, source, volume, pitch);
    }
}

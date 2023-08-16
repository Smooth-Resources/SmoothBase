package net.smoothplugins.smoothbase.serializer.adapter;

import com.google.gson.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;

import java.lang.reflect.Type;
import java.time.Duration;

public class TitleAdapter implements JsonDeserializer<Title> {

    @Override
    public Title deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject titleJsonObject = json.getAsJsonObject();
        Component titleComponent = LegacyComponentSerializer.legacySection().deserialize(titleJsonObject.get("title").getAsString());
        Component subtitleComponent = LegacyComponentSerializer.legacySection().deserialize(titleJsonObject.get("subtitle").getAsString());

        JsonObject timesJsonObject = titleJsonObject.get("times").getAsJsonObject();
        Title.Times times = Title.Times.of(
                Duration.parse(timesJsonObject.get("fadeIn").getAsString()),
                Duration.parse(timesJsonObject.get("stay").getAsString()),
                Duration.parse(timesJsonObject.get("fadeOut").getAsString())
        );

        return Title.title(titleComponent, subtitleComponent, times);
    }
}

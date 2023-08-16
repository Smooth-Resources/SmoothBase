package net.smoothplugins.smoothbase.serializer.adapter;

import com.google.gson.*;
import net.smoothplugins.smoothbase.serializer.BukkitSerializer;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return BukkitSerializer.fromBase64(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(BukkitSerializer.toBase64(itemStack));
    }
}

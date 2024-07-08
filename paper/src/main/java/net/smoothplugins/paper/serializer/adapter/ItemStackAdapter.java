package net.smoothplugins.paper.serializer.adapter;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.Base64;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(jsonElement.getAsString()));
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) return null;
        String itemStackBase64 = Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
        return new JsonPrimitive(itemStackBase64);
    }
}

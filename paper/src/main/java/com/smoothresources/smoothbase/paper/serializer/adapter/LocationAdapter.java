package com.smoothresources.smoothbase.paper.serializer.adapter;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

/**
 * A gson adapter for {@link org.bukkit.Location}.
 * <p>
 * Licenced under GNU-GPLv3 to Minecraftly.
 * @author Cory Redmond &lt;ace@ac3-servers.eu&gt;
 */
public class LocationAdapter implements JsonDeserializer<Location>, JsonSerializer<Location> {

    public static final LocationAdapter INSTANCE = new LocationAdapter();

    @Override
    public Location deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!json.isJsonObject()) {
            throw new JsonParseException( "not a JSON object" );
        }

        final JsonObject obj = (JsonObject) json;
        final JsonElement world = obj.get("world");
        final JsonElement x = obj.get("x");
        final JsonElement y = obj.get("y");
        final JsonElement z = obj.get("z");
        final JsonElement yaw = obj.get("yaw");
        final JsonElement pitch = obj.get("pitch");

        if (world == null || x == null || y == null || z == null || yaw == null || pitch == null) {
            System.out.println("Malformed location json string!");
            return null;
        }

        if (!world.isJsonPrimitive() || !((JsonPrimitive) world).isString()) {
            System.out.println("world is not a string");
            return null;
        }

        if (!x.isJsonPrimitive() || !((JsonPrimitive) x).isNumber()) {
            System.out.println("x is not a number");
            return null;
        }

        if (!y.isJsonPrimitive() || !((JsonPrimitive) y).isNumber()) {
            System.out.println("y is not a number");
            return null;
        }

        if (!z.isJsonPrimitive() || !((JsonPrimitive) z).isNumber()) {
            System.out.println("z is not a number");
            return null;
        }

        if (!yaw.isJsonPrimitive() || !((JsonPrimitive) yaw).isNumber()) {
            System.out.println("yaw is not a number");
            return null;
        }

        if (!pitch.isJsonPrimitive() || !((JsonPrimitive) pitch).isNumber()) {
            System.out.println("pitch is not a number");
            return null;
        }

        World worldInstance = Bukkit.getWorld(world.getAsString());
        if (worldInstance == null) {
            System.out.println("World " + world.getAsString() + " not found!");
            return null;
        }

        return new Location(worldInstance, x.getAsDouble(), y.getAsDouble(), z.getAsDouble(), yaw.getAsFloat(), pitch.getAsFloat());
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        if (location == null) {
            return JsonNull.INSTANCE;
        }

        final JsonObject obj = new JsonObject();
        obj.addProperty("world", location.getWorld().getName());
        obj.addProperty("x", location.getX());
        obj.addProperty("y", location.getY());
        obj.addProperty("z", location.getZ());
        obj.addProperty("yaw", location.getYaw());
        obj.addProperty("pitch", location.getPitch());
        return obj;
    }
}

package net.smoothplugins.smoothbase.serializer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class BukkitSerializer {

    public static String toBase64(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR)) return null;
        return Base64.getEncoder().encodeToString(item.serializeAsBytes());
    }

    public static ItemStack fromBase64(String base64) {
        if (base64 == null) return null;
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(base64));
    }
}

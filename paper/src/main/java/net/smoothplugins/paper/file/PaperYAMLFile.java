package net.smoothplugins.paper.file;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.smoothplugins.common.component.ComponentUtils;
import net.smoothplugins.common.file.YAMLFile;
import net.smoothplugins.common.placeholder.PlaceholderReplacer;
import net.smoothplugins.paper.head.SkullCreator;
import net.smoothplugins.paper.menu.button.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Utility class for handling YAML configuration files in a Paper environment.
 */
public class PaperYAMLFile extends YAMLFile {

    /**
     * Creates a new PaperYAMLFile.
     *
     * @param plugin   The plugin instance.
     * @param fileName The name of the YAML file.
     */
    public PaperYAMLFile(@NotNull Plugin plugin, @NotNull String fileName) {
        super(plugin, fileName, plugin.getDataFolder());
    }

    /**
     * Gets a colored string from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The colored string at the specified path.
     */
    @NotNull
    public String getColoredString(@NotNull Object... path) {
        return getColoredString(new HashMap<>(), path);
    }

    /**
     * Gets a colored string from the configuration node at the specified path with placeholders replaced.
     *
     * @param placeholders The placeholders to replace.
     * @param path         The path to the configuration node.
     * @return The colored string at the specified path with placeholders replaced.
     */
    @NotNull
    public String getColoredString(@NotNull HashMap<String, String> placeholders, @NotNull Object... path) {
        String coloredText = ChatColor.translateAlternateColorCodes('&', getString(path));
        return PlaceholderReplacer.replace(coloredText, placeholders);
    }

    /**
     * Gets a list of colored strings from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The list of colored strings at the specified path.
     */
    @NotNull
    public List<String> getColoredStringList(@NotNull Object... path) {
        return getColoredStringList(new HashMap<>(), path);
    }

    /**
     * Gets a list of colored strings from the configuration node at the specified path with placeholders replaced.
     *
     * @param placeholders The placeholders to replace.
     * @param path         The path to the configuration node.
     * @return The list of colored strings at the specified path with placeholders replaced.
     */
    @NotNull
    public List<String> getColoredStringList(@NotNull HashMap<String, String> placeholders, @NotNull Object... path) {
        return getStringList(path)
                .stream()
                .map(line -> PlaceholderReplacer.replace(ChatColor.translateAlternateColorCodes('&', line), placeholders))
                .toList();
    }

    /**
     * Gets a material from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The material at the specified path, or null if not found.
     */
    @Nullable
    public Material getMaterial(@NotNull Object... path) {
        return Material.getMaterial(getString(path).toUpperCase(Locale.ROOT));
    }

    /**
     * Gets a sound from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The sound at the specified path, or null if not found.
     */
    @Nullable
    public Sound getSound(@NotNull Object... path) {
        return Sound.sound(Key.key(getString(path).toLowerCase(Locale.ROOT)), Sound.Source.MASTER, 1.0f, 1.0f);
    }

    /**
     * Gets a button from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The button at the specified path.
     * @throws IllegalArgumentException If the material or base64 string is invalid.
     */
    @NotNull
    public Button getButton(@NotNull Object... path) {
        return getButton(new HashMap<>(), path);
    }

    /**
     * Gets a button from the configuration node at the specified path with placeholders replaced.
     *
     * @param placeholders The placeholders to replace.
     * @param path         The path to the configuration node.
     * @return The button at the specified path with placeholders replaced.
     * @throws IllegalArgumentException If the material or base64 string is invalid.
     */
    @NotNull
    public Button getButton(@NotNull HashMap<String, String> placeholders, @NotNull Object... path) {
        Material material = getMaterial(path, "material");
        if (material == null) {
            throw new IllegalArgumentException(getString(path, "material") + " is not a valid material");
        }

        ItemStack item = new ItemStack(material);

        if (item.getType() == Material.PLAYER_HEAD && getNode(path, "base64").getString() != null) {
            item = SkullCreator.itemFromBase64(getString(path, "base64"));

            if (item == null) {
                throw new IllegalArgumentException("Invalid base64 string: " + getString(path, "base64"));
            }
        }

        Component name = getComponent(placeholders, path, "name");
        name = ComponentUtils.removeDecorations(name, TextDecoration.ITALIC);

        List<Component> lore = getComponentList(placeholders, path, "lore");
        lore = ComponentUtils.removeDecorations(lore, TextDecoration.ITALIC);

        Component finalName = name;
        List<Component> finalLore = lore;
        item.editMeta(meta -> {
            meta.displayName(finalName);
            meta.lore(finalLore);
        });

        List<Integer> slots = getIntegerList(path, "slots");
        if (slots.isEmpty()) {
            slots.add(getInt(path, "slot"));
        }

        return new Button(item, slots);
    }
}

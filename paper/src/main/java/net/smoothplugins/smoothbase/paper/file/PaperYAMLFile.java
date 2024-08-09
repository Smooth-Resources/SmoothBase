package net.smoothplugins.smoothbase.paper.file;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.smoothplugins.smoothbase.common.component.ComponentUtils;
import net.smoothplugins.smoothbase.common.file.YAMLFile;
import net.smoothplugins.smoothbase.common.placeholder.PlaceholderReplacer;
import net.smoothplugins.smoothbase.paper.head.SkullCreator;
import net.smoothplugins.smoothbase.paper.menu.button.Button;
import net.smoothplugins.smoothbase.paper.menu.button.ClickableButton;
import net.smoothplugins.smoothbase.paper.menu.event.PlayerClickButtonEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
        List<String> pathList = new java.util.ArrayList<>(Stream.of(path).map(Object::toString).toList());

        pathList.add("material");
        Material material = getMaterial(pathList.toArray());

        if (material == null) {
            throw new IllegalArgumentException(getString(pathList.toArray()) + " is not a valid material");
        }

        pathList.remove(pathList.size() - 1);

        ItemStack item = new ItemStack(material);

        pathList.add("base64");
        if (item.getType() == Material.PLAYER_HEAD && getNode(pathList.toArray()).getString() != null) {
            item = SkullCreator.itemFromBase64(getString(pathList));

            if (item == null) {
                throw new IllegalArgumentException("Invalid base64 string: " + getString(pathList));
            }
        }
        pathList.remove(pathList.size() - 1);

        pathList.add("name");
        Component name = getComponent(placeholders, pathList.toArray());
        name = ComponentUtils.removeDecorations(name, TextDecoration.ITALIC);
        pathList.remove(pathList.size() - 1);

        pathList.add("lore");
        List<Component> lore = getComponentList(placeholders, pathList.toArray());
        lore = ComponentUtils.removeDecorations(lore, TextDecoration.ITALIC);
        pathList.remove(pathList.size() - 1);

        Component finalName = name;
        List<Component> finalLore = lore;
        item.editMeta(meta -> {
            meta.displayName(finalName);
            meta.lore(finalLore);
        });


        pathList.add("slots");
        List<Integer> slots = getIntegerList(pathList.toArray());
        if (slots.isEmpty()) {
            pathList.remove(pathList.size() - 1);
            pathList.add("slot");
            slots.add(getInt(pathList.toArray()));
        }

        return new Button(item, slots);
    }

    public ClickableButton getClickableButton(Consumer<PlayerClickButtonEvent> onClick, @NotNull Object... path) {
        return getClickableButton(new HashMap<>(), onClick, path);
    }

    public ClickableButton getClickableButton(@NotNull HashMap<String, String> placeholders,
                                              @NotNull Consumer<PlayerClickButtonEvent> onClick,
                                              @NotNull Object... path) {
        Button button = getButton(placeholders, path);
        return new ClickableButton(button.getItemStack(), button.getSlots().get(0), onClick);
    }
}

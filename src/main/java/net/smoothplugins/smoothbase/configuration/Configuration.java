package net.smoothplugins.smoothbase.configuration;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.smoothplugins.smoothbase.utility.ComponentTranslator;
import net.smoothplugins.smoothbase.utility.PlaceholderReplacer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public class Configuration extends YamlConfiguration {

    private static final String DEFAULT_EXTENSION = ".yml";

    private final String fileName;
    private final Plugin plugin;
    private final File folder;

    public Configuration(Plugin plugin, String fileName, String fileExtension, File folder) {
        this.folder = folder;
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(fileExtension) ? "" : fileExtension);
        this.createFile();
    }

    public Configuration(Plugin plugin, String fileName) {
        this(plugin, fileName, DEFAULT_EXTENSION);
    }

    public Configuration(Plugin plugin, String fileName, String fileExtension) {
        this(plugin, fileName, fileExtension, plugin.getDataFolder());
    }

    @Override
    @NotNull
    public String getString(@NotNull String path) {
        String string = super.getString(path);
        if (string == null) return path;

        return string;
    }

    @NotNull
    public String getString(String path, @Nullable HashMap<String, String> placeholders) {
        return PlaceholderReplacer.replace(getString(path), placeholders);
    }

    @NotNull
    public String getColoredString(String path) {
        String message = getString(path);

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @NotNull
    public String getColoredString(String path, @Nullable HashMap<String, String> placeholders) {
        return PlaceholderReplacer.replace(getColoredString(path), placeholders);
    }

    @NotNull
    public Component getComponent(String path) {
        return ComponentTranslator.toComponent(getString(path));
    }

    @NotNull
    public Component getComponent(String path, @Nullable HashMap<String, String> placeholders) {
        return ComponentTranslator.toComponent(getString(path, placeholders));
    }

    @NotNull
    public List<String> getColoredList(String path) {
        List<String> coloredList = new ArrayList<>();
        getStringList(path).forEach(text -> {
            coloredList.add(ChatColor.translateAlternateColorCodes('&', text));
        });

        return coloredList;
    }

    @NotNull
    public List<String> getColoredList(String path, @Nullable HashMap<String, String> placeholders) {
        List<String> list = new ArrayList<>();
        getColoredList(path).forEach(text -> {
            list.add(PlaceholderReplacer.replace(text, placeholders));
        });

        return list;
    }

    @NotNull
    public List<Component> getComponentList(String path) {
        List<Component> componentList = new ArrayList<>();
        getStringList(path).forEach(text -> {
            componentList.add(ComponentTranslator.toComponent(text));
        });

        return componentList;
    }

    @NotNull
    public List<Component> getComponentList(String path, @Nullable HashMap<String, String> placeholders) {
        List<Component> componentList = new ArrayList<>();
        getStringList(path).forEach(text -> {
            componentList.add(ComponentTranslator.toComponent(PlaceholderReplacer.replace(text, placeholders)));
        });

        return componentList;
    }

    @Nullable
    public Material getMaterial(String path) {
        String materialName = getString(path);
        if (materialName == null) return null;

        return Material.getMaterial(materialName.toUpperCase(Locale.ROOT));
    }

    @Nullable
    public Sound getSound(String path) {
        String soundName = getString(path);
        if (soundName == null) return null;

        org.bukkit.Sound sound = org.bukkit.Sound.valueOf(soundName.toUpperCase(Locale.ROOT));
        return Sound.sound(sound, Sound.Source.MASTER, 1, 1);
    }

    @Nullable
    public Sound getSound(String typePath, String volumePath, String pitchPath) {
        String soundName = getString(typePath);

        org.bukkit.Sound sound = org.bukkit.Sound.valueOf(soundName.toUpperCase(Locale.ROOT));
        float volume = getFloat(volumePath);
        float pitch = getFloat(pitchPath);
        return Sound.sound(sound, Sound.Source.MASTER, volume, pitch);
    }

    @NotNull
    public Title getTitle(String titlePath, String subtitlePath, @Nullable HashMap<String, String> placeholders) {
        Component title = getComponent(titlePath, placeholders);
        Component subtitle = getComponent(subtitlePath, placeholders);

        return Title.title(title, subtitle);
    }

    @NotNull
    public Title getTitle(String titlePath, String subtitlePath, String fadeInPath, String stayPath, String fadeOutPath, @Nullable HashMap<String, String> placeholders) {
        Component title = getComponent(titlePath, placeholders);
        Component subtitle = getComponent(subtitlePath, placeholders);

        Duration fadeIn = Duration.ofMillis(getInt(fadeInPath));
        Duration stay = Duration.ofMillis(getInt(stayPath));
        Duration fadeOut = Duration.ofMillis(getInt(fadeOutPath));

        return Title.title(title, subtitle, Title.Times.of(fadeIn, stay, fadeOut));
    }

    public float getFloat(String path) {
        return (float) getDouble(path);
    }

    private void createFile() {
        try {
            File file = new File(folder, fileName);

            if (file.exists()) {
                load(file);
                save(file);
                return;
            }

            if (plugin.getResource(fileName) != null) {
                plugin.saveResource(fileName, false);
            } else {
                save(file);
            }

            load(file);
        } catch (InvalidConfigurationException | IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Creation of Configuration '" + fileName + "' failed.", e);
        }
    }

    public void save() {
        File folder = plugin.getDataFolder();
        File file = new File(folder, fileName);

        try {
            save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Save of the file '" + fileName + "' failed.", e);
        }
    }

    public void reload() {
        File folder = plugin.getDataFolder();
        File file = new File(folder, fileName);

        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "Reload of the file '" + fileName + "' failed.", e);
        }
    }
}

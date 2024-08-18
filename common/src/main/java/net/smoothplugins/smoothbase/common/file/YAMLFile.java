package net.smoothplugins.smoothbase.common.file;

import net.kyori.adventure.text.Component;
import net.smoothplugins.smoothbase.common.component.ComponentUtils;
import net.smoothplugins.smoothbase.common.placeholder.PlaceholderReplacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Utility class for handling YAML configuration files.
 */
public class YAMLFile {

    private final String fileName;
    private final Object plugin;
    private final File folder;
    private YamlConfigurationLoader yamlConfigurationLoader;
    private ConfigurationNode mainNode;

    /**
     * Creates a new YAMLFile.
     *
     * @param plugin   The plugin instance.
     * @param fileName The name of the YAML file.
     * @param folder   The folder where the YAML file is located.
     */
    public YAMLFile(@NotNull Object plugin, @NotNull String fileName, @NotNull File folder) {
        this.folder = folder;
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(".yml") ? "" : ".yml");
        this.createFile();
    }

    /**
     * Creates the YAML file if it does not exist and loads its content.
     */
    private void createFile() {
        File file = new File(folder, fileName);
        if (!file.exists()) {
            try {
                folder.mkdirs();
                Files.copy(Objects.requireNonNull(plugin.getClass().getClassLoader().getResourceAsStream(fileName)), file.toPath());
            } catch (IOException e) {
                throw new RuntimeException("Error while creating file " + fileName + " (file system permissions?): " + e);
            }
        }

        yamlConfigurationLoader = YamlConfigurationLoader.builder().path(file.toPath()).build();
        try {
            mainNode = yamlConfigurationLoader.load();
        } catch (ConfigurateException e) {
            throw new RuntimeException("Error while loading file " + fileName + ": " + e);
        }
    }

    /**
     * Reloads the YAML file.
     */
    public void reload() {
        try {
            this.mainNode = yamlConfigurationLoader.load();
        } catch (ConfigurateException e) {
            throw new RuntimeException("Error while reloading file " + fileName + ": " + e);
        }
    }

    /**
     * Saves the YAML file.
     */
    public void save() {
        try {
            this.yamlConfigurationLoader.save(mainNode);
        } catch (ConfigurateException e) {
            throw new RuntimeException("Error while saving file " + fileName + ": " + e);
        }
    }

    /**
     * Gets the main configuration node.
     *
     * @return The main configuration node.
     * @throws RuntimeException If the main node is null.
     */
    @NotNull
    public ConfigurationNode getMainNode() {
        if (mainNode == null) {
            throw new RuntimeException("Error while getting main node from file " + fileName + ": main node is null.");
        }

        return mainNode;
    }

    /**
     * Gets a configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The configuration node at the specified path.
     */
    @NotNull
    public ConfigurationNode getNode(@NotNull Object... path) {
        return getMainNode().node(path);
    }

    /**
     * Sets a value in the configuration node at the specified path.
     *
     * @param value The value to set.
     * @param path  The path to the configuration node.
     * @throws RuntimeException If there is a serialization error.
     */
    public void set(@Nullable Object value, @NotNull Object... path) {
        try {
            getMainNode().node(path).set(value);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a string from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The string value at the specified path.
     */
    @NotNull
    public String getString(@NotNull Object... path) {
        return getString(new HashMap<>(), path);
    }

    /**
     * Gets a string from the configuration node at the specified path with placeholders replaced.
     *
     * @param placeholders The placeholders to replace.
     * @param path         The path to the configuration node.
     * @return The string value at the specified path with placeholders replaced.
     */
    @NotNull
    public String getString(@NotNull HashMap<String, List<String>> placeholders, @NotNull Object... path) {
        String text = getMainNode().node(path).getString();
        if (text == null) {
            return "Path " + Arrays.toString(path) + " not found in " + fileName;
        }

        return PlaceholderReplacer.replace(text, placeholders);
    }

    /**
     * Gets a boolean from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The boolean value at the specified path.
     */
    public boolean getBoolean(@NotNull Object... path) {
        return getMainNode().node(path).getBoolean();
    }

    /**
     * Gets an integer from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The integer value at the specified path.
     */
    public int getInt(@NotNull Object... path) {
        return getMainNode().node(path).getInt();
    }

    /**
     * Gets a float from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The float value at the specified path.
     */
    public float getFloat(@NotNull Object... path) {
        return (float) getMainNode().node(path).getDouble();
    }

    /**
     * Gets a long from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The long value at the specified path.
     */
    public long getLong(@NotNull Object... path) {
        return getMainNode().node(path).getLong();
    }

    /**
     * Gets a double from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The double value at the specified path.
     */
    public double getDouble(@NotNull Object... path) {
        return getMainNode().node(path).getDouble();
    }

    /**
     * Gets a list of strings from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The list of strings at the specified path.
     */
    @NotNull
    public List<String> getStringList(@NotNull Object... path) {
        return getStringList(new HashMap<>(), path);
    }

    /**
     * Gets a list of strings from the configuration node at the specified path with placeholders replaced.
     *
     * @param placeholders The placeholders to replace.
     * @param path         The path to the configuration node.
     * @return The list of strings at the specified path with placeholders replaced.
     */
    @NotNull
    public List<String> getStringList(@NotNull HashMap<String, List<String>> placeholders, @NotNull Object... path) {
        try {
            List<String> list = getMainNode().node(path).getList(String.class, new ArrayList<>());
            return PlaceholderReplacer.replace(list, placeholders);
        } catch (SerializationException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list of booleans from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The list of booleans at the specified path.
     */
    @NotNull
    public List<Boolean> getBooleanList(@NotNull Object... path) {
        try {
            return getMainNode().node(path).getList(Boolean.class, new ArrayList<>());
        } catch (SerializationException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list of integers from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The list of integers at the specified path.
     */
    @NotNull
    public List<Integer> getIntegerList(@NotNull Object... path) {
        try {
            return getMainNode().node(path).getList(Integer.class, new ArrayList<>());
        } catch (SerializationException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list of floats from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The list of floats at the specified path.
     */
    @NotNull
    public List<Float> getFloatList(@NotNull Object... path) {
        try {
            return getMainNode().node(path).getList(Float.class, new ArrayList<>());
        } catch (SerializationException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list of longs from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The list of longs at the specified path.
     */
    @NotNull
    public List<Long> getLongList(@NotNull Object... path) {
        try {
            return getMainNode().node(path).getList(Long.class, new ArrayList<>());
        } catch (SerializationException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list of doubles from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The list of doubles at the specified path.
     */
    @NotNull
    public List<Double> getDoubleList(@NotNull Object... path) {
        try {
            return getMainNode().node(path).getList(Double.class, new ArrayList<>());
        } catch (SerializationException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Gets a component from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The component at the specified path.
     */
    @NotNull
    public Component getComponent(Object... path) {
        return getComponent(new HashMap<>(), path);
    }

    /**
     * Gets a component from the configuration node at the specified path with placeholders replaced.
     *
     * @param placeholders The placeholders to replace.
     * @param path         The path to the configuration node.
     * @return The component at the specified path with placeholders replaced.
     */
    @NotNull
    public Component getComponent(@NotNull HashMap<String, List<String>> placeholders, @NotNull Object... path) {
        return ComponentUtils.toComponent(PlaceholderReplacer.replace(getString(path), placeholders));
    }

    /**
     * Gets a list of components from the configuration node at the specified path.
     *
     * @param path The path to the configuration node.
     * @return The list of components at the specified path.
     */
    @NotNull
    public List<Component> getComponentList(@NotNull Object... path) {
        return getComponentList(new HashMap<>(), path);
    }

    /**
     * Gets a list of components from the configuration node at the specified path with placeholders replaced.
     *
     * @param placeholders The placeholders to replace.
     * @param path         The path to the configuration node.
     * @return The list of components at the specified path with placeholders replaced.
     */
    @NotNull
    public List<Component> getComponentList(@NotNull HashMap<String, List<String>> placeholders, @NotNull Object... path) {
        List<Component> componentList = new ArrayList<>();
        getStringList(path).forEach(text -> {
            componentList.add(ComponentUtils.toComponent(PlaceholderReplacer.replace(text, placeholders)));
        });

        return componentList;
    }
}

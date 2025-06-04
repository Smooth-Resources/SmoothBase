package com.smoothresources.smoothbase.common.database.nosql;

import com.smoothresources.smoothbase.common.database.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract class representing a NoSQL database.
 */
public abstract class NoSQLDatabase extends Database {

    /**
     * Inserts a JSON document with the specified key.
     *
     * @param key  The key of the document.
     * @param json The JSON document.
     */
    public abstract void insert(@NotNull String key, @NotNull String json);

    /**
     * Retrieves a JSON document by key.
     *
     * @param key The key of the document.
     * @return The JSON document, or null if not found.
     */
    @Nullable
    public abstract String get(@NotNull String key);

    /**
     * Updates a JSON document with the specified key.
     *
     * @param key  The key of the document.
     * @param json The updated JSON document.
     */
    public abstract void update(@NotNull String key, @NotNull String json);

    /**
     * Deletes a JSON document by key.
     *
     * @param key The key of the document.
     */
    public abstract void delete(@NotNull String key);

    /**
     * Checks if a JSON document exists by key.
     *
     * @param key The key of the document.
     * @return True if the document exists, false otherwise.
     */
    public abstract boolean exists(@NotNull String key);
}

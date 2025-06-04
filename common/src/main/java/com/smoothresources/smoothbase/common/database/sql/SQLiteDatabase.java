package com.smoothresources.smoothbase.common.database.sql;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class representing a SQLite database.
 */
public class SQLiteDatabase extends SQLDatabase {

    private final String databaseName;
    private final File folder;
    private Connection connection;

    /**
     * Creates a new SQLiteDatabase.
     *
     * @param databaseName The name of the SQLite database file.
     * @param folder       The folder where the database file is located.
     */
    public SQLiteDatabase(@NotNull String databaseName, @NotNull File folder) {
        this.databaseName = databaseName + (databaseName.endsWith(".db") ? "" : ".db");
        this.folder = folder;
    }

    @Override
    public void connect() {
        File databaseFile = new File(folder, databaseName);
        if (!databaseFile.exists()) {
            try {
                folder.mkdirs();
                databaseFile.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create SQLite database file: " + e);
            }
        }

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to SQLite database: " + e);
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close SQLite database connection: " + e);
        }
    }

    @NotNull
    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }

            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check SQLite database connection status: " + e);
        }
    }
}

package net.smoothplugins.smoothbase.common.database.sql;

import net.smoothplugins.smoothbase.common.database.Database;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract class representing a SQL database.
 */
public abstract class SQLDatabase extends Database {

    /**
     * Gets a connection to the SQL database.
     *
     * @return The SQL connection.
     */
    @NotNull
    public abstract Connection getConnection();

    /**
     * Executes a SQL query using a connection from this database.
     *
     * @param query The SQL query to execute.
     * @throws RuntimeException If a SQL error occurs.
     */
    public void execute(@NotNull SQLQuery query) {
        try (Connection connection = getConnection()) {
            query.execute(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute query on SQL database: " + e);
        }
    }
}

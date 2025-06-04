package com.smoothresources.smoothbase.common.database.sql;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface representing a SQL query to be executed.
 */
public interface SQLQuery {

    /**
     * Executes the SQL query using the provided connection.
     *
     * @param connection The SQL connection to use.
     * @throws SQLException If a SQL error occurs.
     */
    void execute(@NotNull Connection connection) throws SQLException;
}

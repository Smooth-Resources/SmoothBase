package net.smoothplugins.smoothbase.common.database.sql;

import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;

/**
 * Class representing a MySQL database.
 */
public class MySQLDatabase extends SQLDatabase {

    private final String host, databaseName, user, password;
    private final int port;
    private HikariDataSource dataSource;

    /**
     * Creates a new MySQLDatabase.
     *
     * @param host         The host of the MySQL server.
     * @param databaseName The name of the MySQL database.
     * @param user         The username for the MySQL database.
     * @param password     The password for the MySQL database.
     * @param port         The port of the MySQL server.
     */
    public MySQLDatabase(@NotNull String host, @NotNull String databaseName, @NotNull String user, @Nullable String password, int port) {
        this.host = host;
        this.databaseName = databaseName;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    @Override
    public void connect() {
        dataSource = new HikariDataSource();
        dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        dataSource.addDataSourceProperty("serverName", host);
        dataSource.addDataSourceProperty("port", port);
        dataSource.addDataSourceProperty("databaseName", databaseName);
        dataSource.addDataSourceProperty("user", user);
        dataSource.addDataSourceProperty("password", password);
    }

    @Override
    public void disconnect() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @NotNull
    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get connection from MySQL database: " + e);
        }
    }
}

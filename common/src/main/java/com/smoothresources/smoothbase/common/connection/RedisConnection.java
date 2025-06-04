package com.smoothresources.smoothbase.common.connection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.logging.Logger;

/**
 * Manages the connection to a Redis server.
 */
public class RedisConnection {

    private final String host, password;
    private final int port;
    private JedisPool pool;
    private final Logger logger;

    /**
     * Creates a new RedisConnection.
     *
     * @param host     The Redis server host.
     * @param password The password for the Redis server, or null if no password is required.
     * @param port     The port of the Redis server.
     * @param logger   The logger for logging connection issues.
     */
    public RedisConnection(@NotNull String host, @Nullable String password, int port, @NotNull Logger logger) {
        this.host = host;
        this.password = password;
        this.port = port;
        this.logger = logger;
    }

    /**
     * Creates a new RedisConnection (without password).
     *
     * @param host     The Redis server host.
     * @param port     The port of the Redis server.
     * @param logger   The logger for logging connection issues.
     */
    public RedisConnection(@NotNull String host, int port, @NotNull Logger logger) {
        this.host = host;
        this.password = null;
        this.port = port;
        this.logger = logger;
    }

    /**
     * Connects to the Redis server using the provided configuration.
     */
    public void connect() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(12);

            if (password == null || password.isEmpty()) {
                pool = new JedisPool(config, host, port, 10000);
            } else {
                pool = new JedisPool(config, host, port, 10000, password);
            }

            try (Jedis jedis = pool.getResource()) {
                jedis.ping();
            }
        } catch (JedisConnectionException e) {
            logger.severe("Failed to connect to Redis server: " + e);
        }
    }

    /**
     * Disconnects from the Redis server and closes the connection pool.
     */
    public void disconnect() {
        if (pool != null) {
            pool.close();
        }
    }

    /**
     * Gets a resource (Jedis instance) from the pool.
     *
     * @return A Jedis instance from the pool.
     * @throws IllegalStateException If the connection is not established.
     */
    @NotNull
    public Jedis getResource() {
        if (pool == null) {
            throw new IllegalStateException("Redis connection is not established (have you called connect method?).");
        }

        return pool.getResource();
    }

    /**
     * Gets the JedisPool instance.
     *
     * @return The JedisPool instance, or null if the connection is not established.
     */
    @Nullable
    public JedisPool getPool() {
        return pool;
    }
}

package net.smoothplugins.common.database.nosql;

import net.smoothplugins.common.connection.RedisConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;

/**
 * Class representing a Redis NoSQL database.
 */
public class RedisDatabase extends NoSQLDatabase {

    private final RedisConnection connection;
    private final String prefix;

    /**
     * Creates a new RedisDatabase.
     *
     * @param connection The Redis connection instance.
     * @param prefix     The prefix for keys in the Redis database.
     */
    public RedisDatabase(@NotNull RedisConnection connection, @NotNull String prefix) {
        this.connection = connection;
        this.prefix = prefix;
    }

    /**
     * Gets a Jedis instance from the connection pool.
     *
     * @return A Jedis instance.
     */
    @NotNull
    public Jedis getJedis() {
        return connection.getResource();
    }

    @Override
    public void connect() {
        // Redis connection is handled via the RedisConnection class
    }

    @Override
    public void disconnect() {
        // Redis connection is handled via the RedisConnection class
    }

    @Override
    public void insert(@NotNull String key, @NotNull String json) {
        try (Jedis jedis = getJedis()) {
            jedis.set(prefix + key, json);
        }
    }

    @Nullable
    @Override
    public String get(@NotNull String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(prefix + key);
        }
    }

    @Override
    public void update(@NotNull String key, @NotNull String json) {
        try (Jedis jedis = getJedis()) {
            jedis.set(prefix + key, json);
        }
    }

    @Override
    public void delete(@NotNull String key) {
        try (Jedis jedis = getJedis()) {
            jedis.del(prefix + key);
        }
    }
}

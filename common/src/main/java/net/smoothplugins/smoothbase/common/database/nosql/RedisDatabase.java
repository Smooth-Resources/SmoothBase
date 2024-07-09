package net.smoothplugins.smoothbase.common.database.nosql;

import net.smoothplugins.smoothbase.common.connection.RedisConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Removes the TTL of a key.
     * @param key The key to remove the TTL from.
     * @return True if the TTL was removed, false otherwise (only if the key does not exist).
     */
    public boolean removeTTL(@NotNull String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.persist(prefix + key) == 1;
        }
    }

    /**
     * Sets the TTL of a key.
     * @param key The key to set the TTL for.
     * @param seconds The amount of seconds the key should be stored for.
     * @return True if the TTL was set, false otherwise (only if the key does not exist).
     */
    public boolean setTTL(@NotNull String key, long seconds) {
        try (Jedis jedis = getJedis()) {
            return jedis.expire(prefix + key, seconds) == 1;
        }
    }

    /**
     * Gets all values for a key with a specific prefix.
     *
     * @param prefix The prefix to search for (it will be appended with a wildcard) (prefix can be empty).
     * @return A list of values with the specified prefix.
     */
    @NotNull
    public List<String> getValues(@NotNull String prefix) {
        try (Jedis jedis = getJedis()) {
            ScanParams scanParams = new ScanParams().match(this.prefix + prefix + "*");
            List<String> keys = new ArrayList<>();
            String cursor = ScanParams.SCAN_POINTER_START;

            do {
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                keys.addAll(scanResult.getResult());
                cursor = scanResult.getCursor();
            } while (!cursor.equals(ScanParams.SCAN_POINTER_START));

            List<String> results = new ArrayList<>();
            for (String tempKey : keys) {
                results.add(jedis.get(tempKey));
            }

            return results;
        }
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

    @Override
    public boolean exists(@NotNull String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.exists(prefix + key);
        }
    }
}

package net.smoothplugins.smoothbase.storage;

import net.smoothplugins.smoothbase.connection.RedisConnection;
import net.smoothplugins.smoothbase.serializer.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.ArrayList;
import java.util.List;

public class RedisStorage {

    private final RedisConnection connection;
    private final String prefix;

    public RedisStorage(RedisConnection connection, String prefix) {
        this.connection = connection;
        this.prefix = connection.getCluster() + prefix + ":";
    }

    public void create(String key, String value) {
        try (Jedis jedis = connection.getPool().getResource()) {
            jedis.set(prefix + key, value);
        }
    }

    public void createWithTTL(String key, String value, int seconds) {
        try (Jedis jedis = connection.getPool().getResource()) {
            jedis.setex(prefix + key, seconds, value);
        }
    }

    public void update(String key, String value){
        create(key, value);
    }

    public void updateWithTTL(String key, String value, int seconds){
        createWithTTL(key, value, seconds);
    }

    public boolean contains(String key) {
        try (Jedis jedis = connection.getPool().getResource()) {
            return jedis.exists(prefix + key);
        }
    }

    @Nullable
    public String get(String key) {
        try (Jedis jedis = connection.getPool().getResource()) {
            return jedis.get(prefix + key);
        }
    }

    public void delete(String key) {
        try (Jedis jedis = connection.getPool().getResource()) {
            jedis.del(prefix + key);
        }
    }

    @NotNull
    public List<String> getAllValues() {
        try (Jedis jedis = connection.getPool().getResource()) {
            ScanParams scanParams = new ScanParams().match(prefix + "*");
            List<String> keys = new ArrayList<>();
            String cursor = ScanParams.SCAN_POINTER_START;

            while (true) {
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                keys.addAll(scanResult.getResult());
                cursor = scanResult.getCursor();

                if (cursor.equals(ScanParams.SCAN_POINTER_START)) {
                    break;
                }
            }

            List<String> results = new ArrayList<>();
            for (String tempKey : keys) {
                results.add(jedis.get(tempKey));
            }

            return results;
        }
    }

    public boolean setTTL(String key, int seconds){
        try (Jedis jedis = connection.getPool().getResource()) {
            return jedis.expire(prefix + key, seconds) == 1;
        }
    }

    public boolean removeTTL(String key){
        try (Jedis jedis = connection.getPool().getResource()) {
            return jedis.persist(prefix + key) == 1;
        }
    }

    public boolean hasTTL(String key) {
        try (Jedis jedis = connection.getPool().getResource()) {
            return jedis.ttl(prefix + key) != -1;
        }
    }

    public long getTTL(String key) {
        try (Jedis jedis = connection.getPool().getResource()) {
            return jedis.ttl(prefix + key);
        }
    }

    public RedisConnection getConnection() {
        return connection;
    }
}

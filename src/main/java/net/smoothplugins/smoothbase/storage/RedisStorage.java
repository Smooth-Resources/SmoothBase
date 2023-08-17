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

public class RedisStorage<K, V> {

    private final RedisConnection connection;
    private final Serializer serializer;
    private final String prefix;
    private final Class<K> keyClass;
    private final Class<V> valueClass;

    public RedisStorage(RedisConnection connection, Serializer serializer, String prefix, Class<K> keyClass, Class<V> valueClass) {
        this.connection = connection;
        this.serializer = serializer;
        this.prefix = connection.getCluster() + prefix + ":";
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    public void create(K key, V value) {
        try (Jedis jedis = connection.getPool().getResource()) {
            jedis.set(prefix + key.toString(), serializer.serialize(value));
        }
    }

    public void createWithTTL(K key, V value, int seconds) {
        try (Jedis jedis = connection.getPool().getResource()) {
            jedis.setex(prefix + key.toString(), seconds, serializer.serialize(value));
        }
    }

    public void update(K key, V value){
        create(key, value);
    }

    public void updateWithTTL(K key, V value, int seconds){
        createWithTTL(key, value, seconds);
    }

    public boolean contains(K key) {
        try (Jedis jedis = connection.getPool().getResource()) {
            return jedis.exists(prefix + key.toString());
        }
    }

    @Nullable
    public V get(K key) {
        try (Jedis jedis = connection.getPool().getResource()) {
            return serializer.deserialize(jedis.get(prefix + key.toString()), valueClass);
        }
    }

    public void delete(K key) {
        try (Jedis jedis = connection.getPool().getResource()) {
            jedis.del(prefix + key.toString());
        }
    }

    @NotNull
    public List<V> getAllValues() {
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

            List<V> results = new ArrayList<>();
            for (String tempKey : keys) {
                results.add(serializer.deserialize(jedis.get(tempKey), valueClass));
            }

            return results;
        }
    }

    public boolean setTTL(K key, int seconds){
        try (Jedis jedis = connection.getPool().getResource()) {
            return jedis.expire(prefix + key.toString(), seconds) == 1;
        }
    }

    public boolean removeTTL(K key){
        try (Jedis jedis = connection.getPool().getResource()) {
            return jedis.persist(prefix + key.toString()) == 1;
        }
    }

    public RedisConnection getConnection() {
        return connection;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public String getPrefix() {
        return prefix;
    }

    public Class<K> getKeyClass() {
        return keyClass;
    }

    public Class<V> getValueClass() {
        return valueClass;
    }
}

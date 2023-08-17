package net.smoothplugins.smoothbase.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class CacheStorage<K, V> {

    private final HashMap<K, V> cache;

    public CacheStorage() {
        this.cache = new HashMap<>();
    }

    public void create(K key, V value) {
        cache.put(key, value);
    }

    public void update(K key, V value) {
        cache.replace(key, value);
    }

    public boolean contains(K key) {
        return cache.containsKey(key);
    }

    @Nullable
    public V get(K key) {
        return cache.get(key);
    }

    public void delete(K key) {
        cache.remove(key);
    }

    public void deleteAll() {
        cache.clear();
    }

    @NotNull
    public List<K> getAllKeys() {
        return cache.keySet().stream().toList();
    }

    @NotNull
    public List<V> getAllValues() {
        return cache.values().stream().toList();
    }

    public HashMap<K, V> getCache() {
        return cache;
    }
}

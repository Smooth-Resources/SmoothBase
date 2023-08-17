package net.smoothplugins.smoothbase.storage;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import net.smoothplugins.smoothbase.serializer.Serializer;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MongoStorage<K, V> {

    private final MongoCollection<Document> collection;
    private final Serializer serializer;
    private final Class<K> keyClass;
    private final Class<V> valueClass;

    public MongoStorage(MongoCollection<Document> collection, Serializer serializer, Class<K> keyClass, Class<V> valueClass) {
        this.collection = collection;
        this.serializer = serializer;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    public void create(V value) {
        collection.insertOne(Document.parse(serializer.serialize(value)));
    }

    public void update(K key, V value){
        collection.replaceOne(new Document(key.toString(), "_id"), Document.parse(serializer.serialize(value)));
    }

    public boolean contains(K key) {
        return collection.find(new Document(key.toString(), "_id")).first() != null;
    }

    @Nullable
    public V get(K key) {
        Document result = collection.find(new Document(key.toString(), "_id")).first();
        if (result != null) {
            return serializer.deserialize(result.toJson(), valueClass);
        }

        return null;
    }

    public void delete(K key) {
        collection.deleteOne(new Document(key.toString(), "_id"));
    }

    @NotNull
    public List<V> getAllValues() {
        List<V> list = new ArrayList<>();
        collection.find().forEach((Block<Document>) document -> list.add(serializer.deserialize(document.toJson(), valueClass)));

        return list;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public Class<K> getKeyClass() {
        return keyClass;
    }

    public Class<V> getValueClass() {
        return valueClass;
    }
}

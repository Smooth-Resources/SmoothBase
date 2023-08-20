package net.smoothplugins.smoothbase.storage;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MongoStorage {

    private final MongoCollection<Document> collection;

    public MongoStorage(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public void create(String JSON) {
        collection.insertOne(Document.parse(JSON));
    }

    public void update(String key, String value, String JSON){
        collection.replaceOne(new Document(key, value), Document.parse(JSON));
    }

    public boolean contains(String key, String value) {
        return collection.find(new Document(key, value)).first() != null;
    }

    @Nullable
    public String get(String key, String value) {
        Document result = collection.find(new Document(key, value)).first();
        if (result != null) {
            return result.toJson();
        }

        return null;
    }

    public void delete(String key, String value) {
        collection.deleteOne(new Document(key, value));
    }

    @NotNull
    public List<String> getAllValues() {
        List<String> list = new ArrayList<>();
        collection.find().forEach((Block<Document>) document -> list.add(document.toJson()));

        return list;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }
}

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

    public void update(String key, String objective, String value){
        collection.replaceOne(new Document(key, objective), Document.parse(value));
    }

    public boolean contains(String key, String objective) {
        return collection.find(new Document(key, objective)).first() != null;
    }

    @Nullable
    public String get(String key, String objective) {
        Document result = collection.find(new Document(key, objective)).first();
        if (result != null) {
            return result.toJson();
        }

        return null;
    }

    public void delete(String key, String objective) {
        collection.deleteOne(new Document(key, objective));
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

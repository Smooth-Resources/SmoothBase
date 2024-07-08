package net.smoothplugins.common.database.nosql;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class representing a MongoDB NoSQL database.
 */
public class MongoDBDatabase extends NoSQLDatabase {

    private final String uri, databaseName, collectionName;
    private MongoCollection<Document> collection;

    /**
     * Creates a new MongoDBDatabase.
     *
     * @param uri            The URI of the MongoDB server.
     * @param databaseName   The name of the MongoDB database.
     * @param collectionName The name of the MongoDB collection.
     */
    public MongoDBDatabase(@NotNull String uri, @NotNull String databaseName, @NotNull String collectionName) {
        this.uri = uri;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    /**
     * Gets the MongoCollection instance.
     *
     * @return The MongoCollection instance, or null if not connected.
     * @throws IllegalStateException If the MongoDB connection is not established.
     */
    @NotNull
    public MongoCollection<Document> getCollection() {
        if (collection == null) {
            throw new IllegalStateException("MongoDB connection is not established (have you called connect method?).");
        }

        return collection;
    }

    @Override
    public void connect() {
        MongoClient client = new MongoClient(new MongoClientURI(uri));
        collection = client.getDatabase(databaseName).getCollection(collectionName);
    }

    @Override
    public void disconnect() {
        // No need to disconnect
    }

    @Override
    public void insert(@NotNull String key, @NotNull String json) {
        Document document = Document.parse(json).append("_id", key);
        collection.insertOne(document);
    }

    @Nullable
    @Override
    public String get(@NotNull String key) {
        Document document = collection.find(new Document("_id", key)).first();
        return document == null ? null : document.toJson();
    }

    @Override
    public void update(@NotNull String key, @NotNull String json) {
        Document document = Document.parse(json).append("_id", key);
        collection.replaceOne(new Document("_id", key), document);
    }

    @Override
    public void delete(@NotNull String key) {
        collection.deleteOne(new Document("_id", key));
    }
}

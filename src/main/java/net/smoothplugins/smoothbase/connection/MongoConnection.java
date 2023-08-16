package net.smoothplugins.smoothbase.connection;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

    private final MongoDatabase database;

    public MongoConnection(String uri, String databaseName) {
        MongoClient client = new MongoClient(new MongoClientURI(uri));
        this.database = client.getDatabase(databaseName);
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}

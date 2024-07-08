package net.smoothplugins.common.database;

/**
 * Abstract class representing a generic database.
 */
public abstract class Database {

    /**
     * Connects to the database.
     */
    public abstract void connect();

    /**
     * Disconnects from the database.
     */
    public abstract void disconnect();
}

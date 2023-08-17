package net.smoothplugins.smoothbase.messenger;

import java.util.UUID;

public interface Messenger {

    /**
     * Send a message to a channel, without expecting a response
     * @param channel
     * @param JSON
     */
    void send(String channel, String JSON);

    /**
     * Send a message to a channel, expecting a response.
     * @param channel
     * @param JSON
     * @param response
     * @param timeout
     */
    void sendRequest(String channel, String JSON, Response response, long timeout);

    /**
     * Send a response to a request.
     * @param channel
     * @param JSON
     * @param identifier
     */
    void sendResponse(String channel, String JSON, UUID identifier);

    /**
     * Called when a message is received.
     * @param channel
     * @param message
     */
    void onMessage(String channel, Message message);

    void register();

    void unregister();
}

package net.smoothplugins.smoothbase.messenger;

public interface Messenger {

    /**
     * Send a message to a channel, without expecting a response
     * @param channel
     * @param message
     */
    void send(String channel, Object message);

    /**
     * Send a message to a channel, expecting a response.
     * @param channel
     * @param message
     * @param response
     * @param timeout
     */
    void sendWithResponse(String channel, Object message, Response response, long timeout);

    /**
     * Called when a message is received.
     * @param channel
     * @param message
     */
    void onMessage(String channel, Object message);

    void register();

    void unregister();
}

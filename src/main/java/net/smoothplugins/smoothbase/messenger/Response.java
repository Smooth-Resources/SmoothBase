package net.smoothplugins.smoothbase.messenger;

public interface Response {

    /**
     * Called when the response is received in the specified timeout.
     * @param channel
     * @param response
     */
    void onSuccess(String channel, Object response);

    /**
     * Called when the response is not received in the specified timeout
     * @param channel
     * @param response
     */
    void onFail(String channel, Object response);
}

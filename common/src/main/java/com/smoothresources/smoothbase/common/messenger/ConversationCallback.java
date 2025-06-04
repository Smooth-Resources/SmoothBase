package com.smoothresources.smoothbase.common.messenger;

import org.jetbrains.annotations.NotNull;

/**
 * Callback interface for handling the results of a conversation.
 */
public interface ConversationCallback {

    /**
     * Called when the conversation is successful.
     *
     * @param object The resulting object from the conversation.
     */
    void onSuccess(@NotNull Object object);

    /**
     * Called when the conversation times out.
     */
    void onTimeout();

    /**
     * Gets the timeout duration for the conversation.
     *
     * @return The timeout duration in milliseconds.
     */
    long getTimeout();
}

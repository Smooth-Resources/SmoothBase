package com.smoothresources.smoothbase.common.messenger.interceptor;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Interface for intercepting conversation messages.
 */
public interface ConversationInterceptor extends Interceptor {

    /**
     * Intercepts a conversation object with a unique identifier.
     *
     * @param object         The object to intercept.
     * @param conversationId The unique identifier of the conversation.
     */
    void intercept(@NotNull Object object, @NotNull UUID conversationId);
}

package net.smoothplugins.common.messenger.interceptor;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for intercepting messages.
 */
public interface Interceptor {

    /**
     * Intercepts an object.
     *
     * @param object The object to intercept.
     */
    void intercept(@NotNull Object object);
}

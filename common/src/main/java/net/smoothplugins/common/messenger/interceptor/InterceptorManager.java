package net.smoothplugins.common.messenger.interceptor;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

/**
 * Manages the registration and invocation of interceptors.
 */
public class InterceptorManager {

    private final HashMap<Class<?>, Interceptor> interceptors = new HashMap<>();

    /**
     * Registers an interceptor for a specific class type.
     *
     * @param clazz       The class type.
     * @param interceptor The interceptor to register.
     */
    public void registerInterceptor(@NotNull Class<?> clazz, @NotNull Interceptor interceptor) {
        interceptors.put(clazz, interceptor);
    }

    /**
     * Unregisters an interceptor for a specific class type.
     *
     * @param clazz The class type.
     */
    public void unregisterInterceptor(@NotNull Class<?> clazz) {
        interceptors.remove(clazz);
    }

    /**
     * Intercepts an object.
     *
     * @param object The object to intercept.
     * @throws IllegalArgumentException If no interceptor is found for the class type.
     */
    public void intercept(@NotNull Object object) {
        Interceptor interceptor = interceptors.get(object.getClass());

        if (interceptor == null) {
            throw new IllegalArgumentException("No interceptor found for class " + object.getClass().getName());
        }

        interceptor.intercept(object);
    }

    /**
     * Intercepts a conversation object with a unique identifier.
     *
     * @param object             The object to intercept.
     * @param conversationUUID   The unique identifier of the conversation.
     * @throws IllegalArgumentException If no conversation interceptor is found for the class type.
     */
    public void intercept(@NotNull Object object, @NotNull UUID conversationUUID) {
        Interceptor interceptor = interceptors.get(object.getClass());

        if (!(interceptor instanceof ConversationInterceptor conversationInterceptor)) {
            throw new IllegalArgumentException("No conversation interceptor found for class " + object.getClass().getName());
        } else {
            conversationInterceptor.intercept(object, conversationUUID);
        }
    }
}

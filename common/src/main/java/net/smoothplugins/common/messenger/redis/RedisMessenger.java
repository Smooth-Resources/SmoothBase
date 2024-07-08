package net.smoothplugins.common.messenger.redis;

import com.google.gson.Gson;
import net.smoothplugins.common.connection.RedisConnection;
import net.smoothplugins.common.messenger.Conversation;
import net.smoothplugins.common.messenger.ConversationCallback;
import net.smoothplugins.common.messenger.Message;
import net.smoothplugins.common.messenger.Messenger;
import net.smoothplugins.common.messenger.interceptor.InterceptorManager;
import net.smoothplugins.common.serializer.Serializer;
import net.smoothplugins.common.task.TaskManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Implementation of Messenger using Redis.
 */
public class RedisMessenger implements Messenger {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RedisMessenger.class);
    private final TaskManager taskManager;
    private final Logger logger;
    private final Serializer serializer;
    private final InterceptorManager interceptorManager;
    private final RedisConnection connection;
    private final String channel;

    private final HashMap<UUID, ConversationCallback> conversationCallbacks;
    private boolean closing;
    private Subscriber subscriber;

    /**
     * Creates a new RedisMessenger.
     *
     * @param taskManager         The task manager for running asynchronous tasks.
     * @param logger              The logger for logging messages.
     * @param serializer          The serializer for serializing messages.
     * @param interceptorManager  The manager for handling interceptors.
     * @param connection          The Redis connection.
     * @param channel             The Redis channel for messaging.
     */
    public RedisMessenger(@NotNull TaskManager taskManager, @NotNull Logger logger, @NotNull Serializer serializer,
                          @NotNull InterceptorManager interceptorManager, @NotNull RedisConnection connection,
                          @NotNull String channel) {
        this.taskManager = taskManager;
        this.logger = logger;
        this.serializer = serializer;
        this.interceptorManager = interceptorManager;
        this.connection = connection;
        this.channel = channel;
        this.conversationCallbacks = new HashMap<>();
    }

    /**
     * Gets a Jedis instance from the connection pool.
     *
     * @return A Jedis instance.
     */
    @NotNull
    public Jedis getJedis() {
        return connection.getResource();
    }

    @Override
    public void connect() {
        // Redis connection is handled via the RedisConnection class
        subscriber = new Subscriber();
        taskManager.runTaskAsync(subscriber);
    }

    @Override
    public void disconnect() {
        // Redis connection is handled via the RedisConnection class
        closing = true;
        subscriber.unsubscribe();
    }

    @Override
    public void send(@NotNull Message message) {
        try (Jedis jedis = getJedis()) {
            jedis.publish(channel, serializer.serialize(message));
        }
    }

    @Override
    public void send(@NotNull Conversation conversation) {
        if (conversation.getType() == Conversation.Type.REQUEST) {
            conversationCallbacks.put(conversation.getConversationUUID(), conversation.getCallback());
            try (Jedis jedis = getJedis()) {
                jedis.publish(channel, serializer.serialize(conversation));
            }

            taskManager.runTaskLaterAsync(() -> {
                if (!conversationCallbacks.containsKey(conversation.getConversationUUID())) return;

                ConversationCallback callback = conversationCallbacks.get(conversation.getConversationUUID());
                conversationCallbacks.remove(conversation.getConversationUUID());
                callback.onTimeout();
            }, conversation.getCallback().getTimeout());
        } else {
            // This is the response of a previous request
            try (Jedis jedis = getJedis()) {
                jedis.publish(channel, serializer.serialize(conversation));
            }
        }
    }

    @Override
    public void onMessage(@NotNull Message message) {
        Object object = new Gson().fromJson(message.getJson(), message.getClazz());
        interceptorManager.intercept(object);
    }

    @Override
    public void onMessage(@NotNull Conversation conversation) {
        Object object = new Gson().fromJson(conversation.getJson(), conversation.getClazz());

        if (conversation.getType() == Conversation.Type.REQUEST) {
            interceptorManager.intercept(object, conversation.getConversationUUID());
        } else {
            // This is the response of a previous request
            if (!conversationCallbacks.containsKey(conversation.getConversationUUID())) return;

            ConversationCallback callback = conversationCallbacks.get(conversation.getConversationUUID());
            conversationCallbacks.remove(conversation.getConversationUUID());
            callback.onSuccess(object);
        }
    }

    private class Subscriber extends JedisPubSub implements Runnable {

        @Override
        public void run() {
            boolean first = true;

            while (!closing && !Thread.interrupted() && !connection.getPool().isClosed()) {
                try (Jedis jedis = connection.getPool().getResource()) {
                    if (first) {
                        first = false;
                    } else {
                        logger.info("Reconnected to Redis");
                    }

                    jedis.subscribe(this, channel);
                } catch (Exception e) {
                    if (closing) {
                        return;
                    }

                    logger.warning("Lost connection to Redis" + e);
                    try {
                        unsubscribe();
                    } catch (Exception e2) {
                        logger.warning("Failed to unsubscribe from Redis" + e2);
                    }

                    // Sleep for 5 seconds to prevent massive spam in console
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }

                    logger.warning("Error while Redis subscribing, trying to reconnect...");
                }
            }
        }

        @Override
        public void onMessage(String channel, String message) {
            if (channel.equals(RedisMessenger.this.channel)) {
                Conversation conversation = serializer.deserialize(message, Conversation.class);
                if (conversation != null) {
                    RedisMessenger.this.onMessage(conversation);
                } else {
                    Message finalMessage = serializer.deserialize(message, Message.class);
                    if (finalMessage == null) {
                        logger.warning("Received invalid message from Redis (the message is not a Message object nor Conversation object): " + message);
                        return;
                    }

                    RedisMessenger.this.onMessage(finalMessage);
                }
            }
        }
    }
}

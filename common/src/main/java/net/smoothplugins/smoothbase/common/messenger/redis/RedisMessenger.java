package net.smoothplugins.smoothbase.common.messenger.redis;

import com.google.gson.Gson;
import net.smoothplugins.smoothbase.common.connection.RedisConnection;
import net.smoothplugins.smoothbase.common.messenger.Conversation;
import net.smoothplugins.smoothbase.common.messenger.ConversationCallback;
import net.smoothplugins.smoothbase.common.messenger.Message;
import net.smoothplugins.smoothbase.common.messenger.Messenger;
import net.smoothplugins.smoothbase.common.messenger.interceptor.InterceptorManager;
import net.smoothplugins.smoothbase.common.serializer.Serializer;
import net.smoothplugins.smoothbase.common.task.TaskManager;
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
        System.out.println("Sending conversation");
        if (conversation.getType() == Conversation.Type.REQUEST) {
            System.out.println("Conversation is a request with timeout: " + conversation.getCallback().getTimeout());
            conversationCallbacks.put(conversation.getConversationUUID(), conversation.getCallback());
            try (Jedis jedis = getJedis()) {
                jedis.publish(channel, serializer.serialize(conversation));
            }

            taskManager.runTaskLaterAsync(() -> {
                if (!conversationCallbacks.containsKey(conversation.getConversationUUID())) return;

                System.out.println("Conversation timeout");
                ConversationCallback callback = conversationCallbacks.get(conversation.getConversationUUID());
                conversationCallbacks.remove(conversation.getConversationUUID());
                callback.onTimeout();
            }, conversation.getCallback().getTimeout());
        } else {
            System.out.println("Conversation is a response");
            // This is the response of a previous request
            try (Jedis jedis = getJedis()) {
                System.out.println("1");
                jedis.publish(channel, serializer.serialize(conversation));
                System.out.println("2");
            }
        }
    }

    @Override
    public void onMessage(@NotNull Message message) {
        System.out.println("Received message");
        try {
            Object object = new Gson().fromJson(message.getJson(), message.getClazz());
            interceptorManager.intercept(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(@NotNull Conversation conversation) {
        System.out.println("Received conversation");
        System.out.println("1");
        System.out.println("Trying to convert json: " + conversation.getJson());
        System.out.println("Into class: " + conversation.getClazz());
        Object object;
        try {
            if (conversation.getClazz().toString().contains("UpdatedUserResponse")) {
                System.out.println("1.1");
                object = new Gson().fromJson(conversation.getJson(), conversation.getClazz());
                System.out.println("1.2");
            } else {
                object = new Gson().fromJson(conversation.getJson(), conversation.getClazz());
            }
        } catch (Exception e) {
            System.out.println("Failed to convert json to class: " + e);
            e.printStackTrace();
            return;
        }

        System.out.println("2");

        if (conversation.getType() == Conversation.Type.REQUEST) {
            System.out.println("Conversation is a request");
            interceptorManager.intercept(object, conversation.getConversationUUID());
        } else {
            System.out.println("Conversation is a response");
            // This is the response of a previous request
            System.out.println("Conversation uuid: " + conversation.getConversationUUID());
            if (!conversationCallbacks.containsKey(conversation.getConversationUUID())) return;

            System.out.println("Conversation has a callback");
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
            System.out.println("Received: " + message);
            if (channel.equals(RedisMessenger.this.channel)) {
                Conversation conversation = serializer.deserialize(message, Conversation.class);
                if (conversation == null) {
                    logger.warning("Received invalid message (channel: " + channel + "): " + message);
                    return;
                }

                if (conversation.getType() != null) { // If the message is not a conversation, the conversation type will be null.
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

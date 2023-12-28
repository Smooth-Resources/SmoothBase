package net.smoothplugins.smoothbase.messenger;

import net.smoothplugins.smoothbase.serializer.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class DefaultMessenger implements Messenger {

    private MessageConsumer consumer;
    private Serializer serializer;
    private Plugin plugin;

    private String CHANNEL;

    private HashMap<UUID, Response> pendingResponses = new HashMap<>();

    @Override
    public void send(String JSON) {
        Message finalMessage = new Message(Message.MessageType.NORMAL, null, JSON);
        onMessage(finalMessage);
    }

    @Override
    public void sendRequest(String JSON, Response response, long timeout) {
        UUID identifier = UUID.randomUUID();
        Message finalMessage = new Message(Message.MessageType.REQUEST, identifier, JSON);
        onMessage(finalMessage);

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            if (pendingResponses.containsKey(identifier)) {
                pendingResponses.get(identifier).onFail(CHANNEL);
                pendingResponses.remove(identifier);
            }
        }, (int) ((timeout / 1000F) * 20L));
    }

    @Override
    public void sendResponse(String JSON, UUID identifier) {
        Message finalMessage = new Message(Message.MessageType.RESPONSE, identifier, JSON);
        onMessage(finalMessage);
    }

    @Override
    public void onMessage(Message message) {
        if (message.getType() == Message.MessageType.RESPONSE) {
            if (!pendingResponses.containsKey(message.getIdentifier())) return;

            Response response = pendingResponses.get(message.getIdentifier());
            response.onSuccess(CHANNEL, message.getJSON());
            pendingResponses.remove(message.getIdentifier());
        } else {
            consumer.consume(message.getJSON(), message.getIdentifier());
        }
    }

    @Override
    public void register() {
        CHANNEL = "messenger:";
    }

    @Override
    public void unregister() {

    }
}

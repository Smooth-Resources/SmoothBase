package net.smoothplugins.smoothbase.messenger;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface MessageConsumer {

    void consume(String JSON, @Nullable UUID identifier);
}

package net.smoothplugins.smoothbase.notification;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.smoothplugins.smoothbase.configuration.Configuration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

/**
 * This class is used to display a notification to a player(s).
 * A notification can contain a chat message, an actionbar message, a title and a sound.
 * <p>
 * An example is available in the example file (/src/main/resources/example.yml).
 */
public class Notification {

    private final List<Component> chat;
    private final Component actionBar;
    private final Title title;
    private final Sound sound;

    public Notification(List<Component> chat, Component actionBar, Title title, Sound sound) {
        this.chat = chat;
        this.actionBar = actionBar;
        this.title = title;
        this.sound = sound;
    }

    @NotNull
    public static Notification of(Configuration config, String path, @Nullable HashMap<String, String> placeholders) {
        return new Notification(
                getChat(config, path + ".chat", placeholders),
                getActionbar(config, path + ".actionbar", placeholders),
                getTitle(config, path + ".title", placeholders),
                getSound(config, path + ".sound")
        );
    }

    private static List<Component> getChat(Configuration config, String path, HashMap<String, String> placeholders) {
        if (!config.getBoolean(path + ".enabled")) return null;

        return config.getComponentList(path + ".content", placeholders);
    }

    private static Component getActionbar(Configuration config, String path, HashMap<String, String> placeholders) {
        if (!config.getBoolean(path + ".enabled")) return null;

        return config.getComponent(path + ".content", placeholders);
    }

    private static Title getTitle(Configuration config, String path, HashMap<String, String> placeholders) {
        if (!config.getBoolean(path + ".enabled")) return null;

        return config.getTitle(path + ".title", path + ".subtitle", path + ".fadeIn", path + ".stay", path + ".fadeOut", placeholders);
    }

    private static Sound getSound(Configuration config, String path) {
        if (!config.getBoolean(path + ".enabled")) return null;

        return config.getSound(path + ".content");
    }

    public List<Component> getChat() {
        return chat;
    }

    public Component getActionBar() {
        return actionBar;
    }

    public Title getTitle() {
        return title;
    }

    public Sound getSound() {
        return sound;
    }

    public void send(Player... players) {
        Audience audience = Audience.audience(players);

        if (getChat() != null) {
            getChat().forEach(audience::sendMessage);
        }

        if (getActionBar() != null) {
            audience.sendActionBar(getActionBar());
        }

        if (getTitle() != null) {
            audience.showTitle(getTitle());
        }

        if (getSound() != null) {
            audience.playSound(getSound());
        }
    }
}

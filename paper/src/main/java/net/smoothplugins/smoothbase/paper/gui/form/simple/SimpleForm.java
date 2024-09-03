package net.smoothplugins.smoothbase.paper.gui.form.simple;

import net.smoothplugins.smoothbase.paper.gui.form.Form;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.component.ButtonComponent;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class SimpleForm extends Form {

    private HashMap<ButtonComponent, Runnable> buttonActions;

    public SimpleForm(@NotNull Player player, @NotNull FloodgatePlayer floodgatePlayer) {
        super(player, floodgatePlayer);
        buttonActions = new HashMap<>();
    }

    public abstract String getContent();
    public abstract HashMap<ButtonComponent, Runnable> getButtons();

    @Override
    public org.geysermc.cumulus.form.Form getForm() {
        org.geysermc.cumulus.form.SimpleForm.Builder builder = org.geysermc.cumulus.form.SimpleForm.builder()
                .title(getTitle())
                .content(getContent());

        getButtons().forEach((buttonComponent, onClick) -> {
            buttonActions.put(buttonComponent, onClick);
            builder.button(buttonComponent);
        });

        builder.validResultHandler(response -> {
            Runnable runnable = buttonActions.get(response.clickedButton());
            if (runnable != null) {
                runnable.run();
            }
        });

        return builder.build();
    }
}

package net.smoothplugins.smoothbase.paper.gui.form;

import net.smoothplugins.smoothbase.paper.gui.GUI;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.jetbrains.annotations.NotNull;

public abstract class Form extends GUI {

    private final FloodgatePlayer floodgatePlayer;
    private org.geysermc.cumulus.form.Form form;

    public Form(@NotNull Player player, @NotNull FloodgatePlayer floodgatePlayer) {
        super(player);
        this.floodgatePlayer = floodgatePlayer;
    }

    public FloodgatePlayer getFloodgatePlayer() {
        return floodgatePlayer;
    }

    public void setForm(org.geysermc.cumulus.form.Form form) {
        this.form = form;
    }

    @Override
    public void open() {
        form = getForm();
        floodgatePlayer.sendForm(form);
    }

    public abstract org.geysermc.cumulus.form.Form getForm();

    public abstract String getTitle();
}

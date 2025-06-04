package com.smoothresources.smoothbase.paper.gui.form.modal;

import com.smoothresources.smoothbase.paper.gui.form.Form;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.jetbrains.annotations.NotNull;

public abstract class ModalForm extends Form {

    public ModalForm(@NotNull Player player, @NotNull FloodgatePlayer floodgatePlayer) {
        super(player, floodgatePlayer);
    }

    public abstract String getContent();
    public abstract String getButton1();
    public abstract String getButton2();
    public abstract void onButton1Click();
    public abstract void onButton2Click();

    @Override
    public org.geysermc.cumulus.form.Form getForm() {
        return org.geysermc.cumulus.form.ModalForm.builder()
                .title(getTitle())
                .content(getContent())
                .button1(getButton1())
                .button2(getButton2())
                .validResultHandler((modalForm, modalFormResponse) -> {
                    if (modalFormResponse.clickedFirst()) {
                        onButton1Click();
                    } else {
                        onButton2Click();
                    }
                })
                .build();
    }
}

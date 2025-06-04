package com.smoothresources.smoothbase.paper.gui;

import org.bukkit.entity.Player;

public abstract class GUI {

    private final Player player;

    public GUI(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract void open();
}

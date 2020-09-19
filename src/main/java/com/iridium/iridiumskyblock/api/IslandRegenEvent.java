package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.Island;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class IslandRegenEvent extends IslandEvent implements Cancellable {
    private boolean cancelled;
    public IslandRegenEvent(@NotNull Island island) {
        super(island);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

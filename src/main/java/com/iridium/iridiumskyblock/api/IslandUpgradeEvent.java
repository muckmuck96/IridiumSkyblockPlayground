package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.Island;
import org.jetbrains.annotations.NotNull;

public class IslandUpgradeEvent extends IslandEvent {
    private boolean cancelled;
    private boolean upgradable;
    private UpgradeType upgradeType;

    public IslandUpgradeEvent(@NotNull Island island, @NotNull UpgradeType upgradeType, @NotNull boolean upgradable) {
        super(island);
        this.upgradeType = upgradeType;
        this.upgradable = upgradable;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public UpgradeType getUpgradeType() {
        return upgradeType;
    }

    public boolean isUpgradable() {
        return upgradable;
    }

    public static enum UpgradeType {
        ISLAND_SIZE, MEMBER_SIZE, WARPS, GENERATOR;
    }
}

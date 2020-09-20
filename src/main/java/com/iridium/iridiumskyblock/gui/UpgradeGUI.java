package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Island;
import com.iridium.iridiumskyblock.Utils;
import com.iridium.iridiumskyblock.api.IslandUpgradeEvent;
import com.iridium.iridiumskyblock.configs.Upgrades;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class UpgradeGUI extends GUI implements Listener {

    public UpgradeGUI(Island island) {
        super(island, IridiumSkyblock.getInventories().upgradeGUISize, IridiumSkyblock.getInventories().upgradeGUITitle);
        IridiumSkyblock.getInstance().registerListeners(this);
    }

    @Override
    public void addContent() {
        super.addContent();
        if (getInventory().getViewers().isEmpty()) return;
        Island island = getIsland();
        if (island != null) {
            if (IridiumSkyblock.getUpgrades().sizeUpgrade.enabled)
                setItem(IridiumSkyblock.getUpgrades().sizeUpgrade.slot, Utils.makeItemHidden(IridiumSkyblock.getInventories().size, getIsland()));
            if (IridiumSkyblock.getUpgrades().memberUpgrade.enabled)
                setItem(IridiumSkyblock.getUpgrades().memberUpgrade.slot, Utils.makeItemHidden(IridiumSkyblock.getInventories().member, getIsland()));
            if (IridiumSkyblock.getUpgrades().warpUpgrade.enabled)
                setItem(IridiumSkyblock.getUpgrades().warpUpgrade.slot, Utils.makeItemHidden(IridiumSkyblock.getInventories().warp, getIsland()));
            if (IridiumSkyblock.getUpgrades().oresUpgrade.enabled)
                setItem(IridiumSkyblock.getUpgrades().oresUpgrade.slot, Utils.makeItemHidden(IridiumSkyblock.getInventories().ores, getIsland()));
        }
    }

    @EventHandler
    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().equals(getInventory())) {
            e.setCancelled(true);
            if (e.getClickedInventory() == null || !e.getClickedInventory().equals(getInventory())) return;
            Player p = (Player) e.getWhoClicked();
            if (e.getSlot() == IridiumSkyblock.getUpgrades().sizeUpgrade.slot && IridiumSkyblock.getUpgrades().sizeUpgrade.enabled) {
                if (IridiumSkyblock.getUpgrades().sizeUpgrade.upgrades.containsKey(getIsland().getSizeLevel() + 1)) {
                    Upgrades.IslandUpgrade upgrade = IridiumSkyblock.getUpgrades().sizeUpgrade.upgrades.get(getIsland().getSizeLevel() + 1);
                    IslandUpgradeEvent islandUpgradeEvent = new IslandUpgradeEvent(getIsland(), IslandUpgradeEvent.UpgradeType.ISLAND_SIZE, Utils.canBuyStandalone(p, upgrade.vaultCost, upgrade.crystalsCost), p);
                    Bukkit.getPluginManager().callEvent(islandUpgradeEvent);
                    if(islandUpgradeEvent.isCancelled()) {
                        return;
                    }
                    if (Utils.canBuy(p, upgrade.vaultCost, upgrade.crystalsCost)) {
                        getIsland().setSizeLevel(getIsland().getSizeLevel() + 1);
                    } else {
                        e.getWhoClicked().sendMessage(Utils.color(IridiumSkyblock.getMessages().notEnoughCrystals.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                    }
                } else {
                    e.getWhoClicked().sendMessage(Utils.color(IridiumSkyblock.getMessages().maxLevelReached.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                }
            }
            if (e.getSlot() == IridiumSkyblock.getUpgrades().memberUpgrade.slot && IridiumSkyblock.getUpgrades().memberUpgrade.enabled) {
                if (IridiumSkyblock.getUpgrades().memberUpgrade.upgrades.containsKey(getIsland().getMemberLevel() + 1)) {
                    Upgrades.IslandUpgrade upgrade = IridiumSkyblock.getUpgrades().memberUpgrade.upgrades.get(getIsland().getMemberLevel() + 1);
                    IslandUpgradeEvent islandUpgradeEvent = new IslandUpgradeEvent(getIsland(), IslandUpgradeEvent.UpgradeType.MEMBER_SIZE, Utils.canBuyStandalone(p, upgrade.vaultCost, upgrade.crystalsCost), p);
                    Bukkit.getPluginManager().callEvent(islandUpgradeEvent);
                    if(islandUpgradeEvent.isCancelled()) {
                        return;
                    }
                    if (Utils.canBuy(p, upgrade.vaultCost, upgrade.crystalsCost)) {
                        getIsland().setMemberLevel(getIsland().getMemberLevel() + 1);
                    } else {
                        e.getWhoClicked().sendMessage(Utils.color(IridiumSkyblock.getMessages().notEnoughCrystals.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                    }
                } else {
                    e.getWhoClicked().sendMessage(Utils.color(IridiumSkyblock.getMessages().maxLevelReached.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                }
            }
            if (e.getSlot() == IridiumSkyblock.getUpgrades().warpUpgrade.slot && IridiumSkyblock.getUpgrades().warpUpgrade.enabled) {
                if (IridiumSkyblock.getUpgrades().warpUpgrade.upgrades.containsKey(getIsland().getWarpLevel() + 1)) {
                    Upgrades.IslandUpgrade upgrade = IridiumSkyblock.getUpgrades().warpUpgrade.upgrades.get(getIsland().getWarpLevel() + 1);
                    IslandUpgradeEvent islandUpgradeEvent = new IslandUpgradeEvent(getIsland(), IslandUpgradeEvent.UpgradeType.WARPS, Utils.canBuyStandalone(p, upgrade.vaultCost, upgrade.crystalsCost), p);
                    Bukkit.getPluginManager().callEvent(islandUpgradeEvent);
                    if(islandUpgradeEvent.isCancelled()) {
                        return;
                    }
                    if (Utils.canBuy(p, upgrade.vaultCost, upgrade.crystalsCost)) {
                        getIsland().setWarpLevel(getIsland().getWarpLevel() + 1);
                    } else {
                        e.getWhoClicked().sendMessage(Utils.color(IridiumSkyblock.getMessages().notEnoughCrystals.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                    }
                } else {
                    e.getWhoClicked().sendMessage(Utils.color(IridiumSkyblock.getMessages().maxLevelReached.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                }
            }
            if (e.getSlot() == IridiumSkyblock.getUpgrades().oresUpgrade.slot && IridiumSkyblock.getUpgrades().oresUpgrade.enabled) {
                if (IridiumSkyblock.getUpgrades().oresUpgrade.upgrades.containsKey(getIsland().getOreLevel() + 1)) {
                    Upgrades.IslandUpgrade upgrade = IridiumSkyblock.getUpgrades().oresUpgrade.upgrades.get(getIsland().getOreLevel() + 1);
                    IslandUpgradeEvent islandUpgradeEvent = new IslandUpgradeEvent(getIsland(), IslandUpgradeEvent.UpgradeType.GENERATOR, Utils.canBuyStandalone(p, upgrade.vaultCost, upgrade.crystalsCost), p);
                    Bukkit.getPluginManager().callEvent(islandUpgradeEvent);
                    if(islandUpgradeEvent.isCancelled()) {
                        return;
                    }
                    if (Utils.canBuy(p, upgrade.vaultCost, upgrade.crystalsCost)) {
                        getIsland().setOreLevel(getIsland().getOreLevel() + 1);
                    } else {
                        e.getWhoClicked().sendMessage(Utils.color(IridiumSkyblock.getMessages().notEnoughCrystals.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                    }
                } else {
                    e.getWhoClicked().sendMessage(Utils.color(IridiumSkyblock.getMessages().maxLevelReached.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                }
            }
        }
    }
}

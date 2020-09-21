package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.*;
import com.iridium.iridiumskyblock.support.Vault;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BankGUI extends GUI implements Listener {

    public BankGUI(Island island) {
        super(island, IridiumSkyblock.getInventories().bankGUISize, IridiumSkyblock.getInventories().bankGUITitle);
        IridiumSkyblock.getInstance().registerListeners(this);
    }

    @Override
    public void addContent() {
        super.addContent();
        if (getInventory().getViewers().isEmpty()) return;
        if (IridiumSkyblock.getIslandManager().islands.containsKey(islandID)) {
            setItem(IridiumSkyblock.getInventories().experience.slot == null ? 11 : IridiumSkyblock.getInventories().experience.slot, Utils.makeItemHidden(IridiumSkyblock.getInventories().experience, getIsland()));
            setItem(IridiumSkyblock.getInventories().crystals.slot == null ? 13 : IridiumSkyblock.getInventories().crystals.slot, Utils.makeItemHidden(IridiumSkyblock.getInventories().crystals, getIsland()));
            setItem(IridiumSkyblock.getInventories().money.slot == null ? 15 : IridiumSkyblock.getInventories().money.slot, Utils.makeItemHidden(IridiumSkyblock.getInventories().money, getIsland()));
        }
    }

    @EventHandler
    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().equals(getInventory())) {
            e.setCancelled(true);
            if (e.getClickedInventory() == null || !e.getClickedInventory().equals(getInventory())) return;
            Player p = (Player) e.getWhoClicked();
            Island island = getIsland();
            User u = User.getUser(p);
            if (e.getSlot() == (IridiumSkyblock.getInventories().experience.slot == null ? 11 : IridiumSkyblock.getInventories().experience.slot)) {
                if (e.getClick().equals(ClickType.SHIFT_LEFT)) {
                    if ((island.getPermissions((u.islandID == island.getId() || island.isCoop(u.getIsland())) ? (island.isCoop(u.getIsland()) ? Role.Member : u.getRole()) : Role.Visitor).withdrawBank) || u.bypassing) {
                        Utils.setTotalExperience(p, Utils.getTotalExperience(p) + island.exp);
                        island.exp = 0;
                    }
                } else if (e.getClick().equals(ClickType.SHIFT_RIGHT)) {
                    island.exp += Utils.getTotalExperience(p);
                    Utils.setTotalExperience(p, 0);
                } else if (e.getClick().equals(ClickType.RIGHT)) {
                    if (Utils.getTotalExperience(p) > 100) {
                        island.exp += 100;
                        Utils.setTotalExperience(p, Utils.getTotalExperience(p) - 100);
                    } else {
                        island.exp += Utils.getTotalExperience(p);
                        Utils.setTotalExperience(p, 0);
                    }
                } else if (e.getClick().equals(ClickType.LEFT)) {
                    if ((island.getPermissions((u.islandID == island.getId() || island.isCoop(u.getIsland())) ? (island.isCoop(u.getIsland()) ? Role.Member : u.getRole()) : Role.Visitor).withdrawBank) || u.bypassing) {
                        if (island.exp > 100) {
                            island.exp -= 100;
                            Utils.setTotalExperience(p, Utils.getTotalExperience(p) + 100);
                        } else {
                            Utils.setTotalExperience(p, Utils.getTotalExperience(p) + island.exp);
                            island.exp = 0;
                        }
                    }
                }
            }
            if(IridiumSkyblock.getConfiguration().enableCrystalWithdraw) {
                if (e.getSlot() == (IridiumSkyblock.getInventories().crystals.slot == null ? 13 : IridiumSkyblock.getInventories().crystals.slot)) {
                    if (e.getClick().equals(ClickType.SHIFT_LEFT)) {
                        if ((island.getPermissions((u.islandID == island.getId() || island.isCoop(u.getIsland())) ? (island.isCoop(u.getIsland()) ? Role.Member : u.getRole()) : Role.Visitor).withdrawBank) || u.bypassing) {
                            if (island.getCrystals() > 0) {
                                if (p.getInventory().firstEmpty() != -1){
                                    p.getInventory().addItem(Utils.getCrystals(island.getCrystals()));
                                    island.setCrystals(0);
                                } else {
                                    p.sendMessage(Utils.color(IridiumSkyblock.getMessages().inventoryFull
                                            .replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                                }
                            }
                        }
                    } else if (e.getClick().equals(ClickType.SHIFT_RIGHT)) {
                        int i = 0;
                        for (ItemStack itemStack : p.getInventory().getContents()) {
                            if (itemStack == null) {
                                i++;
                                continue;
                            }
                            int crystals = Utils.getCrystals(itemStack) * itemStack.getAmount();
                            if (crystals != 0) {
                                island.setCrystals(island.getCrystals() + crystals);
                                p.getInventory().clear(i);
                            }
                            i++;
                        }
                    } else if (e.getClick().equals(ClickType.RIGHT)) {
                        int i = 0;
                        for (ItemStack itemStack : p.getInventory().getContents()) {
                            if (itemStack == null) {
                                i++;
                                continue;
                            }
                            int crystals = Utils.getCrystals(itemStack) * itemStack.getAmount();
                            if (crystals != 0) {
                                island.setCrystals(island.getCrystals() + crystals);
                                p.getInventory().clear(i);
                                return;
                            }
                            i++;
                        }
                    } else if (e.getClick().equals(ClickType.LEFT)) {
                        if ((island.getPermissions((u.islandID == island.getId() || island.isCoop(u.getIsland())) ? (island.isCoop(u.getIsland()) ? Role.Member : u.getRole()) : Role.Visitor).withdrawBank) || u.bypassing) {
                            if (island.getCrystals() > 0) {
                                if (p.getInventory().firstEmpty() != -1){
                                    island.setCrystals(island.getCrystals() - 1);
                                    p.getInventory().addItem(Utils.getCrystals(1));
                                } else {
                                    p.sendMessage(Utils.color(IridiumSkyblock.getMessages().inventoryFull
                                            .replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                                }
                            }
                        }
                    }
                }
            }
            if (e.getSlot() == (IridiumSkyblock.getInventories().money.slot == null ? 15 : IridiumSkyblock.getInventories().money.slot)) {
                if (Vault.econ != null) {
                    if (e.getClick().equals(ClickType.SHIFT_LEFT)) {
                        if ((island.getPermissions((u.islandID == island.getId() || island.isCoop(u.getIsland())) ? (island.isCoop(u.getIsland()) ? Role.Member : u.getRole()) : Role.Visitor).withdrawBank) || u.bypassing) {
                            Vault.econ.depositPlayer(p, island.money);
                            island.money = 0;
                        }
                    } else if (e.getClick().equals(ClickType.SHIFT_RIGHT)) {
                        island.money += Vault.econ.getBalance(p);
                        Vault.econ.withdrawPlayer(p, Vault.econ.getBalance(p));
                    } else if (e.getClick().equals(ClickType.RIGHT)) {
                        double depositValue = Vault.econ.getBalance(p) > 1000 ? 1000 : Vault.econ.getBalance(p);
                        if (!(island.money > Double.MAX_VALUE - depositValue)) {
                            island.money += depositValue;
                            Vault.econ.withdrawPlayer(p, depositValue);
                        }
                    } else if (e.getClick().equals(ClickType.LEFT)) {
                        if ((island.getPermissions((u.islandID == island.getId() || island.isCoop(u.getIsland())) ? (island.isCoop(u.getIsland()) ? Role.Member : u.getRole()) : Role.Visitor).withdrawBank) || u.bypassing) {
                            if (island.money > 1000) {
                                island.money -= 1000;
                                Vault.econ.depositPlayer(p, 1000);
                            } else {
                                Vault.econ.depositPlayer(p, island.money);
                                island.money = 0;
                            }
                        }
                    }
                }
            }
        }
    }
}
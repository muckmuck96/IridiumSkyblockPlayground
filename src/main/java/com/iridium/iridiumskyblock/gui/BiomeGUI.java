package com.iridium.iridiumskyblock.gui;

import com.google.common.io.InsecureRecursiveDeleteException;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Island;
import com.iridium.iridiumskyblock.Utils;
import com.iridium.iridiumskyblock.XBiome;
import org.apache.commons.lang.WordUtils;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BiomeGUI extends GUI implements Listener {

    public int page;

    public BiomeGUI root;

    public Map<Integer, BiomeGUI> pages = new HashMap<>();

    public Map<Integer, XBiome> biomes = new HashMap<>();

    public BiomeGUI(Island island) {
        IridiumSkyblock.getInstance().registerListeners(this);
        int size = (int) (Math.floor(Biome.values().length / ((double) IridiumSkyblock.getInventories().biomeGUISize - 9)) + 1);
        for (int i = 1; i <= size; i++) {
            pages.put(i, new BiomeGUI(island, i, this));
        }
    }

    public BiomeGUI(Island island, int page, BiomeGUI root) {
        super(island, IridiumSkyblock.getInventories().biomeGUISize, IridiumSkyblock.getInventories().biomeGUITitle);
        this.page = page;
        this.root = root;
    }

    @Override
    public void addContent() {
        super.addContent();
        if (getInventory().getViewers().isEmpty()) return;
        int i = 0;
        int slot = 0;
        for (XBiome biome : IridiumSkyblock.getConfiguration().biomes) {
            if (biome.parseBiome() != null) {
                if (i >= 45 * (page - 1) && slot < 45) {
                    setItem(slot, Utils.makeItem(IridiumSkyblock.getInventories().biome, Collections.singletonList(new Utils.Placeholder("biome", WordUtils.capitalize(biome.name().toLowerCase().replace("_", " "))))));
                    biomes.put(slot, biome);
                    slot++;
                }
                i++;
            }
        }
        setItem(getInventory().getSize() - 3, Utils.makeItem(IridiumSkyblock.getInventories().nextPage));
        setItem(getInventory().getSize() - 7, Utils.makeItem(IridiumSkyblock.getInventories().previousPage));
    }

    @EventHandler
    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (getInventory() == null) {
            for (BiomeGUI gui : pages.values()) {
                gui.onInventoryClick(e);
            }
        } else {
            if (e.getInventory().equals(getInventory())) {
                e.setCancelled(true);
                if (e.getClickedInventory() == null || !e.getClickedInventory().equals(getInventory())) return;
                if (e.getSlot() == getInventory().getSize() - 3) {
                    if (root.pages.containsKey(page + 1)) {
                        e.getWhoClicked().openInventory(root.pages.get(page + 1).getInventory());
                    }
                }
                if (e.getSlot() == getInventory().getSize() - 7) {
                    if (root.pages.containsKey(page - 1)) {
                        e.getWhoClicked().openInventory(root.pages.get(page - 1).getInventory());
                    }
                }
                if (biomes.containsKey(e.getSlot())) {
                    getIsland().setBiome(biomes.get(e.getSlot()));
                    if(!IridiumSkyblock.getConfiguration().instantBiomeUpdate) {
                        IridiumSkyblock.getInstance().saveIslandManager();
                        e.getWhoClicked().sendMessage(Utils.color(IridiumSkyblock.getMessages().biomeUpdateOnRestart.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                    }
                }
            }
        }
    }
}
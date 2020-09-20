package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.configs.Inventories;
import com.iridium.iridiumskyblock.support.Vault;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    public static ItemStack makeItem(Material material, int amount, int type, String name, List<String> lore, Object object) {
        ItemStack item = new ItemStack(material, amount, (short) type);
        ItemMeta m = item.getItemMeta();
        m.setLore(lore);
        m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack makeItem(XMaterial material, int amount, String name) {
        ItemStack item = material.parseItem(true);
        if (item == null) return null;
        item.setAmount(amount);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack makeItem(XMaterial material, int amount, String name, List<String> lore) {
        ItemStack item = material.parseItem(true);
        if (item == null) return null;
        item.setAmount(amount);
        ItemMeta m = item.getItemMeta();
        m.setLore(Utils.color(lore));
        m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(m);
        return item;
    }

    public static ItemStack makeItem(Inventories.Item item, List<Placeholder> placeholders) {
        try {
            ItemStack itemstack = makeItem(item.material, item.amount, processMultiplePlaceholders(item.title, placeholders), processMultiplePlaceholders(item.lore, placeholders));
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(processMultiplePlaceholders(item.headOwner, placeholders));
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            return makeItem(XMaterial.STONE, item.amount, processMultiplePlaceholders(item.title, placeholders), processMultiplePlaceholders(item.lore, placeholders));
        }
    }

    public static ItemStack makeItem(Inventories.Item item) {
        try {
            ItemStack itemstack = makeItem(item.material, item.amount, item.title, item.lore);
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(item.headOwner);
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            return makeItem(XMaterial.STONE, item.amount, item.title, item.lore);
        }
    }

    public static ItemStack makeItem(Inventories.Item item, Island island) {
        try {
            ItemStack itemstack = makeItem(item.material, item.amount, processIslandPlaceholders(item.title, island), color(processIslandPlaceholders(item.lore, island)));
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(item.headOwner);
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            return makeItem(XMaterial.STONE, item.amount, processIslandPlaceholders(item.title, island), color(processIslandPlaceholders(item.lore, island)));
        }
    }

    public static ItemStack makeItemHidden(Inventories.Item item) {
        try {
            ItemStack itemstack = makeItemHidden(item.material, item.amount, item.title, item.lore);
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(item.headOwner);
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            return makeItemHidden(XMaterial.STONE, item.amount, item.title, item.lore);
        }
    }

    public static ItemStack makeItemHidden(Inventories.Item item, Island island) {
        return makeItemHidden(item, getIslandPlaceholders(island));
    }

    public static ItemStack makeItemHidden(Inventories.Item item, List<Placeholder> placeholders) {
        try {
            ItemStack itemstack = makeItemHidden(item.material, item.amount, processMultiplePlaceholders(item.title, placeholders), color(processMultiplePlaceholders(item.lore, placeholders)));
            if (item.material == XMaterial.PLAYER_HEAD && item.headOwner != null) {
                SkullMeta m = (SkullMeta) itemstack.getItemMeta();
                m.setOwner(item.headOwner);
                itemstack.setItemMeta(m);
            }
            return itemstack;
        } catch (Exception e) {
            e.printStackTrace();
            return makeItemHidden(XMaterial.STONE, item.amount, processMultiplePlaceholders(item.title, placeholders), color(processMultiplePlaceholders(item.lore, placeholders)));
        }
    }

    public static ItemStack makeItemHidden(XMaterial material, int amount, String name, List<String> lore) {
        ItemStack item = material.parseItem(true);
        if (item == null) return null;
        item.setAmount(amount);
        ItemMeta m = item.getItemMeta();
        m.setLore(Utils.color(lore));
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(m);
        return item;
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> color(List<String> strings) {
        return strings.stream().map(Utils::color).collect(Collectors.toList());
    }

    public static boolean isBlockValuable(Block b) {
        return IridiumSkyblock.getBlockValues().blockvalue.containsKey(XMaterial.matchXMaterial(b.getType())) || b.getState() instanceof CreatureSpawner || IridiumSkyblock.getConfiguration().limitedBlocks.containsKey(XMaterial.matchXMaterial(b.getType()));
    }

    public static boolean isBlockValuable(XMaterial material) {
        return IridiumSkyblock.getBlockValues().blockvalue.containsKey(material) || IridiumSkyblock.getConfiguration().limitedBlocks.containsKey(material);
    }

    public static List<Island> getTopIslands() {
        List<Island> islands = new ArrayList<>(IridiumSkyblock.getIslandManager().islands.values());
        islands.sort(Comparator.comparingDouble(Island::getValue));
        Collections.reverse(islands);
        return islands;
    }

    public static List<Island> getIslands() {
        List<Island> islands = new ArrayList<>(IridiumSkyblock.getIslandManager().islands.values());
        islands.sort(Comparator.comparingInt(Island::getVotes));
        Collections.reverse(islands);
        return islands;
    }

    public static boolean isSafe(Location loc, Island island) {
        if (loc == null) return false;
        if (loc.getY() < 1) return false;
        if (!island.isInIsland(loc)) return false;
        if (!loc.getBlock().getType().name().endsWith("AIR")) return false;
        if (loc.clone().add(0, -1, 0).getBlock().getType().name().endsWith("AIR"))
            return false;
        return !loc.clone().add(0, -1, 0).getBlock().isLiquid();
    }

    public static int getIslandRank(Island island) {
        int i = 1;
        for (Island is : getTopIslands()) {
            if (is.equals(island)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    public static Location getNewHome(Island island, Location loc) {
        Block b;
        if (loc != null) {
            b = loc.getWorld().getHighestBlockAt(loc);
            while (!XMaterial.matchXMaterial(b.getType()).name().endsWith("AIR")) {
                b = b.getLocation().clone().add(0, 1, 0).getBlock();
            }
            if (isSafe(b.getLocation(), island)) {
                return b.getLocation().add(0.5, 0, 0.5);
            }
        }

        for (double X = island.getPos1().getX(); X <= island.getPos2().getX(); X++) {
            for (double Z = island.getPos1().getZ(); Z <= island.getPos2().getZ(); Z++) {
                b = loc.getWorld().getHighestBlockAt((int) X, (int) Z);
                while (!XMaterial.matchXMaterial(b.getType()).name().endsWith("AIR")) {
                    b = b.getLocation().clone().add(0, 1, 0).getBlock();
                }
                if (isSafe(b.getLocation(), island)) {
                    return b.getLocation().add(0.5, 0, 0.5);
                }
            }
        }
        return null;
    }

    public static List<Placeholder> getIslandPlaceholders(Island island) {
        List<Placeholder> placeholders = new ArrayList<>(Arrays.asList(
                // Upgrades
                new Placeholder("sizevaultcost", IridiumSkyblock.getUpgrades().sizeUpgrade.upgrades.containsKey(island.getSizeLevel() + 1) ? IridiumSkyblock.getUpgrades().sizeUpgrade.upgrades.get(island.getSizeLevel() + 1).vaultCost + "" : IridiumSkyblock.getMessages().maxlevelreached),
                new Placeholder("membervaultcost", IridiumSkyblock.getUpgrades().memberUpgrade.upgrades.containsKey(island.getMemberLevel() + 1) ? IridiumSkyblock.getUpgrades().memberUpgrade.upgrades.get(island.getMemberLevel() + 1).vaultCost + "" : IridiumSkyblock.getMessages().maxlevelreached),
                new Placeholder("warpvaultcost", IridiumSkyblock.getUpgrades().warpUpgrade.upgrades.containsKey(island.getWarpLevel() + 1) ? IridiumSkyblock.getUpgrades().warpUpgrade.upgrades.get(island.getWarpLevel() + 1).vaultCost + "" : IridiumSkyblock.getMessages().maxlevelreached),
                new Placeholder("oresvaultcost", IridiumSkyblock.getUpgrades().oresUpgrade.upgrades.containsKey(island.getOreLevel() + 1) ? IridiumSkyblock.getUpgrades().oresUpgrade.upgrades.get(island.getOreLevel() + 1).vaultCost + "" : IridiumSkyblock.getMessages().maxlevelreached),

                new Placeholder("sizecrystalscost", IridiumSkyblock.getUpgrades().sizeUpgrade.upgrades.containsKey(island.getSizeLevel() + 1) ? IridiumSkyblock.getUpgrades().sizeUpgrade.upgrades.get(island.getSizeLevel() + 1).crystalsCost + "" : IridiumSkyblock.getMessages().maxlevelreached),
                new Placeholder("membercrystalscost", IridiumSkyblock.getUpgrades().memberUpgrade.upgrades.containsKey(island.getMemberLevel() + 1) ? IridiumSkyblock.getUpgrades().memberUpgrade.upgrades.get(island.getMemberLevel() + 1).crystalsCost + "" : IridiumSkyblock.getMessages().maxlevelreached),
                new Placeholder("warpcrystalscost", IridiumSkyblock.getUpgrades().warpUpgrade.upgrades.containsKey(island.getWarpLevel() + 1) ? IridiumSkyblock.getUpgrades().warpUpgrade.upgrades.get(island.getWarpLevel() + 1).crystalsCost + "" : IridiumSkyblock.getMessages().maxlevelreached),
                new Placeholder("orescrystalscost", IridiumSkyblock.getUpgrades().oresUpgrade.upgrades.containsKey(island.getOreLevel() + 1) ? IridiumSkyblock.getUpgrades().oresUpgrade.upgrades.get(island.getOreLevel() + 1).crystalsCost + "" : IridiumSkyblock.getMessages().maxlevelreached),

                new Placeholder("sizecost", IridiumSkyblock.getUpgrades().sizeUpgrade.upgrades.containsKey(island.getSizeLevel() + 1) ? IridiumSkyblock.getUpgrades().sizeUpgrade.upgrades.get(island.getSizeLevel() + 1).crystalsCost + "" : IridiumSkyblock.getMessages().maxlevelreached),
                new Placeholder("membercost", IridiumSkyblock.getUpgrades().memberUpgrade.upgrades.containsKey(island.getMemberLevel() + 1) ? IridiumSkyblock.getUpgrades().memberUpgrade.upgrades.get(island.getMemberLevel() + 1).crystalsCost + "" : IridiumSkyblock.getMessages().maxlevelreached),
                new Placeholder("warpcost", IridiumSkyblock.getUpgrades().warpUpgrade.upgrades.containsKey(island.getWarpLevel() + 1) ? IridiumSkyblock.getUpgrades().warpUpgrade.upgrades.get(island.getWarpLevel() + 1).crystalsCost + "" : IridiumSkyblock.getMessages().maxlevelreached),
                new Placeholder("generatorcost", IridiumSkyblock.getUpgrades().oresUpgrade.upgrades.containsKey(island.getOreLevel() + 1) ? IridiumSkyblock.getUpgrades().oresUpgrade.upgrades.get(island.getOreLevel() + 1).crystalsCost + "" : IridiumSkyblock.getMessages().maxlevelreached),

                new Placeholder("sizeblocks", IridiumSkyblock.getUpgrades().sizeUpgrade.upgrades.get(island.getSizeLevel()).size + ""),
                new Placeholder("membercount", IridiumSkyblock.getUpgrades().memberUpgrade.upgrades.get(island.getMemberLevel()).size + ""),
                new Placeholder("warpcount", IridiumSkyblock.getUpgrades().warpUpgrade.upgrades.get(island.getWarpLevel()).size + ""),

                new Placeholder("sizelevel", island.getSizeLevel() + ""),
                new Placeholder("memberlevel", island.getMemberLevel() + ""),
                new Placeholder("warplevel", island.getWarpLevel() + ""),
                new Placeholder("oreslevel", island.getOreLevel() + ""),
                new Placeholder("generatorlevel", island.getOreLevel() + ""),
                // Boosters
                new Placeholder("spawnerbooster", island.getSpawnerBooster() + ""),
                new Placeholder("farmingbooster", island.getFarmingBooster() + ""),
                new Placeholder("expbooster", island.getExpBooster() + ""),
                new Placeholder("flightbooster", island.getFlightBooster() + ""),

                new Placeholder("spawnerbooster_seconds", island.getSpawnerBooster() % 60 + ""),
                new Placeholder("farmingbooster_seconds", island.getFarmingBooster() % 60 + ""),
                new Placeholder("expbooster_seconds", island.getExpBooster() % 60 + ""),
                new Placeholder("flightbooster_seconds", island.getFlightBooster() % 60 + ""),
                new Placeholder("spawnerbooster_minutes", (int) Math.floor(island.getSpawnerBooster() / 60.00) + ""),
                new Placeholder("farmingbooster_minutes", (int) Math.floor(island.getFarmingBooster() / 60.00) + ""),
                new Placeholder("expbooster_minutes", (int) Math.floor(island.getExpBooster() / 60.00) + ""),
                new Placeholder("flightbooster_minutes", (int) Math.floor(island.getFlightBooster() / 60.00) + ""),
                new Placeholder("spawnerbooster_crystalcost", IridiumSkyblock.getBoosters().spawnerBooster.crystalsCost + ""),
                new Placeholder("farmingbooster_crystalcost", IridiumSkyblock.getBoosters().farmingBooster.crystalsCost + ""),
                new Placeholder("expbooster_crystalcost", IridiumSkyblock.getBoosters().experianceBooster.crystalsCost + ""),
                new Placeholder("flightbooster_crystalcost", IridiumSkyblock.getBoosters().flightBooster.crystalsCost + ""),
                new Placeholder("spawnerbooster_vaultcost", IridiumSkyblock.getBoosters().spawnerBooster.vaultCost + ""),
                new Placeholder("farmingbooster_vaultcost", IridiumSkyblock.getBoosters().farmingBooster.vaultCost + ""),
                new Placeholder("expbooster_vaultcost", IridiumSkyblock.getBoosters().experianceBooster.vaultCost + ""),
                new Placeholder("flightbooster_vaultcost", IridiumSkyblock.getBoosters().flightBooster.vaultCost + ""),

                //Bank
                new Placeholder("experience", island.exp + ""),
                new Placeholder("crystals", island.getCrystals() + ""),
                new Placeholder("money", island.money + ""),
                new Placeholder("value", island.getValue() + "")
        ));
        return placeholders;
    }

    public static String processIslandPlaceholders(String line, Island island) {
        return processMultiplePlaceholders(line, getIslandPlaceholders(island));
    }

    public static List<String> processIslandPlaceholders(List<String> lines, Island island) {
        List<String> newlist = new ArrayList<>();
        for (String string : lines) {
            newlist.add(processIslandPlaceholders(string, island));
        }
        return newlist;
    }

    public static List<String> processMultiplePlaceholders(List<String> lines, List<Placeholder> placeholders) {
        List<String> newlist = new ArrayList<>();
        for (String string : lines) {
            newlist.add(processMultiplePlaceholders(string, placeholders));
        }
        return newlist;
    }

    public static String processMultiplePlaceholders(String line, List<Placeholder> placeholders) {
        for (Placeholder placeholder : placeholders) {
            line = placeholder.process(line);
        }
        return color(line);
    }

    public static void pay(Player p, double vault, int crystals) {
        User u = User.getUser(p);
        if (u.getIsland() != null) {
            u.getIsland().setCrystals(u.getIsland().getCrystals() + crystals);
            if (Vault.econ == null) {
                u.getIsland().money += vault;
            } else {
                Vault.econ.depositPlayer(p, vault);
            }
        } else {
            if (Vault.econ == null) {
                IridiumSkyblock.getInstance().getLogger().warning("Vault plugin not found");
                return;
            }
            Vault.econ.depositPlayer(p, vault);
        }
    }

    public static boolean canBuy(Player p, double vault, int crystals) {
        User u = User.getUser(p);
        if (u.getIsland() != null) {
            if (Vault.econ != null) {
                if (Vault.econ.getBalance(p) >= vault && u.getIsland().getCrystals() >= crystals) {
                    Vault.econ.withdrawPlayer(p, vault);
                    u.getIsland().setCrystals(u.getIsland().getCrystals() - crystals);
                    return true;
                }
            }
            if (u.getIsland().money >= vault && u.getIsland().getCrystals() >= crystals) {
                u.getIsland().money -= vault;
                u.getIsland().setCrystals(u.getIsland().getCrystals() - crystals);
                return true;
            }
        }
        if (Vault.econ != null) {
            if (Vault.econ.getBalance(p) >= vault && crystals == 0) {
                Vault.econ.withdrawPlayer(p, vault);
                return true;
            }
        }
        return false;
    }

    public static boolean canBuyStandalone(Player p, double vault, int crystals) {
        User u = User.getUser(p);
        if (u.getIsland() != null) {
            if (Vault.econ != null) {
                if (Vault.econ.getBalance(p) >= vault && u.getIsland().getCrystals() >= crystals) {
                    return true;
                }
            }
            if (u.getIsland().money >= vault && u.getIsland().getCrystals() >= crystals) {
                return true;
            }
        }
        if (Vault.econ != null) {
            if (Vault.econ.getBalance(p) >= vault && crystals == 0) {
                return true;
            }
        }
        return false;
    }

    public static int getExpAtLevel(final int level) {
        if (level <= 15) {
            return (2 * level) + 7;
        } else if (level <= 30) {
            return (5 * level) - 38;
        }
        return (9 * level) - 158;
    }

    public static int getTotalExperience(final Player player) {
        int exp = Math.round(getExpAtLevel(player.getLevel()) * player.getExp());
        int currentLevel = player.getLevel();

        while (currentLevel > 0) {
            currentLevel--;
            exp += getExpAtLevel(currentLevel);
        }
        if (exp < 0) {
            exp = Integer.MAX_VALUE;
        }
        return exp;
    }

    public static void setTotalExperience(final Player player, final int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("Experience is negative!");
        }
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);

        int amount = exp;
        while (amount > 0) {
            final int expToLevel = getExpAtLevel(player.getLevel());
            amount -= expToLevel;
            if (amount >= 0) {
                // give until next level
                player.giveExp(expToLevel);
            } else {
                // give the rest
                amount += expToLevel;
                player.giveExp(amount);
                amount = 0;
            }
        }
    }

    public static ItemStack getCrystals(int amount) {
        ItemStack itemStack = makeItemHidden(IridiumSkyblock.getInventories().crystal, Collections.singletonList(new Placeholder("amount", amount + "")));
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setInteger("crystals", amount);
        return nbtItem.getItem();
    }

    public static int getCrystals(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return 0;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("crystals")) {
            return nbtItem.getInteger("crystals");
        }
        return 0;
    }

    public static class Placeholder {

        private final String key;
        private final String value;

        public Placeholder(String key, String value) {
            this.key = "{" + key + "}";
            this.value = value;
        }

        public String process(String line) {
            if (line == null) return "";
            return line.replace(key, value);
        }
    }
}

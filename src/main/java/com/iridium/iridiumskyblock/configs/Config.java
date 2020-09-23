package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumskyblock.*;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public String prefix = "&b&lIridiumSkyblock &8»";
    public String worldName = "IridiumSkyblock";
    public String netherWorldName = worldName + "_nether";
    public String chatRankPlaceholder = "[ISLAND_RANK]";
    public String chatValuePlaceholder = "[ISLAND_VALUE]";
    public String chatNAMEPlaceholder = "[ISLAND_NAME]";
    public String chatLevelPlaceholder = "[ISLAND_LEVEL]";
    public String placeholderDefaultValue = "N/A";
    public boolean createCooldown = true;
    public boolean doIslandBackup = true;
    public boolean islandShop = true;
    public boolean automaticUpdate = true;
    public boolean defaultIslandPublic = true;
    public boolean netherIslands = true;
    public boolean forceShortPortalRadius = true;
    public boolean islandMenu = true;
    public boolean voidTeleport = true;
    public boolean notifyAvailableUpdate = true;
    public boolean clearInventories = false;
    public boolean restartUpgradesOnRegen = true;
    public boolean allowWaterInNether = true;
    public boolean disableLeafDecay = true;
    public boolean createIslandonHome = true;
    public boolean allowExplosions = true;
    public boolean disablePvPBetweenIslandMembers = true;
    public boolean disablePvPOnIslands = true;
    public boolean enableCrystalWithdraw = true;
    public boolean instantBiomeUpdate = true;
    public int deleteBackupsAfterDays = 7;
    public int regenCooldown = 3600;
    public int distance = 151;
    public int backupIntervalMinutes = 60;
    public int valueUpdateInterval = 20 * 30;
    public double valuePerLevel = 100.00;
    public double dailyMoneyInterest = 0.5;
    public double dailyCrystalsInterest = 5;
    public double dailyExpInterest = 0.01;
    public XBiome defaultBiome = XBiome.PLAINS;
    public XBiome defaultNetherBiome = XBiome.NETHER;
    public MissionRestart missionRestart = MissionRestart.Daily;
    public String worldSpawn = "world";
    public Map<Role, Permissions> defaultPermissions = new HashMap<Role, Permissions>() {{
        for (Role role : Role.values()) {
            if (role == Role.Visitor) {
                put(role, new Permissions(false, false, false, false, false, false, false, false, false, true, true, false, false, false, false, false));
            } else {
                put(role, new Permissions());
            }
        }
    }};
    public Map<Integer, Integer> islandTopSlots = new HashMap<Integer, Integer>() {{
        put(1, 4);
        put(2, 12);
        put(3, 14);
        put(4, 19);
        put(5, 20);
        put(6, 21);
        put(7, 22);
        put(8, 23);
        put(9, 24);
        put(10, 25);
    }};

    public Map<XMaterial, Integer> limitedBlocks = new HashMap<XMaterial, Integer>() {{
        put(XMaterial.HOPPER, 50);
        put(XMaterial.SPAWNER, 10);
    }};

    public Map<XMaterial, Double> blockvalue = null;
    public Map<String, Double> spawnervalue = null;
    public List<XBiome> biomes = Arrays.asList(XBiome.values());

    public List<EntityType> blockedEntities = Arrays.asList(EntityType.PRIMED_TNT, EntityType.MINECART_TNT, EntityType.FIREBALL, EntityType.SMALL_FIREBALL, EntityType.ENDER_PEARL);

}

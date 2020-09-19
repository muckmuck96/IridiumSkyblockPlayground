package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.*;
import com.iridium.iridiumskyblock.configs.Missions;
import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Crops;

import java.util.List;
import java.util.Map;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            if (event.isCancelled()) return;
            final Block block = event.getBlock();
            final Location location = block.getLocation();
            final IslandManager islandManager = IridiumSkyblock.getIslandManager();
            final Island island = islandManager.getIslandViaLocation(location);
            if (island == null) return;

            final Player player = event.getPlayer();
            final User user = User.getUser(player);

            if (user.islandID == island.getId()) {
                for (Missions.Mission mission : IridiumSkyblock.getMissions().missions) {
                    final int key = island.getMissionLevels().computeIfAbsent(mission.name, (name) -> 1);
                    final Map<Integer, Missions.MissionData> levels = mission.levels;
                    final Missions.MissionData level = levels.get(key);

                    if (level == null) continue;
                    if (level.type != MissionType.BLOCK_BREAK) continue;

                    final List<String> conditions = level.conditions;

                    if (
                            conditions.isEmpty()
                                    ||
                                    (
                                            block.getState().getData() instanceof Crops
                                                    &&
                                                    conditions.contains(((Crops) block.getState().getData()).getState().toString())
                                    )
                                    ||
                                    (conditions.contains(XMaterial.matchXMaterial(block.getType()).name()) && !conditions.contains(CropState.RIPE.toString()))

                    )
                        island.addMission(mission.name, 1);
                }
            }

            if (!island.getPermissions(user).breakBlocks || (!island.getPermissions(user).breakSpawners && XMaterial.matchXMaterial(block.getType()).equals(XMaterial.SPAWNER))) {
                if (XMaterial.matchXMaterial(block.getType()).equals(XMaterial.SPAWNER)) {
                    player.sendMessage(Utils.color(IridiumSkyblock.getMessages().noPermissionBreakSpawners.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                } else {
                    player.sendMessage(Utils.color(IridiumSkyblock.getMessages().noPermissionBuild.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                }
                event.setCancelled(true);
            }
        } catch (Exception e) {
            IridiumSkyblock.getInstance().sendErrorMessage(e);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMonitorBreakBlock(BlockBreakEvent event) {
        try {
            final Block block = event.getBlock();
            final Location location = block.getLocation();
            final IslandManager islandManager = IridiumSkyblock.getIslandManager();
            final Island island = islandManager.getIslandViaLocation(location);
            if (island == null) return;

            if (Utils.isBlockValuable(block)) {
                final Material material = block.getType();
                final String materialName = XMaterial.matchXMaterial(material).name();
                island.valuableBlocks.computeIfPresent(materialName, (name, original) -> original - 1);

                Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), island::calculateIslandValue);
            }

            island.failedGenerators.remove(location);
        } catch (Exception e) {
            IridiumSkyblock.getInstance().sendErrorMessage(e);
        }
    }
}

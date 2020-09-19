package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.*;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.gui.ConfirmationGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RegenCommand extends Command {

    public RegenCommand() {
        super(Arrays.asList("regen", "reset"), "Regenerate your island", "", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        User user = User.getUser(p);
        if (user.getIsland() != null) {
            if (user.role.equals(Role.Owner)) {
                if (user.bypassing || user.getIsland().getPermissions(user.role).regen) {
                    long time = user.getIsland().canGenerate() / 1000;
                    if (time == 0 || user.bypassing) {
                        if (IridiumSkyblock.getSchematics().schematics.size() == 1) {
                            p.openInventory(new ConfirmationGUI(user.getIsland(), () -> {
                                for (Schematics.FakeSchematic schematic : IridiumSkyblock.getSchematics().schematics) {
                                    user.getIsland().setSchematic(schematic.name);
                                    user.getIsland().setHome(user.getIsland().getHome().add(schematic.x, schematic.y, schematic.z));
                                    user.getIsland().setNetherhome(user.getIsland().getNetherhome().add(schematic.x, schematic.y, schematic.z));
                                }
                                user.getIsland().pasteSchematic(true);
                                if (IridiumSkyblock.getConfiguration().restartUpgradesOnRegen) {
                                    user.getIsland().resetMissions();
                                    user.getIsland().setSizeLevel(1);
                                    user.getIsland().setMemberLevel(1);
                                    user.getIsland().setWarpLevel(1);
                                    user.getIsland().setOreLevel(1);
                                    user.getIsland().setFlightBooster(0);
                                    user.getIsland().setExpBooster(0);
                                    user.getIsland().setFarmingBooster(0);
                                    user.getIsland().setSpawnerBooster(0);
                                    user.getIsland().setCrystals(0);
                                    user.getIsland().exp = 0;
                                    user.getIsland().money = 0;
                                }
                                user.getIsland().teleportPlayersHome();
                            }, IridiumSkyblock.getMessages().resetAction).getInventory());
                        } else {
                            p.openInventory(user.getIsland().getSchematicSelectGUI().getInventory());
                        }
                    } else {
                        int day = (int) TimeUnit.SECONDS.toDays(time);
                        int hours = (int) Math.floor(TimeUnit.SECONDS.toHours(time - day * 86400));
                        int minute = (int) Math.floor((time - day * 86400 - hours * 3600) / 60.00);
                        int second = (int) Math.floor((time - day * 86400 - hours * 3600) % 60.00);
                        p.sendMessage(Utils.color(IridiumSkyblock.getMessages().regenCooldown.replace("%days%", day + "").replace("%hours%", hours + "").replace("%minutes%", minute + "").replace("%seconds%", second + "").replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                    }
                } else {
                    sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().noPermission.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
                }
            } else {
                sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().mustBeIslandOwner.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
            }
        } else {
            sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
        }
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island) {
        Player p = (Player) sender;
        if (island != null) {
            if (IridiumSkyblock.getSchematics().schematics.size() == 1) {
                p.openInventory(new ConfirmationGUI(island, () -> {
                    for (Schematics.FakeSchematic schematic : IridiumSkyblock.getSchematics().schematics) {
                        island.setSchematic(schematic.name);
                        island.setHome(island.getHome().add(schematic.x, schematic.y, schematic.z));
                        island.setNetherhome(island.getNetherhome().add(schematic.x, schematic.y, schematic.z));
                    }
                    island.pasteSchematic(true);
                    if (IridiumSkyblock.getConfiguration().restartUpgradesOnRegen) {
                        island.resetMissions();
                        island.setSizeLevel(1);
                        island.setMemberLevel(1);
                        island.setWarpLevel(1);
                        island.setOreLevel(1);
                        island.setFlightBooster(0);
                        island.setExpBooster(0);
                        island.setFarmingBooster(0);
                        island.setSpawnerBooster(0);
                        island.setCrystals(0);
                        island.exp = 0;
                        island.money = 0;
                    }
                    island.teleportPlayersHome();
                }, IridiumSkyblock.getMessages().resetAction).getInventory());
            } else {
                p.openInventory(island.getSchematicSelectGUI().getInventory());
            }
        } else {
            sender.sendMessage(Utils.color(IridiumSkyblock.getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getConfiguration().prefix)));
        }
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}

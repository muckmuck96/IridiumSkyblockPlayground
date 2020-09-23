package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.Island;
import com.iridium.iridiumskyblock.IslandManager;
import com.iridium.iridiumskyblock.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class CustomQuestCommand extends Command {

    public CustomQuestCommand() {
        super(Arrays.asList("customquest", "cq"), "CustomQuests reward system", "iridiumskyblock.customquest", false);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        Player p = Bukkit.getPlayer(args[3]);
        Island island = User.getUser(p).getIsland();

        if(island.getCustomQuestsFinished() == null){
            island.setCustomQuestsFinished(new HashSet<>());
        }

        if (island.getCustomQuestsFinished().contains(args[2])) {

            if (args[1].equals("other") || args[1].equals("o")) {

                String command = "";
                for (int i = 4; i < args.length; i++) {
                    command = command + args[i] + " ";
                }

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

            }

        } else {

            if (args[1].equals("first") || args[1].equals("f")) {

                String command = "";
                for (int i = 4; i < args.length; i++) {
                    command = command + args[i] + " ";
                }

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

                island.getCustomQuestsFinished().add(args[2]);

            }

        }

    }



    @Override
    public void admin(CommandSender sender, String[] args, Island island) {
        execute(sender, args);
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("first", "f", "other", "o");
        }
        return null;
    }

}

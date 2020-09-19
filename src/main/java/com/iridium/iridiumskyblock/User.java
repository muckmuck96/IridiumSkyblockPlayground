package com.iridium.iridiumskyblock;

import org.bukkit.OfflinePlayer;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class User {

    public String player;
    public String name;
    public int islandID;
    public Role role;
    public Set<Integer> invites;
    public Island.Warp warp;
    public boolean bypassing;
    public boolean islandChat;
    public boolean flying;
    public transient boolean teleportingHome;
    public Date lastCreate;

    public User(OfflinePlayer p) {
        invites = new HashSet<>();
        this.player = p.getUniqueId().toString();
        this.name = p.getName();
        this.islandID = 0;
        bypassing = false;
        islandChat = false;
        flying = false;
        IridiumSkyblock.getIslandManager().users.put(this.player, this);
    }

    public Island getIsland() {
        return IridiumSkyblock.getIslandManager().islands.getOrDefault(islandID, null);
    }


    public Role getRole() {
        if (role == null) {
            if (getIsland() != null) {
                if (getIsland().getOwner().equals(player)) {
                    role = Role.Owner;
                } else {
                    role = Role.Member;
                }
            } else {
                role = Role.Visitor;
            }
        }
        return role;
    }

    public static User getUser(String p) {
        if (IridiumSkyblock.getIslandManager().users == null)
            IridiumSkyblock.getIslandManager().users = new HashMap<>();
        return IridiumSkyblock.getIslandManager().users.get(p);
    }

    public static User getUser(OfflinePlayer p) {
        if (p == null) return null;
        if (IridiumSkyblock.getIslandManager().users == null)
            IridiumSkyblock.getIslandManager().users = new HashMap<>();
        return IridiumSkyblock.getIslandManager().users.containsKey(p.getUniqueId().toString()) ? IridiumSkyblock.getIslandManager().users.get(p.getUniqueId().toString()) : new User(p);
    }
}

package com.division.ticketer.packet;

import com.division.ticketer.net.ServerUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Evan on 1/28/2015.
 */
public class Packet30GetUserStatus extends Packet {

    static final long serialVersionUID = 1194830L;

    private String playerName;
    private boolean online = false;

    public Packet30GetUserStatus(ServerUser user, String playerName) {
        super(user);
        this.playerName = playerName;
    }

    @Override
    public void executePacket() {
        Player player = Bukkit.getPlayerExact(playerName);
        online = player != null;
        getServer().queuePacket(this);
    }
}

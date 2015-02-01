/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.interpreters;

import com.division.ticketer.config.TicketerConfig;
import com.division.ticketer.core.Rank;
import com.division.ticketer.core.Ticketer;
import com.division.ticketer.mysql.DataSource;
import com.division.ticketer.net.NetCase;
import com.division.ticketer.net.Net_Framework;
import java.net.Socket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Evan
 */
public class USTATUSInterpreter extends NetInterpreter {

    public USTATUSInterpreter() {
        super("USTATUS");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        DataSource DB = Ticketer.getDatasource();
        String cleandata = data.replace(NetCase.USTATUS.getNetCase(), "");
        String[] delimit = cleandata.split("%");
        String rankformat = TicketerConfig.getRankFormat(Rank.SYSTEM);
        ChatColor chatcolor = TicketerConfig.getChatColor();
        int ticketid = Integer.parseInt(delimit[0]);
        if (delimit[1].equalsIgnoreCase("active")) {
            String pname = DB.getPlayer(ticketid);
            Player player = Bukkit.getServer().getPlayer(pname);
            if (player != null) {
                player.sendMessage(rankformat + ": " + chatcolor + "Your ticket will be serviced soon.");
            }
            if (DB.isFlagged(ticketid)) {
                DB.setFlag(ticketid, "", 0);
                DB.setGMAssign(ticketid, netFrame.getConnectedUser(sock).getUsername());
                netFrame.massRefresh();
            }
        }
        if (delimit[1].equalsIgnoreCase("closed")) {
            String pname = DB.getPlayer(ticketid);
            Player player = Bukkit.getServer().getPlayer(pname);
            if (player != null) {
                player.sendMessage(rankformat + ": " + chatcolor + "Your ticket has been closed.");
            }
        }
        if (delimit[1].equalsIgnoreCase("pending")) {
            String pname = DB.getPlayer(ticketid);
            Player player = Bukkit.getServer().getPlayer(pname);
            if (player != null) {
                player.sendMessage(rankformat + ": " + chatcolor + "Your conversation with this GM has ended.");
                player.sendMessage(rankformat + ": " + chatcolor + "Your ticket has been forwarded to higher ranked staff member.");
            }
        }
        DB.setState(ticketid, delimit[1]);
    }
}

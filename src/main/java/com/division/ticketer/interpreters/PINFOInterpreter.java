/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.interpreters;

import com.division.ticketer.core.Ticketer;
import com.division.ticketer.mysql.DataSource;
import com.division.ticketer.net.NetCase;
import com.division.ticketer.net.Net_Framework;
import java.net.Socket;
import org.bukkit.Bukkit;

/**
 *
 * @author Evan
 */
public class PINFOInterpreter extends NetInterpreter {

    public PINFOInterpreter() {
        super("PINFO");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        String cleandata = data.replace(NetCase.PINFO.getNetCase(), "");
        int ticketid = Integer.parseInt(cleandata);
        DataSource DB = Ticketer.getDatasource();
        String output;
        String player = DB.getPlayer(ticketid);
        if (Bukkit.getServer().getPlayer(player) != null) {
            output = player + "%true";
        } else {
            output = player + "%false";
        }
        netFrame.sendToClient(sock, NetCase.PINFO, output);
    }
}

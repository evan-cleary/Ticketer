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

/**
 *
 * @author Evan
 */
public class LRQInterpreter extends NetInterpreter {

    public LRQInterpreter() {
        super("LRQ");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        DataSource DB = Ticketer.getDatasource();
        String output = DB.getOpenTickets();
        netFrame.sendToClient(sock, NetCase.LRQ, output);
    }
}

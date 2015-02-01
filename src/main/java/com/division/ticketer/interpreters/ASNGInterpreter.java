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
public class ASNGInterpreter extends NetInterpreter {

    public ASNGInterpreter() {
        super("ASNG");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        DataSource DB = Ticketer.getDatasource();
        String cleandata = data.replace(NetCase.ASNG.getNetCase(), "");
        //String output = DB.getAssignments(cleandata);
        //netFrame.sendToClient(sock, NetCase.ASNG, output);
    }
}

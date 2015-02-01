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
public class RSTATUSInterpreter extends NetInterpreter {

    public RSTATUSInterpreter() {
        super("RSTATUS");
    }
    
    @Override
    public void run(String data, Socket sock, Net_Framework netFrame){
        DataSource DB = Ticketer.getDatasource();
        String cleandata = data.replace(NetCase.RSTATUS.getNetCase(),"");
        int ticketid = Integer.parseInt(cleandata);
        netFrame.sendToClient(sock, NetCase.RSTATUS, DB.getStatus(ticketid));
    }
}

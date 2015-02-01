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
public class UASGNInterpreter extends NetInterpreter {

    public UASGNInterpreter() {
        super("UASGN");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        String cleandata = data.replace(NetCase.UASGN.getNetCase(), "");
        String[] delimit = cleandata.split("%");
        int ticketid = Integer.parseInt(delimit[0]);
        DataSource DB = Ticketer.getDatasource();
        DB.setGMAssign(ticketid, delimit[1]);
        if (DB.isFlagged(ticketid)) {
            DB.setFlag(ticketid, "", 0);
        }
        netFrame.massRefresh();
    }
}

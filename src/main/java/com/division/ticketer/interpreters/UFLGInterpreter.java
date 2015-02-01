/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.interpreters;

import com.division.ticketer.config.Accounts;
import com.division.ticketer.core.Rank;
import com.division.ticketer.core.Ticketer;
import com.division.ticketer.mysql.DataSource;
import com.division.ticketer.net.Connected_User;
import com.division.ticketer.net.NetCase;
import com.division.ticketer.net.Net_Framework;
import java.net.Socket;

/**
 *
 * @author Evan
 */
public class UFLGInterpreter extends NetInterpreter {

    public UFLGInterpreter() {
        super("UFLG");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        DataSource DB = Ticketer.getDatasource();
        String cleandata = data.replace(NetCase.UFLG.getNetCase(), "");
        String[] delimit = cleandata.split("%");
        int ticketid = Integer.parseInt(delimit[0]);
        String msg = delimit[1];
        int flag = Integer.parseInt(delimit[2]);
        DB.setFlag(ticketid, msg, flag);
        DB.setGMAssign(ticketid, "");
        Connected_User cUser = netFrame.getConnectedUser(sock);
        netFrame.notifyGreater(Accounts.getRank(cUser.getUsername()), ticketid, msg);
    }
}

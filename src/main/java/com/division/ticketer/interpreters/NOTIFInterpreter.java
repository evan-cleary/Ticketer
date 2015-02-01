/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.interpreters;

import com.division.ticketer.net.Connected_User;
import com.division.ticketer.net.NetCase;
import com.division.ticketer.net.Net_Framework;
import java.net.Socket;

/**
 *
 * @author Evan
 */
public class NOTIFInterpreter extends NetInterpreter {

    public NOTIFInterpreter() {
        super("NOTIF");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        String cleandata = data.replace(NetCase.NOTIF.getNetCase(), "");
        String[] delimit = cleandata.split("%");
        Connected_User cUser = netFrame.getConnectedUser(delimit[0]);
        if (cUser != null) {
            netFrame.sendToClient(cUser.getSocket(), NetCase.NOTIF, cleandata);
        } else {
            netFrame.sendNotifications(delimit[1], delimit[2]);
        }
    }
}

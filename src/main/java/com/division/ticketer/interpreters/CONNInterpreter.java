/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.interpreters;

import com.division.ticketer.config.Accounts;
import com.division.ticketer.core.Rank;
import com.division.ticketer.core.Ticketer;
import com.division.ticketer.net.Connected_User;
import com.division.ticketer.net.NetCase;
import com.division.ticketer.net.Net_Framework;
import java.net.Socket;

/**
 *
 * @author Evan
 */
public class CONNInterpreter extends NetInterpreter {

    public CONNInterpreter() {
        super("CONN");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netframe) {
        Accounts acc = Ticketer.getAccounts();
        String clean = data.replace(NetCase.CONN.getNetCase(), "");
        String[] delimit = clean.split("%");
        String username = delimit[1];
        String password = delimit[0];
        Rank rank;
        if (Accounts.verifiedUser(username, password) != null) {
            netframe.connectUser(sock, username);
            Connected_User cUser = netframe.getConnectedUser(sock);
            netframe.createSpecListen(cUser);
            if (cUser != null) {
                rank = Accounts.getRank(cUser.getUsername());
                netframe.sendToClient(sock, NetCase.CONNC, rank.getRank());
            } else {
                System.out.println("[Severe] Error retrieving connected user.");
            }
        } else{
            netframe.sendToClient(sock, NetCase.CONNR, null);
        }
    }
}

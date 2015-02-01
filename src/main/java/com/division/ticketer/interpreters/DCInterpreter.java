/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.interpreters;

import com.division.ticketer.net.Connected_User;
import com.division.ticketer.net.Net_Framework;
import java.net.Socket;

/**
 *
 * @author Evan
 */
public class DCInterpreter extends NetInterpreter {

    public DCInterpreter() {
        super("DC");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        Connected_User cUser = netFrame.getConnectedUser(sock);
        cUser.setConnected(false);
    }
}

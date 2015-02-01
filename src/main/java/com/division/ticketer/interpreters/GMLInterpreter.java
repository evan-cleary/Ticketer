/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.interpreters;

import com.division.ticketer.config.Accounts;
import com.division.ticketer.core.Ticketer;
import com.division.ticketer.net.NetCase;
import com.division.ticketer.net.Net_Framework;
import java.net.Socket;

/**
 *
 * @author Evan
 */
public class GMLInterpreter extends NetInterpreter {

    public GMLInterpreter() {
        super("GML");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
        Accounts acc = Ticketer.getAccounts();
        String accountList = acc.getAccounts();
        netFrame.sendToClient(sock, NetCase.GML, accountList);
    }
}

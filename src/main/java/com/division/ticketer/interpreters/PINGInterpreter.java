/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.interpreters;

import com.division.ticketer.net.Net_Framework;
import java.net.Socket;

/**
 *
 * @author Evan
 */
public class PINGInterpreter extends NetInterpreter {

    public PINGInterpreter() {
        super("PING");
    }

    @Override
    public void run(String data, Socket sock, Net_Framework netFrame) {
    }
}

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
public interface TicketerInterpreter {

    String getname();

    void run(String data, Socket sock, Net_Framework netframe);

}

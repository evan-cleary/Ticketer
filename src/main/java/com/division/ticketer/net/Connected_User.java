/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.division.ticketer.net;

import java.net.Socket;

/**
 *
 * @author Evan
 */
public class Connected_User {
    
    private Socket mySocket;
    private String myUsername;
    private boolean connected = true;
    
    public Connected_User(String username,Socket sock){
        this.mySocket = sock;
        this.myUsername = username;
    }
    
    public Socket getSocket(){
        return mySocket;
    }
    
    public String getUsername(){
        return myUsername;
    }
    public void setConnected(boolean conn){
        this.connected = conn;
    }
    public boolean isConnected(){
        return connected;
    }
}

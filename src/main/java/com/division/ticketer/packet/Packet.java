package com.division.ticketer.packet;

import com.division.ticketer.net.Server;
import com.division.ticketer.net.ServerUser;
import com.division.ticketer.net.TicketUser;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by Evan on 1/25/2015.
 */
public abstract class Packet implements Serializable {

    static final long serialVersionUID = 1194899L;

    private ServerUser user;
    private transient Socket socket;
    private transient Server server;

    public Packet(ServerUser user){
        this.user = user;
    }

    public abstract void executePacket();

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        user.setConnection(socket);
    }

    public ServerUser getUser() {
        return user;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}

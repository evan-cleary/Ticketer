package com.division.ticketer.net;

import com.division.ticketer.packet.Packet;

import java.net.Socket;

/**
 * Created by Evan on 1/25/2015.
 */
public interface Server {

    public void startServer(int port);

    public void queuePacket(Packet packet);

    public boolean isUserConnected(ServerUser user);

    public void regsiterUser(ServerUser user);

    public void deregisterUser(ServerUser user);
}

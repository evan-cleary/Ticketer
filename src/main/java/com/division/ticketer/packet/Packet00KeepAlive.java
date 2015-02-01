package com.division.ticketer.packet;

import com.division.ticketer.net.TicketUser;

/**
 * Created by Evan on 1/25/2015.
 */
public class Packet00KeepAlive extends Packet{


    static final long serialVersionUID = 1194800L;

    public Packet00KeepAlive(TicketUser user) {
        super(user);
    }

    @Override
    public void executePacket() {

    }
}

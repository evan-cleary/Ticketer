package com.division.ticketer.packet;

import com.division.ticketer.config.Accounts;
import com.division.ticketer.net.LoginUser;
import com.division.ticketer.net.TicketUser;

/**
 * Created by Evan on 1/25/2015.
 */
public class Packet80Connection extends Packet {

    static final long serialVersionUID = 1194880L;

    private LoginUser loginUser;
    private TicketUser ticketUser;

    public Packet80Connection(TicketUser user) {
        super(user);
        this.ticketUser = user;
    }

    @Override
    public void executePacket() {
        ticketUser = Accounts.verifiedUser(loginUser.getUsername(),loginUser.getPassword());
        ticketUser.setConnection(getSocket());
        if (!getServer().isUserConnected(ticketUser)) {
            getServer().regsiterUser(ticketUser);
            getServer().queuePacket(new Packet80Connection(ticketUser));
        }
    }
}
